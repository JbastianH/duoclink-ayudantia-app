package com.joel.duoclinkayudantia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.model.Ayudantia
import com.joel.duoclinkayudantia.repository.AyudantiaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrearAyudantiaUiState(
    val materia: String = "",
    val cupo: String = "1",
    val horarioInicio: String = "",
    val horarioFin: String = "",
    val dia: String = "",
    val lugar: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AyudantiaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AyudantiaRepository()

    private val _ayudantias = MutableStateFlow<List<Ayudantia>>(emptyList())
    val ayudantias: StateFlow<List<Ayudantia>> = _ayudantias.asStateFlow()

    private val _formState = MutableStateFlow(CrearAyudantiaUiState())
    val formState: StateFlow<CrearAyudantiaUiState> = _formState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repo.getAyudantias()
                    .catch { e -> 
                        android.util.Log.e("AyudantiaViewModel", "Error al obtener ayudantías", e)
                    }
                    .collect { items ->
                        android.util.Log.d("AyudantiaViewModel", "Ayudantías recibidas: ${items.size}")
                        _ayudantias.value = items
                    }
            } catch (e: Exception) {
                android.util.Log.e("AyudantiaViewModel", "Excepción en init", e)
            }
        }
    }

    fun onMateriaChange(v: String) = _formState.update { it.copy(materia = v) }
    fun onCupoChange(v: String) = _formState.update { it.copy(cupo = v) }
    fun onHorarioInicioChange(v: String) = _formState.update { it.copy(horarioInicio = v) }
    fun onHorarioFinChange(v: String) = _formState.update { it.copy(horarioFin = v) }
    fun onDiaChange(v: String) = _formState.update { it.copy(dia = v) }
    fun onLugarChange(v: String) = _formState.update { it.copy(lugar = v) }

    fun publicarAyudantia() {
        val s = _formState.value
        if (s.materia.isBlank() || s.dia.isBlank() || s.lugar.isBlank()) {
            _formState.update { it.copy(error = "Completa todos los campos obligatorios") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, error = null) }
            try {
                val cupoInt = s.cupo.toIntOrNull() ?: 1
                val horario = "${s.horarioInicio} a ${s.horarioFin}"
                
                val ayudantia = Ayudantia(
                    materia = s.materia,
                    cupo = cupoInt,
                    horario = horario,
                    dia = s.dia,
                    lugar = s.lugar
                )
                
                repo.crearAyudantia(ayudantia)
                _formState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun unirse(ayudantia: Ayudantia) {
        viewModelScope.launch {
            try {
                repo.unirse(ayudantia.id)
            } catch (e: Exception) {
                // Manejar error visualmente si es necesario
            }
        }
    }

    fun resetSuccess() {
        _formState.update { it.copy(success = false, materia = "", cupo = "1", horarioInicio = "", horarioFin = "", dia = "", lugar = "") }
    }
}
