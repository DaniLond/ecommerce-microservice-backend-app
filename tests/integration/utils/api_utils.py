"""
Utilidades para pruebas de integración para todos los servicios.
Soporte para servicios de infraestructura y microservicios de negocio.
"""

import requests
import time
from config.config import (
    API_GATEWAY_URL,
    AUTH_ENDPOINT,
    TEST_USER,
    REQUEST_TIMEOUT,
    SERVICES_CONFIG,
)

_jwt_token = None
_current_service = ""


def set_current_service(service_name):
    """
    Establece el servicio actual para las pruebas.

    Args:
        service_name (str): Nombre del servicio (ej: 'user-service', 'api-gateway', etc.)
    """
    global _current_service
    if service_name in SERVICES_CONFIG:
        _current_service = service_name
        print(f"🔧 Servicio actual establecido: {service_name}")
    else:
        raise ValueError(
            f"Servicio '{service_name}' no está configurado. Servicios disponibles: {list(SERVICES_CONFIG.keys())}"
        )


def get_auth_token():
    """
    Obtiene un token JWT de autenticación.
    ⚠️ NOTA: La autenticación está temporalmente deshabilitada debido a problemas
    con el proxy-client. Este método retorna None para permitir pruebas sin auth.

    Returns:
        str: Token JWT o None si falla la autenticación.
    """
    global _jwt_token

    if _jwt_token:
        return _jwt_token

    try:
        response = requests.post(
            AUTH_ENDPOINT,
            json={"username": TEST_USER["username"], "password": TEST_USER["password"]},
            headers={"Content-Type": "application/json", "Accept": "application/json"},
            timeout=REQUEST_TIMEOUT,
        )

        if response.status_code == 200:
            auth_data = response.json()
            _jwt_token = auth_data.get("jwtToken")
            if _jwt_token:
                print(f"✅ Token JWT obtenido exitosamente")
                return _jwt_token
            else:
                print(f"⚠️ Token no encontrado en la respuesta, continuando sin autenticación")
                return None
        else:
            print(f"⚠️ Error {response.status_code} al obtener token, continuando sin autenticación")
            return None

    except requests.exceptions.RequestException as e:
        print(f"⚠️ Error de conexión al obtener token: {e}, continuando sin autenticación")
        return None


def get_headers(service_name=None, token=None, use_admin=False):
    """
    Genera headers para las solicitudes según el servicio.

    Args:
        service_name (str, optional): Nombre del servicio. Si es None, usa el servicio actual.
        token (str, optional): Token JWT (deprecado, se usa HTTP Basic Auth ahora).
        use_admin (bool, optional): Si es True, usa credenciales de admin en lugar de user.

    Returns:
        dict: Headers para las solicitudes.
    """
    if service_name is None:
        service_name = _current_service

    service_config = SERVICES_CONFIG.get(service_name, {})
    requires_auth = service_config.get("requires_auth", True)

    headers = {
        "Content-Type": "application/json",
        "Accept": "application/json",
    }

    if requires_auth:
        # Usar HTTP Basic Authentication
        from config.config import TEST_USER, TEST_ADMIN
        import base64
        
        credentials = TEST_ADMIN if use_admin else TEST_USER
        auth_string = f"{credentials['username']}:{credentials['password']}"
        auth_bytes = auth_string.encode('ascii')
        base64_bytes = base64.b64encode(auth_bytes)
        base64_string = base64_bytes.decode('ascii')
        headers["Authorization"] = f"Basic {base64_string}"

    return headers


def make_request(
    method, endpoint, data=None, params=None, headers=None, service_name=None, use_admin=False
):
    """
    Realiza una solicitud HTTP al servicio especificado.

    Args:
        method (str): Método HTTP ('GET', 'POST', 'PUT', 'DELETE').
        endpoint (str): Endpoint relativo del servicio (ej: '/api/users' o '/actuator/health').
        data (dict, optional): Datos para la solicitud.
        params (dict, optional): Parámetros de consulta.
        headers (dict, optional): Headers adicionales.
        service_name (str, optional): Nombre del servicio. Si es None, usa el servicio actual.
        use_admin (bool, optional): Si es True, usa credenciales de admin para la solicitud.

    Returns:
        Response: Objeto de respuesta.
    """
    if service_name is None:
        service_name = _current_service

    # Auto-detect: use admin credentials for PUT/PATCH/DELETE on secured services
    from config.config import SERVICES_CONFIG
    service_config = SERVICES_CONFIG.get(service_name, {})
    if not use_admin and method.upper() in ['PUT', 'PATCH', 'DELETE'] and service_config.get('requires_auth', False):
        use_admin = True

    # Debug: mostrar qué servicio se está usando
    print(f"🔧 Usando servicio: {service_name}")

    service_config = SERVICES_CONFIG.get(service_name)
    if not service_config:
        raise ValueError(
            f"Servicio '{service_name}' no está configurado. Servicios disponibles: {list(SERVICES_CONFIG.keys())}"
        )

    # Construir URL completa
    if endpoint.startswith("/"):
        endpoint = endpoint[1:]  # Remover / inicial si existe

    base_url = service_config["url"]
    path_prefix = service_config["path_prefix"]

    if path_prefix:
        url = f"{base_url}/{path_prefix}/{endpoint}"
    else:
        url = f"{base_url}/{endpoint}"

    print(f"🌐 {method} {url} (servicio: {service_name})")

    # Headers según el servicio
    request_headers = get_headers(service_name, use_admin=use_admin)
    if headers:
        request_headers.update(headers)

    try:
        if method.upper() == "GET":
            return requests.get(
                url, headers=request_headers, params=params, timeout=REQUEST_TIMEOUT
            )
        elif method.upper() == "POST":
            if isinstance(data, str):
                # Para endpoints como /encrypt que esperan texto plano
                request_headers["Content-Type"] = "text/plain"
                return requests.post(
                    url, headers=request_headers, data=data, timeout=REQUEST_TIMEOUT
                )
            else:
                return requests.post(
                    url, headers=request_headers, json=data, timeout=REQUEST_TIMEOUT
                )
        elif method.upper() == "PUT":
            return requests.put(
                url, headers=request_headers, json=data, timeout=REQUEST_TIMEOUT
            )
        elif method.upper() == "DELETE":
            return requests.delete(
                url, headers=request_headers, timeout=REQUEST_TIMEOUT
            )
        else:
            raise ValueError(f"Método HTTP no soportado: {method}")

    except requests.exceptions.RequestException as e:
        print(f"❌ Error en la solicitud a {url}: {e}")
        raise


def validate_response_schema(response, schema):
    """
    Valida que la respuesta cumpla con un esquema esperado.

    Args:
        response (Response): Respuesta HTTP.
        schema (dict): Esquema esperado con tipos de datos.

    Returns:
        bool: True si la validación es exitosa, False en caso contrario.
    """
    try:
        response_data = response.json()

        # Si es una colección, extraemos el primer elemento
        if isinstance(response_data, dict) and "collection" in response_data:
            if len(response_data["collection"]) > 0:
                response_data = response_data["collection"][0]
            else:
                return True  # Colección vacía es válida

        # Si es una lista directa, validamos el primer elemento
        elif isinstance(response_data, list) and len(response_data) > 0:
            response_data = response_data[0]

        for key, expected_type in schema.items():
            if key not in response_data:
                print(f"❌ La clave '{key}' no está presente en la respuesta")
                return False

            # Permitir que los campos sean None
            if response_data[key] is None:
                continue

            if not isinstance(response_data[key], expected_type):
                print(
                    f"❌ La clave '{key}' no es del tipo esperado {expected_type}, es {type(response_data[key])}"
                )
                return False

        return True
    except Exception as e:
        print(f"❌ Error al validar el esquema: {e}")
        return False


def check_service_health(service_name):
    """
    Verifica que un servicio específico esté disponible.

    Args:
        service_name (str): Nombre del servicio a verificar.

    Returns:
        bool: True si el servicio está disponible, False en caso contrario.
    """
    try:
        old_service = _current_service
        set_current_service(service_name)

        # Para servicios de infraestructura, usar actuator/health
        if service_name in [
            "service-discovery",
            "cloud-config",
            "api-gateway",
            "proxy-client",
        ]:
            response = make_request("GET", "actuator/health", service_name=service_name)
            result = response.status_code in [200, 404]
        else:
            # Para microservicios de negocio, usar un endpoint básico
            endpoint_map = {
                "user-service": "api/users",
                "product-service": "api/products",
                "order-service": "api/orders",
                "payment-service": "api/payments",
                "favourite-service": "api/favourites",
                "shipping-service": "api/shippings",
            }
            endpoint = endpoint_map.get(service_name, "actuator/health")
            response = make_request("GET", endpoint, service_name=service_name)
            result = response.status_code in [
                200,
                401,
                403,
            ]  # Incluir códigos de autenticación

        set_current_service(old_service)
        return result
    except Exception as e:
        print(f"❌ Error verificando {service_name}: {e}")
        return False


def wait_for_services(services=None, max_retries=30, delay=2):
    """
    Espera a que los servicios estén disponibles.

    Args:
        services (list, optional): Lista de servicios a verificar. Si es None, verifica todos.
        max_retries (int): Número máximo de reintentos.
        delay (int): Delay entre reintentos en segundos.

    Returns:
        dict: Estado de cada servicio.
    """
    if services is None:
        services = list(SERVICES_CONFIG.keys())

    print(f"🔍 Verificando disponibilidad de servicios: {', '.join(services)}")

    results = {}
    
    # Lista de servicios opcionales que no deben fallar las pruebas
    optional_services = ["proxy-client"]

    for service in services:
        print(f"\n🔍 Verificando {service}...")
        # Reducir reintentos para servicios opcionales
        retries = 3 if service in optional_services else max_retries
        for i in range(retries):
            if check_service_health(service):
                print(f"✅ {service} disponible")
                results[service] = True
                break
            else:
                print(f"⏳ {service} no disponible... intento {i+1}/{retries}")
                time.sleep(delay)
        else:
            if service in optional_services:
                print(f"⚠️ {service} no disponible (servicio opcional, continuando...)")
                results[service] = False
            else:
                print(f"❌ {service} no disponible después de {retries} intentos")
                results[service] = False

    return results


def reset_auth_token():
    """
    Resetea el token de autenticación para forzar una nueva autenticación.
    """
    global _jwt_token
    _jwt_token = None
    print("🔄 Token de autenticación reseteado")


def test_all_services_connectivity():
    """
    Prueba la conectividad de todos los servicios configurados.

    Returns:
        dict: Resultado detallado de las pruebas de conectividad.
    """
    print("🧪 Probando conectividad de todos los servicios...")

    results = wait_for_services()

    # Resumen
    available_services = [service for service, status in results.items() if status]
    unavailable_services = [
        service for service, status in results.items() if not status
    ]

    print(f"\n📊 Resumen de conectividad:")
    print(
        f"✅ Servicios disponibles ({len(available_services)}): {', '.join(available_services)}"
    )
    if unavailable_services:
        print(
            f"❌ Servicios no disponibles ({len(unavailable_services)}): {', '.join(unavailable_services)}"
        )

    return results


def is_success_status(status_code):
    """
    Verifica si un código de estado HTTP indica éxito.
    Acepta tanto 200 (OK) como 201 (Created) como exitosos.
    
    Args:
        status_code (int): Código de estado HTTP
        
    Returns:
        bool: True si el código es exitoso (200-299)
    """
    return 200 <= status_code < 300
