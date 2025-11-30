package proyecto_semestral_git.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

// --- NUEVAS IMPORTACIONES JPA ---
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
// 1. @Entity: Indica que esta clase es una tabla en la base de datos
@Entity
// 2. @Table: Define el nombre de la tabla en MySQL (se llamará "envios")
@Table(name = "envios")
public class EnvioModel implements Serializable {
    private static final long serialVersionUID = 1L; 

    // 3. @Id: Marca este campo como la Llave Primaria (Primary Key)
    @Id
    // 4. @GeneratedValue: Permite que la base de datos genere el ID automáticamente (1, 2, 3...)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String nombre; // Ej: "Envío Estándar", "Envío Express"
    private int precio; // Costo de este método de envío
    private String tiempoEstimado; // Ej: "3-5 días hábiles"
}