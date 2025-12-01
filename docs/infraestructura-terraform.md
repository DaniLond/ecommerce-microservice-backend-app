# Infraestructura como Código con Terraform

La infraestructura del proyecto se gestionó completamente como código usando **Terraform** en el repositorio de infraestructura (`ecommerce-microservice-Infraestructura`). El objetivo fue automatizar la provisión de todos los recursos necesarios en Azure para soportar la arquitectura de microservicios.

## Estructura y organización
- El repositorio está organizado en módulos reutilizables: `aks` (clúster Kubernetes), `networking` (redes y subredes), `key_vault` (gestión de secretos) y `resource_group` (grupos de recursos).
- Cada módulo define recursos independientes y parametrizables, facilitando la reutilización y el mantenimiento.
- La raíz del proyecto contiene archivos principales (`backend.tf`, `provider.tf`, etc.) y una carpeta `environments` con subcarpetas para cada ambiente (`dev`, `prod`).

## Ambientes y configuración
- Cada ambiente (`dev`, `prod`) tiene su propia carpeta con archivos `main.tf`, `variables.tf`, `outputs.tf` y archivos de variables (`terraform.tfvars`).
- Esto permite desplegar entornos aislados, con configuraciones y tamaños de recursos adaptados a cada necesidad.
- Se implementó un backend remoto para el estado de Terraform, pensado para trabajo colaborativo y evitar conflictos.

## Recursos y arquitectura
- Se definió un clúster AKS (Azure Kubernetes Service) para orquestar los microservicios.
- Se crearon redes virtuales, subredes y reglas de seguridad para aislar y proteger los servicios.
- Se incluyó Azure Key Vault para la gestión segura de secretos y credenciales.


## Arquitectura

![alt text](<Imagen de WhatsApp 2025-12-01 a las 01.17.55_914021bc.jpg>)

El diagrama representa la arquitectura de infraestructura diseñada para soportar los microservicios del eCommerce en Azure:

- **Usuarios/Clientes**: Acceden a la plataforma a través de internet.
- **Azure Application Gateway / Load Balancer**: Recibe el tráfico externo y lo distribuye hacia el clúster AKS, asegurando alta disponibilidad y balanceo de carga.
- **AKS (Azure Kubernetes Service)**: Es el núcleo de la solución, donde se orquestan y ejecutan todos los microservicios (API Gateway, servicios de dominio, discovery, config, etc.).
- **VNet y Subredes**: Todo el tráfico interno y externo está segmentado en redes virtuales y subredes para aislar ambientes y mejorar la seguridad.
- **Azure Key Vault**: Gestiona los secretos, credenciales y claves de acceso de manera segura, accesible solo por los servicios autorizados.
- **Bases de datos (Azure Database for MySQL, etc.)**: Cada microservicio puede tener su propia base de datos, siguiendo el patrón de base de datos por servicio.
- **Monitorización y Logs**: Se integran servicios como Prometheus, Grafana, ELK Stack y Zipkin/Jaeger para monitoreo, trazabilidad y gestión de logs.
- **Ambientes Dev y Prod**: El diagrama contempla la existencia de ambientes separados, cada uno con su propio clúster, recursos y configuración.

Esta arquitectura busca garantizar escalabilidad, seguridad, observabilidad y facilidad de gestión, siguiendo buenas prácticas de microservicios y cloud native.

## Modularidad y buenas prácticas
- El uso de módulos permite escalar y modificar la infraestructura fácilmente.
- Variables y outputs bien definidos facilitan la integración con otros sistemas y la automatización de pipelines.
- Separamos claramente la lógica de infraestructura de la lógica de aplicación.

## Dificultades y retos
- Nos enfrentamos a limitaciones de cuotas de IPs públicas y privadas, así como restricciones en los tipos de VM disponibles en Azure para nuestra suscripción.
- El clúster AKS no pudo ser desplegado completamente por falta de recursos y límites de la plataforma, a pesar de liberar IPs y ajustar configuraciones.
- La integración entre infraestructura y backend fue un reto, especialmente para coordinar los despliegues y la gestión de secretos.
- Los pipelines de backend funcionaron correctamente, pero la infraestructura quedó incompleta y no se logró un despliegue final en AKS.

## Aprendizajes
- La importancia de planificar bien las cuotas y recursos antes de automatizar despliegues en la nube.
- El valor de la modularidad y la documentación clara para facilitar el trabajo en equipo.
- La necesidad de monitorear y ajustar la infraestructura según las restricciones reales del proveedor cloud.

---
[Volver al README principal](../README.md)
