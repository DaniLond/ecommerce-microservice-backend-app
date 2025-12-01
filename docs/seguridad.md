
# Seguridad

## ¿Qué se implementó?

- **Escaneo de vulnerabilidades:** Se integró Trivy en los pipelines CI/CD para analizar las imágenes Docker de los microservicios y detectar vulnerabilidades conocidas antes de cada despliegue. Los resultados se suben como artefactos y alertas en GitHub.

- **Pruebas de seguridad automatizadas:** Se desarrolló un script completo (`tests/security/run-security-tests.sh`) que ejecuta OWASP ZAP en modo automatizado y manual contra los endpoints de los microservicios. Este script realiza crawling, pruebas de autenticación, verifica headers de seguridad y genera reportes HTML y JSON con los hallazgos. Las pruebas pueden ejecutarse localmente o integrarse en pipelines.

- **RBAC:** Se incluyeron configuraciones RBAC en los manifiestos de Kubernetes para los componentes de monitoreo (Prometheus, ELK), pero no se desplegaron ni validaron en un entorno real.

- **Gestión de secretos:** Se utilizaron secretos de GitHub Actions (`secrets.*`) para almacenar credenciales y variables sensibles en los pipelines CI/CD, protegiendo accesos a Azure, Docker y notificaciones. 
- **TLS:** Se dejó preparada la configuración para habilitar TLS en algunos manifiestos, pero no se completó la implementación ni la validación en ambientes locales o AKS.

## Dificultades
- No se logró desplegar ni validar la gestión de secretos ni TLS por limitaciones de infraestructura y tiempo.
- La seguridad de los endpoints depende de la correcta ejecución de los scripts y la revisión manual de los reportes.

---
[Volver al README principal](../README.md)
