package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.UsuarioModel;
import proyecto_semestral_git.repository.UsuarioRepository; // Importamos el repositorio
import proyecto_semestral_git.dto.AuthRequestDTO;
import proyecto_semestral_git.dto.AuthResponseDTO;

import org.springframework.beans.factory.annotation.Autowired; // Inyección de dependencias
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Eliminamos ConcurrentHashMap y AtomicInteger

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    // Inyectamos el repositorio que conecta con la base de datos MySQL
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario) {
        
        // 1. Verificar si el nombre de usuario ya existe en la Base de Datos
        // Usamos el método mágico findByUsername que creamos en el repositorio
        Optional<UsuarioModel> existente = usuarioRepository.findByUsername(usuario.getUsername());
        
        if (existente.isPresent()) {
            // Retorna "Conflict" (409) si el usuario ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // 2. "Encriptar" la contraseña (simulación)
        // En una app real, usarías Spring Security y Bcrypt aquí.
        String passwordSimuladaHash = "hashed_pass_" + usuario.getPassword();
        usuario.setPassword(passwordSimuladaHash); // Guardamos la contraseña ya transformada
        
        // 3. Guardar el usuario en la Base de Datos
        // El ID se genera automáticamente gracias a @GeneratedValue en el modelo
        UsuarioModel usuarioGuardado = usuarioRepository.save(usuario);

        // Limpiamos la contraseña antes de devolver el objeto al cliente por seguridad
        // (Aunque @JsonProperty(access = WRITE_ONLY) ya ayuda con esto en el JSON)
        usuarioGuardado.setPassword(null);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/usuarios/obtener/" + usuarioGuardado.getId()));
        
        return new ResponseEntity<>(usuarioGuardado, headers, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión (autenticar).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        
        // 1. Buscar si el usuario existe en la Base de Datos
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(authRequest.getUsername());
        
        if (usuarioOpt.isEmpty()) {
            // Usuario no encontrado -> 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        UsuarioModel usuario = usuarioOpt.get();
        
        // 2. Verificar la contraseña
        // Simulamos el mismo hash que usamos al registrar
        String passwordIngresadaSimuladaHash = "hashed_pass_" + authRequest.getPassword();
        
        // Comparamos la contraseña de la BD con la que nos enviaron
        if (!usuario.getPassword().equals(passwordIngresadaSimuladaHash)) {
            // Contraseña incorrecta -> 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 3. El login es exitoso.
        // Generar un "token" simulado
        String token = UUID.randomUUID().toString();
        
        // Limpiamos la contraseña antes de enviarla en la respuesta
        usuario.setPassword(null);
        
        // 4. Devolver la respuesta exitosa con el token y los datos del usuario
        AuthResponseDTO response = new AuthResponseDTO(token, usuario);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/obtener/{id}")
    public ResponseEntity<UsuarioModel> obtener(@PathVariable int id) {
        Optional<UsuarioModel> u = usuarioRepository.findById(id);
        
        if (u.isPresent()) {
            return ResponseEntity.ok(u.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<UsuarioModel> listar() {
        return usuarioRepository.findAll();
    }
}