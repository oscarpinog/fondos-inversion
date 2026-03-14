**MSc en Ingeniería de Software.**
*Oscar Rodriguez*
# Sistema de Gestión de Fondos de Inversión
Este proyecto desarrollado en **Java 21** con **Spring Boot 3.4+** permite la gestión de clientes y su vinculación a fondos de inversión. Implementa seguridad mediante JWT, persistencia con **JPA + Hibernate** y pruebas unitarias de controladores y servicios.

---
### ☁️☁️AWS DESPLIEGUE!☁️☁️
La aplicación esta ejecutándose en AWS.

Visita la ruta:
http://btg-fondos-app-env.eba-97ik4vhc.us-east-2.elasticbeanstalk.com/swagger-ui/index.html

☁️☁️☁️☁️☁️☁️☁️☁️☁️☁️☁️☁️☁️
### 📊 Reporte de Cobertura de Pruebas - JaCoCo

El reporte de cobertura de pruebas unitarias generado por JaCoCo está disponible en la siguiente ruta local:

👉 dentro del proyecto en la carpeta -> ./target/site/jacoco/index.html

> ⚠️ Para visualizar este reporte, abre el archivo `index.html` en un navegador.

## 🛠️ Tecnologías Usadas

- Java 21
- Spring Boot
- Spring Security (JWT)
- Spring Web
- Spring Data JPA
- Lombok
- Maven
- JUnit 5 + Mockito (pruebas)
- Swagger/OpenAPI para documentación
- Jacoco para cobertura de pruebas

---

## 🔐 Autenticación

La seguridad está implementada con JWT. Para consumir los endpoints protegidos, primero debes autenticarte:

### Login

```
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

### Usuarios precargados

| Usuario | Rol        | Contraseña |
|---------|------------|------------|
| admin   | ROLE_ADMIN | admin      |
| cli     | ROLE_USER  | cli        |

---

## ✅ Autorización

Solo los siguientes endpoints requieren autenticación como **ADMIN**:

- `POST /api/transacciones/suscribirse`
- `POST /api/transacciones/cancelar`
- `POST /api/clientes`

Para el resto de endpoints como `GET /api/clientes/{id}` o `GET /api/transacciones/cliente/{id}`, es suficiente autenticarse con un usuario de rol `USER`.

---

## 🔄 Relaciones de Entidades

- `ClienteEntity` ↔ `TransaccionEntity`: **Uno a Muchos**
- `FondoEntity` ↔ `TransaccionEntity`: **Uno a Muchos**
- `TransaccionEntity`: tabla intermedia que representa la relación de suscripciones y cancelaciones entre clientes y fondos.

---

## 🧪 Pruebas Unitarias

Se han implementado pruebas con **JUnit 5** y **Mockito** en:

- Controladores (`ClienteController`, `TransaccionController`, etc.)
- Implementaciones de servicios (`ClienteServiceImpl`, `TransaccionServiceImpl`)

Para visualizar la cobertura de pruebas con **Jacoco**:

```bash
mvn clean verify
```

Luego abre el archivo:

```
target/site/jacoco/index.html
```

---

## 🧪 Swagger UI

La documentación y pruebas de los endpoints están disponibles en:

📍 [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## ⚙️ Requisitos y configuración

### Requisitos

- Java 21
- Maven 3.9+
- IDE recomendado: STS, IntelliJ o VS Code,Eclipse

### Lombok

Este proyecto usa **Lombok**. Asegúrate de tener el plugin instalado en tu IDE para evitar errores de compilación.

### Base de Datos H2

Se usa base de datos **en memoria** para pruebas automáticas. Accesible vía navegador:

```
http://localhost:8080/h2-console
```

---

## 📦 Cómo ejecutar



## 🧾 Consideraciones del Negocio

- El saldo inicial de un cliente es **COP $500.000**.
- Cada transacción tiene un ID único y registra:
  - Cliente
  - Fondo
  - Tipo (APERTURA o CANCELACIÓN)
  - Monto
  - Fecha
- Cada fondo tiene un **monto mínimo** de vinculación.
- Al cancelar una suscripción, se **devuelve** el valor vinculado al saldo del cliente.
- Si el cliente no tiene saldo suficiente para suscribirse, se devuelve el mensaje:

```
No tiene saldo disponible para vincularse al fondo <Nombre del fondo>
```

---

## 📚 Scripts útiles

### Ver cobertura de pruebas (Jacoco)

```bash
#!/bin/bash
cd /ruta/del/proyecto
mvn clean verify
open target/site/jacoco/index.html
```

---

## 📬 Contacto
MSc en Ingeniería de Software Oscar Rodriguez
Este proyecto fue desarrollado como parte de una prueba académica o técnica.  
Para más información, contacta a `oscarpino711@gmail.com`.

---