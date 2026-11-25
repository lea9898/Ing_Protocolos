# Firma de Induztek

La llave original MiRegistro (PKCS12) no se pudo abrir con la contraseña indicada. Se creó una llave nueva JKS para este proyecto, alias `induztek`, con la contraseña solicitada tanto para almacén como para clave. No es la misma identidad de firma de MiRegistro. No compartir la llave en repositorios públicos.

En Android Studio: Build > Generate Signed Bundle / APK > elegir Android App Bundle o APK > seleccionar `firma/induztek.jks` > alias `induztek` > ingresar la contraseña solicitada en ambos campos > release.

Alternativamente en PowerShell, desde el proyecto Android, asignar `$env:INDUZTEK_SIGNING_PASSWORD` a la contraseña y ejecutar `./gradlew.bat assembleRelease bundleRelease`. Sin esa variable, Gradle no configura la firma release automáticamente.

No se generaron APK/AAB nuevos: falta compilar con Android SDK y Gradle en el equipo. Los binarios de MiRegistro no se incorporan porque pertenecen a otra aplicación. Una instalación anterior firmada con otra llave puede requerir desinstalación (con pérdida de datos locales).
