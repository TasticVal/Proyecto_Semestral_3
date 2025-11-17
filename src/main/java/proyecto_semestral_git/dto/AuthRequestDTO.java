package proyecto_semestral_git.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para la solicitud de Login.
 * Solo contiene los campos necesarios para la autenticación.
 */
@Data
@NoArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String password;
}
