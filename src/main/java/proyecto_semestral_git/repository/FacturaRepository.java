package proyecto_semestral_git.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_semestral_git.model.FacturaModel;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaModel, Integer> {
    // Aquí puedes buscar facturas por número si lo necesitas
    // Optional<FacturaModel> findByNumeroFactura(String numeroFactura);
}