package cl.duocuc.miregistro.data

import cl.duocuc.miregistro.data.remote.ActualizarUsuarioRequest
import cl.duocuc.miregistro.data.remote.CambiarContrasenaRequest
import cl.duocuc.miregistro.data.remote.LoginRequest
import cl.duocuc.miregistro.data.remote.RegistroRequest
import cl.duocuc.miregistro.data.remote.RetrofitClient
import cl.duocuc.miregistro.data.remote.RolApi
import cl.duocuc.miregistro.data.remote.UsuarioApi
import cl.duocuc.miregistro.data.remote.aUsuario
import cl.duocuc.miregistro.model.Rol
import cl.duocuc.miregistro.model.Usuario
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

// =====================================================================
//  NUEVO (Retrofit) — UsuarioRepository.kt
//  ---------------------------------------------------------------------
//  Patrón Repository: es el ÚNICO lugar que sabe DE DÓNDE vienen los datos.
//
//   Pantalla -> ViewModel -> Repository -> Retrofit -> microservicio -> MySQL
//
//  Antes el ViewModel hablaba directo con SQLite (UsuarioDbHelper).
//  Ahora habla con el Repository; si mañana cambia la API, solo cambia esta
//  clase y el ViewModel y las pantallas quedan igual.
//
//  Cada función es suspend porque espera la respuesta de la red.
// =====================================================================

class UsuarioRepository(
    private val usuarioApi: UsuarioApi = RetrofitClient.usuarioApi,
    private val rolApi: RolApi = RetrofitClient.rolApi
) {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario =
        llamar { usuarioApi.iniciarSesion(LoginRequest(correo, contrasena)) }.aUsuario()

    suspend fun registrar(usuario: Usuario): Usuario {
        val datos = RegistroRequest(
            nombre = usuario.nombre,
            correo = usuario.correo,
            contrasena = usuario.clave,
            ciudad = usuario.ciudad,
            rolId = usuario.rolId
        )
        return llamar { usuarioApi.registrar(datos) }.aUsuario()
    }

    suspend fun obtener(id: Long): Usuario =
        llamar { usuarioApi.obtener(id) }.aUsuario()

    // ifBlank { null }: si no hay texto, no se envía ?buscar= y la API devuelve todos
    suspend fun listar(buscar: String): List<Usuario> =
        llamar { usuarioApi.listar(buscar.ifBlank { null }) }.map { it.aUsuario() }

    suspend fun actualizar(usuario: Usuario): Usuario {
        val datos = ActualizarUsuarioRequest(
            nombre = usuario.nombre,
            correo = usuario.correo,
            ciudad = usuario.ciudad,
            rolId = usuario.rolId
        )
        return llamar { usuarioApi.actualizar(usuario.id, datos) }.aUsuario()
    }

    suspend fun cambiarContrasena(id: Long, actual: String, nueva: String) =
        llamar { usuarioApi.cambiarContrasena(id, CambiarContrasenaRequest(actual, nueva)) }

    suspend fun eliminar(id: Long) =
        llamar { usuarioApi.eliminar(id) }

    suspend fun listarRoles(): List<Rol> =
        llamar { rolApi.listar() }.map { Rol(id = it.id, nombre = it.nombre) }

    // Ejecuta UNA llamada a la API y traduce los errores a un mensaje legible:
    //  - HttpException: el servidor respondió con error (401, 404, 409...).
    //    Spring Boot manda {"message": "..."} y ese texto se muestra al usuario.
    //  - IOException: no hubo respuesta (servidor apagado, sin red, URL mal escrita).
    private suspend fun <T> llamar(peticion: suspend () -> T): T = try {
        peticion()
    } catch (e: HttpException) {
        throw Exception(mensajeDelServidor(e))
    } catch (e: IOException) {
        throw Exception("No se pudo conectar con el servidor")
    }

    private fun mensajeDelServidor(e: HttpException): String {
        val cuerpo = e.response()?.errorBody()?.string()
        val mensaje = try {
            JSONObject(cuerpo ?: "").optString("message")
        } catch (ex: Exception) {
            ""
        }
        return mensaje.ifBlank { "Error del servidor (${e.code()})" }
    }
}
