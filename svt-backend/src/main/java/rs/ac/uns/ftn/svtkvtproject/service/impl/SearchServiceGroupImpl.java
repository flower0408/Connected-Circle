package rs.ac.uns.ftn.svtkvtproject.service.impl;

import lombok.RequiredArgsConstructor;
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
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchGroupByRangeOfPosts;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchServieGroup;

@Service
@RequiredArgsConstructor
public class SearchServiceGroupImpl implements SearchServieGroup {

    private final ElasticsearchOperations elasticsearchTemplate;


    @Override
    public List<GroupDocument> searchGroupsByName(String name) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForName(name));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByDescription(String description) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForDescription(description));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByPDFContent(String content) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForPDFDescription(content));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<GroupDocument> searchGroupsByPosts(SearchGroupByRangeOfPosts data) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(searchByNumPostsRange(data.getGreaterThan(), data.getLessThan()));
        return runQuery(searchQueryBuilder.build());

    }

    private List<GroupDocument> runQuery(NativeQuery searchQuery) {
        SearchHits<GroupDocument> searchHits = elasticsearchTemplate.search(searchQuery, GroupDocument.class,
                IndexCoordinates.of("groups"));
        return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
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
    public List<GroupDocument> searchGroupsBooleanQuery(String name, String description, String pdfContent, String operation) {
        var searchQueryBuilder = new NativeQueryBuilder().withQuery(buildComplexSearchQuery(name, description, pdfContent, operation));
        return runQuery(searchQueryBuilder.build());
    }

    private Query buildComplexSearchQuery(String name, String description, String pdfContent, String operation) {
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


    private Query simpleSearchForName(String name) {
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
    }
}
