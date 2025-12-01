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

    // 1. Mock del Repositorio
    private val repo = mockk<AyudantiaRepository>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)
    private lateinit var viewModel: AyudantiaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // SOLUCIÓN 1: Mockear Log para evitar "Method not mocked"
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        // SOLUCIÓN 2: Definir comportamiento del repo ANTES de instanciar el ViewModel
        // (porque el init llama a getAyudantias inmediatamente)
        coEvery { repo.getAyudantias() } returns flowOf(emptyList())

        viewModel = AyudantiaViewModel(application, repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun publicarAyudantia_emptyTitle_returnsError() = runTest {
        // GIVEN: Un estado inicial vacío
        viewModel.onMateriaChange("") 

        // WHEN: Intentamos publicar
        viewModel.publicarAyudantia()

        // THEN: El estado debe tener un error y NO llamar al repositorio
        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        
        // Verificamos que NUNCA se llamó a crearAyudantia en el repo
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_emptyPlace_returnsError() = runTest {
        // GIVEN: Materia llena pero lugar vacío
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("") 

        // WHEN: Intentamos publicar
        viewModel.publicarAyudantia()

        // THEN: Error esperado
        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_emptyDay_returnsError() = runTest {
        // GIVEN: Materia y lugar llenos pero día vacío
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 1")
        viewModel.onDiaChange("")

        // WHEN: Intentamos publicar
        viewModel.publicarAyudantia()

        // THEN: Error esperado
        val state = viewModel.formState.value
        state.error shouldBe "Completa todos los campos obligatorios"
        coVerify(exactly = 0) { repo.crearAyudantia(any()) }
    }

    @Test
    fun publicarAyudantia_repoError_handlesException() = runTest {
        // GIVEN: Datos válidos
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 1")
        viewModel.onDiaChange("Lunes")
        
        // Simulamos error en el repo
        val errorMsg = "Error de conexión"
        coEvery { repo.crearAyudantia(any()) } throws Exception(errorMsg)

        // WHEN: Publicamos
        viewModel.publicarAyudantia()
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN: El estado debe reflejar el error
        val state = viewModel.formState.value
        state.error shouldBe errorMsg
        state.success shouldBe false
    }

    @Test
    fun publicarAyudantia_validData_callsRepository() = runTest {
        // GIVEN: Datos válidos en el formulario
        viewModel.onMateriaChange("Matemáticas")
        viewModel.onLugarChange("Sala 101")
        viewModel.onCupoChange("5")
        viewModel.onHorarioInicioChange("10:00")
        viewModel.onHorarioFinChange("12:00")
        viewModel.onDiaChange("Lunes")
        
        // Simulamos que el repo responde exitosamente
        coEvery { repo.crearAyudantia(any()) } returns "new_id"

        // WHEN: Publicamos
        viewModel.publicarAyudantia()
        
        // Avanzamos el tiempo de la corrutina
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN: El estado debe ser success
        viewModel.formState.value.success shouldBe true
        
        // Verificamos que SI se llamó al repositorio 1 vez
        coVerify(exactly = 1) { repo.crearAyudantia(any()) }
    }
}
