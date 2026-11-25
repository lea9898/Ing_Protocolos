package cl.duocuc.miregistro.data.remote

import retrofit2.http.GET

// NUEVO (Retrofit): endpoints de roles-service (OTRO microservicio, otro puerto)
interface RolApi {

    @GET("api/roles")
    suspend fun listar(): List<RolResponse>
}
