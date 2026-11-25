package cl.duocuc.miregistro.model

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val contrasenaVisible: Boolean = false,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null
)
