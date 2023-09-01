package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.Banned;
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
public class BannedDTO {

    private Long id = -1L;

    @NotBlank
    private String timestamp;

    @NotBlank
    private boolean blocked;

    @NotNull
    private Long byAdminId;

    @NotNull
    private Long towardsUserId;

    private Long groupId;

    public BannedDTO(Banned banned){
        this.id = banned.getId();
        this.timestamp = banned.getTimestamp().toString();
        this.blocked = banned.isBlocked();
        this.byAdminId = banned.getByAdmin().getId();
        this.towardsUserId = banned.getTowardsUser().getId();
        if (banned.getGroup() != null)
            this.groupId = banned.getGroup().getId();
    }
}
