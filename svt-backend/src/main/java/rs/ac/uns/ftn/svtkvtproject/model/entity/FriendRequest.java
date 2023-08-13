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
@Table(name = "friend_request")
@SQLDelete(sql = "update friend_request set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean approved;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private boolean isDeleted;

    @Column //odnosi se na trenutak kada je zahtev prihvacen
    private LocalDateTime at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", referencedColumnName = "id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", referencedColumnName = "id", nullable = false)
    private User toUser;
}
