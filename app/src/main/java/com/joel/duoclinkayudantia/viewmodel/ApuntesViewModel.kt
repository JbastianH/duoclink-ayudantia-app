package com.joel.duoclinkayudantia.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.repository.ApuntesRepository
import kotlinx.coroutines.launch

class ApuntesViewModel(
    private val repository: ApuntesRepository
) : ViewModel() {

    fun obtenerApuntes(onResult: (List<Apunte>) -> Unit) {
        viewModelScope.launch {
            val apuntes = repository.obtenerApuntes()
            onResult(apuntes)
        }
    }
}

