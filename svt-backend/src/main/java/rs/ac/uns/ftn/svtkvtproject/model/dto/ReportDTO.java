package rs.ac.uns.ftn.svtkvtproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Comment;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;

    @NotBlank
    private String reason;

    @NotBlank
    private String timestamp;

    @NotNull
    private Long byUserId;

    private Boolean accepted;

    private Long onUserId;

    private Long onPostId;

    private Long onCommentId;

    public ReportDTO(Report report){
        this.id = report.getId();
        this.reason = report.getReason().toString();
        this.timestamp = report.getTimestamp().toString();
        this.byUserId = report.getByUser().getId();
        if (report.getOnUser() != null)
            this.onUserId = report.getOnUser().getId();
        if (report.getOnPost() != null)
            this.onPostId = report.getOnPost().getId();
        if (report.getOnComment() != null)
            this.onCommentId = report.getOnComment().getId();
    }


}
