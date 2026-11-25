package cl.duocuc.miregistro.data.remote

import retrofit2.http.*

data class ProtocoloRequest(val numero: String, val cliente: String, val instalacion: String,
    val tag: String, val tipo: String, val tecnico: String, val fecha: String,
    val temperatura: Double, val humedad: Double, val lectura1: Double,
    val lectura10: Double, val comentario: String)

data class ProtocoloResponse(val id: Long, val numero: String, val cliente: String,
    val instalacion: String, val tag: String, val tipo: String, val tecnico: String,
    val fecha: String, val temperatura: Double, val humedad: Double, val lectura1: Double,
    val lectura10: Double, val comentario: String, val resultado: String, val estado: String)

interface ProtocoloApi {
    @GET("api/protocolos") suspend fun listar(): List<ProtocoloResponse>
    @POST("api/protocolos") suspend fun crear(@Body protocolo: ProtocoloRequest): ProtocoloResponse
}
