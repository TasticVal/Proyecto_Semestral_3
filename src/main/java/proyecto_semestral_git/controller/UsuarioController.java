package proyecto_semestral_git.controller;

import proyecto_semestral_git.model.UsuarioModel;
import proyecto_semestral_git.repository.UsuarioRepository;
import proyecto_semestral_git.dto.AuthRequestDTO;
import proyecto_semestral_git.dto.AuthResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*") // IMPORTANTE: Permite la conexión desde el Frontend (HTML)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    // Inyectamos el repositorio que conecta con MySQL
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para registrar un nuevo usuario en la BD.
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioModel> registrar(@RequestBody UsuarioModel usuario) {
        
        // 1. Verificar si el nombre de usuario ya existe
        Optional<UsuarioModel> existente = usuarioRepository.findByUsername(usuario.getUsername());
        
        if (existente.isPresent()) {
            // Retorna 409 Conflict si ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // 2. "Encriptar" la contraseña (simulación simple)
        String passwordSimuladaHash = "hashed_pass_" + usuario.getPassword();
        usuario.setPassword(passwordSimuladaHash); 
        
        // 3. Guardar en MySQL
        UsuarioModel usuarioGuardado = usuarioRepository.save(usuario);

        // Limpiamos la contraseña del objeto respuesta por seguridad
        usuarioGuardado.setPassword(null);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/usuarios/obtener/" + usuarioGuardado.getId()));
        
        return new ResponseEntity<>(usuarioGuardado, headers, HttpStatus.CREATED);
    }

    /**
     * Endpoint para iniciar sesión (Login).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        
        // 1. Buscar usuario en BD por username
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByUsername(authRequest.getUsername());
        
        if (usuarioOpt.isEmpty()) {
            // Usuario no encontrado -> 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        UsuarioModel usuario = usuarioOpt.get();
        
        // 2. Verificar contraseña (simulando el hash)
        String passwordIngresadaHash = "hashed_pass_" + authRequest.getPassword();
        
        if (!usuario.getPassword().equals(passwordIngresadaHash)) {
            // Contraseña incorrecta -> 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 3. Login exitoso -> Generar Token
        String token = UUID.randomUUID().toString();
        
        // Limpiamos password antes de enviar
        usuario.setPassword(null);
        
        AuthResponseDTO response = new AuthResponseDTO(token, usuario);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/obtener/{id}")
    public ResponseEntity<UsuarioModel> obtener(@PathVariable int id) {
        Optional<UsuarioModel> u = usuarioRepository.findById(id);
        
        return u.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UsuarioModel> listar() {
        return usuarioRepository.findAll();
    }
}