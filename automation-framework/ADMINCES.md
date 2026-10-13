# Scripts de prueba de AdminCES

## Escenarios

- GP-01: registrar un Administrador y comprobar sus datos y su acceso desde otra sesión.
- GP-02: crear un Administrador de prueba, cambiar su contraseña, comprobar acceso con la nueva y rechazo con la anterior.
- GP-03: crear un Tester Junior y comprobar nombre, apellido, email, país y perfil; volver a comprobarlos después de cargar nuevamente la aplicación.
- GP-04: crear un Tester para esta prueba, eliminarlo y comprobar su ausencia antes y después de cargar nuevamente la aplicación.

Cada caso crea su propia cuenta, sin depender del orden de ejecución. Los Administradores de GP-01/02 y el Tester de GP-03 quedan registrados. El email generado se muestra en la consola. GP-04 elimina exclusivamente su propio Tester.

## Archivos

| Archivo | Función |
|---|---|
| `src/test/java/com/tatf/tests/adminces/AdminCESTest.java` | Contiene los cuatro tests con pasos numerados. |
| `src/test/java/com/tatf/tests/adminces/AdminCESPage.java` | Reúne las acciones y comprobaciones comunes de AdminCES. |
| `src/test/java/com/tatf/tests/adminces/CuentaPrueba.java` | Prepara los datos y un email diferente para cada cuenta. |
| `src/test/java/com/tatf/tests/adminces/AdminCESConfig.java` | Lee la configuración local. |
| `adminces.properties.example` | Ejemplo de configuración sin credenciales reales. |

## Integración con el framework

Los tests declaran `IBrowser` e `IVerify`. El navegador se obtiene con `BrowserFactory.getBrowser(false)` y las verificaciones con `IVerify.create()`.

`IBrowser` permite buscar elementos, completar campos, pulsar botones y esperar condiciones. `IVerify` compara los datos esperados con los obtenidos. No se crea un WebDriver propio para los scripts.

Se añadieron dos operaciones a la clase `Wait`: `cssClickable()` y `until(BooleanSupplier)`. Permiten esperar elementos que se pueden pulsar y condiciones de la aplicación sin pausas fijas. Las interfaces originales se conservan.

La plataforma usa Factory para crear el navegador y Singleton para mantener su instancia. Por eso las pruebas se ejecutan en un solo hilo. `@BeforeEach` prepara cada prueba y `@AfterEach` cierra su navegador. GP-01 y GP-02 abren sesiones nuevas para comprobar credenciales sin reutilizar un acceso previo.

## Preparación en IntelliJ

1. Abrir la carpeta `automation-framework` como proyecto Maven.
2. Seleccionar JDK 17 y recargar Maven.
3. Tener Chrome instalado.
4. Copiar `adminces.properties.example` a `adminces.properties` dentro de esa carpeta.
5. Completar los valores de configuración.

En PowerShell, desde `automation-framework`:

```powershell
if (!(Test-Path adminces.properties)) { Copy-Item adminces.properties.example adminces.properties }
notepad adminces.properties
```

El archivo local queda excluido por .gitignore. No subirlo a GitHub.

| Propiedad | Contenido |
|---|---|
| `url` | Dirección inicial de AdminCES. |
| `claveTaller` | Contraseña del primer formulario de acceso al taller. |
| `admin.email` | Email de un Administrador existente para GP-03 y GP-04. |
| `admin.clave` | Contraseña de ese Administrador. |
| `prueba.clave` | Contraseña válida para las cuentas nuevas. |
| `prueba.nuevaClave` | Contraseña nueva y distinta para GP-02. |
| `login.error.css` | Selector CSS del mensaje real de contraseña incorrecta. |

`login.error.css` debe corresponder al mensaje de rechazo del login; no al formulario completo. Si falta, GP-02 se detiene antes de crear la cuenta.

## Ejecución posterior

Los comandos siguientes quedan como instrucciones para cuando se decida ejecutar los tests. La preparación de este entregable no ejecutó las pruebas.

```powershell
mvn -DskipTests test-compile
```

Ese comando solo compila. Para ejecutar un caso posteriormente:

```powershell
mvn "-Dtest=AdminCESTest#crearAdministrador" test
mvn "-Dtest=AdminCESTest#reiniciarContrasena" test
mvn "-Dtest=AdminCESTest#crearTester" test
mvn "-Dtest=AdminCESTest#eliminarTester" test
```

Para ejecutar los cuatro posteriormente:

```powershell
mvn test
```

El POM selecciona únicamente AdminCESTest. Los ejemplos originales del framework se conservan, pero no se ejecutan junto con estos casos.

## Comportamiento pendiente de validación funcional

Los campos utilizan los id y name de los formularios de AdminCES. La navegación pública se localiza por el texto de los enlaces. Las respuestas de los formularios se esperan mediante un aviso SweetAlert2 o la desaparición del formulario. Si el sitio responde de otra manera, debe ajustarse esa espera.

Los avisos SweetAlert2 se cierran antes de abrir el menú; un aviso de error hace fallar el caso. La baja admite una confirmación JavaScript o SweetAlert2. Otro tipo de modal requiere ajustar ese paso.

El servidor mostró `Cannot GET /adminces/view-users` al recargar directamente esa ruta. La comprobación de persistencia carga la página inicial y vuelve a abrir la tabla desde el menú; inicia sesión otra vez si es necesario.

No se han ejecutado pruebas funcionales de esta implementación. La compilación no certifica el comportamiento del sitio.
