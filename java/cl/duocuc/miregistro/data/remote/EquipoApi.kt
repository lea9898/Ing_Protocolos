package cl.duocuc.miregistro.data.remote
import retrofit2.http.*
data class EquipoRequest(val codigo:String,val nombre:String,val marca:String,val modelo:String,val serie:String,val estado:String,val instalacion:String)
data class EquipoResponse(val id:Long,val codigo:String,val nombre:String,val marca:String,val modelo:String,val serie:String,val estado:String,val instalacion:String)
interface EquipoApi {
 @GET("api/equipos") suspend fun listar(): List<EquipoResponse>
 @POST("api/equipos") suspend fun crear(@Body equipo:EquipoRequest): EquipoResponse
}
