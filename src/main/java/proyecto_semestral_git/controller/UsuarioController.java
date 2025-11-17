package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.UsuarioModel;
import proyecto_semestral_git.dto.AuthRequestDTO;
import proyecto_semestral_git.dto.AuthResponseDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    // Repositorio en memoria para usuarios.
    // La clave será el ID (Integer), pero también necesitaremos buscar por username (String)
    private final ConcurrentMap<Integer, UsuarioModel> repoById = new ConcurrentHashMap<>();
    
    // Un "índice" simple para buscar por nombre de usuario y guardar contraseñas
    // En una app real, la base de datos haría esto.
    private final ConcurrentMap<String, String> repoPasswords = new ConcurrentHashMap<>();
    
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    /**
     * Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario) {
        
        // 1. Verificar si el nombre de usuario ya existe
        if (repoPasswords.containsKey(usuario.getUsername())) {
            // Retorna "Conflict" (409) si el usuario ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        int id = idGenerator.getAndIncrement();
        usuario.setId(id);
        
        // 2. "Encriptar" la contraseña (simulación)
        // En una app real, usarías Spring Security y Bcrypt
        // NUNCA guardes contraseñas en texto plano.
        String passwordSimuladaHash = "hashed_pass_" + usuario.getPassword();
        
        // 3. Guardar el usuario
        // Guardamos la contraseña "hasheada" en nuestro repo de contraseñas
        repoPasswords.put(usuario.getUsername(), passwordSimuladaHash);
        
        // Guardamos el modelo de usuario (sin la contraseña) en el repo principal
        usuario.setPassword(null); // Limpiamos la contraseña del modelo
        repoById.put(id, usuario);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/usuarios/obtener/" + id));
        
        return new ResponseEntity<>(usuario, headers, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión (autenticar).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        
        // 1. Buscar si el usuario existe
        String passwordSimuladaHash = repoPasswords.get(authRequest.getUsername());
        if (passwordSimuladaHash == null) {
            // No encontrado (404) o No autorizado (401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 2. Verificar la contraseña (simulación)
        String passwordIngresadaSimuladaHash = "hashed_pass_" + authRequest.getPassword();
        
        if (!passwordSimuladaHash.equals(passwordIngresadaSimuladaHash)) {
            // Contraseña incorrecta
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 3. El login es exitoso. Buscar el UsuarioModel completo
        Optional<UsuarioModel> usuarioOpt = repoById.values().stream()
                .filter(u -> u.getUsername().equals(authRequest.getUsername()))
                .findFirst();

        if (usuarioOpt.isEmpty()) {
            // Esto sería un error interno, pero por si acaso
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 4. Generar un "token" simulado
        String token = UUID.randomUUID().toString();
        
        // 5. Devolver la respuesta exitosa
        AuthResponseDTO response = new AuthResponseDTO(token, usuarioOpt.get());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/obtener/{id}")
    public ResponseEntity<UsuarioModel> obtener(@PathVariable int id) {
        UsuarioModel u = repoById.get(id);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @GetMapping
    public List<UsuarioModel> listar() {
        return new ArrayList<>(repoById.values());
    }

    // Faltarían PUT (actualizar perfil) y DELETE, pero esto cubre el login.
}
