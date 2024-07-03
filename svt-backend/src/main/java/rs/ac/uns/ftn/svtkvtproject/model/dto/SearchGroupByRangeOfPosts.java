package rs.ac.uns.ftn.svtkvtproject.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchGroupByRangeOfPosts {
    private Integer greaterThan;
    private Integer lessThan;
}
