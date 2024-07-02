package rs.ac.uns.ftn.svtkvtproject.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;

import java.util.Optional;

@Repository
public interface  GroupDocumentRepository extends ElasticsearchRepository<GroupDocument, String>  {

    Optional<GroupDocument> findByDatabaseId(Integer databaseId);



}
