package cl.duoclink.duoclink_backend.repositorios;

import cl.duoclink.duoclink_backend.modelos.Nota;
import cl.duoclink.duoclink_backend.modelos.enums.TipoNota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface NotaRepositorio extends MongoRepository<Nota, String> {

    Page<Nota> findAllByTipo(TipoNota tipo, Pageable pageable);

    Page<Nota> findAllByTagsIn(Collection<String> tags, Pageable pageable);

    Page<Nota> findAllByTituloContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String titulo, String descripcion, Pageable pageable
    );
}