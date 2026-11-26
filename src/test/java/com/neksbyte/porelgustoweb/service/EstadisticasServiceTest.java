package com.neksbyte.porelgustoweb.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasServiceTest {

    @Test
    void origenDeLaVisita() {
        assertEquals("Directo", EstadisticasService.origen(null, "porelgusto.mx", null));
        assertEquals("Google", EstadisticasService.origen("https://www.google.com.mx/", "porelgusto.mx", null));
        assertEquals("Facebook", EstadisticasService.origen("https://l.facebook.com/l.php?u=x", "porelgusto.mx", null));
        assertEquals("WhatsApp", EstadisticasService.origen("https://wa.me/", "porelgusto.mx", null));
        assertEquals("Interno", EstadisticasService.origen("https://porelgusto.mx/catalogo", "porelgusto.mx", null));
        assertEquals("Instagram", EstadisticasService.origen("https://www.google.com/", "porelgusto.mx", "instagram"));
    }

    @Test
    void dispositivoYRobots() {
        String iphone = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 Version/17.0 Mobile/15E148 Safari/604.1";
        assertEquals("Celular", EstadisticasService.dispositivo(iphone));
        assertEquals("Safari", EstadisticasService.navegador(iphone));
        assertEquals("Computadora", EstadisticasService.dispositivo("Mozilla/5.0 (X11; Linux x86_64) Chrome/130.0 Safari/537.36"));
        assertTrue(EstadisticasService.esRobot("Googlebot/2.1"));
        assertTrue(EstadisticasService.esRobot(null));
        assertFalse(EstadisticasService.esRobot(iphone));
    }
}
