package proyecto_semestral_git.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_semestral_git.model.UsuarioModel;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    
    // ¡Método Mágico! 
    // Al escribir "findByUsername", Spring Data JPA entiende automáticamente
    // que debe crear una consulta SQL: "SELECT * FROM usuarios WHERE username = ?"
    Optional<UsuarioModel> findByUsername(String username);
}