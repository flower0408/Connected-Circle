package rs.ac.uns.ftn.svtkvtproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtkvtproject.model.entity.GroupAdmin;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupAdminDTO {

    private Long id;

    private Long userId;

    private Long groupId;

    public GroupAdminDTO(GroupAdmin groupAdmin){
        this.id = groupAdmin.getId();
        this.userId = groupAdmin.getUser().getId();
        this.groupId = groupAdmin.getGroup().getId();
    }


}
