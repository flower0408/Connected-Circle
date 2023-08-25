package rs.ac.uns.ftn.svtkvtproject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svtkvtproject.model.dto.PostDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtkvtproject.model.dto.UserDTO;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Image;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Post;
import rs.ac.uns.ftn.svtkvtproject.model.entity.Report;
import rs.ac.uns.ftn.svtkvtproject.model.entity.User;
import rs.ac.uns.ftn.svtkvtproject.model.enums.ReportReason;
import rs.ac.uns.ftn.svtkvtproject.security.TokenUtils;
import rs.ac.uns.ftn.svtkvtproject.service.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/reports")
public class ReportController {

    ReportService reportService;

    PostService postService;

    CommentService commentService;

    UserService userService;

    AuthenticationManager authenticationManager;

    TokenUtils tokenUtils;

    private static final Logger logger = LogManager.getLogger(ReactionController.class);

    @Autowired
    public ReportController(ReportService reportService, PostService postService, CommentService commentService,
                              UserService userService, AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.reportService = reportService;
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping()
    public ResponseEntity<List<ReportDTO>> getAll(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all reports");
        List<Report> reports = reportService.findAll();
        List<ReportDTO> reportDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Report report: reports) {
            reportDTOS.add(new ReportDTO(report));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReportDTO>> getAllReports(@RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding all reports");
        List<Report> reports = reportService.findAllReports();
        List<ReportDTO> reportDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Report report: reports) {
            reportDTOS.add(new ReportDTO(report));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getOne(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding report for id: " + id);
        Report report = reportService.findById(Long.parseLong(id));
        if (report == null) {
            logger.error("Report not found for id: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating response");
        ReportDTO reportDTO = new ReportDTO(report);
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<ReportDTO>> getReportsForPost(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reports for post with id: " + id);
        List<Report> reports = reportService.findReportsForPost(Long.parseLong(id));
        List<ReportDTO> reportDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Report report: reports) {
            reportDTOS.add(new ReportDTO(report));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<List<ReportDTO>> getReportsForComment(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reports for comment with id: " + id);
        List<Report> reports = reportService.findReportsForComment(Long.parseLong(id));
        List<ReportDTO> reportDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Report report: reports) {
            reportDTOS.add(new ReportDTO(report));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReportDTO>> getReportsForUser(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding reports for user with id: " + id);
        List<Report> reports = reportService.findReportsForUser(Long.parseLong(id));
        List<ReportDTO> reportDTOS = new ArrayList<>();
        logger.info("Creating response");
        for (Report report: reports) {
            reportDTOS.add(new ReportDTO(report));
        }
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ReportDTO> addReport(@RequestBody @Validated ReportDTO newReport, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Creating report from DTO");

        // Map the report reason string to the enum
        logger.info("Selected reason: " + newReport.getReason());
        ReportReason reportReason = getReportReasonFromString(newReport.getReason());
        if (reportReason == null) {
            logger.error("Invalid report reason");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Now you can use reportReason to create the Report instance
        Report createdReport = reportService.createReport(newReport);

        if (createdReport == null) {
            logger.error("Report couldn't be created from DTO");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("Creating response");
        ReportDTO reportDTO = new ReportDTO(createdReport);
        logger.info("Created and sent response");

        return new ResponseEntity<>(reportDTO, HttpStatus.CREATED);
    }
    public ReportReason getReportReasonFromString(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return null; // or a default enum value, depending on your business logic
        }
       // String trimmedReason = reason.trim(); // Trim the reason string
        switch (reason.trim()) {
            case "BREAKS_RULES":
                return ReportReason.BREAKS_RULES;
            case "HARASSMENT":
                return ReportReason.HARASSMENT;
            case "HATE":
                return ReportReason.HATE;
            case "SHARING_PERSONAL_INFORMATION":
                return ReportReason.SHARING_PERSONAL_INFORMATION;
            case "IMPERSONATION":
                return ReportReason.IMPERSONATION;
            case "COPYRIGHT_VIOLATION":
                return ReportReason.COPYRIGHT_VIOLATION;
            case "TRADEMARK_VIOLATION":
                return ReportReason.TRADEMARK_VIOLATION;
            case "SPAM":
                return ReportReason.SPAM;
            case "SELF_HARM_OR_SUICIDE":
                return ReportReason.SELF_HARM_OR_SUICIDE;
            case "OTHER":
                return ReportReason.OTHER;
            default:
                return null; // Invalid reason
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReaction(@PathVariable String id, @RequestHeader("authorization") String token) {
        logger.info("Checking authorization");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Deleting report with id: " + id);
        Integer deleted = reportService.deleteReport(Long.parseLong(id));
        if (deleted != 0) {
            logger.info("Successfully deleted report with id: " + id);
            return new ResponseEntity(deleted, HttpStatus.NO_CONTENT);
        }
        logger.error("Failed to delete report with id: " + id);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/edit/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ReportDTO> editUser(@PathVariable String id,@RequestBody @Validated ReportDTO editedReport, @RequestHeader("authorization") String token) {
        logger.info("Authentication check");
        String cleanToken = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(cleanToken);
        User user = userService.findByUsername(username);
        //User user1 = this.userService.findByUsername(user.getName());
        if (user == null) {
            logger.error("User not found with token: " + cleanToken);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Finding original report with id: " + editedReport.getId());
        Report oldReport = reportService.findById(Long.parseLong(id));
        if (oldReport == null) {
            logger.error("Original report not found with id: " + editedReport.getId());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        logger.info("Applying changes of report");
        if (editedReport.getAccepted() != null) {
            oldReport.setAccepted(editedReport.getAccepted());

            if (oldReport.getAccepted()) {
                if (oldReport.getOnPostId() != null) {
                    // Mark the reported post as deleted
                    Integer deleteResult = postService.deletePost(oldReport.getOnPostId());
                    if (deleteResult > 0) {
                        // Successfully marked as deleted
                    } else {
                        // Handle error (if needed)
                    }
                }

                if (oldReport.getOnCommentId() != null) {
                    // Mark the reported comment as deleted
                    Integer deleteResult = commentService.deleteComment(oldReport.getOnCommentId());
                    if (deleteResult > 0) {
                        // Successfully marked as deleted
                    } else {
                        // Handle error (if needed)
                    }
                }
            }
        }
        oldReport = reportService.saveReport(oldReport);
        logger.info("Creating response");
        ReportDTO updatedReport = new ReportDTO(oldReport);
        logger.info("Created and sent response");

        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

}
