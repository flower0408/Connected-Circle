package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long id = -1L;

    @NotBlank
    private String content;

    @NotBlank
    private String title;

    @NotNull
    private String creationDate;

    @NotNull
    private Long postedByUserId;

    private Long belongsToGroupId;

    private List<ImageDTO> images;

    public PostDTO(Post createdPost) {
        this.id = createdPost.getId();
        this.title = createdPost.getTitle();
        this.content = createdPost.getContent();
        this.creationDate = createdPost.getCreationDate().toString();
        this.postedByUserId = createdPost.getPostedBy().getId();
    }
}
