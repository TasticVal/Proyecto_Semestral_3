package proyecto_semestral_git.model;

import java.io.Serializable;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity // Convierte la clase en tabla
@Table(name = "pedidos") // Nombre de la tabla en la BD
public class PedidoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String nombreCliente;
    private String direccionCliente;

    // RELACIÓN: Un pedido tiene MUCHOS productos.
    // JPA creará automáticamente una tabla intermedia llamada 'pedido_productos'
    // para relacionar los IDs de los pedidos con los IDs de los productos.
    @ManyToMany
    @JoinTable(
        name = "pedido_productos", 
        joinColumns = @JoinColumn(name = "pedido_id"), 
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<ProductoModel> productos;
    
    // RELACIÓN: Un pedido tiene UN método de envío.
    // Se creará una columna 'envio_id' en la tabla 'pedidos'.
    @ManyToOne
    @JoinColumn(name = "envio_id")
    private EnvioModel metodoEnvio;

    private String estado;
    private int totalCalculado;
}