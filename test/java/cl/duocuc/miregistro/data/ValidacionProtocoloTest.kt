package cl.duocuc.miregistro.data

import cl.duocuc.miregistro.data.remote.ProtocoloRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pruebas unitarias con la misma estructura de la plantilla: @Test y assert. */
class ValidacionProtocoloTest {
    private fun base() = ProtocoloRequest("PT-2026-0001", "Cliente", "Planta", "TR-01", "Transformador",
        "Tecnico", "23-11-2026", 20.0, 50.0, 100.0, 220.0, "")
    @Test fun aceptaDatosCorrectos() { assertTrue(ValidacionProtocolo.esValido(base())) }
    @Test fun rechazaNumeroVacio() { assertFalse(ValidacionProtocolo.esValido(base().copy(numero = ""))) }
    @Test fun rechazaClienteVacio() { assertFalse(ValidacionProtocolo.esValido(base().copy(cliente = ""))) }
    @Test fun rechazaTagVacio() { assertFalse(ValidacionProtocolo.esValido(base().copy(tag = ""))) }
    @Test fun rechazaFechaInvalida() { assertFalse(ValidacionProtocolo.esValido(base().copy(fecha = "31-02-2026"))) }
    @Test fun rechazaFechaMalFormateada() { assertFalse(ValidacionProtocolo.esValido(base().copy(fecha = "2026-11-23"))) }
    @Test fun rechazaHumedadNegativa() { assertFalse(ValidacionProtocolo.esValido(base().copy(humedad = -1.0))) }
    @Test fun rechazaHumedadMayorACien() { assertFalse(ValidacionProtocolo.esValido(base().copy(humedad = 101.0))) }
    @Test fun rechazaTemperaturaAlta() { assertFalse(ValidacionProtocolo.esValido(base().copy(temperatura = 71.0))) }
    @Test fun rechazaTemperaturaBaja() { assertFalse(ValidacionProtocolo.esValido(base().copy(temperatura = -21.0))) }
    @Test fun rechazaLecturaCero() { assertFalse(ValidacionProtocolo.esValido(base().copy(lectura1 = 0.0))) }
    @Test fun rechazaLecturaNegativa() { assertFalse(ValidacionProtocolo.esValido(base().copy(lectura10 = -2.0))) }
    @Test fun calculaAprobado() { assertEquals("Aprobado", ValidacionProtocolo.resultado(base())) }
    @Test fun calculaObservado() { assertEquals("Observado", ValidacionProtocolo.resultado(base().copy(lectura10 = 150.0))) }
    @Test fun calculaRechazado() { assertEquals("Rechazado", ValidacionProtocolo.resultado(base().copy(lectura10 = 50.0))) }
}
