package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

    private Long id = -1L;

    @NotBlank
    private String path;

    private Long belongsToPostId;

    private Long belongsToUserId;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.path = image.getPath();
        if (image.getBelongsToPost() != null)
            this.belongsToPostId = image.getBelongsToPost().getId();
        if (image.getBelongsToUser() != null)
            this.belongsToUserId = image.getBelongsToUser().getId();
    }
}
