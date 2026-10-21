# Laboratorio 07: Manejo de excepciones en Java

**Requisitos:** JDK 21 y Apache Maven 3.9+.

## Ejecución

Desde la carpeta del proyecto:

```bash
mvn clean compile
mvn exec:java -Dexec.args="1"
mvn exec:java -Dexec.args="todos"
```

Cambia el número de 1 a 12. No requiere conexión a internet durante la ejecución una vez resueltos los plugins de Maven.

## Casos

1. **try/catch**: división por cero; captura y continuación.
2. **catch múltiples**: distinguir NumberFormatException y ArithmeticException.
3. **finally**: ejecutar código de finalización aunque haya excepción. Nota: no es garantía absoluta ante terminación abrupta de la JVM.
4. **throw**: lanzar IllegalArgumentException ante un monto inválido.
5. **throws / checked**: Files.readString declara IOException; el método llamador la captura.
6. **unchecked**: NumberFormatException no requiere declaración throws.
7. **propagación**: repositorio -> servicio -> controlador -> main.
8. **try-with-resources**: Scanner se cierra automáticamente; se recomienda para AutoCloseable.
9. **multicatch**: compartir una misma respuesta para excepciones distintas.
10. **personalizada checked**: SaldoInsuficienteException extends Exception.
11. **causa encadenada**: new IllegalStateException(mensaje, causa), getCause().
12. **integrador bancario**: procesar cinco retiros, capturar errores de negocio y continuar.

## Checked vs unchecked

- **Checked:** heredan de Exception pero no de RuntimeException; Java exige capturarlas o declararlas (ejemplo: IOException, SaldoInsuficienteException).
- **Unchecked:** RuntimeException y subclases; no exigen declaración (ejemplo: NumberFormatException, IllegalArgumentException).
- **Error:** problemas graves de la JVM; normalmente no se deben capturar para continuar (ejemplo: OutOfMemoryError).
- `throws` **declara o propaga**, pero por sí solo **no maneja** una excepción.

## Resultado esperado del caso 12

```text
APROBADO CLI-001 monto=100 saldo=400.00
RECHAZADO CLI-001 monto=900 causa=Saldo 400.00, retiro solicitado 900
RECHAZADO CLI-999 monto=20 causa=Cliente no registrado: CLI-999
RECHAZADO CLI-001 monto=-5 causa=El monto debe ser positivo
APROBADO CLI-001 monto=50 saldo=350.00
Saldo final CLI-001: 350.00
```

## Retos para estudiantes

1. **Básico:** agregar un sexto retiro con monto cero y comprobar que el lote sigue procesando; registrar cuántas operaciones se aprobaron.
2. **Intermedio:** crear `MontoInvalidoException extends Exception` y reemplazar la validación que hoy usa IllegalArgumentException. Actualizar `throws` y `catch`.
3. **Avanzado:** agregar transferencia entre dos clientes, validando origen, destino, saldo y monto. Evitar descuentos parciales: verificar todo antes de modificar los saldos.

## Preguntas de discusión

- ¿Qué cambia entre `throw` y `throws`?
- ¿Por qué `catch (Exception e)` puede ocultar errores de programación?
- ¿Por qué el orden de `catch` debe ir de específico a general?
- ¿Cuándo usar validación normal y cuándo lanzar una excepción?
- ¿Por qué no es recomendable capturar `OutOfMemoryError` para continuar trabajando?

## Estructura

```text
pom.xml
src/main/java/com/laboratorio/excepciones/
  Main.java
  Banco.java
  SaldoInsuficienteException.java
  ClienteNoEncontradoException.java
```

El laboratorio usa exclusivamente la biblioteca estándar de Java. El caso de archivos está diseñado para fallar de forma controlada; no modifica archivos del equipo.
