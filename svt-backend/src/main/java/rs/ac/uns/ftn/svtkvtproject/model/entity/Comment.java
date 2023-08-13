package rs.ac.uns.ftn.svtkvtproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@SQLDelete(sql = "update comment set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String text;

    @Column(nullable = false)
    private LocalDate timestamp;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to_user_id", referencedColumnName = "id", nullable = false)
    private User belongsToUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belongs_to_post_id", referencedColumnName = "id")
    private Post belongsToPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replies_to_comment_id", referencedColumnName = "id")
    private Comment repliesTo;

    //    Associations

   /* @ManyToOne
    private Post post;

    @ManyToOne
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> replies = new HashSet<Comment>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Reaction> reactions = new HashSet<Reaction>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "id")
    private Set<Report> reports = new HashSet<Report>();*/
}
