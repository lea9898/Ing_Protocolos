package cl.duocuc.miregistro.model

data class Usuario(
    val nombre: String,
    val correo: String,
    val ciudad: String,
    // API: clave primaria de la fila. 0 = "aún no guardado en la BD".
    val id: Long = 0,
    // Rol: clave foránea -> qué rol tiene. Por defecto, Técnico de terreno.
    val rolId: Long = Rol.ID_TECNICO_TERRENO,
    // Rol: nombre del rol. NO es columna de "usuarios": llega gracias al JOIN.
    val rolNombre: String = "",
    // Contraseña para autenticación local de la demostración.
    val clave: String = "",
    val avatarUrl: String? = null
    // Los nuevos campos van al final y con valor por defecto:
    // Usuario(nombre, correo, ciudad) sigue funcionando en el Formulario.
)
