package rs.ac.uns.ftn.svtkvtproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banned")
@SQLDelete(sql = "update banned set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Banned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "by_admin_id", referencedColumnName = "id", nullable = false)
    private User byAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "towards_user_id", referencedColumnName = "id", nullable = false)
    private User towardsUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @Column(nullable = false)
    private boolean blocked;

    @Column(nullable = false)
    private boolean isDeleted;

    public Long getTowardsUserId() {
        return towardsUser != null ? towardsUser.getId() : null;
    }
}
