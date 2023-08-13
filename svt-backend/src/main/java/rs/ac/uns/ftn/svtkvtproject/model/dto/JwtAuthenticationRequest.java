package rs.ac.uns.ftn.svtkvtproject.model.dto;
// DTO za login
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

