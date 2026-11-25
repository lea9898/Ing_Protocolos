package cl.duocuc.miregistro.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64

// Guarda la foto del perfil por usuario para que siga disponible al volver a abrirlo.
object AvatarPreferencias {
    private const val ARCHIVO = "avatares_perfil"

    fun guardar(context: Context, idUsuario: Long, foto: Bitmap) {
        val texto = Base64.encodeToString(ConvertidorImagen.aBytes(foto), Base64.DEFAULT)
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .edit().putString("avatar_$idUsuario", texto).apply()
    }

    fun leer(context: Context, idUsuario: Long): Bitmap? {
        val texto = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .getString("avatar_$idUsuario", null) ?: return null
        return try {
            ConvertidorImagen.aBitmap(Base64.decode(texto, Base64.DEFAULT))
        } catch (_: Exception) {
            null
        }
    }
}
