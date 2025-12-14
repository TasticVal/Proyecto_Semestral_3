package proyecto_semestral_git.controller;


import proyecto_semestral_git.model.FacturaModel;
import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.repository.FacturaRepository;
import proyecto_semestral_git.repository.PedidoRepository; // IMPORTANTE: Necesitamos buscar el pedido

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

    @Autowired
    private PedidoRepository pedidoRepository; // Inyectamos el repo de pedidos
    
    private final AtomicInteger numeroFacturaGenerator = new AtomicInteger(1);

    @PostMapping("/generar")
    public ResponseEntity<FacturaModel> generarFactura(@RequestBody PedidoModel pedido) {
        
        if (pedido == null || pedido.getId() == 0) {
            return ResponseEntity.badRequest().build();
        }
        
        // Verificamos si ya existe una factura para este pedido para no duplicar
        // (Esto es opcional pero recomendado)
        // List<FacturaModel> facturas = facturaRepository.findAll();
        // ... lógica de búsqueda ...

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
                
                // Estos datos se usan en la respuesta inmediata (JSON), pero NO se guardan en la tabla facturas
                .productos(pedido.getProductos()) 
                .metodoEnvio(pedido.getMetodoEnvio()) 
                
                .montoNeto(neto)
                .montoIva(iva)
                .totalPagado(total) 
                .build();

        FacturaModel facturaGuardada = facturaRepository.save(factura);
        
        // Al devolver, nos aseguramos que los productos sigan ahí (JPA a veces limpia los transient al guardar)
        facturaGuardada.setProductos(pedido.getProductos());
        facturaGuardada.setMetodoEnvio(pedido.getMetodoEnvio());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/facturas/obtener/" + facturaGuardada.getId()));
        
        return new ResponseEntity<>(facturaGuardada, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FacturaModel> listar() {
        return facturaRepository.findAll();
    }

    // --- CORRECCIÓN CLAVE AQUÍ ---
    @GetMapping("/obtener/{id}")
    public ResponseEntity<FacturaModel> obtener(@PathVariable int id) {
        Optional<FacturaModel> fOpt = facturaRepository.findById(id);
        
        if (fOpt.isPresent()) {
            FacturaModel factura = fOpt.get();
            
            // TRUCO: Como los productos son @Transient (no están en la tabla factura),
            // buscamos el Pedido original y los copiamos de vuelta para mostrarlos.
            Optional<PedidoModel> pedidoOpt = pedidoRepository.findById(factura.getPedidoId());
            
            if (pedidoOpt.isPresent()) {
                PedidoModel pedido = pedidoOpt.get();
                factura.setProductos(pedido.getProductos());
                factura.setMetodoEnvio(pedido.getMetodoEnvio());
            }
            
            return ResponseEntity.ok(factura);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}