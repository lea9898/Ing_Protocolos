@echo off
setlocal
call "%~dp0seleccionar_java.bat" 17
if errorlevel 1 (pause & exit /b 1)
cd /d "%~dp0"
call gradlew.bat assembleDebug testDebugUnitTest --max-workers=2
if errorlevel 1 (echo ERROR: revisa el primer mensaje de Gradle. & pause & exit /b 1)
echo APK: app\build\outputs\apk\debug\app-debug.apk
pause
