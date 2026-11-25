package cl.duocuc.miregistro.data

import cl.duocuc.miregistro.data.remote.ProtocoloApi
import cl.duocuc.miregistro.data.remote.ProtocoloRequest
import cl.duocuc.miregistro.data.remote.RetrofitClient

class ProtocoloRepository(private val api: ProtocoloApi = RetrofitClient.protocoloApi) {
    suspend fun listar(): List<Map<String, String>> = api.listar().map { p ->
        mapOf("numero" to p.numero, "cliente" to p.cliente, "instalacion" to p.instalacion,
            "tag" to p.tag, "tipo" to p.tipo, "tecnico" to p.tecnico, "fecha" to p.fecha,
            "temperatura" to p.temperatura.toString(), "humedad" to p.humedad.toString(),
            "lectura1" to p.lectura1.toString(), "lectura10" to p.lectura10.toString(),
            "comentario" to p.comentario, "resultado" to p.resultado, "estado" to p.estado)
    }
    suspend fun insertar(p: ProtocoloRequest): Boolean {
        if (!ValidacionProtocolo.esValido(p)) return false
        api.crear(p)
        return true
    }
}
