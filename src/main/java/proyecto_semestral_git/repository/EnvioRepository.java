package proyecto_semestral_git.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_semestral_git.model.EnvioModel;

@Repository
public interface EnvioRepository extends JpaRepository<EnvioModel, Integer> {

}