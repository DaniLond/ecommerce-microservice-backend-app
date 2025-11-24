#!/usr/bin/env python3
"""
Script para ejecutar pruebas de performance con Locust
Verifica que el backend esté disponible antes de ejecutar las pruebas
"""

import os
import sys
import time
import argparse
import subprocess
from datetime import datetime
import requests
from colorama import init, Fore, Style

# Inicializar colorama para colores en Windows
init(autoreset=True)

# Configuración
API_GATEWAY_URL = "http://localhost:8080"
REPORTS_DIR = "reports"
LOCUST_FILE = "locustfile.py"

# Configuraciones de pruebas
TEST_CONFIGS = {
    "debug": {
        "users": 5,
        "spawn_rate": 1,
        "run_time": "60s",
        "description": "Prueba de depuración (5 usuarios, 1 min)"
    },
    "quick": {
        "users": 20,
        "spawn_rate": 5,
        "run_time": "120s",
        "description": "Prueba rápida (20 usuarios, 2 min)"
    },
    "performance": {
        "users": 50,
        "spawn_rate": 8,
        "run_time": "300s",
        "description": "Prueba de rendimiento (50 usuarios, 5 min)"
    },
    "load": {
        "users": 75,
        "spawn_rate": 10,
        "run_time": "600s",
        "description": "Prueba de carga (75 usuarios, 10 min)"
    },
    "stress": {
        "users": 150,
        "spawn_rate": 20,
        "run_time": "300s",
        "description": "Prueba de estrés (150 usuarios, 5 min)"
    },
    "spike": {
        "users": 300,
        "spawn_rate": 50,
        "run_time": "180s",
        "description": "Prueba de picos (300 usuarios, 3 min)"
    }
}


def print_header(message):
    """Imprime un encabezado con formato"""
    print(f"\n{Fore.BLUE}{'=' * 80}")
    print(f"{Fore.BLUE}{message.center(80)}")
    print(f"{Fore.BLUE}{'=' * 80}{Style.RESET_ALL}\n")


def print_success(message):
    """Imprime un mensaje de éxito"""
    print(f"{Fore.GREEN}✅ {message}{Style.RESET_ALL}")


def print_error(message):
    """Imprime un mensaje de error"""
    print(f"{Fore.RED}❌ {message}{Style.RESET_ALL}")


def print_warning(message):
    """Imprime un mensaje de advertencia"""
    print(f"{Fore.YELLOW}⚠️  {message}{Style.RESET_ALL}")


def print_info(message):
    """Imprime un mensaje informativo"""
    print(f"{Fore.CYAN}ℹ️  {message}{Style.RESET_ALL}")


def check_backend_health():
    """Verifica si el backend está disponible y saludable"""
    print_info("Verificando disponibilidad del backend...")
    
    # Lista de servicios a verificar
    services = [
        ("API Gateway", f"{API_GATEWAY_URL}/actuator/health"),
        ("Product Service", f"{API_GATEWAY_URL}/product-service/api/products"),
        ("User Service", f"{API_GATEWAY_URL}/user-service/api/users"),
        ("Order Service", f"{API_GATEWAY_URL}/order-service/api/orders"),
        ("Payment Service", f"{API_GATEWAY_URL}/payment-service/api/payments"),
    ]
    
    backend_available = False
    available_services = []
    
    for service_name, url in services:
        try:
            response = requests.get(url, timeout=5)
            if response.status_code in [200, 401, 403]:  # 401/403 significa que el servicio está activo pero requiere auth
                print_success(f"{service_name} disponible (HTTP {response.status_code})")
                available_services.append(service_name)
                backend_available = True
            else:
                print_warning(f"{service_name} respondió con HTTP {response.status_code}")
        except requests.exceptions.ConnectionError:
            print_error(f"{service_name} no disponible (Connection Error)")
        except requests.exceptions.Timeout:
            print_error(f"{service_name} no disponible (Timeout)")
        except Exception as e:
            print_error(f"{service_name} error: {str(e)}")
    
    return backend_available, available_services


def create_reports_directory():
    """Crea el directorio de reportes si no existe"""
    if not os.path.exists(REPORTS_DIR):
        os.makedirs(REPORTS_DIR)
        print_success(f"Directorio de reportes creado: {REPORTS_DIR}")
    else:
        print_info(f"Directorio de reportes ya existe: {REPORTS_DIR}")


def run_locust_test(test_name, config, host_url):
    """Ejecuta una prueba de Locust con la configuración especificada"""
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    
    print_header(f"Ejecutando: {config['description']}")
    
    # Construir comando de Locust
    cmd = [
        "locust",
        "--host", host_url,
        "--users", str(config["users"]),
        "--spawn-rate", str(config["spawn_rate"]),
        "--run-time", config["run_time"],
        "--headless",
        "--html", f"{REPORTS_DIR}/{test_name}_{timestamp}.html",
        "--csv", f"{REPORTS_DIR}/{test_name}_{timestamp}",
        "--logfile", f"{REPORTS_DIR}/{test_name}_{timestamp}.log",
        "-f", LOCUST_FILE
    ]
    
    print_info(f"Comando: {' '.join(cmd)}\n")
    
    try:
        # Ejecutar Locust
        result = subprocess.run(cmd, capture_output=False, text=True)
        
        if result.returncode == 0:
            print_success(f"Prueba '{test_name}' completada exitosamente")
            print_info(f"Reportes generados en: {REPORTS_DIR}/{test_name}_{timestamp}.*")
            return True
        else:
            print_error(f"Prueba '{test_name}' falló con código de salida {result.returncode}")
            return False
            
    except FileNotFoundError:
        print_error("Locust no está instalado o no se encuentra en el PATH")
        print_info("Instala Locust con: pip install -r requirements.txt")
        return False
    except KeyboardInterrupt:
        print_warning("\nPrueba interrumpida por el usuario")
        return False
    except Exception as e:
        print_error(f"Error al ejecutar la prueba: {str(e)}")
        return False


def main():
    """Función principal"""
    parser = argparse.ArgumentParser(
        description="Ejecutar pruebas de performance con Locust",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Tipos de prueba disponibles:
  debug       - Prueba de depuración (5 usuarios, 1 min)
  quick       - Prueba rápida (20 usuarios, 2 min)
  performance - Prueba de rendimiento (50 usuarios, 5 min)
  load        - Prueba de carga (75 usuarios, 10 min)
  stress      - Prueba de estrés (150 usuarios, 5 min)
  spike       - Prueba de picos (300 usuarios, 3 min)
  suite       - Suite completa de pruebas (debug + quick + performance)
  
Ejemplos:
  python run_performance_tests.py quick
  python run_performance_tests.py performance
  python run_performance_tests.py suite
  python run_performance_tests.py --no-backend-check quick
        """
    )
    
    parser.add_argument(
        "test_type",
        nargs="?",
        default="quick",
        choices=list(TEST_CONFIGS.keys()) + ["suite"],
        help="Tipo de prueba a ejecutar (default: quick)"
    )
    
    parser.add_argument(
        "--no-backend-check",
        action="store_true",
        help="Saltar verificación de disponibilidad del backend"
    )
    
    parser.add_argument(
        "--host",
        default=API_GATEWAY_URL,
        help=f"URL del API Gateway (default: {API_GATEWAY_URL})"
    )
    
    args = parser.parse_args()
    
    # Actualizar URL si se proporciona
    host_url = args.host
    
    # Header principal
    print_header("E-commerce Performance Testing Suite")
    print_info(f"Host: {host_url}")
    print_info(f"Tipo de prueba: {args.test_type}")
    print_info(f"Fecha: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
    
    # Verificar que existe el archivo de Locust
    if not os.path.exists(LOCUST_FILE):
        print_error(f"No se encuentra el archivo: {LOCUST_FILE}")
        print_info("Asegúrate de estar en el directorio correcto")
        return 1
    
    # Crear directorio de reportes
    create_reports_directory()
    
    # Verificar disponibilidad del backend
    if not args.no_backend_check:
        backend_available, available_services = check_backend_health()
        
        if not backend_available:
            print_error("\n❌ El backend no está disponible")
            print_warning("\nOpciones:")
            print("  1. Inicia el backend con: docker-compose up -d")
            print("  2. Ejecuta las pruebas sin verificación: python run_performance_tests.py --no-backend-check quick")
            print("  3. Verifica que el puerto correcto esté configurado\n")
            return 1
        else:
            print_success(f"\n✅ Backend disponible ({len(available_services)} servicios activos)\n")
            time.sleep(2)
    else:
        print_warning("Saltando verificación del backend (--no-backend-check)\n")
    
    # Ejecutar pruebas
    if args.test_type == "suite":
        # Ejecutar suite de pruebas
        tests_to_run = ["debug", "quick", "performance"]
        print_info(f"Ejecutando suite completa: {', '.join(tests_to_run)}\n")
        
        results = []
        for test_name in tests_to_run:
            success = run_locust_test(test_name, TEST_CONFIGS[test_name], host_url)
            results.append((test_name, success))
            
            if success and test_name != tests_to_run[-1]:
                print_info("\nEsperando 10 segundos antes de la siguiente prueba...")
                time.sleep(10)
        
        # Resumen
        print_header("Resumen de Resultados")
        for test_name, success in results:
            status = "✅ ÉXITO" if success else "❌ FALLO"
            print(f"{status} - {test_name}")
    else:
        # Ejecutar una sola prueba
        success = run_locust_test(args.test_type, TEST_CONFIGS[args.test_type], host_url)
        return 0 if success else 1
    
    print_header("Pruebas de Performance Completadas")
    print_success(f"Reportes disponibles en: {REPORTS_DIR}/")
    
    return 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except KeyboardInterrupt:
        print_warning("\n\nPruebas interrumpidas por el usuario")
        sys.exit(130)
