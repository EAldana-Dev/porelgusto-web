package com.neksbyte.porelgustoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CatalogoController {

    @GetMapping("/Arreglos")
    public String inicio(Model model) {

    System.out.println("ENTRANDO AL CONTROLADOR");

    model.addAttribute("titulo", "Catalogo");

    List<String> products = List.of(
    "Laptop",
    "Mouse",
    "Keyboard",
    "Monitor",
    "Impresora",
    "Escáner",
    "Tablet",
    "Smartphone",
    "Auriculares",
    "Altavoces",
    "Micrófono",
    "Webcam",
    "Router",
    "Switch de red",
    "Hub USB",
    "Disco duro externo",
    "Memoria USB",
    "Tarjeta SD",
    "Tarjeta gráfica",
    "Procesador",
    "Memoria RAM",
    "Placa base",
    "Fuente de alimentación",
    "Gabinete",
    "Refrigeración líquida",
    "Ventilador CPU",
    "Pasta térmica",
    "Cable HDMI",
    "Cable USB-C",
    "Cable Ethernet",
    "Adaptador de corriente",
    "Cargador inalámbrico",
    "Power bank",
    "Soporte para laptop",
    "Base refrigerante",
    "Alfombrilla para mouse",
    "Reposamuñecas",
    "Silla ergonómica",
    "Escritorio ajustable",
    "Lámpara LED de escritorio",
    "Organizador de cables",
    "Regleta eléctrica",
    "UPS",
    "Proyector",
    "Pantalla de proyección",
    "Televisor",
    "Soundbar",
    "Consola de videojuegos",
    "Control de videojuegos",
    "Volante de carreras",
    "Joystick",
    "Realidad virtual",
    "Dron",
    "Cámara digital",
    "Cámara de acción",
    "Trípode",
    "Estabilizador",
    "Tarjeta de captura",
    "Mesa de mezclas",
    "Interfaz de audio",
    "Sintetizador MIDI",
    "Teclado musical",
    "Guitarra eléctrica",
    "Amplificador",
    "Pedal de efectos",
    "Smartwatch",
    "Pulsera de actividad",
    "Báscula inteligente",
    "Termómetro digital",
    "Medidor de presión",
    "Oxímetro",
    "Bombilla inteligente",
    "Enchufe inteligente",
    "Cámara de seguridad",
    "Sensor de movimiento",
    "Timbre inteligente",
    "Cerrojo inteligente",
    "Robot aspirador",
    "Aspiradora",
    "Cafetera",
    "Microondas",
    "Freidora de aire",
    "Licuadora",
    "Batidora",
    "Tostadora",
    "Plancha",
    "Ventilador",
    "Purificador de aire",
    "Humidificador",
    "Calefactor",
    "Aire acondicionado",
    "Impresora 3D",
    "Filamento PLA",
    "Plotter de corte",
    "Laminadora",
    "Destructora de papel",
    "Calculadora científica",
    "Reloj de escritorio",
    "Pizarra digital"
);

    System.out.println("PRODUCTOS: " + products);

    model.addAttribute("products", products);

    return "catalogo";
}


}
