package com.tatf.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class ConsultarInmobiliariaTest {

    private static WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
    }

    @Test
    void consultarPropiedad() {
        driver.get("http://cestore.ces.com.uy/InmoCES/");

        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("c#3)d4e9d!bbf4d4ae26df!963=335&]");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.cssSelector("button[onclick=\"contactarPropiedad('Cabaña frente al mar')\"]")).click();

        driver.findElement(By.id("f-email")).sendKeys("consulta@test.com");
        driver.findElement(By.id("f-mensaje")).sendKeys("Me interesa la propiedas, pueden contactarme para resolverme algunas dudas? Muchas gracias.");
        driver.findElement(By.xpath("//button[contains(text(),'Enviar mensaje')]")).click();
        String mensaje=driver.findElement(By.id("success-msg")).findElement(By.tagName("h3")).getText();

        Assertions.assertEquals("¡Mensaje enviado!", mensaje, "El mensaje no es el esperado");
    }
}
