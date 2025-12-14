package com.joel.duoclinkayudantia.viewmodel

import androidx.lifecycle.ViewModel
import com.joel.duoclinkayudantia.repository.ApuntesRepository

class ApuntesViewModel(
    private val repository: ApuntesRepository
) : ViewModel()