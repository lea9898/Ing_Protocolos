package cl.duocuc.miregistro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.miregistro.viewmodel.LoginViewModel
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoginScreen(onIngresar: (String, String) -> Unit, onRegistro: () -> Unit, errorServidor: String = "", cargando: Boolean = false) {
    val vm: LoginViewModel = viewModel()
    val estado by vm.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("INDUZTEK", style = MaterialTheme.typography.headlineLarge)
            Text("Inicio de sesión", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = estado.correo, onValueChange = vm::onCorreoCambia,
                isError = estado.errorCorreo != null, supportingText = { estado.errorCorreo?.let { Text(it) } },
                label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = estado.contrasena, onValueChange = vm::onContrasenaCambia,
                isError = estado.errorContrasena != null, supportingText = { estado.errorContrasena?.let { Text(it) } },
                trailingIcon = { TextButton(onClick = vm::onAlternarVisibilidad) { Text(if (estado.contrasenaVisible) "Ocultar" else "Ver") } },
                label = { Text("Contraseña") }, visualTransformation = if (estado.contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            if (errorServidor.isNotBlank()) Text(errorServidor, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Button(
                enabled = !cargando,
                onClick = {
                    if (vm.validar()) onIngresar(estado.correo, estado.contrasena)
                }, modifier = Modifier.fillMaxWidth()
            ) { Text("Ingresar") }
            TextButton(onClick = onRegistro, modifier = Modifier.fillMaxWidth()) {
                Text("Crear una cuenta")
            }
            Spacer(Modifier.height(12.dp))
            Text("Acceso de demostración", style = MaterialTheme.typography.titleSmall)
            Text("Las cuentas se administran en el microservicio.")
            
        }
    }
}
