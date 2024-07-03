package rs.ac.uns.ftn.svtkvtproject.service.impl;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.service.interfaces.SearchServicePost;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServicePostImpl implements SearchServicePost {

    private final ElasticsearchOperations elasticsearchTemplate;
    @Override
    public List<PostDocument> getPostsByPostName(String postName) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForTitle(postName));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<PostDocument> getPostsByPostContent(String content) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForContent(content));
        return runQuery(searchQueryBuilder.build());
    }

    @Override
    public List<PostDocument> getPostsByPDFContent(String content) {
        var searchQueryBuilder =
                new NativeQueryBuilder().withQuery(simpleSearchForPDFContent(content));
        return runQuery(searchQueryBuilder.build());
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

    private Query simpleSearchForTitle(String title) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("title").query(title).analyzer("serbian_simple")));
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForContent(String content) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("content").query(content).analyzer("serbian_simple")));
            return b;
        })))._toQuery();
    }

    private Query simpleSearchForPDFContent(String content) {
        return BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
            b.should(sb -> sb.match(m -> m.field("content_sr").query(content).analyzer("serbian_simple")));
            b.should(sb -> sb.match(m -> m.field("content_en").query(content).analyzer("english")));
            return b;
        })))._toQuery();
    }
}
