package proyecto_semestral_git.dto;

import proyecto_semestral_git.model.UsuarioModel;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object (DTO) para la respuesta de Login.
 * Devuelve un "token" (simulado) y la información del usuario (sin la contraseña).
 */
@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UsuarioModel usuario;
}
