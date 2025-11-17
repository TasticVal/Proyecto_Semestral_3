package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.EnvioModel; 
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
@RequestMapping("/envios") 
public class EnvioController {
    
    private final ConcurrentMap<Integer, EnvioModel> repo = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @GetMapping
    public List<EnvioModel> listar() {
        return new ArrayList<>(repo.values());
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<EnvioModel> obtener(@PathVariable int id) {
        EnvioModel e = repo.get(id);
        if (e == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(e);
    }

    @PostMapping("/crear")
    public ResponseEntity<EnvioModel> crear(@RequestBody EnvioModel envio) {
        int id = idGenerator.getAndIncrement();
        envio.setId(id);
        repo.put(id, envio);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/envios/" + id)); 
        
        return new ResponseEntity<>(envio, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EnvioModel> actualizar(@PathVariable int id, @RequestBody EnvioModel envio) {
        EnvioModel existente = repo.get(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

       
        existente.setNombre(envio.getNombre());
        existente.setPrecio(envio.getPrecio());
        existente.setTiempoEstimado(envio.getTiempoEstimado()); 
        
        repo.put(id, existente);
        return ResponseEntity.ok(existente);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        EnvioModel removed = repo.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
