# Gestión de Productos - Prueba Técnica Java Jr

Aplicación de escritorio Java Swing + JDBC + MySQL para CRUD de productos.

## Stack
- Java 21, Swing, JDBC
- MySQL 8.0 (Docker)
- Librerías: `mysql-connector-j-8.0.33`, `dotenv-java-3.0.0`

## Estructura
```
src/
  Main.java
  config/Conexion.java
  modelo/Producto.java
  dao/ProductoDAO.java
  controlador/ProductoControlador.java
  vista/VentanaPrincipal.java
query.sql
docker-compose.yml
.env.example -> .env (copiar)
lib/ (mysql-connector, dotenv, protobuf)
```

## Configuración
1. Crear `.env` desde plantilla (no se versiona `.env`):
```bash
cp .env.example .env
# editar si necesitas: DB_PASSWORD, DB_NAME, DB_PORT, DB_HOST
```
2. Levantar BD:
```bash
docker compose up -d
# verificar
docker ps | grep bd_productos
```
3. Crear estructura:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -proot < query.sql
# o ejecutar query.sql desde IntelliJ/Workbench
```

## Ejecución
- IntelliJ: Run `Main.java` (Nimbus L&F, abre `VentanaPrincipal`).
- CLI:
```bash
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" Main
```

## Funcionalidades (PDF - 8 casos)
1. **Registrar** - valida código/nombre obligatorios, precio>0, stock>=0, código único.
2. **Listar** - carga al iniciar en `JTable` dentro de `JScrollPane`.
3. **Buscar** - por código o nombre (`LIKE %texto%`), Enter o botón Buscar.
4. **Seleccionar** - click en tabla puebla formulario.
5. **Modificar** - actualiza producto seleccionado.
6. **Eliminar** - con `JOptionPane` confirmación.
7. **Bajo Stock** - filtro `stock < 10` (umbbral `ProductoDAO.UMBRAL_BAJO_STOCK`), toggle Ver Todos.
8. **Ajustar Stock** - botones `+`/`-` junto a Stock piden cantidad y hacen `stock = stock +/- delta` validando no negativo.

Reglas: mensajes con `JOptionPane`, refresco inmediato de tabla, persistencia JDBC.

## Validaciones
- Código/Nombre requeridos, máx 50/100 chars
- Precio `int` en Gs sin decimales >0 (acepta puntos de miles ej: 1.500.000)
- Stock `int` >=0
- Estado Activo/Inactivo
- Duplicados por `codigo UNIQUE`

## Git
Historial en `main` con commits incrementales por capa.
