package cl.duocuc.miregistro.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

// =====================================================================
//  NUEVO (Pruebas unitarias) — FormularioViewModelTest.kt
//  ---------------------------------------------------------------------
//  Probamos las REGLAS del registro que viven en FormularioViewModel:
//   - nombre obligatorio
//   - correo con @ y punto
//   - contraseña de al menos 8 caracteres y un número
//   - aceptar los términos
//
//  Truco: una función llenarFormularioValido() deja TODO correcto y cada
//  prueba "rompe" solo UNA cosa. Si la prueba falla, sabemos exactamente
//  qué regla dejó de funcionar.
// =====================================================================

class FormularioViewModelTest {

    private lateinit var viewModel: FormularioViewModel

    @Before
    fun preparar() {
        viewModel = FormularioViewModel()
    }

    // Función de apoyo (no es una prueba: no tiene @Test)
    private fun llenarFormularioValido() {
        viewModel.onNombreCambia("Ana Pérez")
        viewModel.onCorreoCambia("ana@duocuc.cl")
        viewModel.onContrasenaCambia("Prueba123")
        viewModel.onCiudadSeleccionada("Valparaíso")
        viewModel.onTerminosCambia(true)
    }

    @Test
    fun onRegistrar_conDatosValidos_devuelveUsuarioConLosDatos() {
        llenarFormularioValido()

        val usuario = viewModel.onRegistrar()

        // assertNotNull: si llega null la prueba falla aquí con un mensaje claro
        assertNotNull(usuario)
        assertEquals("Ana Pérez", usuario?.nombre)
        assertEquals("ana@duocuc.cl", usuario?.correo)
        assertEquals("Valparaíso", usuario?.ciudad)
    }

    @Test
    fun onRegistrar_conContrasenaCorta_devuelveNull() {
        llenarFormularioValido()
        viewModel.onContrasenaCambia("123")   // rompemos SOLO la contraseña

        val usuario = viewModel.onRegistrar()

        assertNull(usuario)
        assertEquals("La contraseña debe tener al menos 8 caracteres", viewModel.uiState.value.errorContrasena)
    }

    @Test
    fun onRegistrar_conCorreoSinArroba_devuelveNull() {
        llenarFormularioValido()
        viewModel.onCorreoCambia("anaduocuc.cl")   // rompemos SOLO el correo

        val usuario = viewModel.onRegistrar()

        assertNull(usuario)
        assertEquals("Ingresa un correo válido", viewModel.uiState.value.errorCorreo)
    }

    @Test
    fun onRegistrar_sinAceptarTerminos_devuelveNull() {
        llenarFormularioValido()
        viewModel.onTerminosCambia(false)   // rompemos SOLO los términos

        val usuario = viewModel.onRegistrar()

        assertNull(usuario)
        assertEquals("Debes aceptar los términos", viewModel.uiState.value.errorTerminos)
    }
}
