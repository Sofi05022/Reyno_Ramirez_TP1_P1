package juego;

import java.awt.Image;
import entorno.Entorno;
import java.awt.Color; 

public class Fondo {
    Image Fondo;
    Entorno e;
    private Personaje mago; 
    Image imagenMenu; 
    private int demoniosRestantes;

    public Fondo(String ruta, Entorno ent, Personaje mago) { 
        this.Fondo = entorno.Herramientas.cargarImagen("imagenes/Fondo.png");
        this.e = ent;
        this.mago = mago; 
        this.imagenMenu = entorno.Herramientas.cargarImagen("imagenes/FondoMenu.png"); 
    }

    public void dibujar() {
        e.dibujarImagen(Fondo, e.ancho() / 2, e.alto() / 2, 0, (double) e.ancho() / Fondo.getWidth(null));
    }

    public void dibujarMenu() {
        // Definir las dimensiones y posición del rectángulo del menu
        int menuRectAncho = 200;
        int menuRectAlto = 560; 
        int menuRectX = menuRectAncho / 2 + 10;
        int menuRectY = menuRectAlto / 2 + 20; 
        e.dibujarImagen(imagenMenu, menuRectX, menuRectY, 0, 1.0);
      
        
        // --- Mostrar la vida del mago ---
        e.cambiarFont("Segoe UI Emoji", 16, Color.YELLOW); 
        e.escribirTexto("❤️ " + mago.getVidas(), menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 40);

        
        // --- Mostrar los poderes ---
        e.escribirTexto("✨Poderes:", menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 65);
        
        // como se ve la cantidad de demonios en el menu
        e.escribirTexto("👿 " + demoniosRestantes, menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 180);

        int espacio = 0;
        for (String poder : mago.getPoderes()) {
            espacio += 30; 
            e.escribirTexto(poder, menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 65 + espacio);
			
        }
    }
    // muestra la cantidad de demoios restantes en el menu
    public void setDemoniosRestantes(int cantidad) {
        this.demoniosRestantes = cantidad;
    }
    
}