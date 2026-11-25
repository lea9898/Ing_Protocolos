@echo off
rem Called from another batch file; intentionally no setlocal.
set "INDUZTEK_JDK="
if "%~1"=="17" if defined JAVA17_HOME call :check "%JAVA17_HOME%" %~1
if "%~1"=="21" if defined JAVA21_HOME call :check "%JAVA21_HOME%" %~1
if not defined INDUZTEK_JDK if defined JAVA_HOME call :check "%JAVA_HOME%" %~1
for %%D in ("%USERPROFILE%\.jdks" "%ProgramFiles%\Eclipse Adoptium" "%ProgramFiles%\Java" "%ProgramFiles%\Microsoft" "%ProgramFiles%\Amazon Corretto") do (
 if not defined INDUZTEK_JDK if exist "%%~D" for /d %%J in ("%%~D\*") do if not defined INDUZTEK_JDK call :check "%%~fJ" %~1
)
if not defined INDUZTEK_JDK (
 echo ERROR: JDK %~1 no encontrado. Instala esa version y configura JAVA%~1_HOME.
 exit /b 1
)
set "JAVA_HOME=%INDUZTEK_JDK%"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo JAVA_HOME=%JAVA_HOME%
exit /b 0
:check
if not exist "%~1\bin\javac.exe" exit /b 0
findstr /B /L /C:"JAVA_VERSION=" "%~1\release" | findstr /L /C:"%~2." >nul
if not errorlevel 1 set "INDUZTEK_JDK=%~1"
exit /b 0
