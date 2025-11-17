package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.FacturaModel;
import proyecto_semestral_git.model.PedidoModel; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final ConcurrentMap<Integer, FacturaModel> repo = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    
    private final AtomicInteger numeroFacturaGenerator = new AtomicInteger(1);


    @PostMapping("/generar")
    public ResponseEntity<FacturaModel> generarFactura(@RequestBody PedidoModel pedido) {
        
       
        if (pedido == null || pedido.getId() == 0) {
            
            return ResponseEntity.badRequest().build();
        }
        

        int idFactura = idGenerator.getAndIncrement();
        String numeroFactura = "F-" + String.format("%04d", numeroFacturaGenerator.getAndIncrement());

        FacturaModel factura = FacturaModel.builder()
                .id(idFactura)
                .numeroFactura(numeroFactura)
                .fechaEmision(LocalDateTime.now())
                .pedidoId(pedido.getId()) 
                .nombreCliente(pedido.getNombreCliente())
                .direccionCliente(pedido.getDireccionCliente())
                .productos(pedido.getProductos()) 
                .metodoEnvio(pedido.getMetodoEnvio()) 
                .totalPagado(pedido.getTotalCalculado()) 
                .build();

        
        repo.put(idFactura, factura);
        
 

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facturas/obtener/" + idFactura));
        
        return new ResponseEntity<>(factura, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FacturaModel> listar() {
        return new ArrayList<>(repo.values());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<FacturaModel> obtener(@PathVariable int id) {
        FacturaModel f = repo.get(id);
        if (f == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(f);
    }
    

}
