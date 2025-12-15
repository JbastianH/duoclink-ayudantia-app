package cl.duoc.msapuntes.controller;

import cl.duoc.msapuntes.model.Apunte;
import cl.duoc.msapuntes.service.ApunteService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/apuntes")
@CrossOrigin(origins = "*") 
public class ApunteController {

    @Autowired
    private ApunteService apunteService;

    @GetMapping
    public ResponseEntity<?> getAllApuntes() {
        try {
            List<Apunte> apuntes = apunteService.getAllApuntes();
            return ResponseEntity.ok(apuntes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al obtener apuntes"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApunteById(@PathVariable String id) {
        try {
            Apunte apunte = apunteService.getApunteById(id);
            if (apunte != null) {
                return ResponseEntity.ok(apunte);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nota no encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createApunte(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> payload) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            Apunte created = apunteService.createApunte(payload, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApunte(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            boolean deleted = apunteService.deleteApunte(id, userId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Nota eliminada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nota no encontrada"));
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "No autorizado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApunte(@RequestHeader("Authorization") String authHeader, @PathVariable String id, @RequestBody Map<String, Object> payload) {
        String userId = verifyToken(authHeader);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No autenticado"));
        }

        try {
            boolean updated = apunteService.updateApunte(id, payload, userId);
            if (updated) {
                return ResponseEntity.ok(Map.of("message", "Nota actualizada"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nota no encontrada"));
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
