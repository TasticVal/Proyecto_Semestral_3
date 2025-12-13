package proyecto_semestral_git.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "facturas")
public class FacturaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; 
    
    private String numeroFactura; 
    private LocalDateTime fechaEmision;
    
    private int pedidoId; 
    
    private String nombreCliente;
    private String direccionCliente;

    @Transient
    private List<ProductoModel> productos;
    
    @Transient
    private EnvioModel metodoEnvio;

    
    private int montoNeto; 
    private int montoIva;  
    private int totalPagado; 
}