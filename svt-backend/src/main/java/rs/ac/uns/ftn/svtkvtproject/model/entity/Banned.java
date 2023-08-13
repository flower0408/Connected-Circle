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

   // @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "by_admin_id")
    //private Administrator admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "by_group_admin_id", referencedColumnName = "id")
    private GroupAdmin groupAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "towards_user_id", referencedColumnName = "id")
    private User towardsUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_group_id", referencedColumnName = "id")
    private Group group;

    @Column(nullable = false)
    private boolean isDeleted;
}
