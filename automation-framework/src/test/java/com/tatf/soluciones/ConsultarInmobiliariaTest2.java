package com.tatf.soluciones;

import com.tatf.core.browser.BrowserFactory;
import com.tatf.core.browser.IBrowser;
import com.tatf.core.element.Element;
import com.tatf.core.verification.IVerify;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConsultarInmobiliariaTest2 {

    private static IBrowser browser;

    @BeforeAll
    static void beforeAll() {
        browser = BrowserFactory.getBrowser(true);
    }

    @AfterAll
    static void afterAll() {
        BrowserFactory.quitBrowser();
    }

    @Test
    void consultarPropiedad() {
        browser.interaction().navigateTo("http://cestore.ces.com.uy/InmoCES/");
        browser.find().css("input[type='password']").write("c#3)d4e9d!bbf4d4ae26df!963=335&]");
        browser.find().css("button[type='submit']").click();

        browser.find().css("button[onclick=\"contactarPropiedad('Cabaña frente al mar')\"]").click();

        browser.find().id("f-email").write("consulta@test.com");
        browser.find().id("f-mensaje").write("Me interesa la propiedas, pueden contactarme para resolverme algunas dudas? Muchas gracias.");
        browser.find().xpath("//button[contains(text(),'Enviar mensaje')]").click();

        browser.wait("success-msg").id();
        Element alerta= browser.find().id("success-msg");
        String mensajeObtenido =alerta.tag("h3").getText();
        IVerify.create().verify("¡Mensaje enviado!", mensajeObtenido, "No se mostró el mensaje de confirmación de envío.");
    }
}
