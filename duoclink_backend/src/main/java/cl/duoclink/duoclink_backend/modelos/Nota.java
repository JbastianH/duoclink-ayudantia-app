package cl.duoclink.duoclink_backend.modelos;

import cl.duoclink.duoclink_backend.modelos.enums.TipoNota;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notas")
public class Nota {

    @Id
    private String id;

    @TextIndexed(weight = 5)
    private String titulo;

    // opcional: breve descripción/resumen
    @TextIndexed(weight = 1)
    private String descripcion;

    // para texto libre o URL (enlace/multimedia/documento)
    private String cuerpo;

    @Indexed
    private TipoNota tipo;

    @Builder.Default
    @Indexed
    private List<String> tags = new ArrayList<>();

    @CreatedDate
    private Instant creado;

    @LastModifiedDate
    private Instant actualizado;
}