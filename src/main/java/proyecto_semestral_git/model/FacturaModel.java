package proyecto_semestral_git.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class FacturaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; 
    private String numeroFactura; 
    private LocalDateTime fechaEmision;
    
    
    private int pedidoId; 
    

    
    private String nombreCliente;
    private String direccionCliente;

    
    private List<ProductoModel> productos;
    
    
    private EnvioModel metodoEnvio;

    
    private int totalPagado;
    
    
}
