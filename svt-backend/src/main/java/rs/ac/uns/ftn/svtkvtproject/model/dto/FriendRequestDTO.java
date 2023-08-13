package rs.ac.uns.ftn.svtkvtproject.model.dto;

import rs.ac.uns.ftn.svtkvtproject.model.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {

    private Long id = -1L;

    private boolean approved;

    private String createdAt;

    private String at;

    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    public FriendRequestDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        if (friendRequest.getApproved() != null)
            this.approved = friendRequest.getApproved();
        this.createdAt = friendRequest.getCreatedAt().toString();
        if (friendRequest.getAt() != null)
            this.at = friendRequest.getAt().toString();
        this.fromUserId = friendRequest.getFromUser().getId();
        this.toUserId = friendRequest.getToUser().getId();
    }
}
