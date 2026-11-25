package cl.duocuc.miregistro.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duocuc.miregistro.data.remote.*
import kotlinx.coroutines.launch
@Composable fun EquiposContenido(esAdministrador:Boolean) {
 val api=RetrofitClient.equipoApi
 val scope=rememberCoroutineScope()
 var equipos by remember { mutableStateOf(emptyList<EquipoResponse>()) }
 var error by remember { mutableStateOf("") }
 var buscar by remember { mutableStateOf("") }
 var formulario by remember { mutableStateOf(false) }
 val campos=remember { mutableStateListOf("","","","","", "Operativo", "") }
 val etiquetas=listOf("Codigo","Nombre","Marca","Modelo","Serie","Estado","Instalacion")
 suspend fun cargar(){ try { equipos=api.listar(); error="" } catch(e:Exception){error="No se pudo consultar equipos"} }
 LaunchedEffect(Unit){cargar()}
 OutlinedTextField(buscar,{buscar=it},label={Text("Buscar equipo")},modifier=Modifier.fillMaxWidth())
 if(esAdministrador) Button(onClick={formulario=!formulario}){Text(if(formulario) "Cancelar" else "Registrar equipo")}
 if(error.isNotBlank()) Text(error,color=MaterialTheme.colorScheme.error)
 LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)) {
 if(formulario && esAdministrador){
 items(etiquetas.size){i -> OutlinedTextField(campos[i],{campos[i]=it},label={Text(etiquetas[i])},modifier=Modifier.fillMaxWidth()) }
 item { Text("Estados: Operativo, Mantenimiento, Fuera de servicio")
 Button(onClick={scope.launch {
 if(campos.any{it.isBlank() || it.length>150} || campos[5] !in listOf("Operativo","Mantenimiento","Fuera de servicio")){error="Revisa los campos y el estado"} else try {
 api.crear(EquipoRequest(campos[0],campos[1],campos[2],campos[3],campos[4],campos[5],campos[6])); formulario=false; cargar()
 } catch(e:Exception){error="No se pudo guardar: revisa el codigo y la conexion"}
 }}){Text("Guardar equipo")} }
 }
 items(equipos.filter{listOf(it.codigo,it.nombre,it.marca,it.modelo,it.serie,it.estado,it.instalacion).any{v->v.contains(buscar,true)}}){e ->
 Card(Modifier.fillMaxWidth()){Column(Modifier.padding(12.dp)){Text("${e.codigo} - ${e.nombre}");Text("${e.marca} / ${e.modelo} / ${e.serie}");Text("${e.instalacion} - ${e.estado}")}}
 }
 }
}
