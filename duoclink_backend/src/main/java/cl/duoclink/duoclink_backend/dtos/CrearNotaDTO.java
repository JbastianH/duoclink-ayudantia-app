package cl.duoclink.duoclink_backend.dtos;

import cl.duoclink.duoclink_backend.modelos.enums.TipoNota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearNotaDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no debe superar 200 caracteres")
    private String titulo;

    @Size(max = 500, message = "La descripción no debe superar 500 caracteres")
    private String descripcion;

    // Para TEXTO será el contenido; para ENLACE/MULTIMEDIA/DOCUMENTO puede ser una URL
    private String cuerpo;

    @NotNull(message = "El tipo es obligatorio")
    private TipoNota tipo;

    @Builder.Default
    private List<@Size(min = 1, max = 40, message = "Cada tag debe tener entre 1 y 40 caracteres") String> tags = new ArrayList<>();

    // Validaciones condicionales simples
    public boolean esCuerpoRequerido() {
        return tipo == TipoNota.TEXTO;
    }

    public boolean esUrlRequerida() {
        return tipo == TipoNota.ENLACE || tipo == TipoNota.MULTIMEDIA || tipo == TipoNota.DOCUMENTO;
    }
}