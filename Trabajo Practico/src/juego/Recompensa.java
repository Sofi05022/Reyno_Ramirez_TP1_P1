package juego;

import java.awt.Color;
import java.awt.Image; 
import entorno.Entorno;
import entorno.Herramientas; 


public class Recompensa {
    double x, y;
    double ancho = 30; // Ajustado el tamaño para la imagen de recompensa
    double alto = 30;  // Ajustado el tamaño para la imagen de recompensa
    boolean recolectada = false;
    private Image imagenRecompensa; // Nuevo campo para la imagen de la recompensa

    public Recompensa(double x, double y) {
        this.x = x;
        this.y = y;
        // Cargar la imagen de la recompensa
        this.imagenRecompensa = Herramientas.cargarImagen("imagenes/Gema.png");
        // Asegurarse de que el ancho y alto del objeto Recompensa se basen en la imagen si es necesario
        // O mantener valores fijos para un tamaño consistente
        if (imagenRecompensa != null) {
            this.ancho = imagenRecompensa.getWidth(null) * 0.5; // Ajusta la escala si es necesario
            this.alto = imagenRecompensa.getHeight(null) * 0.5; // Ajusta la escala si es necesario
        }
    }

    public void dibujar(Entorno e) {
        if (!recolectada) {
            // Dibujar la imagen de la recompensa en lugar del rectángulo blanco
            e.dibujarImagen(imagenRecompensa, x, y, 0, 1.0); // Usar escala 1.0 o ajustar según necesidad
        }
    }

    public boolean colisionaCon(Personaje p) {
        return p.getBordeDer() > (x - ancho / 2) &&
               p.getBordeIzq() < (x + ancho / 2) &&
               p.getBordeInf() > (y - alto / 2) &&
               p.getBordeSup() < (y + alto / 2);
    }

    public boolean isRecolectada() {
        return recolectada;
    }

    public void setRecolectada(boolean recolectada) {
        this.recolectada = recolectada;
    }
}



