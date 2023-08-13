package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private String creationDate;

    @NotNull
    private boolean suspended;

    private String suspendedReason;

    public GroupDTO(Group createdGroup) {
        this.id = createdGroup.getId();
        this.name = createdGroup.getName();
        this.description = createdGroup.getDescription();
        this.creationDate = createdGroup.getCreationDate().toString();
        this.suspended = createdGroup.isSuspended();
        this.suspendedReason = createdGroup.getSuspendedReason();
    }
}
