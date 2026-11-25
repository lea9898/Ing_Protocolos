package cl.duocuc.miregistro.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

// =====================================================================
//  NUEVO (Foto en servidor) — AvatarApi.kt   (ARCHIVO ENTREGADO LISTO)
//  ---------------------------------------------------------------------
//  Un archivo (la foto) NO viaja como JSON: viaja como "multipart/form-data",
//  igual que cuando un formulario web sube un archivo.
//
//   @Multipart -> la petición se arma en partes (en vez de un cuerpo JSON)
//   @Part      -> cada parte: aquí, el archivo con el nombre "archivo"
//                 (debe coincidir con @RequestParam("archivo") del microservicio)
//
//  El servidor guarda la imagen en su carpeta uploads/avatares y responde el
//  usuario actualizado con su nueva avatarUrl.
// =====================================================================

interface AvatarApi {

    @Multipart
    @POST("api/usuarios/{id}/avatar")
    suspend fun subir(@Path("id") id: Long, @Part archivo: MultipartBody.Part): UsuarioResponse
}
