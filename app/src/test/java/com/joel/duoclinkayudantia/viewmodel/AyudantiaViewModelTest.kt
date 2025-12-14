package com.joel.duoclinkayudantia.viewmodel

import com.joel.duoclinkayudantia.repository.AyudantiaRepository
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import android.app.Application
import android.util.Log

@OptIn(ExperimentalCoroutinesApi::class)
class AyudantiaViewModelTest {

    private val repo = mockk<AyudantiaRepository>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)
    private lateinit var viewModel: AyudantiaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        coEvery { repo.getAyudantias() } returns flowOf(emptyList())

        viewModel = AyudantiaViewModel(application, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun publicarAyudantia_emptyTitle_returnsError() = runTest {
        viewModel.onMateriaChange("") 

        viewModel.publicarAyudantia()

        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_emptyPlace_returnsError() = runTest {
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("") 

        viewModel.publicarAyudantia()

        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_emptyDay_returnsError() = runTest {
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 1")
        viewModel.onDiaChange("")

        viewModel.publicarAyudantia()

        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_repoError_handlesException() = runTest {
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 1")
        viewModel.onDiaChange("Lunes")
        
        val errorMsg = "Error de conexión"
        coEvery { repo.crearAyudantia(any()) } throws Exception(errorMsg)

        viewModel.publicarAyudantia()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.formState.value
        state.error shouldBe errorMsg
        state.success shouldBe false
    }

    @Test
    fun publicarAyudantia_validData_callsRepository() = runTest {
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 101")
        viewModel.onCupoChange("5")
        viewModel.onHorarioInicioChange("10:00")
        viewModel.onHorarioFinChange("12:00")
        viewModel.onDiaChange("Lunes")
        
        coEvery { repo.crearAyudantia(any()) } returns "new_id"

        viewModel.publicarAyudantia()
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.value.success shouldBe true
        
        coVerify(exactly = 1) { repo.crearAyudantia(any()) }
    }
}
