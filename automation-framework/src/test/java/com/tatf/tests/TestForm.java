package com.tatf.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class TestForm {

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
    void completeForm() {
        driver.get("http://cestore.ces.com.uy/autotestlab/");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("3&44fcf@42e157ff0f)21f2#ecb12ad9");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.linkText("Formulario")).click();
        driver.findElement(By.id("nombre")).sendKeys("Juan Pérez");
        driver.findElement(By.id("comentarios")).sendKeys("Comentario de prueba generado por automatización.");
        driver.findElement(By.id("aceptoTerminos")).click();
        driver.findElement(By.id("femenino")).click();

        Select paisSelect = new Select(driver.findElement(By.id("pais")));
        paisSelect.selectByValue("uruguay");

        driver.findElement(By.id("fecha")).sendKeys("15/03/1995");
        driver.findElement(By.linkText("Enviar")).click();

    }
}
