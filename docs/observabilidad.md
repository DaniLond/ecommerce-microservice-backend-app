

# Observabilidad y Monitoreo

## ¿Qué se preparó?
Se diseñó una solución completa de observabilidad para el sistema, pensando en cubrir métricas, logs y trazabilidad de las transacciones entre microservicios. Para esto, se crearon y dejaron listos los manifiestos de Kubernetes en la carpeta `k8s/monitoring/` para desplegar:
- **Prometheus** y **Grafana**: para recolectar y visualizar métricas técnicas y de negocio de todos los servicios.
- **ELK Stack** (Elasticsearch, Logstash, Kibana): para centralizar y consultar logs de todos los microservicios desde un solo lugar.
- **Jaeger** y **Zipkin**: para hacer tracing distribuido y poder seguir el recorrido de cada petición a través de los distintos servicios.

## Dashboards, alertas y health checks
Se definieron dashboards personalizados en Grafana para cada microservicio y se configuraron alertas críticas usando Alertmanager. Además, todos los servicios exponen endpoints de health y readiness/liveness probes, lo que permite monitorear su estado y disponibilidad de forma automática.

## ¿Qué no se pudo lograr?
A pesar de tener toda la configuración lista, no fue posible desplegar ni validar la infraestructura de monitoreo en AKS debido a limitaciones de recursos y cuotas en Azure. Por lo tanto, la integración real de los servicios con el stack de monitoreo y la comprobación de alertas no se pudo completar en un entorno productivo.

## Resumen
En conclusión, el proyecto cuenta con una solución de observabilidad y monitoreo completamente preparada a nivel de manifiestos y configuración, pero no se logró poner en marcha en la nube. Si en el futuro se dispone de los recursos necesarios, bastaría con aplicar estos manifiestos para tener visibilidad total del sistema.

---
[Volver al README principal](../README.md)
