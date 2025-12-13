package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.PedidoModel;
import proyecto_semestral_git.model.ProductoModel;
import proyecto_semestral_git.repository.PedidoRepository;
import proyecto_semestral_git.repository.ProductoRepository; // Necesario para buscar y actualizar el stock del producto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@CrossOrigin(origins = "*") // Permite que el Frontend HTML se conecte sin bloqueos
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Inyectamos el repositorio de productos para poder manipular el stock
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<PedidoModel> listar() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/obetener/{id}")
    public ResponseEntity<PedidoModel> obtener(@PathVariable int id) {
        Optional<PedidoModel> p = pedidoRepository.findById(id);
        
        if (p.isPresent()) {
            return ResponseEntity.ok(p.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear Pedido con VALIDACIÓN DE STOCK.
     * 1. Verifica si el producto existe.
     * 2. Verifica si hay stock disponible.
     * 3. Descuenta el stock.
     * 4. Guarda el pedido.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody PedidoModel pedido) {
        int total = 0;
        List<ProductoModel> productosProcesados = new ArrayList<>();

        if (pedido.getProductos() != null) {
            for (ProductoModel pInput : pedido.getProductos()) {
                Optional<ProductoModel> prodOpt = productoRepository.findById(pInput.getId());
                
                if (prodOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Error: Producto no encontrado con ID: " + pInput.getId());
                }

                ProductoModel prodReal = prodOpt.get();

                if (prodReal.getStock() < 1) {
                    return ResponseEntity.badRequest().body("Stock insuficiente para el producto: " + prodReal.getNombre());
                }

                prodReal.setStock(prodReal.getStock() - 1);
                
                productoRepository.save(prodReal);

                total += prodReal.getPrecio();
                
                productosProcesados.add(prodReal);
            }
        }
        
        pedido.setProductos(productosProcesados);

        if (pedido.getMetodoEnvio() != null) {
            total += pedido.getMetodoEnvio().getPrecio();
        }
        
        pedido.setTotalCalculado(total);
        
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
             pedido.setEstado("PENDIENTE");
        }

        PedidoModel pedidoGuardado = pedidoRepository.save(pedido);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/pedidos/" + pedidoGuardado.getId()));
        
        return new ResponseEntity<>(pedidoGuardado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar-estado/{id}")
    public ResponseEntity<PedidoModel> actualizarEstado(@PathVariable int id, @RequestBody String nuevoEstado) {
        Optional<PedidoModel> existenteOpt = pedidoRepository.findById(id);
        
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PedidoModel existente = existenteOpt.get();
        
        String estadoLimpio = nuevoEstado.replace("\"", "");
        existente.setEstado(estadoLimpio);
        
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