# Backend de Microservicios para eCommerce

Este repositorio contiene la implementación del backend de un sistema de eCommerce basado en microservicios. El objetivo principal es demostrar una arquitectura moderna, escalable y resiliente, integrando buenas prácticas de desarrollo, despliegue y operación.

## Descripción General

El sistema está compuesto por varios microservicios independientes, cada uno responsable de un dominio específico (usuarios, productos, pedidos, pagos, envíos, favoritos, etc.). Cada servicio se desarrolla en Java con Spring Boot y se comunica a través de HTTP y mensajería. La infraestructura está preparada para ejecutarse en contenedores Docker y orquestarse en Kubernetes (AKS).

### Características principales
- **Arquitectura desacoplada:** Cada microservicio es autónomo y puede evolucionar de forma independiente.
- **Resiliencia:** Se aplican patrones como Circuit Breaker, Retry, Bulkhead y TimeLimiter usando Resilience4j.
- **Automatización:** Integración y despliegue continuo con GitHub Actions.
- **Observabilidad:** Manifiestos listos para Prometheus, Grafana, ELK y Jaeger (no desplegados por defecto).
- **Seguridad:** Análisis de vulnerabilidades y pruebas automatizadas de seguridad.
- **Gestión de cambios:** Estrategias para releases, rollback y versionado.

## Estructura del Proyecto

- `api-gateway/`         Puerta de entrada y ruteo de peticiones.
- `service-discovery/`   Descubrimiento de servicios (Eureka).
- `cloud-config/`        Configuración centralizada.
- `user-service/`        Gestión de usuarios.
- `product-service/`     Gestión de productos.
- `order-service/`       Gestión de pedidos.
- `payment-service/`     Procesamiento de pagos.
- `shipping-service/`    Gestión de envíos.
- `favourite-service/`   Favoritos de usuario.
- `proxy-client/`        Cliente para pruebas de integración.

## ¿Cómo ejecutar localmente?

1. Clona el repositorio y navega a la carpeta principal.
2. Asegúrate de tener Docker y Docker Compose instalados.
3. Ejecuta `docker compose up` para levantar todos los servicios.
4. Accede a los endpoints de cada microservicio según la configuración de puertos.

## Pruebas y Calidad

- Pruebas unitarias y de integración con JUnit y Mockito.
- Pruebas de performance con Locust.
- Pruebas de seguridad automatizadas con OWASP ZAP y Trivy.
- Cobertura de pruebas y reportes generados en CI.

## Despliegue y Operación

- Despliegue automatizado en AKS usando Terraform y manifiestos de Kubernetes.
- Rollback manual disponible mediante scripts y kubectl.
- Observabilidad lista para ser habilitada en el clúster.

## Consideraciones de Seguridad

- Uso de secretos en GitHub Actions para credenciales sensibles.
- Manifiestos preparados para integración con Azure Key Vault (no implementado).
- TLS y RBAC definidos en manifiestos, no desplegados por defecto.

---

## Documentación por Etapas

- [Patrones de Diseño](./docs/patrones-diseno.md)
- [Pruebas](./docs/pruebas.md)
- [Gestión de Cambios](./docs/change-management.md)
- [Change Management y Release Notes](./docs/change-management.md)
- [Observabilidad](./docs/observabilidad.md)
- [Seguridad](./docs/seguridad.md)
- [CI/CD](./docs/cicd.md)
- [Infraestructura como Código](./docs/infraestructura-terraform.md)
- [Metodología Ágil y Branching](./docs/metodologia-branching.md)
- [Repositorio de Infraestructura (GitHub)](https://github.com/DaniLond/ecommerce-microservice-Infraestructura)
