package rs.ac.uns.ftn.svtkvtproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@SQLDelete(sql = "update post set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_user_id", referencedColumnName = "id")
    private User postedBy;

    @Column(nullable = false)
    private boolean isDeleted;

    //  Associations
/*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<Comment>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
    private Set<Reaction> reactions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
    private Set<Report> reports = new HashSet<Report>();

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Image> images = new HashSet<>();

    @ManyToOne
    private Group postedInGroup;*/
}
