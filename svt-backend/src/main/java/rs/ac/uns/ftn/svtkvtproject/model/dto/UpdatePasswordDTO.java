package rs.ac.uns.ftn.svtkvtproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//dto za promenu lozinke
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {

    private String oldPassword;
    private String newPassword;
}
