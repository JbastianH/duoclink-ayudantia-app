package cl.duoc.msapuntes.service;

import cl.duoc.msapuntes.model.Apunte;
import cl.duoc.msapuntes.repository.ApunteRepository;
import com.google.cloud.firestore.FieldValue;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ApunteService {

    private final ApunteRepository repository;

    public ApunteService(ApunteRepository repository) {
        this.repository = repository;
    }

    public List<Apunte> getAllApuntes() throws ExecutionException, InterruptedException {
        return repository.findAll();
    }

    public Apunte getApunteById(String id) throws ExecutionException, InterruptedException {
        return repository.findById(id).orElse(null);
    }

    public Apunte createApunte(Map<String, Object> payload, String userId) throws ExecutionException, InterruptedException {
        String titulo = (String) payload.getOrDefault("titulo", "");
        String tipo = (String) payload.getOrDefault("tipo", "text");
        Object tagsRaw = payload.get("tags");
        String cuerpo = "";
        
        if ("text".equals(tipo)) {
            cuerpo = (String) payload.getOrDefault("cuerpo", "");
        } else {
            cuerpo = (String) payload.getOrDefault("url", "");
        }

        List<String> tags = parseTags(tagsRaw);

        Map<String, Object> docData = new HashMap<>();
        docData.put("titulo", titulo);
        docData.put("cuerpo", cuerpo);
        docData.put("tags", tags);
        docData.put("tipo", tipo);
        docData.put("creado", FieldValue.serverTimestamp());
        docData.put("userId", userId);

        String id = repository.save(docData);

        // Return the created object (approximate, as serverTimestamp is not available immediately without refetch)
        return new Apunte(id, tipo, titulo, "text".equals(tipo) ? cuerpo : null, !"text".equals(tipo) ? cuerpo : null, tags, new Date().toString(), userId);
    }

    public boolean deleteApunte(String id, String userId) throws ExecutionException, InterruptedException {
        Optional<Apunte> apunteOpt = repository.findById(id);
        
        if (apunteOpt.isEmpty()) return false;
        
        Apunte apunte = apunteOpt.get();
        if (apunte.getUserId() == null || !apunte.getUserId().equals(userId)) {
            throw new SecurityException("No autorizado");
        }

        repository.deleteById(id);
        return true;
    }

    public boolean updateApunte(String id, Map<String, Object> payload, String userId) throws ExecutionException, InterruptedException {
        Optional<Apunte> apunteOpt = repository.findById(id);
        
        if (apunteOpt.isEmpty()) return false;
        
        Apunte apunte = apunteOpt.get();
        if (apunte.getUserId() == null || !apunte.getUserId().equals(userId)) {
            throw new SecurityException("No autorizado");
        }

        Map<String, Object> updates = new HashMap<>();
        if (payload.containsKey("titulo")) updates.put("titulo", payload.get("titulo"));
        if (payload.containsKey("cuerpo")) updates.put("cuerpo", payload.get("cuerpo"));
        if (payload.containsKey("url")) updates.put("cuerpo", payload.get("url")); // Map url to cuerpo in DB
        if (payload.containsKey("tags")) {
            updates.put("tags", parseTags(payload.get("tags")));
        }
        
        if (updates.isEmpty()) return true; // Nothing to update

        repository.update(id, updates);
        return true;
    }

    private List<String> parseTags(Object raw) {
        if (raw instanceof List) {
            return ((List<?>) raw).stream()
                    .map(Object::toString)
                    .filter(s -> !s.trim().isEmpty())
                    .limit(25)
                    .collect(Collectors.toList());
        }
        if (raw instanceof String) {
            return Arrays.stream(((String) raw).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .limit(25)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
