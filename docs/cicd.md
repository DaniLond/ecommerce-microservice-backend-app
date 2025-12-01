# CI/CD Avanzado

## Pipelines implementados
- Se configuraron pipelines de CI/CD usando **GitHub Actions**.
- Cada microservicio tiene su propio workflow para build, test y despliegue.
- Ambientes separados: dev y prod, con promoción controlada.

## Herramientas
- **SonarQube**: Análisis estático de código.
- **Trivy**: Escaneo de vulnerabilidades en imágenes Docker.
- **Versionado semántico**: Automatizado con GitHub Actions.
- **Notificaciones**: Alertas automáticas ante fallos en la pipeline.
- **Aprobaciones**: Requeridas para despliegues a producción.

## Dificultades
- Integrar análisis de seguridad y calidad en todos los servicios.
- Configurar triggers y aprobaciones para ambientes productivos.

## Resultado
- Los pipelines del backend corrieron correctamente y permitieron validar la calidad y seguridad del código antes de cada despliegue.

---
[Volver al README principal](../README.md)
