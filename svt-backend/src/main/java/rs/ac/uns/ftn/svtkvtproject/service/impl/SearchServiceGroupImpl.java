package rs.ac.uns.ftn.svtkvtproject.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByAvgNumberOfLikes;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByRangeOfPosts;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchServieGroup;

@Service
@RequiredArgsConstructor
public class SearchServiceGroupImpl implements SearchServieGroup {

    private final ElasticsearchOperations elasticsearchTemplate;


    @Override
    public List<GroupDocument> searchGroupsByName(String name, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForName(name, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByDescription(String description, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForDescription(description, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByPDFContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForPDFDescription(content, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByPosts(SearchGroupByRangeOfPosts data) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(searchByNumPostsRange(data.getGreaterThan(), data.getLessThan()));
        return runQuery(searchQueryBuilder.build());

    }

    @Override
    public List<GroupDocument> searchGroupsByAvgLikes(SearchGroupByAvgNumberOfLikes data) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(searchByAvgLikes(data.getGreaterThan(), data.getLessThan()));
        return runQuery(searchQueryBuilder.build());
    }

    private List<GroupDocument> runQuery(NativeQuery searchQuery) {
        SearchHits<GroupDocument> searchHits = elasticsearchTemplate.search(searchQuery, GroupDocument.class,
                IndexCoordinates.of("groups"));
        return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public Query searchByAvgLikes(Integer minLikes, Integer maxLikes) {
        return RangeQuery.of(q -> {
            if (minLikes != null) {
                q.field("avgNumberOfLikes").gte(JsonData.of(minLikes));
            }
            if (maxLikes != null) {
                q.field("avgNumberOfLikes").lte(JsonData.of(maxLikes));
            }
            return q;
        })._toQuery();
    }

    public Query searchByNumPostsRange(Integer minPosts, Integer maxPosts) {
        return RangeQuery.of(q -> {
            if (minPosts != null) {
                q.field("numPosts").gte(JsonData.of(minPosts));
            }
            if (maxPosts != null) {
                q.field("numPosts").lte(JsonData.of(maxPosts));
            }
            return q;
        })._toQuery();
    }

    @Override
    public List<GroupDocument> searchGroupsBooleanQuery(String name, String description, String pdfContent, String operation, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildComplexSearchQuery(name, description, pdfContent, operation, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }


    /*private Query buildComplexSearchQuery(String name, String description, String pdfContent, String operation) {
        return BoolQuery.of(q -> {
            switch (operation) {
                case "AND":
                    q.must(mb -> mb.bool(b -> {
                        if (name != null && !name.isEmpty()) {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                        }
                        if (description != null && !description.isEmpty()) {
                            b.must(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                        }
                        if (pdfContent != null && !pdfContent.isEmpty()) {
                            //b.must(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        }
                        return b;
                    }));
                    break;
                case "OR":
                    q.should(mb -> mb.bool(b -> {
                        if (name != null && !name.isEmpty()) {
                            b.should(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                        }
                        if (description != null && !description.isEmpty()) {
                            b.should(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                        }
                        if (pdfContent != null && !pdfContent.isEmpty()) {
                            b.should(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.should(sb -> sb.match(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        }
                        return b;
                    }));
                    break;
                case "NOT":
                    q.must(mb -> mb.bool(b -> {
                        if (name != null && !name.isEmpty()) {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                        }
                        if (description != null && !description.isEmpty()) {
                            b.must(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                        }
                        if (pdfContent != null && !pdfContent.isEmpty()) {
                            b.mustNot(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        }
                        return b;
                    }));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation: " + operation);
            }
            return q;
        })._toQuery();
    }*/

    private Query buildComplexSearchQuery(String name, String description, String pdfContent, String operation, boolean usePhraseQuery, boolean useFuzzyQuery) {
        return BoolQuery.of(q -> {
            switch (operation) {
                case "AND":
                    q.must(mb -> mb.bool(b -> {
                        if (usePhraseQuery) {
                            b.must(sb -> sb.matchPhrase(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.must(sb -> sb.matchPhrase(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.must(sb -> sb.bool(bb -> bb
                                        .should(s -> s.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        } else if (useFuzzyQuery) {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("description").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.must(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")))
                                ));
                            }
                        } else {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.must(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        }
                        return b;
                    }));
                    break;
                case "OR":
                    q.should(mb -> mb.bool(b -> {
                        if (usePhraseQuery) {
                            b.should(sb -> sb.matchPhrase(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.should(sb -> sb.matchPhrase(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            //b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                           // b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.should(sb -> sb.bool(bb -> bb
                                        .should(s -> s.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        } else if (useFuzzyQuery) {
                            b.should(sb -> sb.match(m -> m.field("name").query(name).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.should(sb -> sb.match(m -> m.field("description").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                           // b.should(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).fuzziness(String.valueOf(Fuzziness.ONE)).analyzer("serbian_simple")));
                           // b.should(sb -> sb.match(m -> m.field("content_en").query(pdfContent).fuzziness(String.valueOf(Fuzziness.ONE)).analyzer("english")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.should(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")))
                                ));
                            }
                        } else {
                            b.should(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.should(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            //b.should(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            //b.should(sb -> sb.match(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.should(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        }
                        return b;
                    }));
                    break;
                case "NOT":
                    q.mustNot(mb -> mb.bool(b -> {
                        if (usePhraseQuery) {
                            b.must(sb -> sb.matchPhrase(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.must(sb -> sb.matchPhrase(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        } else if (useFuzzyQuery) {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("description").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")));
                        } else {
                            b.must(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        }
                        return b;
                    }));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operation: " + operation);
            }
            return q;
        })._toQuery();
    }


    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("name").query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token).analyzer("english")));
                b.should(sb -> sb.match(m -> m.field("description").query(token).analyzer("serbian_simple")));
            });
            return b;
        })))._toQuery();
    }

    private Query buildSimpleSearchQueryWithMust(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.must(sb -> sb.match(m -> m.field("name").query(token).analyzer("serbian_simple")));
                b.must(sb -> sb.match(m -> m.field("content_sr").query(token).analyzer("serbian_simple")));
                b.must(sb -> sb.match(m -> m.field("content_en").query(token).analyzer("english")));
                b.must(sb -> sb.match(m -> m.field("description").query(token).analyzer("serbian_simple")));
            });
            return b;
        })))._toQuery();
    }


    /*private Query simpleSearchForName(String name) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForDescription(String description) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForPDFDescription(String description) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("content_sr").query(description).analyzer("serbian_simple")));
            b.should(sb -> sb.match(m -> m.field("content_en").query(description).analyzer("english")));
            return b;
        })))._toQuery();
    }*/

   /* private Query simpleSearchForName(String name, boolean usePhraseQuery, boolean useFuzzyQuery) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("name").query(name).analyzer("serbian_simple")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("name").query(name).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
            } else {
                b.should(sb -> sb.match(m -> m.field("name").query(name).analyzer("serbian_simple")));
            }
            return b;
        })))._toQuery();
    }*/


    private Query simpleSearchForName(String name, boolean usePhraseQuery, boolean useFuzzyQuery) {
        if (usePhraseQuery) {
            return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> b.should(sb ->
                    sb.matchPhrase(m -> m.field("name").query(name).analyzer("serbian_simple"))
            ))))._toQuery();
        } else if (useFuzzyQuery) {
            return MatchQuery.of(q -> q.field("name").query(name).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple"))._toQuery();
        } else {
            return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> b.should(sb ->
                    sb.match(m -> m.field("name").query(name).analyzer("serbian_simple"))
            ))))._toQuery();
        }
    }


    /*private Query simpleSearchForDescription(String description, boolean usePhraseQuery, boolean useFuzzyQuery) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("description").query(description).analyzer("serbian_simple")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("description").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
            } else {
                b.should(sb -> sb.match(m -> m.field("description").query(description).analyzer("serbian_simple")));
            }
            return b;
        })))._toQuery();
    }*/

    private Query simpleSearchForDescription(String description, boolean usePhraseQuery, boolean useFuzzyQuery) {
        if (usePhraseQuery) {
            return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> b.should(sb ->
                    sb.matchPhrase(m -> m.field("description").query(description).analyzer("serbian_simple"))
            ))))._toQuery();
        } else if (useFuzzyQuery) {
            return MatchQuery.of(q -> q.field("description").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple"))._toQuery();
        } else {
            return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> b.should(sb ->
                    sb.match(m -> m.field("description").query(description).analyzer("serbian_simple"))
            ))))._toQuery();
        }
    }

    private Query simpleSearchForPDFDescription(String description, boolean usePhraseQuery, boolean useFuzzyQuery) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(description).analyzer("serbian_simple")));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(description).analyzer("english")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("content_sr").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(description).fuzziness(Fuzziness.ONE.asString()).analyzer("english")));
            } else {
                b.should(sb -> sb.match(m -> m.field("content_sr").query(description).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(description).analyzer("english")));
            }
            return b;
        })))._toQuery();
    }




}
