package cl.duoc.msayudantias.repository;

import cl.duoc.msayudantias.model.Autor;
import cl.duoc.msayudantias.model.Ayudantia;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class AyudantiaRepository {

    private static final String COLLECTION_NAME = "ayudantias";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public List<Ayudantia> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getFirestore().collection(COLLECTION_NAME)
                .orderBy("creado", Query.Direction.DESCENDING)
                .limit(50)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Ayudantia> ayudantias = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            ayudantias.add(mapDocumentToAyudantia(doc));
        }
        return ayudantias;
    }

    public Optional<Ayudantia> findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getFirestore().collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return Optional.of(mapDocumentToAyudantia(document));
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

    private Ayudantia mapDocumentToAyudantia(DocumentSnapshot doc) {
        String id = doc.getId();
        String materia = doc.getString("materia");
        if (materia == null) materia = "";

        Long cupoLong = doc.getLong("cupo");
        int cupo = cupoLong != null ? cupoLong.intValue() : 0;

        Long inscritosLong = doc.getLong("inscritos");
        int inscritos = inscritosLong != null ? inscritosLong.intValue() : 0;

        String horario = doc.getString("horario");
        if (horario == null) horario = "";

        String dia = doc.getString("dia");
        if (dia == null) dia = "";

        String lugar = doc.getString("lugar");
        if (lugar == null) lugar = "";

        Autor autor = new Autor("", "");
        Map<String, Object> autorMap = (Map<String, Object>) doc.get("autor");
        if (autorMap != null) {
            autor.setUid((String) autorMap.getOrDefault("uid", ""));
            autor.setNombre((String) autorMap.getOrDefault("nombre", ""));
        }

        String createdAt = null;
        Object creadoObj = doc.get("creado");
        if (creadoObj instanceof com.google.cloud.Timestamp) {
            createdAt = ((com.google.cloud.Timestamp) creadoObj).toDate().toInstant().toString();
        } else if (creadoObj instanceof Date) {
            createdAt = ((Date) creadoObj).toInstant().toString();
        } else if (creadoObj != null) {
            createdAt = creadoObj.toString();
        }

        return new Ayudantia(id, materia, cupo, inscritos, horario, dia, lugar, autor, createdAt);
    }
}
