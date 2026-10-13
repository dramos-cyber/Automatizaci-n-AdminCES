package com.tatf.soluciones;

import com.tatf.core.browser.BrowserFactory;
import com.tatf.core.browser.IBrowser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestForm2 {

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
    void completeForm() {
        browser.interaction().navigateTo("http://cestore.ces.com.uy/autotestlab/");
        browser.find().css("input[type='password']").write("3&44fcf@42e157ff0f)21f2#ecb12ad9");
        browser.find().css("button[type='submit']").click();
        browser.find().link("Formulario").click();
        browser.find().id("nombre").write("Juan Pérez");
        browser.find().id("comentarios").write("Comentario de prueba generado por automatización.");
        browser.find().id("aceptoTerminos").click();
        browser.find().id("femenino").click();
        browser.find().id("pais").selectValue("uruguay");
        browser.find().id("fecha").write("15/03/1995");
        browser.find().link("Enviar").click();
    }
}
