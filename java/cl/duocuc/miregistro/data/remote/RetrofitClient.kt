package cl.duocuc.miregistro.data.remote

import cl.duocuc.miregistro.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// =====================================================================
//  NUEVO (Retrofit) — RetrofitClient.kt
//  ---------------------------------------------------------------------
//  Crea UNA sola vez los clientes de cada microservicio ("object" = una
//  única instancia para toda la app).
//
//  10.0.2.2 = dirección especial del EMULADOR para llegar al "localhost"
//  del computador. En un celular físico se usa la IP del PC en la misma
//  red WiFi (ej: http://192.168.1.10:8082/).
//  La URL base DEBE terminar en "/".
// =====================================================================

object RetrofitClient {
    val equipoApi: EquipoApi by lazy { crear("http://${BuildConfig.SERVIDOR_HOST}:8084/").create(EquipoApi::class.java) }

    private val URL_USUARIOS = "http://${BuildConfig.SERVIDOR_HOST}:8082/"
    private val URL_PROTOCOLOS = "http://${BuildConfig.SERVIDOR_HOST}:8083/"
    private val URL_ROLES = "http://${BuildConfig.SERVIDOR_HOST}:8081/"

    // Arma un Retrofit para una URL base. Gson traduce JSON <-> data class.
    private fun crear(urlBase: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // by lazy = se crea la primera vez que se usa, no al abrir la app
    val protocoloApi: ProtocoloApi by lazy { crear(URL_PROTOCOLOS).create(ProtocoloApi::class.java) }

    val usuarioApi: UsuarioApi by lazy { crear(URL_USUARIOS).create(UsuarioApi::class.java) }

    val rolApi: RolApi by lazy { crear(URL_ROLES).create(RolApi::class.java) }

    // NUEVO (Foto en servidor): misma URL que usuarios, otra interfaz (la subida de la foto)
    val avatarApi: AvatarApi by lazy { crear(URL_USUARIOS).create(AvatarApi::class.java) }
}
