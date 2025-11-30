package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.FacturaModel;
import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.repository.FacturaRepository; // Importamos el repositorio de facturas
// (Opcional) Si quieres actualizar el estado del pedido, necesitarías importar PedidoRepository también

import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

// Eliminamos ConcurrentHashMap

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    // Inyectamos el repositorio de Facturas
    @Autowired
    private FacturaRepository facturaRepository;
    
    // Mantenemos este generador solo para crear el STRING bonito "F-0001", "F-0002"
    // (El ID numérico interno lo manejará la base de datos)
    private final AtomicInteger numeroFacturaGenerator = new AtomicInteger(1);


    @PostMapping("/generar")
    public ResponseEntity<FacturaModel> generarFactura(@RequestBody PedidoModel pedido) {
        
        // Validación simple
        if (pedido == null || pedido.getId() == 0) {
            // No podemos facturar un pedido que no existe o no es válido
            return ResponseEntity.badRequest().build();
        }
        
        // Generamos el número de factura legible (ej: F-0001)
        // Nota: En un sistema real, esto también podría sacarse de una secuencia de BD.
        String numeroFactura = "F-" + String.format("%04d", numeroFacturaGenerator.getAndIncrement());

        // Construimos el objeto Factura (Snapshot del pedido)
        FacturaModel factura = FacturaModel.builder()
                // .id(idFactura) -> YA NO LO ASIGNAMOS, MySQL lo generará
                .numeroFactura(numeroFactura)
                .fechaEmision(LocalDateTime.now())
                .pedidoId(pedido.getId()) 
                .nombreCliente(pedido.getNombreCliente())
                .direccionCliente(pedido.getDireccionCliente())
                
                // NOTA IMPORTANTE:
                // Como marcamos 'productos' y 'metodoEnvio' como @Transient en el modelo,
                // estos datos viajarán en el JSON de respuesta al cliente,
                // PERO NO se guardarán en la tabla 'facturas' de la BD.
                // La BD solo guardará 'totalPagado' y 'pedidoId'.
                .productos(pedido.getProductos()) 
                .metodoEnvio(pedido.getMetodoEnvio()) 
                .totalPagado(pedido.getTotalCalculado()) 
                .build();

        // Guardamos en MySQL
        FacturaModel facturaGuardada = facturaRepository.save(factura);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facturas/obtener/" + facturaGuardada.getId()));
        
        return new ResponseEntity<>(facturaGuardada, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FacturaModel> listar() {
        return facturaRepository.findAll();
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<FacturaModel> obtener(@PathVariable int id) {
        Optional<FacturaModel> f = facturaRepository.findById(id);
        
        if (f.isPresent()) {
            return ResponseEntity.ok(f.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}