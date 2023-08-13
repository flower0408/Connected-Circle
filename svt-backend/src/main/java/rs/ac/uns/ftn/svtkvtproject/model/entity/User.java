package rs.ac.uns.ftn.svtkvtproject.model.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import rs.ac.uns.ftn.svtkvtproject.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@SQLDelete(sql = "update `user` set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "display_name")
    private String displayName;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean isAdmin;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_friends", inverseJoinColumns=@JoinColumn(name="friend_id"))
    private List<User> friends;


    //  Associations
/*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Post> posts = new HashSet<Post>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<GroupAdmin> groupAdmins = new HashSet<GroupAdmin>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Banned> bans = new HashSet<Banned>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Comment> comments = new HashSet<Comment>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<FriendRequest> friendRequestsFrom = new HashSet<FriendRequest>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<FriendRequest> friendRequestsTo = new HashSet<FriendRequest>();

    @OneToMany
    private Set<Report> reports = new HashSet<Report>();

    @OneToOne
    private Image profileImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<GroupRequest> groupRequests = new HashSet<GroupRequest>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Reaction> reactions = new HashSet<Reaction>();*/
}
