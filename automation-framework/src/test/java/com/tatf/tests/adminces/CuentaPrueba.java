package com.tatf.tests.adminces;

import java.util.UUID;

// Datos de una cuenta nueva. El email cambia en cada ejecución.
public class CuentaPrueba {
    public final String nombre = "Ana";
    public final String apellido = "Prueba";
    public final String pais = "Uruguay";
    public final String email;
    public final String clave;

    public CuentaPrueba(String tipo, String clave) {
        String codigo = UUID.randomUUID().toString().replace("-", "");
        this.email = "qa" + tipo + codigo + "@example.com";
        this.clave = clave;
    }
}
