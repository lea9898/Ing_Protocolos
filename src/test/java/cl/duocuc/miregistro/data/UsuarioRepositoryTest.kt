package cl.duocuc.miregistro.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

// =====================================================================
//  NUEVO (Pruebas unitarias) — UsuarioRepositoryTest.kt
//  ---------------------------------------------------------------------
//  Probamos el Repository usando las API FALSAS (FakeUsuarioApi y FakeRolApi).
//  Revisamos tres responsabilidades del Repository:
//   1. Convierte el DTO de la API (UsuarioResponse) en el modelo de la app (Usuario)
//   2. Si el buscador está vacío, envía null (para que la API devuelva todos)
//   3. Si no hay conexión, entrega un mensaje entendible para el usuario
//
//  runBlocking { }: las funciones del Repository son suspend y solo se pueden
//  llamar dentro de una corrutina. runBlocking crea una y ESPERA a que termine,
//  ideal para pruebas. (En la app NO se usa: congelaría la pantalla.)
// =====================================================================

class UsuarioRepositoryTest {

    private lateinit var apiFalsa: FakeUsuarioApi
    private lateinit var repositorio: UsuarioRepository

    @Before
    fun preparar() {
        apiFalsa = FakeUsuarioApi()
        // Inyectamos las API falsas en lugar de las de Retrofit
        repositorio = UsuarioRepository(usuarioApi = apiFalsa, rolApi = FakeRolApi())
    }

    @Test
    fun iniciarSesion_convierteLaRespuestaEnUsuario() = runBlocking {
        val usuario = repositorio.iniciarSesion("camila@duocuc.cl", "123456")

        assertEquals(1L, usuario.id)
        assertEquals("Camila Rojas", usuario.nombre)
        assertEquals("Administrador", usuario.rolNombre)
    }

    @Test
    fun listar_conTextoVacio_enviaNullALaApi() = runBlocking {
        val lista = repositorio.listar("   ")

        assertEquals(2, lista.size)
        assertNull(apiFalsa.ultimoBuscar)   // "   " se transformó en null
    }

    @Test
    fun listarRoles_convierteRolResponseEnRol() = runBlocking {
        val roles = repositorio.listarRoles()

        assertEquals(2, roles.size)
        assertEquals("Estudiante", roles[1].nombre)
    }

    @Test
    fun sinConexion_lanzaErrorConMensajeAmigable() {
        apiFalsa.sinConexion = true

        // assertThrows: la prueba PASA solo si el bloque lanza una Exception
        val error = assertThrows(Exception::class.java) {
            runBlocking { repositorio.listar("") }
        }

        assertEquals("No se pudo conectar con el servidor", error.message)
    }
}
