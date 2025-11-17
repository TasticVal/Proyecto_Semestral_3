
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
public class ProductoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private int precio;
    private String descripcion;
}
