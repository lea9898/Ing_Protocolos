package cl.duocuc.miregistro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duocuc.miregistro.model.Usuario

/** Pantalla exclusiva del usuario normal: no contiene operaciones de escritura. */
@Composable
fun PanelUsuarioScreen(
    usuario: Usuario,
    onPerfil: () -> Unit,
    onProtocolos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("INDUZTEK | Panel de usuario", style = MaterialTheme.typography.headlineSmall)
            Text("Bienvenido, ${usuario.nombre}", style = MaterialTheme.typography.titleLarge)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Mis datos personales", style = MaterialTheme.typography.titleMedium)
                    Text("Nombre: ${usuario.nombre}")
                    Text("Correo: ${usuario.correo}")
                    Text("Ciudad: ${usuario.ciudad}")
                    Text("Rol: ${usuario.rolNombre}")
                    OutlinedButton(onClick = onPerfil, modifier = Modifier.fillMaxWidth()) {
                        Text("Ver mi perfil y fotografía")
                    }
                }
            }
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Inspecciones y protocolos", style = MaterialTheme.typography.titleMedium)
                    Text("Puedes buscar y consultar registros, clientes y equipos. Acceso de solo lectura.")
                    Button(onClick = onProtocolos, modifier = Modifier.fillMaxWidth()) {
                        Text("Consultar protocolos e inspecciones")
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
        }
    }
}
