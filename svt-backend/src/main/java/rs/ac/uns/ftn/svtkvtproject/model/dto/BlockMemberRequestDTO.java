package rs.ac.uns.ftn.svtkvtproject.model.dto;

import java.math.BigInteger;

public class BlockMemberRequestDTO {

    private Long memberId;
    private Long adminId;
    private BigInteger groupId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }
}
