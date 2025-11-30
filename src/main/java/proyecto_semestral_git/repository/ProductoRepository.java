package proyecto_semestral_git.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto_semestral_git.model.ProductoModel;

/**
 * Repositorio para la entidad ProductoModel.
 * Extiende JpaRepository para obtener operaciones CRUD automáticas.
 * * JpaRepository<Entidad, TipoID>
 * - Entidad: ProductoModel
 * - TipoID: Integer (porque el id en el modelo es 'int')
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Integer> {
    // Aquí puedes definir métodos de consulta personalizados si los necesitas en el futuro.
    // Por ejemplo:
    // List<ProductoModel> findByNombreContaining(String nombre);
}