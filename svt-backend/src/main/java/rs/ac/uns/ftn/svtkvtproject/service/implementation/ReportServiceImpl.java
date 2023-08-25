package rs.ac.uns.ftn.svtkvtproject.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.*;
import rs.ac.uns.ftn.svtkvtproject.model.enums.ReportReason;
import rs.ac.uns.ftn.svtkvtproject.repository.ReportRepository;
import rs.ac.uns.ftn.svtkvtproject.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public void setReportRepository(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    private static final Logger logger = LogManager.getLogger(ReactionServiceImpl.class);


    @Override
    public Report findById(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        if (!report.isEmpty())
            return report.get();
        logger.error("Repository search for report with id: " + id + " returned null");
        return null;
    }


    @Override
    public List<Report> findAll() {
        return this.reportRepository.findAll();
    }

    @Override
    public List<Report> findAllReports() {
        Optional<List<Report>> reports = reportRepository.findAllReports();
        if (!reports.isEmpty())
            return reports.get();
        logger.error("Repository search for reports  returned null");
        return null;
    }

    @Override
    public List<Report> findReportsForPost(Long postId) {
        Optional<List<Report>> reports = reportRepository.findAllByOnPostId(postId);
        if (!reports.isEmpty())
            return reports.get();
        logger.error("Repository search for reports for post with id: " + postId + " returned null");
        return null;
    }

    @Override
    public List<Report> findReportsForComment(Long commentId) {
        Optional<List<Report>> reports = reportRepository.findAllByOnCommentId(commentId);
        if (!reports.isEmpty())
            return reports.get();
        logger.error("Repository search for reports for comment with id: " + commentId + " returned null");
        return null;
    }

    @Override
    public List<Report> findReportsForUser(Long userId) {
        Optional<List<Report>> reports = reportRepository.findAllByOnUserId(userId);
        if (!reports.isEmpty())
            return reports.get();
        logger.error("Repository search for reports for user with id: " + userId + " returned null");
        return null;
    }

    @Override
    public Report createReport(ReportDTO reportDTO) {
        Optional<Report> report = reportRepository.findById(reportDTO.getId());

        if (report.isPresent()) {
            logger.error("Report with id: " + reportDTO.getId() + " already exists in repository");
            return null;
        }

        Report newReport= new Report();
        newReport.setReason(ReportReason.valueOf(reportDTO.getReason()));
        newReport.setTimestamp(LocalDate.parse(reportDTO.getTimestamp()));
        newReport.setAccepted(reportDTO.getAccepted());

        User user = userService.findById(reportDTO.getByUserId());

        if (user == null) {
            logger.error("User with id: " + reportDTO.getByUserId() +
                    ", that made the report, was not found in the database");
            return null;
        }

        newReport.setByUser(user);

        if (reportDTO.getOnCommentId() != null) {
            Comment comment = commentService.findById(reportDTO.getOnCommentId());
            newReport.setOnComment(comment);
        }

        if (reportDTO.getOnPostId() != null) {
            Post post = postService.findById(reportDTO.getOnPostId());
            newReport.setOnPost(post);
        }

        if (reportDTO.getOnUserId() != null) {
            User user1 = userService.findById(reportDTO.getOnUserId());
            newReport.setOnUser(user1);
        }

        newReport.setDeleted(false);
        newReport = reportRepository.save(newReport);

        return newReport;
    }

    @Override
    public Integer deleteReport(Long id) {
        return reportRepository.deleteReportById(id);
    }

    @Override
    public Report updateReport() {
        return null;
    }

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Integer deletePostReports(Long postId) {
        return reportRepository.deleteReportsByOnPostId(postId);
    }

    @Override
    public Integer deleteCommentReports(Long commentId) {
        return reportRepository.deleteReportsByOnCommentId(commentId);
    }

    @Override
    public Integer deleteReportFromUser(Long userId) {
        return reportRepository.deleteReportsMadeByUserId(userId);
    }
}
