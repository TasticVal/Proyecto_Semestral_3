package proyecto_semestral_git.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

<<<<<<< HEAD
=======
// --- NUEVAS IMPORTACIONES (Necesarias para la Base de Datos) ---
>>>>>>> c5f30f6 (imports para producto model y se transforma la entidad producto a tabla)
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
// 1. @Entity: Convierte esta clase en una tabla
@Entity 
// 2. @Table: Le pone nombre a la tabla en MySQL (se llamará "productos")
@Table(name = "productos") 
public class ProductoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    // 3. @Id: Marca esto como la Llave Primariagit
    @Id 
    // 4. @GeneratedValue: Le dice a MySQL que él invente el número (Auto Increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    private String nombre;
    private int precio;
    private String descripcion;
}