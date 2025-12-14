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
    val id: String? = null,
    val materia: String = "",
    val cupo: String = "1",
    val horarioInicio: String = "",
    val horarioFin: String = "",
    val dia: String = "",
    val lugar: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false
)

class AyudantiaViewModel(
    application: Application,
    private val repo: AyudantiaRepository
) : AndroidViewModel(application) {

    @Suppress("unused")
    constructor(application: Application) : this(application, AyudantiaRepository())

    private val _ayudantias = MutableStateFlow<List<Ayudantia>>(emptyList())
    val ayudantias: StateFlow<List<Ayudantia>> = _ayudantias.asStateFlow()

    private val _isLoadingList = MutableStateFlow(false)
    val isLoadingList: StateFlow<Boolean> = _isLoadingList.asStateFlow()

    private val _formState = MutableStateFlow(CrearAyudantiaUiState())
    val formState: StateFlow<CrearAyudantiaUiState> = _formState.asStateFlow()

    val currentUserUid: String?
        get() = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

    init {
        cargarAyudantias()
    }

    fun cargarAyudantias() {
        viewModelScope.launch {
            _isLoadingList.value = true
            try {
                repo.getAyudantias()
                    .catch { e -> 
                        android.util.Log.e("AyudantiaViewModel", "Error al obtener ayudantías", e)
                        _isLoadingList.value = false
                    }
                    .collect { items ->
                        android.util.Log.d("AyudantiaViewModel", "Ayudantías recibidas: ${items.size}")
                        _ayudantias.value = items
                        _isLoadingList.value = false
                    }
            } catch (e: Exception) {
                android.util.Log.e("AyudantiaViewModel", "Excepción en init", e)
                _isLoadingList.value = false
            }
        }
    }

    fun onMateriaChange(v: String) = _formState.update { it.copy(materia = v) }
    fun onCupoChange(v: String) = _formState.update { it.copy(cupo = v) }
    fun onHorarioInicioChange(v: String) = _formState.update { it.copy(horarioInicio = v) }
    fun onHorarioFinChange(v: String) = _formState.update { it.copy(horarioFin = v) }
    fun onDiaChange(v: String) = _formState.update { it.copy(dia = v) }
    fun onLugarChange(v: String) = _formState.update { it.copy(lugar = v) }

    fun prepararEdicion(ayudantia: Ayudantia) {
        val horarios = ayudantia.horario.split(" a ")
        val inicio = horarios.getOrNull(0) ?: ""
        val fin = horarios.getOrNull(1) ?: ""
        
        _formState.update { 
            it.copy(
                id = ayudantia.id,
                materia = ayudantia.materia,
                cupo = ayudantia.cupo.toString(),
                horarioInicio = inicio,
                horarioFin = fin,
                dia = ayudantia.dia,
                lugar = ayudantia.lugar,
                isEditing = true,
                success = false,
                error = null
            )
        }
    }

    fun limpiarFormulario() {
        _formState.value = CrearAyudantiaUiState()
    }

    fun publicarAyudantia() {
        val s = _formState.value
        
        // Validaciones
        if (s.materia.isBlank()) {
            _formState.update { it.copy(error = "Debes ingresar la materia") }
            return
        }
        
        val cupoInt = s.cupo.toIntOrNull()
        if (cupoInt == null || cupoInt < 1) {
            _formState.update { it.copy(error = "El cupo debe ser al menos 1") }
            return
        }
        if (cupoInt > 40) {
            _formState.update { it.copy(error = "El cupo máximo permitido es 40") }
            return
        }

        if (s.dia.isBlank()) {
            _formState.update { it.copy(error = "Debes seleccionar una fecha") }
            return
        }

        // Validar fecha no anterior a hoy
        try {
            val sdf = java.text.SimpleDateFormat("d/M/yyyy", java.util.Locale.getDefault())
            val date = sdf.parse(s.dia)
            val today = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }.time
            
            if (date != null && date.before(today)) {
                _formState.update { it.copy(error = "La fecha no puede ser anterior a hoy") }
                return
            }
        } catch (e: Exception) {
            // Ignorar error de parseo si el formato es incorrecto, aunque viene del DatePicker
        }

        if (s.horarioInicio.isBlank() || s.horarioFin.isBlank()) {
            _formState.update { it.copy(error = "Debes definir el horario de inicio y fin") }
            return
        }

        if (s.lugar.isBlank()) {
            _formState.update { it.copy(error = "Debes ingresar el lugar o sala") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true, error = null) }
            try {
                val horario = "${s.horarioInicio} a ${s.horarioFin}"
                
                val ayudantia = Ayudantia(
                    materia = s.materia,
                    cupo = cupoInt,
                    horario = horario,
                    dia = s.dia,
                    lugar = s.lugar
                )
                
                if (s.isEditing && s.id != null) {
                    repo.actualizarAyudantia(s.id, ayudantia)
                } else {
                    repo.crearAyudantia(ayudantia)
                }
                
                _formState.update { it.copy(isLoading = false, success = true) }
                cargarAyudantias()
            } catch (e: Exception) {
                _formState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }

    fun eliminarAyudantia(id: String) {
        viewModelScope.launch {
            _isLoadingList.value = true
            try {
                repo.eliminarAyudantia(id)
                cargarAyudantias()
            } catch (e: Exception) {
                android.util.Log.e("AyudantiaViewModel", "Error al eliminar", e)
                _isLoadingList.value = false
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
        if (formState.value.success) {
            limpiarFormulario()
        }
    }
}
