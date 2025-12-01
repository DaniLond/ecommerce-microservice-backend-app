
# Pruebas Completas

## Estructura y enfoque de pruebas
El proyecto implementa una estrategia de pruebas integral, abarcando desde pruebas unitarias hasta pruebas de seguridad y rendimiento. Toda la lógica de pruebas se encuentra organizada en la carpeta `tests/`, que contiene subcarpetas especializadas para cada tipo de prueba:

- **unitarias**: Cada microservicio Java cuenta con pruebas unitarias (JUnit 5 + Mockito) para su lógica principal, cubriendo servicios, controladores, helpers y excepciones. La cobertura se mide con JaCoCo y se reporta por servicio.
- **integration/**: Pruebas de integración entre microservicios, usando Python (pytest) para validar la comunicación y los flujos de negocio entre servicios como API Gateway, Service Discovery, Cloud Config, y los servicios de dominio. Los archivos clave incluyen `run_integration_tests.py` y tests como `test_api_gateway.py`, `test_user_service.py`, etc.
- **e2e/**: Pruebas end-to-end automatizadas en Python, simulando escenarios completos de usuario (por ejemplo, compra de productos, pagos, favoritos). Se ejecutan con pytest y generan reportes HTML (`e2e_report.html`).
- **performance/**: Pruebas de carga y estrés con Locust (`locustfile.py`), configuradas para simular múltiples usuarios y medir tiempos de respuesta, throughput y errores bajo carga. Los resultados se almacenan en `performance/reports/`.
- **security/**: Pruebas de seguridad automatizadas con OWASP ZAP (`security_test_suite.py`) y Trivy, escaneando vulnerabilidades en los endpoints y las imágenes Docker. Los reportes se encuentran en `security/security-reports/`.

## Automatización y ejecución
Todas las pruebas se integran y ejecutan automáticamente en los pipelines de CI/CD. Los scripts `run_integration_tests.py`, `run_e2e_tests.py`, `run_performance_tests.py` y `run-security-tests.sh` permiten la ejecución local o en CI, generando reportes detallados para cada etapa.

## Ejemplo de pruebas implementadas
- **Unitarias**: Validación de lógica de negocio, controladores y excepciones en cada microservicio (ver documentación detallada en `Documentación de Pruebas Unitarias`).
- **Integración**: `integration/tests/test_product_service.py` valida la interacción entre el API Gateway y Product Service, incluyendo casos de éxito y error.
- **E2E**: `e2e/tests/test_complete_flows.py` simula un flujo completo de compra, desde el registro de usuario hasta el pago y envío.
- **Rendimiento**: `performance/locustfile.py` define escenarios de carga para medir la escalabilidad y tiempos de respuesta del sistema.
- **Seguridad**: `security/security_test_suite.py` ejecuta escaneos automáticos de vulnerabilidades OWASP y reporta hallazgos críticos.


## Resultado
Los pipelines de backend validan la calidad, seguridad y robustez del sistema antes de cada despliegue, asegurando que todos los servicios funcionen correctamente bajo diferentes escenarios y cargas.

---
[Volver al README principal](../README.md)
