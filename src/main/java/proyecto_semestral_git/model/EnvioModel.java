package proyecto_semestral_git.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvioModel implements Serializable {
    private static final long serialVersionUID = 1L; 

    private int id;
    private String nombre; // Ej: "Envío Estándar", "Envío Express", "Retiro en Tienda"
    private int precio; // Costo de este método de envío
    private String tiempoEstimado; // Ej: "3-5 días hábiles", "24 horas"
}
