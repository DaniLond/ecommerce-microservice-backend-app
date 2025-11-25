# 🧪 **EcommerceCoZam Tests**

Este repositorio contiene una suite completa de pruebas diseñada para evaluar y asegurar la calidad, estabilidad y rendimiento de la **Plataforma de Microservicios EcommerceCoZam**. Las siguientes categorías de testing han sido implementadas para cubrir diversos aspectos del sistema.

## 📊 Resumen de Cobertura de Pruebas

Las pruebas están organizadas en las siguientes categorías principales:

### 1. 🔗 **Pruebas de Integración**

- 🤝 Validan interacciones entre servicios relacionados (ej: Order, Product, User)
- ⚙️ Aseguran configuración correcta de APIs, service discovery y comunicación vía HTTP
- 📁 Ubicadas en `integration-tests/`, incluyendo suites para API Gateway, Payment Service, Product Service y más
- 🌐 Verifican conectividad y flujo de datos entre microservicios

### 2. 🎭 **Pruebas End-to-End (E2E)**

- 👤 Simulan flujos de usuario reales como registro, navegación de productos, checkout y procesamiento de órdenes
- 🐍 Implementadas usando flujos basados en pytest y fixtures reutilizables
- 🎯 Enfocadas en validar la funcionalidad completa del sistema de e-commerce desde la perspectiva del usuario
- 📁 Encontradas en `e2e-tests/`, incluyendo escenarios como compra completa, actualizaciones de inventario y ciclo de vida del usuario

### 3. ⚡ **Pruebas de Rendimiento y Estrés**

- 🦗 Ejecutadas usando **Locust** para simular varios perfiles de usuario (admin, compradores, navegadores)
- 📊 Miden throughput, tiempo de respuesta y comportamiento del sistema bajo alta carga
- 📈 Reportes generados para endpoints y workflows clave
- 📁 Todos los scripts y configuraciones relacionados con rendimiento están en `performance-tests/`

### 4. 🛡️ **Pruebas de Seguridad**

- 🔍 Realizadas con **OWASP ZAP** para evaluar vulnerabilidades como XSS, inyección SQL y configuraciones incorrectas
- 🤖 Escaneos automáticos y reportes incluidos en `security-tests/`, junto con configuraciones Docker para testing local
- 📋 Alertas ZAP y reportes de seguridad versionados y almacenados para auditabilidad

## 🏗️ Estructura del Proyecto

```
📦 ecommerce-tests/
├── 📋 README.md                        # Este archivo
├── 🎭 e2e-tests/                       # Pruebas de flujo de usuario end-to-end
│   ├── 📁 config/                      # Configuración para pruebas E2E
│   ├── 🧪 tests/                       # Casos de prueba E2E
│   ├── 🔧 conftest.py                  # Configuración global de pytest
│   ├── 🚀 run_e2e_tests.py             # Script principal de ejecución
│   └── 📋 requirements.txt             # Dependencias E2E
├── 🔗 integration-tests/               # Pruebas de integración de servicios
│   ├── 📁 config/                      # Configuración de integración
│   ├── 🧪 tests/                       # Casos de prueba por servicio
│   ├── 🛠️ utils/                       # Utilidades compartidas
│   ├── 🔧 conftest.py                  # Setup de entorno de pruebas
│   ├── 🚀 run_integration_tests.py     # Script de ejecución completa
│   └── 📋 requirements.txt             # Dependencias de integración
├── ⚡ performance-tests/               # Pruebas de carga y estrés con Locust
│   ├── 🔧 locust.config                # Configuraciones de carga
│   ├── 🦗 locustfiles.py               # Escenarios de locust
│   ├── 📊 run-performance-test.sh/     # Script para ejecutar pruebas de rendimiento
│   └── 📋 requirements.txt             # Dependencias de integración
└── 🛡️ security-tests/                  # Evaluaciones de seguridad con OWASP ZAP
    ├── 🐳 zap-config/                  # Configuraciones para ZAP
    │   📊 run-security-test.sh/        # Script para ejecutar pruebas de seguridad
    ├── 🔧 zap-rules-to-ignore.conf     # Configuraciones
    └── ⚙️ security_test_suite.py       # Pruebas de seguridad
    └── 📋 requirements.txt             # Dependencias de integración
```

## 🏢 Arquitectura de Microservicios

### 💼 Servicios de Negocio

| Servicio                 | Puerto      | Descripción            | Función Principal                |
| ------------------------ | ----------- | ---------------------- | -------------------------------- |
| 🌐 **api-gateway**       | 8080 → 8222 | Punto de entrada único | Enrutamiento y balanceo de carga |
| 🔐 **proxy-client**      | 8900        | Cliente proxy          | Autenticación y autorización     |
| 🛍️ **product-service**   | 8500        | Gestión de catálogo    | Productos y categorías           |
| 📦 **shipping-service**  | 8600        | Gestión de logística   | Cálculo de costos de envío       |
| 💳 **payment-service**   | 8400        | Procesamiento de pagos | Transacciones y facturación      |
| 📋 **order-service**     | 8300        | Gestión de pedidos     | Carrito de compras y órdenes     |
| 👤 **user-service**      | 8700        | Gestión de usuarios    | Perfiles y autenticación         |
| ❤️ **favourite-service** | 8800        | Productos favoritos    | Lista de deseos por usuario      |

### 🏗️ Servicios de Infraestructura

| Servicio                 | Puerto | Descripción   | Función Principal                      |
| ------------------------ | ------ | ------------- | -------------------------------------- |
| 🔍 **service-discovery** | 8761   | Eureka Server | Registro y descubrimiento de servicios |
| ☁️ **cloud-config**      | 9296   | Config Server | Configuración centralizada             |
| 📊 **zipkin**            | 9411   | Tracing       | Trazabilidad distribuida               |

## 🔗 Endpoints de Verificación

### 🏗️ Servicios de Infraestructura

#### 🔍 Service Discovery (Eureka)

- 🖥️ **Dashboard**: [`http://localhost:8761`](http://localhost:8761)
- ✅ **Health Check**: [`http://localhost:8761/actuator/health`](http://localhost:8761/actuator/health)
- 📱 **Applications**: [`http://localhost:8761/eureka/apps`](http://localhost:8761/eureka/apps)

#### ☁️ Cloud Config

- ✅ **Health Check**: [`http://localhost:9296/actuator/health`](http://localhost:9296/actuator/health)
- ⚙️ **Environment**: [`http://localhost:9296/actuator/env`](http://localhost:9296/actuator/env)
- 📋 **Configuration**: [`http://localhost:9296/application/default`](http://localhost:9296/application/default)

#### 📊 Zipkin

- 🖥️ **Dashboard**: [`http://localhost:9411`](http://localhost:9411)
- ✅ **Health Check**: [`http://localhost:9411/health`](http://localhost:9411/health)
- 🔧 **Services API**: [`http://localhost:9411/api/v2/services`](http://localhost:9411/api/v2/services)

### 💼 Microservicios de Aplicación

#### 🌐 API Gateway (Puerto 8222)

- ✅ **Health Check**: [`http://localhost:8222/actuator/health`](http://localhost:8222/actuator/health)
- 🛣️ **Routes**: [`http://localhost:8222/actuator/gateway/routes`](http://localhost:8222/actuator/gateway/routes)
- 🔧 **Global Filters**: [`http://localhost:8222/actuator/gateway/globalfilters`](http://localhost:8222/actuator/gateway/globalfilters)
- 📊 **Metrics**: [`http://localhost:8222/actuator/metrics`](http://localhost:8222/actuator/metrics)

#### 🛍️ Product Service (Puerto 8500)

- ✅ **Health Check**: [`http://localhost:8500/product-service/actuator/health`](http://localhost:8500/product-service/actuator/health)
- 🏷️ **Products API**: [`http://localhost:8500/product-service/api/products`](http://localhost:8500/product-service/api/products)
- 📂 **Categories API**: [`http://localhost:8500/product-service/api/categories`](http://localhost:8500/product-service/api/categories)
- 📊 **Metrics**: [`http://localhost:8500/product-service/actuator/metrics`](http://localhost:8500/product-service/actuator/metrics)

#### 📋 Order Service (Puerto 8300)

- ✅ **Health Check**: [`http://localhost:8300/order-service/actuator/health`](http://localhost:8300/order-service/actuator/health)
- 🛒 **Orders API**: [`http://localhost:8300/order-service/api/orders`](http://localhost:8300/order-service/api/orders)
- 🛍️ **Carts API**: [`http://localhost:8300/order-service/api/carts`](http://localhost:8300/order-service/api/carts)
- 📊 **Metrics**: [`http://localhost:8300/order-service/actuator/metrics`](http://localhost:8300/order-service/actuator/metrics)

#### 💳 Payment Service (Puerto 8400)

- ✅ **Health Check**: [`http://localhost:8400/payment-service/actuator/health`](http://localhost:8400/payment-service/actuator/health)
- 💰 **Payments API**: [`http://localhost:8400/payment-service/api/payments`](http://localhost:8400/payment-service/api/payments)
- 📊 **Metrics**: [`http://localhost:8400/payment-service/actuator/metrics`](http://localhost:8400/payment-service/actuator/metrics)

#### 📦 Shipping Service (Puerto 8600)

- ✅ **Health Check**: [`http://localhost:8600/shipping-service/actuator/health`](http://localhost:8600/shipping-service/actuator/health)
- 🚚 **Shipping API**: [`http://localhost:8600/shipping-service/api/shippings`](http://localhost:8600/shipping-service/api/shippings)
- 📊 **Metrics**: [`http://localhost:8600/shipping-service/actuator/metrics`](http://localhost:8600/shipping-service/actuator/metrics)

#### 👤 User Service (Puerto 8700)

- ✅ **Health Check**: [`http://localhost:8700/user-service/actuator/health`](http://localhost:8700/user-service/actuator/health)
- 👥 **Users API**: [`http://localhost:8700/user-service/api/users`](http://localhost:8700/user-service/api/users)
- 🏠 **Addresses API**: [`http://localhost:8700/user-service/api/address`](http://localhost:8700/user-service/api/address)
- 🔑 **Credentials API**: [`http://localhost:8700/user-service/api/credentials`](http://localhost:8700/user-service/api/credentials)
- 📊 **Metrics**: [`http://localhost:8700/user-service/actuator/metrics`](http://localhost:8700/user-service/actuator/metrics)

#### ❤️ Favourite Service (Puerto 8800)

- ✅ **Health Check**: [`http://localhost:8800/favourite-service/actuator/health`](http://localhost:8800/favourite-service/actuator/health)
- 💝 **Favourites API**: [`http://localhost:8800/favourite-service/api/favourites`](http://localhost:8800/favourite-service/api/favourites)
- 📊 **Metrics**: [`http://localhost:8800/favourite-service/actuator/metrics`](http://localhost:8800/favourite-service/actuator/metrics)

## 🚀 Ejecución de Pruebas

### 🔗 Pruebas de Integración

```bash
# Ejecutar todas las pruebas de integración
cd integration-tests/
python run_integration_tests.py

# Ejecutar pruebas específicas por servicio
python run_integration_tests.py --service user-service
python run_integration_tests.py --service product-service

# Ejecutar con reporte HTML
python run_integration_tests.py --html --verbose

# Solo verificación de conectividad
python run_integration_tests.py --connectivity
```

### 🎭 Pruebas End-to-End

```bash
# Ejecutar todas las pruebas E2E
cd e2e-tests/
python run_e2e_tests.py

# Ejecutar escenarios específicos
python run_e2e_tests.py --scenario complete_purchase
python run_e2e_tests.py --scenario user_lifecycle

# Generar reportes detallados
python run_e2e_tests.py --html --verbose
```

### ⚡ Pruebas de Rendimiento

```bash
# Ejecutar pruebas de carga básicas
cd performance-tests/
locust -f locustfiles/basic_load_test.py --host=http://localhost:8222

# Pruebas de estrés con múltiples usuarios
locust -f locustfiles/stress_test.py --users=1000 --spawn-rate=10

# Generar reportes de performance
locust -f locustfiles/full_scenario.py --html=reports/performance_report.html
```

### 🛡️ Pruebas de Seguridad

```bash
# Ejecutar escaneo de seguridad básico
cd security-tests/
docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py \
  -t http://localhost:8222 -J zap_report.json

# Escaneo completo de vulnerabilidades
./scripts/run_full_security_scan.sh

# Generar reporte de seguridad
./scripts/generate_security_report.sh
```

## 📊 Reportes y Cobertura

### 📈 Tipos de Reportes Generados

| Tipo de Prueba     | Formato de Reporte | Ubicación                    | Contenido                         |
| ------------------ | ------------------ | ---------------------------- | --------------------------------- |
| 🔬 **Unit Tests**  | HTML, XML, JSON    | `*/coverage/`                | Cobertura de código, test results |
| 🔗 **Integration** | HTML, JSON         | `integration-tests/reports/` | API connectivity, service health  |
| 🎭 **E2E**         | HTML, Screenshots  | `e2e-tests/reports/`         | User flow validation, screenshots |
| ⚡ **Performance** | HTML, CSV          | `performance-tests/reports/` | Load metrics, response times      |
| 🛡️ **Security**    | HTML, JSON, PDF    | `security-tests/reports/`    | Vulnerability assessment          |

### 📊 Métricas de Calidad

| Métrica               | Objetivo    | Herramienta | Frecuencia |
| --------------------- | ----------- | ----------- | ---------- |
| 📈 **Code Coverage**  | > 80%       | pytest-cov  | Por commit |
| ✅ **Test Pass Rate** | > 95%       | pytest      | Por build  |
| ⚡ **Response Time**  | < 200ms P95 | Locust      | Semanal    |
| 🛡️ **Security Score** | 0 Critical  | OWASP ZAP   | Mensual    |

## 🛠️ Dependencias y Herramientas

### 🐍 Python Dependencies

```
pytest==7.4.3          # Framework de testing
requests==2.31.0        # HTTP client para APIs
pytest-html==4.1.1     # Reportes HTML
pytest-xdist==3.5.0    # Ejecución paralela
pytest-cov==4.1.0      # Cobertura de código
```

### 🔧 Herramientas Externas

- 🦗 **Locust** - Testing de carga y rendimiento
- 🛡️ **OWASP ZAP** - Escaneo de vulnerabilidades de seguridad
- 🐳 **Docker** - Ejecución containerizada de tests de seguridad
- 📊 **Allure** - Reportes avanzados de testing (opcional)

## 🏃‍♂️ Quick Start Guide

### 1. 🔧 Setup del Entorno

```bash
# Clonar el repositorio de tests
git clone https://github.com/EcommerceCoZam/ecommerce-tests
cd ecommerce-tests

# Configurar entorno virtual
python -m venv venv
source venv/bin/activate  # Linux/Mac
# venv\Scripts\activate   # Windows

# Instalar dependencias
pip install -r integration-tests/requirements.txt
pip install -r e2e-tests/requirements.txt
```

### 2. 🚀 Verificación Rápida del Sistema

```bash
# Verificar que todos los servicios estén disponibles
cd integration-tests/
python run_integration_tests.py --connectivity

# Ejecutar smoke tests básicos
python run_integration_tests.py --smoke
```

### 3. 📊 Generar Reporte Completo

```bash
# Ejecutar suite completa con reportes
./scripts/run_full_test_suite.sh

# Los reportes se generarán en:
# - integration-tests/reports/
# - e2e-tests/reports/
# - performance-tests/reports/
# - security-tests/reports/
```

## 🔧 Configuración y Personalización

### ⚙️ Variables de Entorno

```bash
# URLs de servicios (ajustar según entorno)
export API_GATEWAY_URL="http://localhost:8222"
export SERVICE_DISCOVERY_URL="http://localhost:8761"
export CLOUD_CONFIG_URL="http://localhost:9296"

# Configuración de autenticación
export TEST_USERNAME="selimhorri"
export TEST_PASSWORD="12345"

# Configuración de performance tests
export LOCUST_USERS="100"
export LOCUST_SPAWN_RATE="10"
export LOCUST_RUN_TIME="5m"
```

### 🎯 Configuración por Entorno

| Entorno            | Gateway URL                              | Configuración   | Notas                |
| ------------------ | ---------------------------------------- | --------------- | -------------------- |
| 🏠 **Local**       | `http://localhost:8222`                  | Default config  | Para desarrollo      |
| 🧪 **Development** | `https://dev-api.ecommercecozam.com`     | Dev profile     | CI/CD testing        |
| 🎭 **Staging**     | `https://staging-api.ecommercecozam.com` | Staging profile | Pre-production       |
| 🚀 **Production**  | `https://api.ecommercecozam.com`         | Prod profile    | Solo read-only tests |
