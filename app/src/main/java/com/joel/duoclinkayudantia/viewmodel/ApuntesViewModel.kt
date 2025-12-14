package com.joel.duoclinkayudantia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.repository.ApuntesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrearApunteUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val url: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class ApuntesViewModel(
    application: Application,
    private val repo: ApuntesRepository
) : AndroidViewModel(application) {

    private val _apuntes = MutableStateFlow<List<Apunte>>(emptyList())
    val apuntes: StateFlow<List<Apunte>> = _apuntes.asStateFlow()

    private val _formState = MutableStateFlow(CrearApunteUiState())
    val formState: StateFlow<CrearApunteUiState> = _formState.asStateFlow()

    fun cargar(token: String) {
        viewModelScope.launch {
            try {
                _apuntes.value = repo.listarApuntes(token)
            } catch (e: Exception) {
                // si quieres: manejar error visual
            }
        }
    }

    fun onTituloChange(v: String) = _formState.update { it.copy(titulo = v) }
    fun onDescripcionChange(v: String) = _formState.update { it.copy(descripcion = v) }
    fun onUrlChange(v: String) = _formState.update { it.copy(url = v) }

    fun publicar(token: String) {
        val s = _formState.value
        if (s.titulo.isBlank()) {
            _formState.update { it.copy(error = "Título obligatorio") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, error = null) }
            try {
                repo.crearApunte(token, Apunte(
                    titulo = s.titulo,
                    descripcion = s.descripcion,
                    url = s.url
                ))
                _formState.update { it.copy(isLoading = false, success = true) }
                cargar(token) // refresca
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message ?: "Error") }
            }
        }
    }

    fun eliminar(token: String, id: Long) {
        viewModelScope.launch {
            try {
                repo.eliminarApunte(token, id)
                cargar(token)
            } catch (_: Exception) { }
        }
    }

    fun resetSuccess() {
        _formState.update { CrearApunteUiState() }
    }
}


