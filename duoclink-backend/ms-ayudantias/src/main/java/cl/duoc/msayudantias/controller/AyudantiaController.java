package cl.duoc.msayudantias.controller;

import cl.duoc.msayudantias.model.Ayudantia;
import cl.duoc.msayudantias.service.AyudantiaService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ayudantias")
@CrossOrigin(origins = "*")
public class AyudantiaController {

    @Autowired
    private AyudantiaService ayudantiaService;

    @GetMapping
    public ResponseEntity<?> getAllAyudantias() {
        try {
            List<Ayudantia> ayudantias = ayudantiaService.getAllAyudantias();
            return ResponseEntity.ok(ayudantias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al obtener ayudantías"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAyudantiaById(@PathVariable String id) {
        try {
            Ayudantia ayudantia = ayudantiaService.getAyudantiaById(id);
            if (ayudantia != null) {
                return ResponseEntity.ok(ayudantia);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ayudantía no encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createAyudantia(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> payload) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            // Fetch user name from Firebase Auth to store in the document
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(userId);
            String userName = userRecord.getDisplayName() != null ? userRecord.getDisplayName() : "Usuario";

            Ayudantia created = ayudantiaService.createAyudantia(payload, userId, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAyudantia(@RequestHeader("Authorization") String authHeader, @PathVariable String id, @RequestBody Map<String, Object> payload) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            boolean updated = ayudantiaService.updateAyudantia(id, payload, userId);
            if (updated) {
                return ResponseEntity.ok(Map.of("message", "Ayudantía actualizada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ayudantía no encontrada"));
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "No autorizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAyudantia(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            boolean deleted = ayudantiaService.deleteAyudantia(id, userId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Ayudantía eliminada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ayudantía no encontrada"));
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "No autorizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error"));
        }
    }

    private String verifyToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (Exception e) {
            System.err.println("Token verification failed: " + e.getMessage());
            return null;
        }
    }
}
