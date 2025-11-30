package proyecto_semestral_git.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_semestral_git.model.PedidoModel;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    // Aquí podrías agregar métodos para buscar pedidos por cliente, por estado, etc.
    // Ejemplo: List<PedidoModel> findByEstado(String estado);
}