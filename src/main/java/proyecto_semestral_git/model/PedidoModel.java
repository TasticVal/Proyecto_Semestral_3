package proyecto_semestral_git.model;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDateTime; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
@Entity 
@Table(name = "pedidos") 
public class PedidoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String nombreCliente;
    private String direccionCliente;

    @ManyToMany
    @JoinTable(
        name = "pedido_productos", 
        joinColumns = @JoinColumn(name = "pedido_id"), 
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<ProductoModel> productos;
    
    @ManyToOne
    @JoinColumn(name = "envio_id")
    private EnvioModel metodoEnvio;
    private String estado;
    private int totalCalculado;
    private LocalDateTime fechaDespacho;
}