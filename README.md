# 🏆 Sistema de Gestión de Liga de Fútbol (Arquitectura N-Capas)

Proyecto académico desarrollado en **Java con Spring Boot, SQL Server, Spring Data JPA, Tailwind CSS y JavaScript ES6**, diseñado bajo el patrón de **Arquitectura en N-Capas** para la gestión integral de un torneo de fútbol.

---

## 📐 Arquitectura del Proyecto

El sistema aplica una separación estricta de responsabilidades en las siguientes 5 capas principales:

```text
               [ Cliente Web ] (Navegador)
                      │
                      ▼
┌───────────────────────────────────────────────────────────┐
│ 1. CAPA DE PRESENTACIÓN (Frontend)                        │
│    index.html + app.js (Tailwind CSS + fetch API REST)    │
└─────────────────────────────┬─────────────────────────────┘
                              │ HTTP (JSON)
                              ▼
┌───────────────────────────────────────────────────────────┐
│ 2. CAPA DE EXPOSICIÓN (Controller REST)                   │
│    EquipoController, EncuentroController                  │
└─────────────────────────────┬─────────────────────────────┘
                              │ DTOs (Data Transfer Objects)
                              ▼
┌───────────────────────────────────────────────────────────┐
│ 3. CAPA DE LÓGICA DE NEGOCIO (Service)                    │
│    EquipoService, EncuentroService                        │
└─────────────────────────────┬─────────────────────────────┘
                              │ Entidades JPA
                              ▼
┌───────────────────────────────────────────────────────────┐
│ 4. CAPA DE ACCESO A DATOS (Repository)                    │
│    EquipoRepository, EncuentroRepository (Spring Data JPA)│
└─────────────────────────────┬─────────────────────────────┘
                              │ JDBC / T-SQL
                              ▼
┌───────────────────────────────────────────────────────────┐
│ 5. CAPA DE PERSISTENCIA (Base de Datos)                   │
│    SQL Server (LigaFutbolDB)                              │
└───────────────────────────────────────────────────────────┘
```

---

## 🚀 Tecnologías Utilizadas

* **Backend:** Java 17, Spring Boot 3.2.5
* **Persistencia:** Spring Data JPA / Hibernate
* **Base de Datos:** Microsoft SQL Server
* **Frontend:** HTML5, Tailwind CSS (vía CDN), JavaScript (ES6 `fetch`)
* **Gestión de Dependencias y Build:** Apache Maven

---

## 🛠️ Requisitos Previos

1. **Java JDK 17** o superior instalado y configurado (`JAVA_HOME`).
2. **Apache Maven 3.8+** (o el Maven embebido en tu IDE IntelliJ IDEA / Eclipse).
3. **Microsoft SQL Server** (LocalDB, Express o Enterprise) ejecutándose en el puerto `1433`.

---

## 🗄️ Configuración de la Base de Datos en SQL Server

1. Abre **SQL Server Management Studio (SSMS)** o **Azure Data Studio**.
2. Crea la base de datos ejecutando el siguiente script SQL:

```sql
CREATE DATABASE LigaFutbolDB;
GO
```

3. Abre el archivo de configuración del proyecto en [`src/main/resources/application.properties`](file:///d:/Proyects/futbol_capas/src/main/resources/application.properties) y ajusta las credenciales de tu SQL Server:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=LigaFutbolDB;encrypt=false;trustServerCertificate=true
spring.datasource.username=tu_usuario_sql
spring.datasource.password=tu_contraseña_sql
```

> **Nota sobre Hibernate DDL:** `spring.jpa.hibernate.ddl-auto=update` creará automáticamente las tablas `equipos` y `encuentros` con sus relaciones y claves foráneas al iniciar la aplicación.

---

## ⚙️ Pasos para Inicializar la Aplicación

### Opción 1: Ejecutar con Maven (Modo Desarrollo)

Abre la terminal en la raíz del proyecto ([`d:\Proyects\futbol_capas`](file:///d:/Proyects/futbol_capas)) y ejecuta:

```bash
mvn spring-boot:run
```

### Opción 2: Compilar y Ejecutar el JAR Generado

1. Compila y empaqueta la aplicación:
   ```bash
   mvn clean package -DskipTests
   ```
2. Ejecuta el archivo ejecutable resultante:
   ```bash
   java -jar target/futbol-0.0.1-SNAPSHOT.jar
   ```

---

## 🌐 Acceso a la Interfaz Web

Una vez inicializado Spring Boot, abre tu navegador e ingresa a:

👉 **[http://localhost:8080](http://localhost:8080)**

---

## 📡 Documentación de la API REST

| Método | Endpoint | Descripción | Cuerpo (JSON Request) | Código HTTP Éxito | Error Cliente |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/equipos` | Registrar un nuevo equipo | `{"nombre": "Real Madrid"}` | `201 Created` | `400 Bad Request` |
| `GET` | `/api/equipos` | Listar todos los equipos | N/A | `200 OK` | N/A |
| `POST` | `/api/encuentros` | Registrar resultado de partido | `{"equipoLocalId": 1, "equipoVisitanteId": 2, "golesLocal": 3, "golesVisitante": 1, "fecha": "2026-08-30"}` | `201 Created` | `400 Bad Request` |
| `GET` | `/api/encuentros` | Listar todos los encuentros | N/A | `200 OK` | N/A |
| `GET` | `/api/encuentros/tabla-posiciones` | Obtener tabla de posiciones calculada | N/A | `200 OK` | N/A |

---

## ⚽ Reglas de Negocio Implementadas en `Service`

1. **Puntaje oficial:**
   * **Victoria:** 3 puntos
   * **Empate:** 1 punto
   * **Derrota:** 0 puntos
2. **Validaciones de Encuentro:**
   * Un equipo **no puede enfrentarse consigo mismo** (`equipoLocalId != equipoVisitanteId`).
   * Los goles **no pueden ser números negativos** (`goles >= 0`).
   * Ambos equipos deben existir previamente en la base de datos.
3. **Criterio de Ordenamiento de la Tabla de Posiciones:**
   1. **Puntos** (Mayor a menor)
   2. **Diferencia de Goles** (Mayor a menor)
   3. **Goles a Favor** (Mayor a menor)
   4. **Nombre del equipo** (Alfabético)

---

## 📂 Estructura del Proyecto

```text
futbol_capas/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/liga/futbol/
    │   │   ├── FutbolApplication.java           # Clase Principal Spring Boot
    │   │   ├── controller/                      # Capa 2: Controllers REST
    │   │   │   ├── EquipoController.java
    │   │   │   └── EncuentroController.java
    │   │   ├── dto/                             # Data Transfer Objects
    │   │   │   ├── EncuentroRequestDTO.java
    │   │   │   ├── EncuentroResponseDTO.java
    │   │   │   └── PosicionDTO.java
    │   │   ├── entity/                          # Entidades JPA (Mapeo SQL)
    │   │   │   ├── Equipo.java
    │   │   │   └── Encuentro.java
    │   │   ├── repository/                      # Capa 4: Repositorios Spring Data
    │   │   │   ├── EquipoRepository.java
    │   │   │   └── EncuentroRepository.java
    │   │   └── service/                         # Capa 3: Lógica de Negocio
    │   │       ├── EquipoService.java
    │   │       └── EncuentroService.java
    │   └── resources/
    │       ├── application.properties           # Configuración Spring & SQL Server
    │       └── static/                          # Capa 1: Frontend Web
    │           ├── index.html                   # Interfaz principal con Tailwind CSS
    │           ├── styles.css                   # Estilos personalizados opcionales
    │           └── app.js                       # Consumo de API REST con fetch()
```
