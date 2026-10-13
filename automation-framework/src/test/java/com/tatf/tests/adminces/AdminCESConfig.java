package com.tatf.tests.adminces;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AdminCESConfig {
    private final Properties valores = new Properties();

    public AdminCESConfig() throws IOException {
        Path archivo = Path.of("adminces.properties");
        if (!Files.exists(archivo)) {
            throw new IllegalStateException("Copiar adminces.properties.example a adminces.properties y completar los datos.");
        }
        try (Reader entrada = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            valores.load(entrada);
        }
    }

    public String leer(String nombre) {
        String valor = valores.getProperty(nombre);
        if (valor == null || valor.isBlank() || valor.equals("COMPLETAR")) {
            throw new IllegalStateException("Falta configurar " + nombre + " en adminces.properties.");
        }
        return valor;
    }
}
