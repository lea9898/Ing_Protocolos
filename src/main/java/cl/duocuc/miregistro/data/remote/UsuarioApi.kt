package cl.duocuc.miregistro.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// =====================================================================
//  NUEVO (Retrofit) — UsuarioApi.kt
//  ---------------------------------------------------------------------
//  Una INTERFAZ: solo se DESCRIBEN los endpoints de usuarios-service.
//  Retrofit escribe el código que hace la llamada HTTP real.
//
//   @GET / @POST / @PUT / @DELETE -> método HTTP + ruta (se suma a la URL base)
//   @Path("id")   -> reemplaza {id} en la ruta:    api/usuarios/5
//   @Query("x")   -> agrega ?x=valor a la URL:     api/usuarios?buscar=ana
//   @Body         -> el objeto se envía como JSON en el cuerpo
//
//  suspend = la función ESPERA la respuesta sin congelar la pantalla.
//  Solo se puede llamar desde una corrutina (viewModelScope.launch).
//  Si el servidor responde con error (400, 401, 404, 409...), Retrofit lanza HttpException.
// =====================================================================

interface UsuarioApi {

    @GET("api/usuarios")
    suspend fun listar(@Query("buscar") buscar: String?): List<UsuarioResponse>

    @GET("api/usuarios/{id}")
    suspend fun obtener(@Path("id") id: Long): UsuarioResponse

    @POST("api/usuarios")
    suspend fun registrar(@Body datos: RegistroRequest): UsuarioResponse

    @POST("api/usuarios/login")
    suspend fun iniciarSesion(@Body datos: LoginRequest): UsuarioResponse

    @PUT("api/usuarios/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body datos: ActualizarUsuarioRequest): UsuarioResponse

    // Sin tipo de retorno (Unit): el servidor responde 204 No Content (sin cuerpo)
    @PUT("api/usuarios/{id}/contrasena")
    suspend fun cambiarContrasena(@Path("id") id: Long, @Body datos: CambiarContrasenaRequest)

    @DELETE("api/usuarios/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
