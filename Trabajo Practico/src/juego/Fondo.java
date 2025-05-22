package juego;

import java.awt.Image;
import entorno.Entorno;
import java.awt.Color; // Necesario para dibujar colores

public class Fondo {
    Image Fondo;
    Entorno e;
    private Personaje mago; // Nueva referencia al personaje
    Image imagenMenu; // Nueva variable para la imagen del menú

    public Fondo(String ruta, Entorno ent, Personaje mago) { // Constructor modificado
        this.Fondo = entorno.Herramientas.cargarImagen(ruta);
        this.e = ent;
        this.mago = mago; // Asignar la referencia al personaje
        this.imagenMenu = entorno.Herramientas.cargarImagen("imagenes/FondoMenu.png"); // Cargar la imagen del menú
    }

    public void dibujar() {
        // Dibuja la imagen de fondo
        e.dibujarImagen(Fondo, e.ancho() / 2, e.alto() / 2, 0, (double) e.ancho() / Fondo.getWidth(null));
    }

    /**
     * Dibuja la interfaz de usuario (vida y poderes del mago) en el entorno.
     */
    public void dibujarMenu() {
        // Definir las dimensiones y posición del rectángulo del menu
        int menuRectAncho = 200;
        int menuRectAlto = 560; // Aumentado para que llegue más abajo
       
        // Posicionado en el lado izquierdo, con 10 píxeles de margen
        int menuRectX = menuRectAncho / 2 + 10;
        // Centrado verticalmente para que el borde superior e inferior tengan 20 píxeles de margen
        int menuRectY = menuRectAlto / 2 + 20; 

        // Dibujar la imagen del menú 
        e.dibujarImagen(imagenMenu, menuRectX, menuRectY, 0, 1.0);

        // --- Mostrar la vida del mago ---
        e.cambiarFont("Arial", 16, Color.WHITE); // Fuente y color para el texto
        e.escribirTexto("Vida: " + mago.getVida(), menuRectX - menuRectAncho / 2 + 10, menuRectY - menuRectAlto / 2 + 25);

        // --- Mostrar los poderes ---
        e.escribirTexto("Poderes:", menuRectX - menuRectAncho / 2 + 10, menuRectY - menuRectAlto / 2 + 50);
        int espacio = 0;
        for (String poder : mago.getPoderes()) {
            espacio += 20; // Espacio entre cada poder
            e.escribirTexto("- " + poder, menuRectX - menuRectAncho / 2 + 20, menuRectY - menuRectAlto / 2 + 50 + espacio);
        }
    }
}
