package com.joel.duoclinkayudantia.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.joel.duoclinkayudantia.model.Autor
import com.joel.duoclinkayudantia.model.Ayudantia
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AyudantiaRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getAyudantias(): Flow<List<Ayudantia>> = callbackFlow {
        android.util.Log.d("AyudantiaRepository", "Consultando ayudantías. Usuario: ${auth.currentUser?.email} (${auth.currentUser?.uid})")
        val listener = db.collection("ayudantias")
            .orderBy("creado", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    android.util.Log.e("AyudantiaRepository", "Error en snapshot listener", e)
                    close(e)
                    return@addSnapshotListener
                }
                val items = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Ayudantia::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        android.util.Log.e("AyudantiaRepository", "Error al convertir documento: ${doc.id}", e)
                        null
                    }
                } ?: emptyList()
                android.util.Log.d("AyudantiaRepository", "Documentos obtenidos: ${items.size}")
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    suspend fun crearAyudantia(ayudantia: Ayudantia): String {
        val user = auth.currentUser ?: throw Exception("No autenticado")
        val newAyudantia = ayudantia.copy(
            autor = Autor(user.uid, user.displayName ?: user.email ?: "Usuario"),
            creado = com.google.firebase.Timestamp.now()
        )
        val ref = db.collection("ayudantias").add(newAyudantia).await()
        return ref.id
    }

    suspend fun unirse(ayudantiaId: String) {
        val ref = db.collection("ayudantias").document(ayudantiaId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(ref)
            val currentInscritos = snapshot.getLong("inscritos") ?: 0
            val cupo = snapshot.getLong("cupo") ?: 0
            if (currentInscritos < cupo) {
                transaction.update(ref, "inscritos", currentInscritos + 1)
            } else {
                throw Exception("Sin cupos")
            }
        }.await()
    }
}