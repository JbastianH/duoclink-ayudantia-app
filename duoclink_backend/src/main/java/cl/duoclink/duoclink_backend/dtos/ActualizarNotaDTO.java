package cl.duoclink.duoclink_backend.dtos;

import cl.duoclink.duoclink_backend.modelos.enums.TipoNota;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarNotaDTO {

    @Size(max = 200, message = "El título no debe superar 200 caracteres")
    private String titulo;

    @Size(max = 500, message = "La descripción no debe superar 500 caracteres")
    private String descripcion;

    // Para TEXTO: contenido; para ENLACE/MULTIMEDIA/DOCUMENTO: URL
    private String cuerpo;

    // Si se envía, se valida y se actualiza el tipo
    private TipoNota tipo;

    // Reemplaza completamente los tags si se envían (puede ser lista vacía)
    private List<@Size(min = 1, max = 40, message = "Cada tag debe tener entre 1 y 40 caracteres") String> tags;
}