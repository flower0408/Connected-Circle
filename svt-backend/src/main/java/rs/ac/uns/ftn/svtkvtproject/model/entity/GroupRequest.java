package rs.ac.uns.ftn.svtkvtproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_request")
@SQLDelete(sql = "update group_request set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class GroupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean approved;

    @Column
    private LocalDateTime createdAt;

    @Column //odnosi se na trenutak kada je zahtev prihvacen
    private LocalDateTime at;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "for_group_id", referencedColumnName = "id", nullable = false)
    private Group forGroup;
}
