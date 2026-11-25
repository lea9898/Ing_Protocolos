package cl.duocuc.miregistro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.miregistro.data.SesionPreferencias
import cl.duocuc.miregistro.data.UsuarioRepository
import cl.duocuc.miregistro.model.Rol
import cl.duocuc.miregistro.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuariosViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UsuarioRepository()
    private val sesion = SesionPreferencias(application)
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()
    private val _filtro = MutableStateFlow("")
    val filtro: StateFlow<String> = _filtro.asStateFlow()
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error.asStateFlow()
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()
    private val _roles = MutableStateFlow<List<Rol>>(emptyList())
    val roles: StateFlow<List<Rol>> = _roles.asStateFlow()

    init {
        viewModelScope.launch {
            try { _roles.value = repo.listarRoles() } catch (e: Exception) { _error.value = e.message.orEmpty() }
            val id = sesion.leer()
            if (id > 0) try { _usuarioActual.value = repo.obtener(id) } catch (_: Exception) { sesion.borrar() }
        }
    }
    fun buscar(texto: String) { _filtro.value = texto; recargar() }
    fun recargar() { viewModelScope.launch {
        try { _usuarios.value = repo.listar(_filtro.value) } catch (e: Exception) { _error.value = e.message.orEmpty() }
    }
    }
    fun obtener(id: Long): Usuario? = _usuarios.value.firstOrNull { it.id == id }
    fun registrar(usuario: Usuario) { viewModelScope.launch {
        _cargando.value = true
        try { val guardado = repo.registrar(usuario.copy(rolId = 3L)); if (_usuarioActual.value == null) { _usuarioActual.value = guardado; sesion.guardar(guardado.id) }; recargar() }
        catch (e: Exception) { _error.value = e.message ?: "Error al registrar" }
        finally { _cargando.value = false }
    }
    }
    fun actualizar(usuario: Usuario) { viewModelScope.launch {
        try { val guardado = repo.actualizar(usuario); if (_usuarioActual.value?.id == guardado.id) _usuarioActual.value = guardado; recargar() }
        catch (e: Exception) { _error.value = e.message.orEmpty() }
    }
    }
    fun eliminar(usuario: Usuario) { viewModelScope.launch {
        try { repo.eliminar(usuario.id); if (_usuarioActual.value?.id == usuario.id) cerrarSesion(); recargar() }
        catch (e: Exception) { _error.value = e.message.orEmpty() }
    }
    }
    fun iniciarSesion(correo: String, clave: String) { viewModelScope.launch {
        _cargando.value = true; _error.value = ""
        try { val usuario = repo.iniciarSesion(correo, clave); _usuarioActual.value = usuario; sesion.guardar(usuario.id); recargar() }
        catch (e: Exception) { _error.value = e.message ?: "No se pudo iniciar sesión" }
        finally { _cargando.value = false }
    }
    }
    fun cerrarSesion() { _usuarioActual.value = null; sesion.borrar() }
}
