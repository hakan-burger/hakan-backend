# Hakan Burger – Backend

API REST del sistema de gestión de pedidos y delivery. Java 21 + Spring Boot + JPA (H2 para desarrollo, PostgreSQL para producción).

## Requisitos

- JDK 21 (en Spring Tool Suite: *Window → Preferences → Java → Installed JREs*)
- No hace falta instalar una base de datos: por defecto usa **H2 en memoria**.

## Cómo ejecutarlo

1. En Spring Tool Suite: clic derecho al proyecto → **Run As → Spring Boot App**.
   O por consola: `mvnw spring-boot:run`
2. La API queda en http://localhost:8080/api
3. Al iniciar se cargan los datos de prueba (`config/DatosIniciales.java`): la carta, zonas,
   direcciones y los pedidos #1047 a #1049. Son los mismos del frontend (`shared/data/mock.ts`).
   Al detener la aplicación los datos se borran y se vuelven a cargar en el siguiente inicio.
4. Para ver las tablas: http://localhost:8080/h2-console
   (JDBC URL `jdbc:h2:mem:hakan_burger`, usuario `sa`, sin clave).

### Conectar el frontend

En el `.env` del frontend poner `VITE_USAR_MOCK=false` y reiniciar `pnpm dev`.
Las pantallas pasan a usar esta API sin cambiar nada más.

### Usar PostgreSQL en lugar de H2

Crear la base de datos `hakan_burger` y ejecutar con el perfil `postgres`:

```bash
set DB_PASSWORD=tu_clave
mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Endpoints

| Método | Ruta | Uso |
|---|---|---|
| GET | `/api/categorias` | Pestañas de la carta |
| GET | `/api/productos?categoria=&buscar=` | Carta |
| GET | `/api/productos/{id}` | Ficha del producto |
| PATCH | `/api/productos/{id}/disponibilidad` | Marcar agotado / disponible |
| POST | `/api/productos/verificar-disponibilidad` | Carrito: productos agotados |
| POST | `/api/pedidos` | Confirmar pedido |
| GET | `/api/pedidos?estado=` | Bandeja de atención y cocina |
| GET | `/api/pedidos/mios` | Mis pedidos |
| GET | `/api/pedidos/{id}` | Detalle y seguimiento |
| PATCH | `/api/pedidos/{id}/estado` | Cambiar estado |
| PATCH | `/api/pedidos/{id}/verificar-pago` | Confirmar pago |
| GET / POST | `/api/direcciones` | Mis direcciones |
| GET | `/api/zonas` | Zonas de reparto y tarifas |
| GET | `/api/sunat/ruc/{ruc}` | Consulta de RUC (simulada) |

Los errores responden texto simple (400 = dato inválido, 404 = no existe) para que el frontend lo muestre tal cual.

## Despliegue (Render + GitHub Actions)

El workflow `.github/workflows/ci-cd.yml` hace lo siguiente:

- En cada push o pull request a `main` o `development`: compila y ejecuta las pruebas.
- En cada push a `main` (por ejemplo, al hacer merge de un PR): si las pruebas pasan, despliega en Render.

Configuración (una sola vez):

1. En https://render.com entrar con la cuenta de GitHub → **New → Web Service** → elegir el repo `hakan-backend`.
2. Runtime **Docker**, rama `main`, plan **Free**. En *Advanced*: **Auto-Deploy: No** (despliega GitHub Actions)
   y **Health Check Path** `/api/categorias`.
3. Variable de entorno `APP_CORS_ORIGENES` = direcciones del frontend separadas por coma
   (ej. `http://localhost:5173,https://hakan-frontend.vercel.app`).
4. En Render: **Settings → Deploy Hook** → copiar la URL.
5. En GitHub: **Settings → Secrets and variables → Actions → New repository secret**
   con el nombre `RENDER_DEPLOY_HOOK_URL` y la URL del paso 4.

En el plan gratuito el servicio se apaga tras 15 minutos sin uso; la primera llamada después tarda unos 50 segundos.
Como usa H2 en memoria, los datos de prueba vuelven a su estado inicial cada vez que se reinicia.

## Estructura (por módulo y capa, arquitectura MVC)

```
src/main/java/com/hakanburger/
├── pedidos/     controller · service · repository · model · dto
├── delivery/    controller · service · repository · model · dto
├── reportes/    controller · service · repository · model · dto
├── config/      configuración general (CORS, seguridad)
└── common/      cosas compartidas (excepciones, respuestas)
```

## Forma de trabajo

Ramas, commits y pull requests: ver [CONTRIBUTING.md](CONTRIBUTING.md).
