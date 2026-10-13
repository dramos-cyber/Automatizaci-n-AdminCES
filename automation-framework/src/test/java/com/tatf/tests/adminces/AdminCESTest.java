package com.tatf.tests.adminces;

import com.tatf.core.browser.BrowserFactory;
import com.tatf.core.browser.IBrowser;
import com.tatf.core.verification.IVerify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminCESTest {
    private IBrowser browser;
    private IVerify verify;
    private AdminCESConfig config;
    private AdminCESPage adminces;

    @BeforeEach
    void preparar() throws Exception {
        config = new AdminCESConfig();
        config.leer("url");
        config.leer("claveTaller");
        config.leer("prueba.clave");
        verify = IVerify.create();
        abrirNavegador();
    }

    @AfterEach
    void cerrar() {
        if (browser != null) {
            BrowserFactory.quitBrowser();
            browser = null;
        }
    }

    @Test
    @DisplayName("GP-01 Crear cuenta Administrador")
    void crearAdministrador() {
        CuentaPrueba cuenta = new CuentaPrueba("admin", config.leer("prueba.clave"));
        System.out.println("Cuenta de prueba: " + cuenta.email);

        // 1. Registrar la cuenta.
        adminces.registrarAdministrador(cuenta);

        // 2. Ingresar desde una sesión nueva.
        nuevaSesion();
        adminces.iniciarSesion(cuenta.email, cuenta.clave);
        adminces.comprobarSesion(cuenta.email);

        // 3. Comprobar todos sus datos y el perfil en la tabla.
        adminces.comprobarUsuario(cuenta, "Administrador");
    }

    @Test
    @DisplayName("GP-02 Reiniciar contraseña")
    void reiniciarContrasena() {
        String nuevaClave = config.leer("prueba.nuevaClave");
        String selectorError = config.leer("login.error.css");
        verify.notVerify(config.leer("prueba.clave"), nuevaClave, "La nueva clave debe ser diferente.");
        CuentaPrueba cuenta = new CuentaPrueba("reset", config.leer("prueba.clave"));
        System.out.println("Cuenta de prueba: " + cuenta.email);

        // 1. Preparar una cuenta Administrador para cambiar su contraseña.
        adminces.registrarAdministrador(cuenta);
        nuevaSesion();
        adminces.iniciarSesion(cuenta.email, cuenta.clave);
        adminces.comprobarSesion(cuenta.email);

        // 2. Cambiar la contraseña desde el acceso público.
        nuevaSesion();
        adminces.reiniciarClave(cuenta.email, nuevaClave);

        // 3. Comprobar que la nueva contraseña permite acceder.
        nuevaSesion();
        adminces.iniciarSesion(cuenta.email, nuevaClave);
        adminces.comprobarSesion(cuenta.email);

        // 4. Comprobar que la contraseña anterior se rechaza.
        nuevaSesion();
        adminces.iniciarSesion(cuenta.email, cuenta.clave);
        adminces.comprobarAccesoRechazado(selectorError);
    }

    @Test
    @DisplayName("GP-03 Crear cuenta Tester")
    void crearTester() {
        config.leer("admin.email");
        config.leer("admin.clave");
        CuentaPrueba cuenta = new CuentaPrueba("tester", config.leer("prueba.clave"));
        System.out.println("Cuenta de prueba: " + cuenta.email);

        // 1. Ingresar como Administrador.
        adminces.iniciarSesionAdministrador();

        // 2. Crear el Tester y comprobar sus datos.
        adminces.crearTester(cuenta);
        adminces.comprobarUsuario(cuenta, "Tester Junior");

        // 3. Volver a cargar la aplicación y comprobar que la cuenta continúa.
        adminces.volverACargarUsuarios();
        adminces.comprobarUsuario(cuenta, "Tester Junior");
    }

    @Test
    @DisplayName("GP-04 Eliminar cuenta Tester")
    void eliminarTester() {
        config.leer("admin.email");
        config.leer("admin.clave");
        CuentaPrueba cuenta = new CuentaPrueba("baja", config.leer("prueba.clave"));
        System.out.println("Cuenta de prueba: " + cuenta.email);

        // 1. Crear un Tester exclusivo para esta prueba.
        adminces.iniciarSesionAdministrador();
        adminces.crearTester(cuenta);
        adminces.comprobarUsuario(cuenta, "Tester Junior");

        // 2. Eliminar únicamente esa cuenta.
        adminces.eliminarTester(cuenta);
        adminces.comprobarAusencia(cuenta.email);

        // 3. Comprobar que continúa ausente al cargar de nuevo la aplicación.
        adminces.volverACargarUsuarios();
        adminces.comprobarAusencia(cuenta.email);
    }

    private void abrirNavegador() {
        browser = BrowserFactory.getBrowser(false);
        adminces = new AdminCESPage(browser, verify, config);
    }

    private void nuevaSesion() {
        cerrar();
        abrirNavegador();
    }
}
