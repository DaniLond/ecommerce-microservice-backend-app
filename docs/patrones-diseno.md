# Patrones de Diseño

## Patrones identificados en la arquitectura
- **Service Discovery**: Eureka para registro y descubrimiento de servicios.
- **API Gateway**: Centraliza el acceso y la seguridad a los microservicios.
- **External Configuration**: Uso de Cloud Config para centralizar la configuración.


## Patrones de resiliencia implementados

- **Circuit Breaker**: Se implementó con Resilience4j en el `proxy-client` para Product, User y Favourite Service. Configurado para abrir el circuito si más del 50% de las últimas 10 llamadas fallan o son lentas (>2s), rechazando nuevas llamadas durante 10 segundos y ejecutando un fallback inmediato. Esto evita que un servicio caído o degradado afecte a todo el sistema.

- **Retry**: Se configuró para reintentar hasta 3 veces operaciones fallidas por errores transitorios (como timeouts o IOExceptions), con backoff exponencial (500ms, 1s, 2s). Así, si un servicio falla temporalmente, el usuario puede recibir una respuesta exitosa sin notar el fallo. No se reintentan errores de negocio (4xx).

- **Bulkhead**: Se limitó a 10 llamadas concurrentes por servicio protegido usando el bulkhead tipo semáforo. Si se supera el límite, las llamadas adicionales son rechazadas y se ejecuta un fallback, evitando que un servicio lento consuma todos los recursos y afecte a los demás.

- **TimeLimiter**: Se estableció un timeout de 3 segundos por operación protegida. Si una llamada supera ese tiempo, se cancela y se ejecuta un fallback, liberando el thread y evitando bloqueos prolongados. Esto protege el thread pool y garantiza tiempos de respuesta máximos.

Todos estos patrones están configurados en `proxy-client/src/main/java/com/selimhorri/app/business/*/service/Resilient*Service.java` y su configuración centralizada en `application.yml`. Se monitorean métricas y eventos a través de Prometheus y logs para ajustar los límites según el comportamiento real del sistema.


## Propósito y beneficios
- Mejorar la disponibilidad y resiliencia ante fallos de red o servicios.
- Proteger los recursos del sistema y evitar degradación global.
- Permitir respuestas rápidas y controladas ante errores.
- Facilitar la gestión y evolución segura de la arquitectura.


---
[Volver al README principal](../README.md)
