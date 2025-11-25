# ====================================================================
# Script de configuración automática para pruebas de integración/E2E
# ====================================================================

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  CONFIGURACIÓN DE PRUEBAS - E-COMMERCE" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"

# Función para imprimir mensajes
function Print-Step {
    param([string]$Message, [string]$Color = "Yellow")
    Write-Host "`n[PASO] $Message" -ForegroundColor $Color
}

function Print-Success {
    param([string]$Message)
    Write-Host "✅ $Message" -ForegroundColor Green
}

function Print-Error {
    param([string]$Message)
    Write-Host "❌ $Message" -ForegroundColor Red
}

function Print-Info {
    param([string]$Message)
    Write-Host "ℹ️  $Message" -ForegroundColor Cyan
}

# ====================================================================
# PASO 1: Verificar Python
# ====================================================================
Print-Step "Verificando instalación de Python..."

try {
    $pythonVersion = python --version 2>&1
    Print-Success "Python encontrado: $pythonVersion"
    
    # Verificar que sea Python 3.8+
    if ($pythonVersion -match "Python 3\.([0-9]+)\.") {
        $minorVersion = [int]$matches[1]
        if ($minorVersion -lt 8) {
            Print-Error "Se requiere Python 3.8 o superior. Tienes: $pythonVersion"
            Print-Info "Descarga Python desde: https://www.python.org/downloads/"
            exit 1
        }
    }
} catch {
    Print-Error "Python no está instalado o no está en el PATH"
    Print-Info "Descarga Python desde: https://www.python.org/downloads/"
    exit 1
}

# ====================================================================
# PASO 2: Verificar Docker
# ====================================================================
Print-Step "Verificando Docker..."

try {
    $dockerRunning = docker ps 2>&1
    if ($LASTEXITCODE -eq 0) {
        Print-Success "Docker está corriendo"
    } else {
        Print-Error "Docker no está corriendo"
        Print-Info "Inicia Docker Desktop y ejecuta: docker-compose up -d"
        exit 1
    }
} catch {
    Print-Error "Docker no está instalado o no está corriendo"
    Print-Info "Descarga Docker Desktop desde: https://www.docker.com/products/docker-desktop/"
    exit 1
}

# ====================================================================
# PASO 3: Verificar servicios de Docker
# ====================================================================
Print-Step "Verificando servicios en Docker..."

$containers = docker ps --format "{{.Names}}" 2>&1
$requiredContainers = @(
    "api-gateway-container",
    "proxy-client-container",
    "user-service-container",
    "product-service-container",
    "order-service-container",
    "payment-service-container",
    "shipping-service-container",
    "favourite-service-container",
    "service-discovery-container",
    "cloud-config-container"
)

$missingContainers = @()
foreach ($container in $requiredContainers) {
    if ($containers -notcontains $container) {
        $missingContainers += $container
    }
}

if ($missingContainers.Count -gt 0) {
    Print-Error "Faltan containers:"
    foreach ($container in $missingContainers) {
        Write-Host "  - $container" -ForegroundColor Red
    }
    Print-Info "Ejecuta desde la raíz del proyecto: docker-compose up -d"
    Print-Info "Luego espera 2-3 minutos para que los servicios inicien"
    
    $response = Read-Host "`n¿Quieres que inicie Docker Compose ahora? (s/n)"
    if ($response -eq "s" -or $response -eq "S") {
        Print-Step "Iniciando servicios con Docker Compose..."
        docker-compose up -d
        Print-Info "Esperando 120 segundos para que servicios inicien..."
        Start-Sleep -Seconds 120
        Print-Success "Servicios iniciados"
    } else {
        exit 1
    }
}  else {
    Print-Success "Todos los containers requeridos están corriendo"
}

# ====================================================================
# PASO 4: Crear entorno virtual
# ====================================================================
Print-Step "Configurando entorno virtual de Python..."

if (Test-Path "venv") {
    Print-Info "Entorno virtual ya existe, saltando creación..."
} else {
    try {
        python -m venv venv
        Print-Success "Entorno virtual creado"
    } catch {
        Print-Error "Error al crear entorno virtual"
        exit 1
    }
}

# ====================================================================
# PASO 5: Activar entorno virtual e instalar dependencias
# ====================================================================
Print-Step "Instalando dependencias de Integration Tests..."

try {
    # Activar entorno y ejecutar pip
    & .\venv\Scripts\python.exe -m pip install --upgrade pip --quiet
    & .\venv\Scripts\pip.exe install -r integration\requirements.txt --quiet
    Print-Success "Dependencias de Integration instaladas"
} catch {
    Print-Error "Error al instalar dependencias de Integration"
    exit 1
}

Print-Step "Instalando dependencias de E2E Tests..."

try {
    & .\venv\Scripts\pip.exe install -r e2e\requirements.txt --quiet
    Print-Success "Dependencias de E2E instaladas"
} catch {
    Print-Error "Error al instalar dependencias de E2E"
    exit 1
}

# ====================================================================
# PASO 6: Verificar conectividad con API Gateway
# ====================================================================
Print-Step "Verificando conectividad con API Gateway..."

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -TimeoutSec 5 -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 404) {
        Print-Success "API Gateway responde en http://localhost:8080"
    } else {
        Print-Error "API Gateway no responde correctamente"
    }
} catch {
    Print-Error "No se puede conectar al API Gateway en http://localhost:8080"
    Print-Info "Verifica que el container api-gateway-container esté corriendo"
    Print-Info "Y que hayan pasado al menos 2-3 minutos desde docker-compose up"
}

# ====================================================================
# PASO 7: Verificar Eureka
# ====================================================================
Print-Step "Verificando Eureka (Service Discovery)..."

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8761/actuator/health" -TimeoutSec 5 -ErrorAction SilentlyContinue
    if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 404) {
        Print-Success "Eureka responde en http://localhost:8761"
        Print-Info "Abre http://localhost:8761 en el navegador para ver servicios registrados"
    }
} catch {
    Print-Error "No se puede conectar a Eureka en http://localhost:8761"
}

# ====================================================================
# RESUMEN Y PRÓXIMOS PASOS
# ====================================================================
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  ✅ CONFIGURACIÓN COMPLETADA" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "📚 PRÓXIMOS PASOS:`n" -ForegroundColor Yellow

Write-Host "1️⃣  Activar el entorno virtual:" -ForegroundColor Cyan
Write-Host "   .\venv\Scripts\activate`n" -ForegroundColor White

Write-Host "2️⃣  Ejecutar pruebas de humo (rápido, ~30 segundos):" -ForegroundColor Cyan
Write-Host "   cd integration" -ForegroundColor White
Write-Host "   python run_integration_tests.py --smoke`n" -ForegroundColor White

Write-Host "3️⃣  Ejecutar todas las pruebas de integración:" -ForegroundColor Cyan
Write-Host "   python run_integration_tests.py --html --verbose`n" -ForegroundColor White

Write-Host "4️⃣  Ejecutar pruebas E2E:" -ForegroundColor Cyan
Write-Host "   cd ..\e2e" -ForegroundColor White
Write-Host "   python run_e2e_tests.py --html --verbose`n" -ForegroundColor White

Write-Host "5️⃣  Ver reportes HTML generados:" -ForegroundColor Cyan
Write-Host "   start integration_report_*.html" -ForegroundColor White
Write-Host "   start e2e_report_*.html`n" -ForegroundColor White

Write-Host "📖 Documentación completa:" -ForegroundColor Yellow
Write-Host "   Lee: CONFIGURACION_Y_GUIA.md`n" -ForegroundColor White

Write-Host "🔧 Comandos útiles:" -ForegroundColor Yellow
Write-Host "   Ver servicios:    docker ps" -ForegroundColor White
Write-Host "   Ver logs:         docker-compose logs -f" -ForegroundColor White
Write-Host "   Reiniciar todo:   docker-compose restart" -ForegroundColor White
Write-Host "   Detener todo:     docker-compose down`n" -ForegroundColor White

Write-Host "========================================`n" -ForegroundColor Cyan

# Crear archivo de comandos rápidos
$quickCommandsFile = "comandos-rapidos.txt"
@"
=== COMANDOS RÁPIDOS PARA PRUEBAS ===

1. Activar entorno virtual:
   .\venv\Scripts\activate

2. Pruebas rápidas (smoke tests):
   cd integration
   python run_integration_tests.py --smoke

3. Todas las pruebas de integración con reporte:
   python run_integration_tests.py --html --verbose

4. Pruebas de un servicio específico:
   python run_integration_tests.py --service user-service

5. Solo pruebas de infraestructura:
   python run_integration_tests.py --service infrastructure

6. Pruebas E2E:
   cd ..\e2e
   python run_e2e_tests.py --html --verbose

7. Ver reporte:
   start *_report_*.html

8. Verificar servicios Docker:
   docker ps

9. Ver logs de un servicio:
   docker logs user-service-container -f

10. Reiniciar servicios:
    docker-compose restart
"@ | Out-File -FilePath $quickCommandsFile -Encoding utf8

Print-Success "Archivo de comandos rápidos creado: $quickCommandsFile"

Write-Host "`n🎉 ¡Todo listo para ejecutar pruebas!`n" -ForegroundColor Green
