package com.joel.duoclinkayudantia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.model.ApunteRequest
import com.joel.duoclinkayudantia.repository.ApuntesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrearApunteUiState(
    val id: String? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val url: String = "",
    val tipo: String = "text",
    val tags: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false
)

class ApuntesViewModel(
    application: Application,
    private val repo: ApuntesRepository
) : AndroidViewModel(application) {

    @Suppress("unused")
    constructor(application: Application) : this(application, ApuntesRepository())

    private val _apuntes = MutableStateFlow<List<Apunte>>(emptyList())
    val apuntes: StateFlow<List<Apunte>> = _apuntes.asStateFlow()

    private val _formState = MutableStateFlow(CrearApunteUiState())
    val formState: StateFlow<CrearApunteUiState> = _formState.asStateFlow()

    private val _apunteSeleccionado = MutableStateFlow<Apunte?>(null)
    val apunteSeleccionado: StateFlow<Apunte?> = _apunteSeleccionado.asStateFlow()

    private val _isLoadingList = MutableStateFlow(false)
    val isLoadingList: StateFlow<Boolean> = _isLoadingList.asStateFlow()

    val currentUserUid: String?
        get() = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

    init {
        cargarApuntes()
    }

    fun cargarApuntes() {
        viewModelScope.launch {
            _isLoadingList.value = true
            try {
                repo.getApuntes()
                    .catch { e ->
                        android.util.Log.e("ApuntesViewModel", "Error al obtener apuntes", e)
                        _isLoadingList.value = false
                    }
                    .collect { items ->
                        _apuntes.value = items
                        _isLoadingList.value = false
                    }
            } catch (e: Exception) {
                _isLoadingList.value = false
            }
        }
    }

    fun cargarApunte(id: String) {
        viewModelScope.launch {
            _isLoadingList.value = true
            try {
                val apunte = repo.getApunte(id)
                _apunteSeleccionado.value = apunte
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingList.value = false
            }
        }
    }

    fun limpiarApunteSeleccionado() {
        _apunteSeleccionado.value = null
    }

    fun onTituloChange(v: String) = _formState.update { it.copy(titulo = v) }
    fun onDescripcionChange(v: String) = _formState.update { it.copy(descripcion = v) }
    fun onUrlChange(v: String) = _formState.update { it.copy(url = v) }
    fun onTipoChange(v: String) = _formState.update { it.copy(tipo = v) }
    fun onTagsChange(v: String) = _formState.update { it.copy(tags = v) }

    fun limpiarFormulario() {
        _formState.value = CrearApunteUiState()
    }

    fun prepararEdicion(apunte: Apunte) {
        _formState.update {
            it.copy(
                id = apunte.id,
                titulo = apunte.titulo,
                descripcion = apunte.descripcion ?: "",
                url = apunte.link ?: "",
                tipo = apunte.type,
                tags = apunte.tags?.joinToString(", ") ?: "",
                isEditing = true,
                success = false,
                error = null
            )
        }
    }

    fun publicarApunte() {
        val s = _formState.value
        
        if (s.titulo.isBlank()) {
            _formState.update { it.copy(error = "El título es obligatorio") }
            return
        }
        
        if (s.tipo == "text" && s.descripcion.isBlank()) {
            _formState.update { it.copy(error = "El contenido es obligatorio") }
            return
        }
        
        if (s.tipo == "link" && s.url.isBlank()) {
            _formState.update { it.copy(error = "La URL es obligatoria") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, error = null) }
            try {
                val tagsList = s.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                
                val apunteRequest = ApunteRequest(
                    titulo = s.titulo,
                    cuerpo = if (s.descripcion.isNotBlank()) s.descripcion else null,
                    url = if (s.url.isNotBlank()) s.url else null,
                    tipo = s.tipo,
                    tags = tagsList
                )

                if (s.isEditing && s.id != null) {
                    repo.actualizarApunte(s.id, apunteRequest)
                } else {
                    repo.crearApunte(apunteRequest)
                }

                _formState.update { it.copy(isLoading = false, success = true) }
                cargarApuntes()
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun eliminarApunte(id: String) {
        viewModelScope.launch {
            _isLoadingList.value = true
            try {
                repo.eliminarApunte(id)
                cargarApuntes()
            } catch (e: Exception) {
                _isLoadingList.value = false
            }
        }
    }

    fun resetSuccess() {
        if (formState.value.success) {
            limpiarFormulario()
        }
    }
}


