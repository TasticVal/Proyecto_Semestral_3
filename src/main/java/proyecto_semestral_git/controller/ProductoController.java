package proyecto_semestral_git.controller;


import proyecto_semestral_git.model.ProductoModel;
import proyecto_semestral_git.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Permite conexión con el Frontend
@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<ProductoModel> listar() {
        return productoRepository.findAll();
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<ProductoModel> obtener(@PathVariable int id) {
        Optional<ProductoModel> p = productoRepository.findById(id);
        
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<ProductoModel> crear(@RequestBody ProductoModel producto) {
        ProductoModel productoGuardado = productoRepository.save(producto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/productos/" + productoGuardado.getId()));
        
        return new ResponseEntity<>(productoGuardado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoModel> actualizar(@PathVariable int id, @RequestBody ProductoModel producto) {
        Optional<ProductoModel> existenteOpt = productoRepository.findById(id);
        
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ProductoModel existente = existenteOpt.get();

        // Actualizamos los campos
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setDescripcion(producto.getDescripcion());
        
        // --- ¡ESTA LÍNEA ES LA QUE FALTABA! ---
        existente.setStock(producto.getStock()); 
        
        ProductoModel actualizado = productoRepository.save(existente);
        
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}