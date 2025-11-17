package proyecto_semestral_git.model;

import java.io.Serializable;
import java.util.List; // Importante: para guardar varios productos

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
public class PedidoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    
    // Datos del cliente
    private String nombreCliente;
    private String direccionCliente;

    // Los productos que el cliente agregó al carrito
    private List<ProductoModel> productos;
    
    // El método de envío que el cliente seleccionó
    private EnvioModel metodoEnvio;

    // Estado del pedido
    private String estado; // Ej: "PENDIENTE", "ENVIADO", "ENTREGADO"

    // El costo total, que calcularemos
    private int totalCalculado;
}