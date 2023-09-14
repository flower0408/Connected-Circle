package rs.ac.uns.ftn.svtkvtproject.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Reaction;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;

import java.util.List;
import java.util.Set;

public interface ReportService {

    Report findById(Long id);

    List<Report> findAll();

    List<Report> findAllReports();

    List<Report> findReportsForPost(Long postId);

    List<Report> findReportsForComment(Long commentId);

    List<Report> findReportsForUser(Long userId);

    Report createReport(ReportDTO reactionDTO);

    Integer deleteReport(Long id);

    Report updateReport();

    Report saveReport(Report report);

    Integer deletePostReports(Long postId);

    Integer deleteCommentReports(Long commentId);

    Integer deleteReportFromUser(Long userId);
}
