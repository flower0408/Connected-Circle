package rs.ac.uns.ftn.svtkvtproject.service.impl;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchPostsByNumberOfLikes;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchServicePost;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.elasticsearch.common.unit.Fuzziness;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServicePostImpl implements SearchServicePost {

    private final ElasticsearchOperations elasticsearchTemplate;
    @Override
    public List<PostDocument> getPostsByPostName(String postName, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForTitle(postName, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<PostDocument> getPostsByPostContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForContent(content, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<PostDocument> getPostsByPDFContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForPDFContent(content, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<PostDocument> getPostsByNumberOfLikes(SearchPostsByNumberOfLikes criteria) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(searchByNumOfLikesRange(criteria.getGreaterThan(), criteria.getLessThan()));
        return runQuery(searchQueryBuilder.build());

    }

    @Override
    public List<PostDocument> searchPostsBooleanQuery(String title, String content, String pdfContent, String operation, boolean usePhraseQuery, boolean useFuzzyQuery) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildComplexSearchQuery(title, content, pdfContent, operation, usePhraseQuery, useFuzzyQuery));
        return runQuery(searchQueryBuilder.build());
    }

    private Query buildComplexSearchQuery(String title, String content, String pdfContent, String operation, boolean usePhraseQuery, boolean useFuzzyQuery) {
        return BoolQuery.of(q -> {
            switch (operation) {
                case "AND":
                    q.must(mb -> mb.bool(b -> {
                        if (usePhraseQuery) {
                            b.must(sb -> sb.matchPhrase(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.must(sb -> sb.matchPhrase(m -> m.field("content").query(content).analyzer("serbian_simple")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.must(sb -> sb.bool(bb -> bb
                                        .should(s -> s.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        } else if (useFuzzyQuery) {
                            b.must(sb -> sb.match(m -> m.field("title").query(title).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("content").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.must(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")))
                                ));
                            }
                        } else {
                            b.must(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
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
                            b.should(sb -> sb.matchPhrase(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.should(sb -> sb.matchPhrase(m -> m.field("content").query(content).analyzer("serbian_simple")));
                            //b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            // b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.should(sb -> sb.bool(bb -> bb
                                        .should(s -> s.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")))
                                        .should(s -> s.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")))
                                ));
                            }
                        } else if (useFuzzyQuery) {
                            b.should(sb -> sb.match(m -> m.field("title").query(title).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.should(sb -> sb.match(m -> m.field("content").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            // b.should(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).fuzziness(String.valueOf(Fuzziness.ONE)).analyzer("serbian_simple")));
                            // b.should(sb -> sb.match(m -> m.field("content_en").query(pdfContent).fuzziness(String.valueOf(Fuzziness.ONE)).analyzer("english")));
                            if (pdfContent != null && !pdfContent.isEmpty()) {
                                b.should(sb -> sb.bool(bb -> bb
                                        .should(s -> s.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")))
                                        .should(s -> s.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")))
                                ));
                            }
                        } else {
                            b.should(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.should(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
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
                            b.must(sb -> sb.matchPhrase(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.must(sb -> sb.matchPhrase(m -> m.field("content").query(content).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.matchPhrase(m -> m.field("content_sr").query(pdfContent).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.matchPhrase(m -> m.field("content_en").query(pdfContent).analyzer("english")));
                        } else if (useFuzzyQuery) {
                            b.must(sb -> sb.match(m -> m.field("title").query(title).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("content").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_sr").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                            b.mustNot(sb -> sb.match(m -> m.field("content_en").query(pdfContent).fuzziness(Fuzziness.ONE.asString()).analyzer("english")));
                        } else {
                            b.must(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
                            b.must(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
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


    public Query searchByNumOfLikesRange(Integer minLikes, Integer maxLikes) {
        return RangeQuery.of(q -> {
            if (minLikes != null) {
                q.field("total_likes").gte(JsonData.of(minLikes));
            }
            if (maxLikes != null) {
                q.field("total_likes").lte(JsonData.of(maxLikes));
            }
            return q;
        })._toQuery();
    }


    private List<PostDocument> runQuery(NativeQuery searchQuery) {
        SearchHits<PostDocument> searchHits = elasticsearchTemplate.search(searchQuery, PostDocument.class,
                IndexCoordinates.of("posts"));
        return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private Query buildSimpleSearchQuery(List<String> tokens) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            tokens.forEach(token -> {
                b.should(sb -> sb.match(m -> m.field("title").query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content").query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_sr").query(token).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(token).analyzer("english")));
            });
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForTitle(String title, boolean usePhraseQuery, boolean useFuzzyQuery) {
       /* return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
            return b;
        })))._toQuery();*/
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("title").query(title).analyzer("serbian_simple")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("title").query(title).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
            } else {
                b.should(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
            }
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery) {
        /*return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
            return b;
        })))._toQuery();*/
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("content").query(content).analyzer("serbian_simple")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("content").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
            } else {
                b.should(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
            }
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForPDFContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery) {
       /* return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("content_sr").query(content).analyzer("serbian_simple")));
            b.should(sb -> sb.match(m -> m.field("content_en").query(content).analyzer("english")));
            return b;
        })))._toQuery();*/
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            if (usePhraseQuery) {
                b.should(sb -> sb.matchPhrase(m -> m.field("content_sr").query(content).analyzer("serbian_simple")));
                b.should(sb -> sb.matchPhrase(m -> m.field("content_en").query(content).analyzer("english")));
            } else if (useFuzzyQuery) {
                b.should(sb -> sb.match(m -> m.field("content_sr").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(content).fuzziness(Fuzziness.ONE.asString()).analyzer("english")));
            } else {
                b.should(sb -> sb.match(m -> m.field("content_sr").query(content).analyzer("serbian_simple")));
                b.should(sb -> sb.match(m -> m.field("content_en").query(content).analyzer("english")));
            }
            return b;
        })))._toQuery();
    }
}
