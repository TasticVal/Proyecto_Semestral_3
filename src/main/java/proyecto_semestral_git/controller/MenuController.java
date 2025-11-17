package proyecto_semestral_git.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador para el "Menú Principal" o "Endpoint Raíz" de la API.
 * Proporciona una guía simple de los endpoints disponibles.
 */
@RestController
@RequestMapping("/") // Se mapea a la raíz: http://localhost:8080/
public class MenuController {

    /**
     * Muestra el "menú" principal de la API.
     * @return Un mapa con los principales recursos de la API y sus rutas.
     */
    @GetMapping
    public Map<String, String> menuPrincipal() {
        // Usamos LinkedHashMap para mantener el orden de inserción
        Map<String, String> menu = new LinkedHashMap<>();
        
        menu.put("mensaje", "Bienvenido a la API de tu Proyecto Semestral");
        menu.put("usuarios", "/usuarios");
        menu.put("productos", "/productos");
        menu.put("envios", "/envios");
        menu.put("pedidos", "/pedidos");
        menu.put("facturas", "/facturas");

        return menu;
    }

    // Nota: Si quieres, puedes agregar más @GetMapping para otras rutas
    // Por ejemplo, un @GetMapping("/health") que retorne {"status": "OK"}
}
