@echo off
echo ╔════════════════════════════════════════╗
echo ║     Starting RMI Server...             ║
echo ╚════════════════════════════════════════╝
echo.

REM Check if bin directory exists
if not exist bin (
    echo ❌ Error: bin directory not found!
    echo.
    echo Please run build.bat first to compile the project.
    echo.
    pause
    exit /b 1
)

echo 🚀 Starting Server Application...
echo.

java -cp "bin;. .\Shared\bin;lib\*" server.Main

if %errorlevel% neq 0 (
    echo.
    echo ❌ Application terminated with errors!
    echo.
)

pause
