@echo off
echo ╔════════════════════════════════════════╗
echo ║     Building RMI Server...             ║
echo ╚════════════════════════════════════════╝
echo.

REM Clean bin directory
if exist bin rmdir /s /q bin
mkdir bin

REM Compile Shared code first
echo 📦 Step 1: Compiling Shared code...
cd . .\Shared
call build.bat
cd . .\RMIServer
echo.

REM Compile Server code
echo 📦 Step 2: Compiling Server code...
javac -encoding UTF-8 -cp ". .\Shared\bin;lib\*" -d bin src\server\*. java src\server\database\*.java src\server\services\*.java src\server\utils\*.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo.
echo ╔════════════════════════════════════════╗
echo ║     ✅ Server Build Successful!         ║
echo ╚════════════════════════════════════════╝
echo.

pause
