
# Change Management y Release Notes

## Proceso de Change Management
- El control de cambios se gestiona principalmente a través de Pull Requests y ramas principales.
- Cada cambio relevante queda registrado en los mensajes de commit y en los PRs, pero el etiquetado automático del tipo de cambio no está implementado.
- La trazabilidad entre cambios y releases se mantiene mediante el uso de commits, tags y releases en GitHub.

## Release Notes
- Se implementó la generación automática de Release Notes y changelog usando GitHub Actions.
- Cada release se publica a partir de un tag (`v*`) y contiene un resumen de cambios, mejoras, correcciones, refactors, documentación y estadísticas, generado a partir de los mensajes de commit.
- El changelog se actualiza automáticamente y se notifica por Slack.

## Planes de rollback
- El pipeline de producción incluye un rollback automático: si falla el despliegue, se ejecuta `kubectl rollout undo` para revertir los deployments de los microservicios.
- Tras el rollback, se valida el estado de los servicios.
- El uso de tags y releases permite identificar y volver a versiones estables fácilmente.

## Dificultades
- Automatizar la generación de notas y mantener la trazabilidad entre cambios y releases depende de la calidad de los mensajes de commit.
- No se implementó el etiquetado automático del tipo de cambio en los PRs, por lo que la clasificación depende de la convención en los mensajes.

---
[Volver al README principal](../README.md)
