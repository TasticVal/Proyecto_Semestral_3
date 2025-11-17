package proyecto_semestral_git.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username; // Nombre de usuario para login
    private String email;

    // Esta propiedad se usará para recibir la contraseña al crear,
    // pero no se enviará de vuelta en los JSONs de respuesta.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}

