package rs.ac.uns.ftn.svtkvtproject.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;

import java.util.Optional;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, String> {
    Optional<PostDocument> findByDatabaseId(Integer databaseId);
}
