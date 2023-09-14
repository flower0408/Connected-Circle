package rs.ac.uns.ftn.svtkvtproject.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;

@Entity
@Table(name = "group_admin")
@SQLDelete(sql = "update group_admin set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
@Setter
@Getter
@RequiredArgsConstructor
public class GroupAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isDeleted;


}


