package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.model.ProductoModel;
import proyecto_semestral_git.repository.PedidoRepository; // Importamos el nuevo repositorio

import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// Eliminamos ConcurrentHashMap y AtomicInteger

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    // Inyectamos el repositorio que conecta con la base de datos
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public List<PedidoModel> listar() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<PedidoModel> obtener(@PathVariable int id) {
        // findById ahora devuelve un Optional
        Optional<PedidoModel> p = pedidoRepository.findById(id);
        
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<PedidoModel> crear(@RequestBody PedidoModel pedido) {
        // --- Lógica de Negocio ---
        // Calculamos el total antes de guardar
        int total = 0;
        
        // Importante: Al recibir el pedido, los productos solo traerán su ID.
        // JPA se encargará de vincularlos si existen en la BD.
        if (pedido.getProductos() != null) {
            for (ProductoModel producto : pedido.getProductos()) {
                // Nota: En un entorno real, deberías buscar el precio actual del producto en la BD
                // para evitar que el cliente envíe un precio falso.
                // Por simplicidad, asumimos que el precio viene en el objeto o lo sumamos así.
                total += producto.getPrecio();
            }
        }
        
        if (pedido.getMetodoEnvio() != null) {
            total += pedido.getMetodoEnvio().getPrecio();
        }
        
        pedido.setTotalCalculado(total);
        
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
             pedido.setEstado("PENDIENTE");
        }
        // -------------------------

        // Guardamos en la base de datos.
        // JPA generará el ID y guardará las relaciones en 'pedido_productos'.
        PedidoModel pedidoGuardado = pedidoRepository.save(pedido);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/pedidos/" + pedidoGuardado.getId()));
        
        return new ResponseEntity<>(pedidoGuardado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar-estado/{id}")
    public ResponseEntity<PedidoModel> actualizarEstado(@PathVariable int id, @RequestBody String nuevoEstado) {
        // Buscamos en la BD
        Optional<PedidoModel> existenteOpt = pedidoRepository.findById(id);
        
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PedidoModel existente = existenteOpt.get();
        
        // Actualizamos y guardamos
        existente.setEstado(nuevoEstado);
        PedidoModel actualizado = pedidoRepository.save(existente);
        
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}