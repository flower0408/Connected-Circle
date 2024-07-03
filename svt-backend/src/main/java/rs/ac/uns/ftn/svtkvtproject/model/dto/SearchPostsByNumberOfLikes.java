package rs.ac.uns.ftn.svtkvtproject.model.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchPostsByNumberOfLikes {
    Integer greaterThan;
    Integer lessThan;
}
