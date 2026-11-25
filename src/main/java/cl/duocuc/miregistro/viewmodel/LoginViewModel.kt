package cl.duocuc.miregistro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.miregistro.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoCambia(valor: String) {
        _uiState.update { it.copy(correo = valor.trim(), errorCorreo = null) }
    }

    fun onContrasenaCambia(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null) }
    }

    fun onAlternarVisibilidad() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    fun validar(): Boolean {
        val estado = _uiState.value

        val errorCorreo = when {
            estado.correo.isBlank() -> "Ingresa tu correo"
            !Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(estado.correo) -> "Ingresa un correo válido"
            else -> null
        }
        val errorContrasena = if (estado.contrasena.isBlank()) "Ingresa tu contraseña" else null

        _uiState.update { it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena) }

        return errorCorreo == null && errorContrasena == null
    }

    fun onLimpiar() {
        _uiState.value = LoginUiState()
    }
}
