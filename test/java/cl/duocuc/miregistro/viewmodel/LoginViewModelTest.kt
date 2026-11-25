package cl.duocuc.miregistro.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// =====================================================================
//  NUEVO (Pruebas unitarias) — LoginViewModelTest.kt
//  ---------------------------------------------------------------------
//  Una prueba unitaria revisa UNA pieza pequeña del código (aquí, el
//  LoginViewModel) sin abrir la app, sin emulador y sin servidor.
//  Se ejecuta en el computador (carpeta src/test) en segundos.
//
//  Cada prueba sigue el patrón AAA:
//   1. Preparar  (Arrange) -> dejar el ViewModel en el estado que necesito
//   2. Ejecutar  (Act)     -> llamar a la función que quiero probar
//   3. Verificar (Assert)  -> comprobar que el resultado es el esperado
// =====================================================================

class LoginViewModelTest {

    // lateinit: se crea en preparar(), antes de cada prueba
    private lateinit var viewModel: LoginViewModel

    // @Before se ejecuta ANTES de CADA @Test.
    // Así cada prueba parte con un ViewModel nuevo y limpio (las pruebas no se mezclan).
    @Before
    fun preparar() {
        viewModel = LoginViewModel()
    }

    @Test
    fun validar_conCamposVacios_devuelveFalseYMuestraErrores() {
        // Ejecutar (no hace falta preparar nada: el formulario parte vacío)
        val resultado = viewModel.validar()

        // Verificar: no deja pasar y deja los mensajes de error en el estado
        assertFalse(resultado)
        assertEquals("Ingresa tu correo", viewModel.uiState.value.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.uiState.value.errorContrasena)
    }

    @Test
    fun validar_conDatosCompletos_devuelveTrueSinErrores() {
        // Preparar: simulamos lo que escribe el usuario en la pantalla
        viewModel.onCorreoCambia("camila@duocuc.cl")
        viewModel.onContrasenaCambia("123456")

        // Ejecutar
        val resultado = viewModel.validar()

        // Verificar
        assertTrue(resultado)
        assertNull(viewModel.uiState.value.errorCorreo)
        assertNull(viewModel.uiState.value.errorContrasena)
    }

    @Test
    fun onCorreoCambia_eliminaEspaciosAlInicioYAlFinal() {
        viewModel.onCorreoCambia("   camila@duocuc.cl   ")

        // trim() del ViewModel debe haber quitado los espacios
        assertEquals("camila@duocuc.cl", viewModel.uiState.value.correo)
    }

    @Test
    fun escribirDespuesDeUnError_borraElMensajeDeError() {
        // Preparar: provocamos el error
        viewModel.validar()

        // Ejecutar: el usuario corrige el correo
        viewModel.onCorreoCambia("c")

        // Verificar: el error del correo desaparece, el de la contraseña sigue
        assertNull(viewModel.uiState.value.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.uiState.value.errorContrasena)
    }
    @Test
    fun validar_conCorreoIncorrecto_devuelveFalse() {
        viewModel.onCorreoCambia("correo-invalido")
        viewModel.onContrasenaCambia("Prueba123")
        assertFalse(viewModel.validar())
        assertEquals("Ingresa un correo válido", viewModel.uiState.value.errorCorreo)
    }
}
