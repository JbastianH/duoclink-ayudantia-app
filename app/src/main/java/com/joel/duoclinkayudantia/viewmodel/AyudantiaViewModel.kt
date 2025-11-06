package com.joel.duoclinkayudantia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.data.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Ayudantia(
    val id: Int = 0,
    val publicadoPor: String,
    val tema: String,
    val lugar: String,
    val hora: String,
    val dia: String,
    val cupos: Int,
    val duracion: Int
)

data class CrearAyudantiaUiState(
    val id: Int? = null,
    val publicadoPor: String = "",
    val tema: String = "",
    val lugar: String = "",
    val hora: String = "",
    val horaInput: String = "",
    val minutoInput: String = "",
    val dia: String = "",
    val cupos: String = "",
    val duracion: String = "",
    val temaError: String? = null,
    val lugarError: String? = null,
    val horaError: String? = null,
    val diaError: String? = null,
    val cuposError: String? = null,
    val duracionError: String? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AyudantiaViewModel(application: Application) : AndroidViewModel(application) {

    private val _ayudantias = MutableStateFlow<List<Ayudantia>>(emptyList())
    val ayudantias: StateFlow<List<Ayudantia>> = _ayudantias.asStateFlow()

    private val _formState = MutableStateFlow(CrearAyudantiaUiState())
    val formState: StateFlow<CrearAyudantiaUiState> = _formState.asStateFlow()

    init {
        viewModelScope.launch {
            _ayudantias.value = listOf(
                Ayudantia(1, "Juan Perez", "Cálculo I", "Sala 301", "14:00", "Lunes", 10, 90),
                Ayudantia(2, "Ana Gomez", "Programación Avanzada", "Lab 5", "16:00", "Miércoles", 5, 120)
            )
            resetFormState()
        }
    }

    fun onFormValueChange(
        tema: String? = null, lugar: String? = null, hora: String? = null,
        dia: String? = null, cupos: String? = null, duracion: String? = null,
        horaInput: String? = null, minutoInput: String? = null
    ) {
        _formState.update { currentState ->
            currentState.copy(
                tema = tema ?: currentState.tema,
                lugar = lugar ?: currentState.lugar,
                hora = hora ?: currentState.hora,
                dia = dia ?: currentState.dia,
                cupos = cupos ?: currentState.cupos,
                duracion = duracion ?: currentState.duracion,
                horaInput = horaInput ?: currentState.horaInput,
                minutoInput = minutoInput ?: currentState.minutoInput,
                temaError = if (tema != null) null else currentState.temaError,
                lugarError = if (lugar != null) null else currentState.lugarError,
                horaError = if (horaInput != null || minutoInput != null) null else currentState.horaError,
                diaError = if (dia != null) null else currentState.diaError,
                cuposError = if (cupos != null) null else currentState.cuposError,
                duracionError = if (duracion != null) null else currentState.duracionError
            )
        }
    }

    fun guardarAyudantia() {
        _formState.update {
            val hora = it.horaInput.padStart(2, '0')
            val minuto = it.minutoInput.padStart(2, '0')
            it.copy(hora = "$hora:$minuto")
        }

        if (!validarFormulario()) return

        val currentState = _formState.value
        if (currentState.id == null) {
            val nuevaAyudantia = Ayudantia(
                id = (_ayudantias.value.maxOfOrNull { it.id } ?: 0) + 1,
                publicadoPor = currentState.publicadoPor,
                tema = currentState.tema,
                lugar = currentState.lugar,
                hora = currentState.hora,
                dia = currentState.dia,
                cupos = currentState.cupos.toInt(),
                duracion = currentState.duracion.toInt()
            )
            _ayudantias.update { it + nuevaAyudantia }
        } else {
            val ayudantiaActualizada = Ayudantia(
                id = currentState.id,
                publicadoPor = currentState.publicadoPor,
                tema = currentState.tema,
                lugar = currentState.lugar,
                hora = currentState.hora,
                dia = currentState.dia,
                cupos = currentState.cupos.toInt(),
                duracion = currentState.duracion.toInt()
            )
            _ayudantias.update { list ->
                list.map { if (it.id == currentState.id) ayudantiaActualizada else it }
            }
        }
        _formState.update { it.copy(success = true) }
    }

    fun eliminarAyudantia(ayudantia: Ayudantia) {
        _ayudantias.update { currentList ->
            currentList.filterNot { it.id == ayudantia.id }
        }
    }

    fun cargarAyudantiaParaEditar(id: Int) {
        val ayudantia = _ayudantias.value.find { it.id == id }
        if (ayudantia != null) {
            val horaSplit = ayudantia.hora.split(":")
            _formState.update {
                it.copy(
                    id = ayudantia.id,
                    publicadoPor = ayudantia.publicadoPor,
                    tema = ayudantia.tema,
                    lugar = ayudantia.lugar,
                    hora = ayudantia.hora,
                    horaInput = horaSplit.getOrElse(0) { "" },
                    minutoInput = horaSplit.getOrElse(1) { "" },
                    dia = ayudantia.dia,
                    cupos = ayudantia.cupos.toString(),
                    duracion = ayudantia.duracion.toString()
                )
            }
        }
    }

    fun resetFormState() {
        viewModelScope.launch {
            val nombre = UserPrefs.nombreFlow(getApplication()).first()
            val apellido = UserPrefs.apellidoFlow(getApplication()).first()
            val nombreCompleto = "$nombre $apellido".trim()
            _formState.value = CrearAyudantiaUiState(publicadoPor = nombreCompleto)
        }
    }

    private fun validarFormulario(): Boolean {
        val state = _formState.value
        val temaError = if (state.tema.isBlank()) "El tema es requerido" else null
        val lugarError = if (state.lugar.isBlank()) "El lugar es requerido" else null
        
        val horaError = when {
            state.horaInput.isBlank() || state.minutoInput.isBlank() -> "La hora es requerida"
            state.horaInput.toIntOrNull() == null || state.minutoInput.toIntOrNull() == null -> "Formato inválido"
            state.horaInput.toInt() !in 0..23 || state.minutoInput.toInt() !in 0..59 -> "Hora o minutos fuera de rango"
            else -> null
        }

        val diaError = if (state.dia.isBlank()) "El día es requerido" else null
        
        val duracionError = if (state.duracion.isBlank()) "La duración es requerida" else null
        val cuposError = when {
            state.cupos.isBlank() -> "Los cupos son requeridos"
            state.cupos.toIntOrNull() == null -> "Debe ser un número"
            state.cupos.toInt() <= 0 -> "Debe ser mayor a 0"
            state.cupos.toInt() > 45 -> "El cupo máximo es 45"
            else -> null
        }

        _formState.update {
            it.copy(
                temaError = temaError,
                lugarError = lugarError,
                horaError = horaError,
                diaError = diaError,
                cuposError = cuposError,
                duracionError = duracionError
            )
        }

        return listOfNotNull(temaError, lugarError, horaError, diaError, cuposError, duracionError).isEmpty()
    }
}