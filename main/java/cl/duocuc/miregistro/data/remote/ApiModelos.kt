package cl.duocuc.miregistro.data.remote

import cl.duocuc.miregistro.model.Usuario

// =====================================================================
//  NUEVO (Retrofit) — ApiModelos.kt   (ARCHIVO ENTREGADO LISTO)
//  ---------------------------------------------------------------------
//  DTO = Data Transfer Object: clases que tienen EXACTAMENTE la forma del
//  JSON que viaja entre la app y los microservicios.
//
//   *Request  -> lo que la app ENVÍA   (cuerpo de un POST o PUT)
//   *Response -> lo que la app RECIBE  (respuesta del servidor)
//
//  Gson une cada propiedad con la clave JSON del MISMO nombre:
//   { "correo": "camila@duocuc.cl" }  <->  val correo: String
//  Si un nombre no coincide, ese dato llega vacío (null).
// =====================================================================

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class RegistroRequest(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val ciudad: String,
    val rolId: Long
)

data class ActualizarUsuarioRequest(
    val nombre: String,
    val correo: String,
    val ciudad: String,
    val rolId: Long
)

data class CambiarContrasenaRequest(
    val actual: String,
    val nueva: String
)

// La API NUNCA devuelve la contraseña: por eso esta clase no la tiene.
// avatarUrl llega null si el usuario no ha subido foto.
data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val correo: String,
    val ciudad: String,
    val rolId: Long,
    val rolNombre: String,
    val avatarUrl: String?
)

data class RolResponse(
    val id: Long,
    val nombre: String
)

// Función de extensión: convierte el DTO de la API en el modelo que usa la app.
// Así las pantallas siguen trabajando con Usuario y no saben que existe una API.
fun UsuarioResponse.aUsuario() = Usuario(
    nombre = nombre,
    correo = correo,
    ciudad = ciudad,
    id = id,
    rolId = rolId,
    rolNombre = rolNombre,
    avatarUrl = avatarUrl
)
