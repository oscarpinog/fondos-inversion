# Proyecto Spring Boot - Configuración y Uso

## ⚙️ Configuración del JDK

- Este proyecto utiliza **Java 21**.  
- Asegúrate de configurar `jdk-21` en el **Java Build Path** de tu IDE.
- Para ello, ve a `Build Path > Add Library > JRE System Library > Installed JREs` y selecciona el JDK 21.

---

## 📦 Librería Lombok

- Se utilizó **[Lombok](https://projectlombok.org/)** para reducir el código boilerplate en clases.
- Debes integrar Lombok en tu IDE para evitar errores de compilación.
  - En Eclipse: Instala el plugin de Lombok.
  - En IntelliJ: Habilita la anotación de procesamiento y agrega el plugin desde settings.

---

## 🗄️ Base de Datos - Oracle

- Se utiliza **Oracle** como base de datos.
-ajustar usuario y password en properties del proyecto.
- El esquema debe ser creado previamente:

- La propiedad en `application.properties` permite la creación/eliminación automática de tablas sin necesidad de scripts SQL manuales:

```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

> ⚠️ **Nota:** Esto eliminará las tablas cada vez que la aplicación se reinicie. Úsalo solo para entornos de desarrollo.

---

## 📚 Documentación con Swagger

- Se habilitó Swagger para facilitar la documentación y prueba de los endpoints REST.
- Accede a la interfaz Swagger desde:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Seguridad - Autenticación y Autorización

- Se implementó seguridad usando **Spring Security + JWT**.
- El proyecto genera automáticamente algunos usuarios por defecto al iniciar, gracias a un `@Bean`.

### Usuarios por defecto

```json
{
  "username": "admin",
  "password": "admin123"
}
```

```json
{
  "username": "aux",
  "password": "aux123"
}
```

---

## 🛠️ Logs

- Se utilizó la anotación `@Slf4j` en las clases `ServiceImpl` para registrar actividad del sistema y facilitar el monitoreo.

## OTROS
-El endpoint de Eliminar comerciantes solo se podra usar con el rol de ROL_ADMIN



## 🐳 Docker - Imagen Oracle 21c-xe

Usaremos la **Express Edition** que se encuentra en:  
https://container-registry.oracle.com/

---

## 📥 Descargar la imagen

```
docker pull container-registry.oracle.com/database/express:latest
```

---

## 🚀 Correr el contenedor y habilitar conexión

Recuerda mapear el puerto al correr el contenedor. Ejecuta desde PowerShell o terminal:

```
docker run --name <container_name> -p 1521:1521 \
  -e ORACLE_PWD=<sysadmin_password> \
  -v [<host_mount_point>:]/opt/oracle/oradata \
  container-registry.oracle.com/database/express:21.3.0-xe
```

### Ejemplo:

```
docker run --name OracleXE -p 1521:1521 \
  -e ORACLE_PWD=1234 \
  -v D:\dataSqlDeveloper:/opt/oracle/oradata \
  container-registry.oracle.com/database/express:21.3.0-xe
```

---

## ✅ Uso de SQL*PLUS desde el contenedor

### Creacion de nuevo usuario!

### Verifica el nombre del contenedor:

```
docker ps
```

### Ingresa al contenedor:

```
docker exec -it <container_name> bash
```

### Dentro del contenedor, ejecuta SQL*Plus:

```
sqlplus system/1234@//localhost/XEPDB1
```

- **Usuario:** system  
- **Contraseña:** 1234 (o la que definiste en `-e ORACLE_PWD`)  
- **Servicio:** XEPDB1 (usado por Oracle Express Edition)

Deberías quedar en el prompt de SQL*Plus:

```
SQL>
```

---

## ⚙️ Comandos SQL para crear usuario y otorgar permisos

```
ALTER SESSION SET CONTAINER=XEPDB1;

CREATE USER desarrollador IDENTIFIED BY 1234;

GRANT CONNECT, RESOURCE TO desarrollador;

ALTER USER desarrollador DEFAULT TABLESPACE USERS;

ALTER USER desarrollador QUOTA UNLIMITED ON USERS;
```

---

## 📥 Conexión desde SQL Developer

### Parámetros para conexión:

- **Usuario:** desarrollador  
- **Contraseña:** 1234  
- **Host:** localhost  
- **Puerto:** 1521  
- **Service Name:** XEPDB1

---

## ✅ Crear conexión en SQL Developer

1. Abre **SQL Developer**
2. Haz clic en el botón `+` (Nueva conexión) o en `Archivo > Nueva conexión`
3. Completa los campos:

```
Nombre conexión: OracleDesarrollador (o el que quieras)
Usuario: desarrollador
Contraseña: 1234
Guardar contraseña: (opcional)
Tipo de conexión: Basic
Host: localhost
Puerto: 1521
Service name: XEPDB1
```



