# Contribuir

Flujo de trabajo
1. Crea un Issue (bug/feature/task) y añádelo al Project Board.
2. Crea una rama desde dev: `feature/<descripcion>`, `task/<descripcion>` o `fix/<descripcion>`.
3. Commits pequeños y atómicos.
4. Abre un PR hacia `dev` y solicita revisión.
5. CI debe pasar. Usa “Squash and merge”.
6. Cuando `dev` esté estable, hacemos release a `main` con tag.

Convención de ramas
- feature/login, feature/ayudantias-crud
- task/ci-setup, task/theme-colors
- fix/login-crash, hotfix/build-break

Commits (Convencional)
- Formato: `tipo(scope): mensaje breve (#ISSUE)`
- Tipos: feat, fix, chore, docs, refactor, style, test
- Ej: `feat(login): pantalla de login con navegación (#12)`

PRs
- Vincula el Issue
- Describe el cambio y el impacto
- Incluye checklist DoD
- Adjunta capturas si es UI

Revisión y merges
- Revisión mínima: 1 aprobación
- Integración: preferir “Squash and merge” a dev
- Releases: merge dev → main con tag semántico (vX.Y.Z)

Código y estilo
- Kotlin idiomático, Jetpack Compose
- Mantener funciones pequeñas y componibles
- ViewModel para estado, navegación por rutas selladas