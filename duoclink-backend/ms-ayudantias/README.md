# Microservicio de Ayudantías (ms-ayudantias)

Este microservicio gestiona las ayudantías del sistema DuocLink. Está construido con Spring Boot y utiliza Firebase Firestore como base de datos.

## Requisitos Previos

- Java 17
- Maven (incluido wrapper `mvnw`)
- Credenciales de Firebase (archivo `.env`)

## Configuración

1.  **Variables de Entorno**:
    El proyecto utiliza `spring-dotenv` para cargar variables desde un archivo `.env` en la raíz del proyecto (`ms-ayudantias/.env`).
    
    Si no tienes este archivo, copia el de `ms-apuntes` o crea uno nuevo con la siguiente estructura:

    ```properties
    FIREBASE_PROJECT_ID=tu-project-id
    FIREBASE_PRIVATE_KEY_ID=tu-private-key-id
    FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n"
    FIREBASE_CLIENT_EMAIL=tu-email@project.iam.gserviceaccount.com
    FIREBASE_CLIENT_ID=tu-client-id
    ```

    > **Nota**: Asegúrate de que `FIREBASE_PRIVATE_KEY` esté entre comillas dobles si contiene saltos de línea `\n`.

2.  **Puerto**:
    El servicio corre por defecto en el puerto **8082**. Puedes cambiarlo en `src/main/resources/application.properties`.

## Ejecución

Para iniciar el servicio, ejecuta el siguiente comando en la terminal dentro de la carpeta `ms-ayudantias`:

```powershell
./mvnw spring-boot:run
```

## Endpoints

La API base es `http://localhost:8082/api/ayudantias`.

| Método | Endpoint | Descripción | Auth Requerida |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Listar todas las ayudantías | No |
| `GET` | `/{id}` | Obtener detalle de una ayudantía | No |
| `POST` | `/` | Crear una nueva ayudantía | Sí (Bearer Token) |
| `PUT` | `/{id}` | Actualizar una ayudantía | Sí (Bearer Token) |
| `DELETE` | `/{id}` | Eliminar una ayudantía | Sí (Bearer Token) |

## Estructura del Proyecto

- `src/main/java/cl/duoc/msayudantias/config`: Configuración de Firebase.
- `src/main/java/cl/duoc/msayudantias/controller`: Controladores REST.
- `src/main/java/cl/duoc/msayudantias/model`: Modelos de datos (Ayudantia, Autor).
- `src/main/java/cl/duoc/msayudantias/repository`: Acceso a datos (Firestore).
- `src/main/java/cl/duoc/msayudantias/service`: Lógica de negocio.
