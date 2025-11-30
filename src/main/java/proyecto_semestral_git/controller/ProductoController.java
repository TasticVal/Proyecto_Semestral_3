package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.ProductoModel;
import proyecto_semestral_git.repository.ProductoRepository; // Importamos el repositorio

import org.springframework.beans.factory.annotation.Autowired; // Para inyección de dependencias
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// Eliminamos las importaciones de ConcurrentHashMap y AtomicInteger

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    // Inyectamos el repositorio que conecta con MySQL
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<ProductoModel> listar() {
        // Usamos el método findAll() del repositorio JPA
        return productoRepository.findAll();
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<ProductoModel> obtener(@PathVariable int id) {
        // Buscamos en la base de datos por ID
        Optional<ProductoModel> p = productoRepository.findById(id);
        
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<ProductoModel> crear(@RequestBody ProductoModel producto) {
        // Guardamos en la base de datos.
        // El ID se genera automáticamente en la BD, así que no necesitamos AtomicInteger.
        ProductoModel productoGuardado = productoRepository.save(producto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/productos/" + productoGuardado.getId()));
        
        return new ResponseEntity<>(productoGuardado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoModel> actualizar(@PathVariable int id, @RequestBody ProductoModel producto) {
        // Primero verificamos si existe en la BD
        Optional<ProductoModel> existenteOpt = productoRepository.findById(id);
        
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProductoModel existente = existenteOpt.get();

        // Actualizamos los campos
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setDescripcion(producto.getDescripcion());
        
        // Guardamos los cambios
        ProductoModel actualizado = productoRepository.save(existente);
        
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        // Verificamos si existe antes de borrar
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        // Borramos de la BD
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}