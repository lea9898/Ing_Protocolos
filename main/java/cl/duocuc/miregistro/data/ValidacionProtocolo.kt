package cl.duocuc.miregistro.data

import cl.duocuc.miregistro.data.remote.ProtocoloRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

object ValidacionProtocolo {
    private val formato = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT)
    fun esValido(p: ProtocoloRequest): Boolean {
        if (listOf(p.numero, p.cliente, p.instalacion, p.tag, p.tipo, p.tecnico).any { it.isBlank() }) return false
        if (p.comentario.length > 500 || !p.temperatura.isFinite() || !p.humedad.isFinite() ||
            !p.lectura1.isFinite() || !p.lectura10.isFinite()) return false
        if (p.temperatura !in -20.0..70.0 || p.humedad !in 0.0..100.0 || p.lectura1 <= 0 || p.lectura10 <= 0) return false
        return try { LocalDate.parse(p.fecha, formato); true } catch (_: Exception) { false }
    }
    fun resultado(p: ProtocoloRequest): String = when {
        p.lectura1 < 100 || p.lectura10 / p.lectura1 < 1.0 -> "Rechazado"
        p.lectura10 / p.lectura1 < 2.0 -> "Observado"
        else -> "Aprobado"
    }
}
