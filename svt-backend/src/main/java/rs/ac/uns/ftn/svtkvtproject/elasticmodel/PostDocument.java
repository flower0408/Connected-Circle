package rs.ac.uns.ftn.svtkvtproject.elasticmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_document")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class PostDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "content", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String content;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;


}