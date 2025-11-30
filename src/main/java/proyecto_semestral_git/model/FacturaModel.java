package proyecto_semestral_git.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

// IMPORTACIONES JPA
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity // Convierte en tabla
@Table(name = "facturas") // Nombre de la tabla
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

    // @Transient: Indica que esta lista NO se guardará en una columna de la base de datos.
    // Esto evita errores complejos de mapeo, ya que guardar un historial exacto de productos
    // requiere tablas adicionales de "DetalleFactura".
    @Transient
    private List<ProductoModel> productos;
    
    // @Transient: Lo mismo para el envío, no se guarda como columna en la tabla facturas.
    @Transient
    private EnvioModel metodoEnvio;

    private int totalPagado;
}