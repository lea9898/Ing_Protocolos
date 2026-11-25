package cl.duocuc.miregistro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.miregistro.model.FormularioUiState
import cl.duocuc.miregistro.model.NivelExperiencia
import cl.duocuc.miregistro.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormularioViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onNombreCambia(valor: String) {
        _uiState.update { it.copy(nombre = valor, errorNombre = null, registroExitoso = false) }
    }

    fun onCorreoCambia(valor: String) {
        _uiState.update { it.copy(correo = valor.trim(), errorCorreo = null, registroExitoso = false) }
    }

    fun onContrasenaCambia(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, registroExitoso = false) }
    }

    fun onCiudadSeleccionada(ciudad: String) {
        _uiState.update { it.copy(ciudad = ciudad) }
    }

    fun onAlternarVisibilidad() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    fun onNivelSeleccionado(nivel: NivelExperiencia) {
        _uiState.update { it.copy(nivel = nivel) }
    }

    fun onNoticiasCambia(valor: Boolean) {
        _uiState.update { it.copy(recibirNoticias = valor) }
    }

    fun onTerminosCambia(valor: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = valor, errorTerminos = null) }
    }

    fun onRegistrar(): Usuario? {
        val estado = _uiState.value

        val errorNombre = when {
            estado.nombre.isBlank() -> "El nombre es obligatorio"
            estado.nombre.trim().length < 3 -> "Escribe al menos 3 caracteres"
            estado.nombre.any { it.isDigit() } -> "El nombre no puede contener números"
            else -> null
        }
        val correoValido = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
            .matches(estado.correo.trim())
        val errorCorreo = if (!correoValido) "Ingresa un correo válido" else null
        val errorContrasena = when {
            estado.contrasena.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            estado.contrasena.none { it.isDigit() } -> "Incluye al menos un número"
            else -> null
        }
        val errorTerminos = if (!estado.aceptaTerminos)
            "Debes aceptar los términos" else null

        val hayErrores = listOf(errorNombre, errorCorreo, errorContrasena, errorTerminos)
            .any { it != null }

        _uiState.update {
            it.copy(
                errorNombre = errorNombre,
                errorCorreo = errorCorreo,
                errorContrasena = errorContrasena,
                errorTerminos = errorTerminos,
                registroExitoso = !hayErrores
            )
        }

        return if (hayErrores) null else Usuario(estado.nombre.trim(), estado.correo.trim(), estado.ciudad, clave = estado.contrasena)
    }

    fun onLimpiar() {
        _uiState.value = FormularioUiState()
    }
}
