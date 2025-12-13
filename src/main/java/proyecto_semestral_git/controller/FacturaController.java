package proyecto_semestral_git.controller;


import proyecto_semestral_git.model.FacturaModel;
import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.repository.FacturaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;
    
    private final AtomicInteger numeroFacturaGenerator = new AtomicInteger(1);

    @PostMapping("/generar")
    public ResponseEntity<FacturaModel> generarFactura(@RequestBody PedidoModel pedido) {
        
        if (pedido == null || pedido.getId() == 0) {
            return ResponseEntity.badRequest().build();
        }
        
        String numeroFactura = "F-" + String.format("%04d", numeroFacturaGenerator.getAndIncrement());

        int total = pedido.getTotalCalculado();
        int neto = (int) Math.round(total / 1.19);
        int iva = total - neto;

        FacturaModel factura = FacturaModel.builder()
                .numeroFactura(numeroFactura)
                .fechaEmision(LocalDateTime.now())
                .pedidoId(pedido.getId()) 
                .nombreCliente(pedido.getNombreCliente())
                .direccionCliente(pedido.getDireccionCliente())
                .productos(pedido.getProductos()) 
                .metodoEnvio(pedido.getMetodoEnvio())             
                .montoNeto(neto)
                .montoIva(iva)
                .totalPagado(total) 
                .build();

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