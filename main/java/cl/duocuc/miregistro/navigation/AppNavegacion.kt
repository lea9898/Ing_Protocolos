package cl.duocuc.miregistro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duocuc.miregistro.ui.screens.*
import cl.duocuc.miregistro.viewmodel.UsuariosViewModel

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val usuariosViewModel: UsuariosViewModel = viewModel()
    val usuarioActual by usuariosViewModel.usuarioActual.collectAsStateWithLifecycle()
    val usuarios by usuariosViewModel.usuarios.collectAsStateWithLifecycle()
    val filtro by usuariosViewModel.filtro.collectAsStateWithLifecycle()
    val roles by usuariosViewModel.roles.collectAsStateWithLifecycle()
    val errorServidor by usuariosViewModel.error.collectAsStateWithLifecycle()
    val cargando by usuariosViewModel.cargando.collectAsStateWithLifecycle()

    val esAdmin = usuarioActual?.rolId == 1L
    val inicio = if (esAdmin) Ruta.Formulario.ruta else Ruta.PanelUsuario.ruta

    LaunchedEffect(usuarioActual?.id, usuarioActual?.rolId) {
        if (usuarioActual != null && navController.currentDestination?.route in listOf(Ruta.Login.ruta, Ruta.Formulario.ruta) && (navController.currentDestination?.route == Ruta.Login.ruta || !esAdmin)) {
            navController.navigate(inicio) {
                popUpTo(Ruta.Login.ruta) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = Ruta.Login.ruta) {
        composable(Ruta.Login.ruta) {
            LoginScreen(
                onIngresar = { correo, clave ->
                    usuariosViewModel.iniciarSesion(correo, clave)
                },
                errorServidor = errorServidor, cargando = cargando,
                onRegistro = { navController.navigate(Ruta.Formulario.ruta) }
            )
        }
        composable(Ruta.Formulario.ruta) {
            if (usuarioActual == null || esAdmin) FormularioScreen(
                onRegistroExitoso = { usuario ->
                    usuariosViewModel.registrar(usuario)
                },
                usuarioActual = usuarioActual,
                onVerMiPerfil = { navController.navigate(Ruta.Perfil.ruta) },
                onVerUsuarios = {
                    if (usuarioActual?.rolId == 1L) { usuariosViewModel.recargar(); navController.navigate(Ruta.Usuarios.ruta) }
                },
                onVerProtocolos = { navController.navigate(Ruta.Protocolos.ruta) },
                onCerrarSesion = {
                    usuariosViewModel.cerrarSesion()
                    navController.navigate(Ruta.Login.ruta) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Ruta.PanelUsuario.ruta) {
            if (usuarioActual != null && !esAdmin) {
                PanelUsuarioScreen(
                    usuario = usuarioActual!!,
                    onPerfil = { navController.navigate(Ruta.Perfil.ruta) },
                    onProtocolos = { navController.navigate(Ruta.Protocolos.ruta) },
                    onCerrarSesion = {
                        usuariosViewModel.cerrarSesion()
                        navController.navigate(Ruta.Login.ruta) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
        composable(Ruta.Protocolos.ruta) {
            if (usuarioActual != null) {
                ProtocolosScreen(
                    esAdministrador = esAdmin,
                    onVolver = {
                        navController.navigate(if (esAdmin) Ruta.Formulario.ruta else Ruta.PanelUsuario.ruta) {
                            popUpTo(if (esAdmin) Ruta.Formulario.ruta else Ruta.PanelUsuario.ruta) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
        composable(Ruta.Perfil.ruta) {
            if (usuarioActual != null) PerfilScreen(
                usuario = usuarioActual,
                onVolver = { navController.navigateUp() },
                soloLectura = !esAdmin
            )
        }
        composable(Ruta.Usuarios.ruta) {
            if (usuarioActual?.rolId == 1L) {
                UsuariosScreen(
                    usuarios = usuarios, onVolver = { navController.navigateUp() },
                    onEditar = { id -> navController.navigate(Ruta.Editar.crear(id)) },
                    filtro = filtro, onBuscar = usuariosViewModel::buscar,
                    onEliminar = usuariosViewModel::eliminar
                )
            }
        }
        composable(
            route = Ruta.Editar.ruta,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entrada ->
            if (usuarioActual?.rolId == 1L) {
                val id = entrada.arguments?.getLong("id") ?: 0L
                val usuario = remember(id) { usuariosViewModel.obtener(id) }
                if (usuario != null) EditarScreen(
                    usuario = usuario, roles = roles,
                    onVolver = { navController.navigateUp() },
                    onGuardar = { editado ->
                        usuariosViewModel.actualizar(editado)
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
