package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.model.ProductoModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final ConcurrentMap<Integer, PedidoModel> repo = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @GetMapping
    public List<PedidoModel> listar() {
        return new ArrayList<>(repo.values());
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<PedidoModel> obtener(@PathVariable int id) {
        PedidoModel p = repo.get(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PostMapping("/crear")
    public ResponseEntity<PedidoModel> crear(@RequestBody PedidoModel pedido) {
        int id = idGenerator.getAndIncrement();
        pedido.setId(id);
        

        int total = 0;
        if (pedido.getProductos() != null) {
            for (ProductoModel producto : pedido.getProductos()) {
                total += producto.getPrecio();
            }
        }
        if (pedido.getMetodoEnvio() != null) {
            total += pedido.getMetodoEnvio().getPrecio();
        }
        pedido.setTotalCalculado(total);
        
        
        pedido.setEstado("PENDIENTE");
        

        repo.put(id, pedido);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/pedidos/" + id));
        
        return new ResponseEntity<>(pedido, headers, HttpStatus.CREATED);
    }


    @PutMapping("/actualizar-estado/{id}")
    public ResponseEntity<PedidoModel> actualizarEstado(@PathVariable int id, @RequestBody String nuevoEstado) {
        PedidoModel existente = repo.get(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        
        existente.setEstado(nuevoEstado);
        repo.put(id, existente);
        return ResponseEntity.ok(existente);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        PedidoModel removed = repo.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
