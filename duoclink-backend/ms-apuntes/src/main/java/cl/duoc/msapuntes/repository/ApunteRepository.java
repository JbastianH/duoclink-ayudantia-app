package cl.duoc.msapuntes.repository;

import cl.duoc.msapuntes.model.Apunte;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class ApunteRepository {

    private static final String COLLECTION_NAME = "notes";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<Apunte> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getFirestore().collection(COLLECTION_NAME)
                .orderBy("creado", Query.Direction.DESCENDING)
                .limit(50)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Apunte> apuntes = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            apuntes.add(mapDocumentToApunte(doc));
        }
        return apuntes;
    }

    public Optional<Apunte> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getFirestore().collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return Optional.of(mapDocumentToApunte(document));
        } else {
            return Optional.empty();
        }
    }

    public String save(Map<String, Object> data) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> addedDocRef = getFirestore().collection(COLLECTION_NAME).add(data);
        return addedDocRef.get().getId();
    }

    public void update(String id, Map<String, Object> data) {
        DocumentReference docRef = getFirestore().collection(COLLECTION_NAME).document(id);
        docRef.update(data);
    }

    public void deleteById(String id) {
        getFirestore().collection(COLLECTION_NAME).document(id).delete();
    }

    // Helper method to map Firestore document to Apunte object
    private Apunte mapDocumentToApunte(DocumentSnapshot doc) {
        String id = doc.getId();
        String tipo = doc.getString("tipo");
        String titulo = doc.getString("titulo");
        String cuerpo = doc.getString("cuerpo");
        String userId = doc.getString("userId");
        
        // Handle tags safely
        List<String> tags = new ArrayList<>();
        Object tagsObj = doc.get("tags");
        if (tagsObj instanceof List<?>) {
            tags = ((List<?>) tagsObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        // Handle timestamp safely
        String fecha = "";
        Object creadoObj = doc.get("creado");
        if (creadoObj instanceof com.google.cloud.Timestamp) {
            fecha = ((com.google.cloud.Timestamp) creadoObj).toDate().toString();
        } else if (creadoObj != null) {
            fecha = creadoObj.toString();
        }

        // Map fields based on type
        String content = "text".equals(tipo) ? cuerpo : null;
        String url = !"text".equals(tipo) ? cuerpo : null;

        return new Apunte(id, tipo, titulo, content, url, tags, fecha, userId);
    }
}
