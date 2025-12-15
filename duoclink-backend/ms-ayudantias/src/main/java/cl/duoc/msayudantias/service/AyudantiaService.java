package cl.duoc.msayudantias.service;

import cl.duoc.msayudantias.model.Autor;
import cl.duoc.msayudantias.model.Ayudantia;
import cl.duoc.msayudantias.repository.AyudantiaRepository;
import com.google.cloud.firestore.FieldValue;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AyudantiaService {

    private final AyudantiaRepository repository;

    public AyudantiaService(AyudantiaRepository repository) {
        this.repository = repository;
    }

    public List<Ayudantia> getAllAyudantias() throws ExecutionException, InterruptedException {
        return repository.findAll();
    }

    public Ayudantia getAyudantiaById(String id) throws ExecutionException, InterruptedException {
        return repository.findById(id).orElse(null);
    }

    public Ayudantia createAyudantia(Map<String, Object> payload, String userId, String userName) throws ExecutionException, InterruptedException {
        String materia = (String) payload.getOrDefault("materia", "");
        int cupo = parseInt(payload.get("cupo"));
        String horario = (String) payload.getOrDefault("horario", "");
        String dia = (String) payload.getOrDefault("dia", "");
        String lugar = (String) payload.getOrDefault("lugar", "");

        Map<String, Object> autorMap = new HashMap<>();
        autorMap.put("uid", userId);
        autorMap.put("nombre", userName); // In a real app, we might fetch this from Auth or DB

        Map<String, Object> docData = new HashMap<>();
        docData.put("materia", materia);
        docData.put("cupo", cupo);
        docData.put("inscritos", 0);
        docData.put("horario", horario);
        docData.put("dia", dia);
        docData.put("lugar", lugar);
        docData.put("autor", autorMap);
        docData.put("creado", FieldValue.serverTimestamp());

        String id = repository.save(docData);

        // Return created object
        return new Ayudantia(id, materia, cupo, 0, horario, dia, lugar, new Autor(userId, userName), new Date().toInstant().toString());
    }

    public boolean updateAyudantia(String id, Map<String, Object> payload, String userId) throws ExecutionException, InterruptedException {
        Optional<Ayudantia> ayudantiaOpt = repository.findById(id);
        if (ayudantiaOpt.isEmpty()) return false;

        Ayudantia ayudantia = ayudantiaOpt.get();
        if (ayudantia.getAutor() == null || !ayudantia.getAutor().getUid().equals(userId)) {
            throw new SecurityException("No autorizado");
        }

        Map<String, Object> updates = new HashMap<>();
        if (payload.containsKey("materia")) updates.put("materia", payload.get("materia"));
        if (payload.containsKey("cupo")) updates.put("cupo", parseInt(payload.get("cupo")));
        if (payload.containsKey("horario")) updates.put("horario", payload.get("horario"));
        if (payload.containsKey("dia")) updates.put("dia", payload.get("dia"));
        if (payload.containsKey("lugar")) updates.put("lugar", payload.get("lugar"));

        if (updates.isEmpty()) return true;

        repository.update(id, updates);
        return true;
    }

    public boolean deleteAyudantia(String id, String userId) throws ExecutionException, InterruptedException {
        Optional<Ayudantia> ayudantiaOpt = repository.findById(id);
        if (ayudantiaOpt.isEmpty()) return false;

        Ayudantia ayudantia = ayudantiaOpt.get();
        if (ayudantia.getAutor() == null || !ayudantia.getAutor().getUid().equals(userId)) {
            throw new SecurityException("No autorizado");
        }

        repository.deleteById(id);
        return true;
    }

    private int parseInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
