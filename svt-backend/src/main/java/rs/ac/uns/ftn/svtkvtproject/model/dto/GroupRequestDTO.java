package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.GroupRequest;
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
public class GroupRequestDTO {

    private Long id = -1L;

    private Boolean approved;

    private String createdAt;

    private String at;

    @NotNull
    private Long createdByUserId;

    @NotNull
    private Long forGroupId;

    public GroupRequestDTO(GroupRequest groupRequest) {
        this.id = groupRequest.getId();
        if (groupRequest.getApproved() != null)
            this.approved = groupRequest.getApproved();
        this.createdAt = groupRequest.getCreatedAt().toString();
        if (groupRequest.getAt() != null)
            this.at = groupRequest.getAt().toString();
        this.createdByUserId = groupRequest.getCreatedBy().getId();
        this.forGroupId = groupRequest.getForGroup().getId();
    }
}
