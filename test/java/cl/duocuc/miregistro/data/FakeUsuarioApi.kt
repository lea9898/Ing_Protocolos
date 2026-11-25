package cl.duocuc.miregistro.data

import cl.duocuc.miregistro.data.remote.ActualizarUsuarioRequest
import cl.duocuc.miregistro.data.remote.CambiarContrasenaRequest
import cl.duocuc.miregistro.data.remote.LoginRequest
import cl.duocuc.miregistro.data.remote.RegistroRequest
import cl.duocuc.miregistro.data.remote.RolApi
import cl.duocuc.miregistro.data.remote.RolResponse
import cl.duocuc.miregistro.data.remote.UsuarioApi
import cl.duocuc.miregistro.data.remote.UsuarioResponse
import java.io.IOException

// =====================================================================
//  NUEVO (Pruebas unitarias) — FakeUsuarioApi.kt
//  ---------------------------------------------------------------------
//  Una prueba unitaria NO debe depender de la red ni de los microservicios:
//  si el servidor está apagado, la prueba fallaría aunque el código esté bien.
//
//  Solución: un "Fake" (API falsa). Implementa la MISMA interfaz UsuarioApi
//  que usa Retrofit, pero responde con datos escritos aquí, en memoria.
//  El Repository no nota la diferencia: solo conoce la interfaz.
//
//  Esto es posible porque UsuarioRepository RECIBE la API en su constructor
//  (inyección de dependencias). En la app recibe la de Retrofit; en la prueba, esta.
//
//  Atajo en Android Studio: escribir "class FakeUsuarioApi : UsuarioApi",
//  Alt+Enter -> Implement members -> seleccionar todos -> OK.
// =====================================================================

class FakeUsuarioApi : UsuarioApi {

    // "Base de datos" en memoria
    private val usuarios = listOf(
        UsuarioResponse(1, "Camila Rojas", "camila@duocuc.cl", "Santiago", 1, "Administrador", null),
        UsuarioResponse(2, "Diego Soto", "diego@duocuc.cl", "Temuco", 3, "Estudiante", null)
    )

    // Interruptor para simular que el microservicio está apagado
    var sinConexion = false

    // Guarda lo que el Repository le envió, para revisarlo en la prueba
    var ultimoBuscar: String? = "sin llamar"

    override suspend fun listar(buscar: String?): List<UsuarioResponse> {
        if (sinConexion) throw IOException("Servidor apagado")
        ultimoBuscar = buscar
        return usuarios
    }

    override suspend fun iniciarSesion(datos: LoginRequest): UsuarioResponse {
        if (sinConexion) throw IOException("Servidor apagado")
        return usuarios.first { it.correo == datos.correo }
    }

    // Las que no usamos en las pruebas quedan con TODO() (si alguien las llama, la prueba falla)
    override suspend fun obtener(id: Long): UsuarioResponse = TODO()
    override suspend fun registrar(datos: RegistroRequest): UsuarioResponse = TODO()
    override suspend fun actualizar(id: Long, datos: ActualizarUsuarioRequest): UsuarioResponse = TODO()
    override suspend fun cambiarContrasena(id: Long, datos: CambiarContrasenaRequest) = TODO()
    override suspend fun eliminar(id: Long) = TODO()
}

// Fake de roles-service: solo tiene una función
class FakeRolApi : RolApi {
    override suspend fun listar(): List<RolResponse> = listOf(
        RolResponse(1, "Administrador"),
        RolResponse(3, "Estudiante")
    )
}
