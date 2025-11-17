
package proyecto_semestral_git.controller;

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
@RequestMapping("/productos")
public class ProductoController {
    private final ConcurrentMap<Integer, ProductoModel> repo = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @GetMapping
    public List<ProductoModel> listar() {
        return new ArrayList<>(repo.values());
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<ProductoModel> obtener(@PathVariable int id) {
        ProductoModel p = repo.get(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @PostMapping("/crear")
    public ResponseEntity<ProductoModel> crear(@RequestBody ProductoModel producto) {
        int id = idGenerator.getAndIncrement();
        producto.setId(id);
        repo.put(id, producto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/productos/" + id));
        return new ResponseEntity<>(producto, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoModel> actualizar(@PathVariable int id, @RequestBody ProductoModel producto) {
        ProductoModel existente = repo.get(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setDescripcion(producto.getDescripcion());
        repo.put(id, existente);
        return ResponseEntity.ok(existente);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        ProductoModel removed = repo.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
