package com.joel.duoclinkayudantia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joel.duoclinkayudantia.model.data.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(app: Application) : AndroidViewModel(app) {

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _apellido = MutableStateFlow("")
    val apellido = _apellido.asStateFlow()

    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri = _fotoUri.asStateFlow()

    init {
        // Carga inicial desde DataStore
        val ctx = getApplication<Application>().applicationContext
        viewModelScope.launch {
            UserPrefs.nombreFlow(ctx).collect { _nombre.value = it }
        }
        viewModelScope.launch {
            UserPrefs.apellidoFlow(ctx).collect { _apellido.value = it }
        }
        viewModelScope.launch {
            UserPrefs.fotoUriFlow(ctx).collect { uriStr ->
                _fotoUri.value = uriStr?.let { Uri.parse(it) }
            }
        }
    }

    fun setNombre(v: String) { _nombre.value = v }
    fun setApellido(v: String) { _apellido.value = v }
    fun setFotoUri(uri: Uri?) { _fotoUri.value = uri }

    fun saveProfile() {
        val ctx = getApplication<Application>().applicationContext
        viewModelScope.launch {
            UserPrefs.save(
                ctx,
                nombre.value,
                apellido.value,
                fotoUri.value?.toString()
            )
        }
    }

    fun clearProfile() {
        setNombre("")
        setApellido("")
        setFotoUri(null)
    }
}