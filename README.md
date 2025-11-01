# Duoc Link Ayudantías

Aplicación móvil (Android, Jetpack Compose) para publicar y descubrir ayudantías entre estudiantes (tema, hora, lugar, duración, cupos).

Stack
- Kotlin, Jetpack Compose, Navigation, ViewModel
- Gradle, GitHub Actions

Paleta institucional
- Duoc Yellow: #FAB21B
- Duoc Blue: #00213E
- Duoc White: #FFFFFF
- Duoc Gray: #CFD4D8

MVP (alcance inicial)
- Autenticación simple (login “dummy” y navegación a Home)
- Listado de ayudantías (mock/local)
- Crear ayudantía (formulario con validación mínima)

Cómo ejecutar
1) Abrir el proyecto en Android Studio (Giraffe+), JDK 17.
2) Ejecutar en emulador o dispositivo físico.

Ramas y releases
- main: estable y liberable (tags semánticos vX.Y.Z)
- dev: integración continua del sprint/iteración
- feature/*, task/*, fix/*: trabajo por Issue
- Flujo: feature → PR a dev → pruebas → merge → release: dev → main + tag

Tablero
- Project Board con columnas: To do / In progress / In review / Done
- Todos los Issues deben estar en el board

Definition of Done (DoD)
- Compila (CI verde)
- UI consistente con la paleta
- Validaciones básicas y manejo de errores visible
- PR con checklist completo y descripción clara
- Pruebas manuales mínimas documentadas en el PR

Roadmap corto
- v0.1.0: Login + Home + lista mock
- v0.2.0: Crear ayudantía + validación
- v0.3.0: Persistencia local (Room) y filtros básicos
