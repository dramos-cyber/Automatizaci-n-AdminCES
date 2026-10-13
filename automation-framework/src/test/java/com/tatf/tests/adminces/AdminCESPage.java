package com.tatf.tests.adminces;

import com.tatf.core.browser.IBrowser;
import com.tatf.core.element.Element;
import com.tatf.core.verification.IVerify;
import org.openqa.selenium.NoAlertPresentException;

public class AdminCESPage {
    private final IBrowser browser;
    private final IVerify verify;
    private final AdminCESConfig config;
    private String emailTesterCreado;

    private static final String MENU_USUARIOS = "a[href='/adminces/view-users']";
    private static final String MENU_CREAR = "a[href='/adminces/create-user']";

    public AdminCESPage(IBrowser browser, IVerify verify, AdminCESConfig config) {
        this.browser = browser;
        this.verify = verify;
        this.config = config;
    }

    public void ingresarAlTaller() {
        browser.interaction().navigateTo(config.leer("url"));
        browser.wait("body").until(() -> existe("#loginForm")
                || existe("a[href='/adminces/register']") || existe(MENU_USUARIOS));
        if (existe("#loginForm")) {
            escribir("#loginForm #pass", config.leer("claveTaller"));
            pulsar("#loginForm button[type='submit']");
            browser.wait("body").until(() -> !existe("#loginForm")
                    && (existe("a[href='/adminces/register']") || existe(MENU_USUARIOS)));
        }
    }

    public void iniciarSesion(String email, String clave) {
        ingresarAlTaller();
        browser.wait("Iniciar sesión").link();
        browser.find().link("Iniciar sesión").click();
        escribir("#formLogin [name='inputEmail']", email);
        escribir("#formLogin [name='inputPassword']", clave);
        pulsar("#formLogin button[type='button']");
    }

    public void comprobarSesion(String email) {
        browser.wait(MENU_USUARIOS).css();
        cerrarAviso();
        abrirUsuarios();
        esperarFila(email);
        verify.verify("Administrador", fila(email).css("td:nth-child(5)").getText().trim(),
                "La cuenta debe tener perfil Administrador.");
    }

    public void iniciarSesionAdministrador() {
        iniciarSesion(config.leer("admin.email"), config.leer("admin.clave"));
        comprobarSesion(config.leer("admin.email"));
    }

    public void registrarAdministrador(CuentaPrueba cuenta) {
        ingresarAlTaller();
        browser.wait("a[href='/adminces/register']").css();
        browser.find().css("a[href='/adminces/register']").click();
        escribir("#formAccount [name='inputFirstName']", cuenta.nombre);
        escribir("#formAccount [name='inputLastName']", cuenta.apellido);
        escribir("#formAccount [name='inputEmail']", cuenta.email);
        escribir("#formAccount [name='inputCountry']", cuenta.pais);
        escribir("#formAccount [name='inputPassword']", cuenta.clave);
        escribir("#formAccount [name='inputRepeatPassword']", cuenta.clave);
        pulsar("#formAccount #btnRegister");
        esperarResultado("#formAccount");
    }

    public void reiniciarClave(String email, String nuevaClave) {
        ingresarAlTaller();
        browser.wait("Reiniciar contraseña").link();
        browser.find().link("Reiniciar contraseña").click();
        escribir("#formResetPassword [name='inputEmail']", email);
        escribir("#formResetPassword [name='inputPassword']", nuevaClave);
        escribir("#formResetPassword [name='inputRepeatPassword']", nuevaClave);
        pulsar("#formResetPassword #btnReset");
        esperarResultado("#formResetPassword");
    }

    public void comprobarAccesoRechazado(String selectorError) {
        browser.wait(selectorError).css();
        verify.verifyTrue(browser.find().css(selectorError).isDisplayed(),
                "Debe mostrarse el mensaje de contraseña incorrecta.");
        verify.verifyFalse(existe(MENU_USUARIOS), "No debe existir acceso al menú de usuarios.");
        verify.verifyTrue(browser.find().id("formLogin").isDisplayed(),
                "El formulario de login debe continuar visible.");
    }

    public void crearTester(CuentaPrueba cuenta) {
        cerrarAviso();
        pulsar(MENU_CREAR);
        escribir("#formCreateUser [name='inputFirstName']", cuenta.nombre);
        escribir("#formCreateUser [name='inputLastName']", cuenta.apellido);
        escribir("#formCreateUser [name='inputEmail']", cuenta.email);
        browser.wait("#formCreateUser [name='inputCountry']").css();
        browser.find().css("#formCreateUser [name='inputCountry']").selectValue(cuenta.pais);
        escribir("#formCreateUser [name='inputPassword']", cuenta.clave);
        pulsar("#formCreateUser label[for='testerJunior']");
        verify.verifyTrue(browser.find().id("testerJunior").isSelected(), "Seleccionar Tester Junior.");
        pulsar("#formCreateUser #btnRegister");
        esperarResultado("#formCreateUser");
        emailTesterCreado = cuenta.email;
        abrirUsuarios();
    }

    public void abrirUsuarios() {
        cerrarAviso();
        pulsar(MENU_USUARIOS);
        browser.wait("#dataTable").css();
    }

    public void volverACargarUsuarios() {
        // La ruta interna no admite una recarga directa en este servidor.
        ingresarAlTaller();
        if (existe(MENU_USUARIOS)) {
            abrirUsuarios();
        } else {
            iniciarSesionAdministrador();
        }
        // Esperar una cuenta conocida evita confundir carga con una tabla vacía.
        esperarFila(config.leer("admin.email"));
    }

    public void comprobarUsuario(CuentaPrueba cuenta, String perfil) {
        esperarFila(cuenta.email);
        Element usuario = fila(cuenta.email);
        verify.verify(cuenta.nombre, usuario.css("td:nth-child(1)").getText().trim(), "Nombre correcto.");
        verify.verify(cuenta.apellido, usuario.css("td:nth-child(2)").getText().trim(), "Apellido correcto.");
        verify.verify(cuenta.email, usuario.css("td:nth-child(3)").getText().trim(), "Email correcto.");
        verify.verify(cuenta.pais, usuario.css("td:nth-child(4)").getText().trim(), "País correcto.");
        verify.verify(perfil, usuario.css("td:nth-child(5)").getText().trim(), "Perfil correcto.");
    }

    public void eliminarTester(CuentaPrueba cuenta) {
        // Solo eliminar la cuenta creada por este objeto durante la prueba.
        verify.verify(emailTesterCreado, cuenta.email, "Eliminar exclusivamente el Tester de esta prueba.");
        comprobarUsuario(cuenta, "Tester Junior");
        Element boton = fila(cuenta.email).css("td:nth-child(6) button");
        verify.verify(cuenta.email, boton.getAttribute("id"), "El botón debe pertenecer al Tester elegido.");
        boton.click();
        browser.wait("body").until(() -> {
            // Algunas aplicaciones muestran una confirmación JavaScript.
            try {
                browser.interaction().switchToAlert().acceptAlert();
            } catch (NoAlertPresentException error) {
                // No hay alerta nativa; revisar la posible confirmación SweetAlert2.
            }
            cerrarAviso();
            return browser.find().xpathList(xpathFila(cuenta.email)).isEmpty();
        });
    }

    public void comprobarAusencia(String email) {
        verify.verifyTrue(browser.find().xpathList(xpathFila(email)).isEmpty(),
                "La cuenta eliminada no debe aparecer en la tabla.");
    }

    private void esperarFila(String email) {
        browser.wait(xpathFila(email)).xpath();
        verify.verify(1, browser.find().xpathList(xpathFila(email)).size(), "Debe existir una única cuenta con ese email.");
    }

    private Element fila(String email) {
        return browser.find().xpath(xpathFila(email));
    }

    private String xpathFila(String email) {
        // Los emails se generan en CuentaPrueba y no contienen comillas.
        if (email.contains("'")) {
            throw new IllegalArgumentException("El email no puede contener comillas simples.");
        }
        return "//*[@id='bodyTable']/tr[td[3][normalize-space(.)='" + email + "']]";
    }

    private void escribir(String selector, String texto) {
        browser.wait(selector).css();
        browser.find().css(selector).clear().write(texto);
    }

    private void pulsar(String selector) {
        browser.wait(selector).cssClickable();
        browser.find().css(selector).click();
    }

    private boolean existe(String selector) {
        return !browser.find().cssList(selector).isEmpty();
    }

    private void esperarResultado(String formulario) {
        // Esperar una respuesta antes de cerrar el navegador o cambiar de sesión.
        browser.wait("body").until(() -> existe(".swal2-popup") || !existe(formulario));
        cerrarAviso();
    }

    private void cerrarAviso() {
        browser.wait("body").until(() -> {
            if (existe(".swal2-popup.swal2-icon-error")) {
                verify.verifyFail("AdminCES informó: " + browser.find().css(".swal2-popup").getText());
            }
            for (Element boton : browser.find().cssList(".swal2-container .swal2-confirm")) {
                if (boton.isDisplayed() && boton.isEnabled()) {
                    boton.click();
                    break;
                }
            }
            return !existe(".swal2-container.swal2-backdrop-show") && !existe(".swal2-popup.swal2-show");
        });
    }
}
