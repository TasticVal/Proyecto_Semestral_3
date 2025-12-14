package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.EnvioModel;
import proyecto_semestral_git.repository.EnvioRepository; // Importamos el repositorio de envíos

import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// Eliminamos ConcurrentHashMap y AtomicInteger
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/envios")
public class EnvioController {
    
    // Inyectamos el repositorio que conecta con MySQL
    @Autowired
    private EnvioRepository envioRepository;

    @GetMapping
    public List<EnvioModel> listar() {
        // Obtenemos todos los métodos de envío de la base de datos
        return envioRepository.findAll();
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<EnvioModel> obtener(@PathVariable int id) {
        // Buscamos por ID en la BD
        Optional<EnvioModel> e = envioRepository.findById(id);
        
        if (e.isPresent()) {
            return ResponseEntity.ok(e.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<EnvioModel> crear(@RequestBody EnvioModel envio) {
        // Guardamos en la base de datos.
        // El ID se genera automáticamente gracias a @GeneratedValue en el modelo.
        EnvioModel envioGuardado = envioRepository.save(envio);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/envios/" + envioGuardado.getId()));
        
        return new ResponseEntity<>(envioGuardado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EnvioModel> actualizar(@PathVariable int id, @RequestBody EnvioModel envio) {
        // Primero verificamos si existe en la BD
        Optional<EnvioModel> existenteOpt = envioRepository.findById(id);
        
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        EnvioModel existente = existenteOpt.get();

        // Actualizamos los campos
        existente.setNombre(envio.getNombre());
        existente.setPrecio(envio.getPrecio());
        existente.setTiempoEstimado(envio.getTiempoEstimado());
        
        // Guardamos los cambios
        EnvioModel actualizado = envioRepository.save(existente);
        
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        // Verificamos si existe antes de borrar
        if (!envioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        // Borramos de la BD
        envioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}