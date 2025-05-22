package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private Personaje mago;
    private Fondo fondo;

    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        this.mago = new Personaje(entorno.ancho() / 2, entorno.alto() / 2, this.entorno);
        this.fondo = new Fondo("imagenes/Fondo.png", this.entorno, this.mago); // Pasa el mago al constructor del Fondo

        this.entorno.iniciar();
    }

    public void tick() {
        fondo.dibujar(); // Dibuja el fondo
        fondo.dibujarMenu(); // Dibuja la interfaz de usuario (vida y poderes) usando el método del Fondo

        // Guarda la posición actual del mago antes de intentar moverlo
        double prevMagoX = mago.x;
        double prevMagoY = mago.y;

        // Lógica de movimiento del personaje
        if (entorno.estaPresionada('d')) {
            mago.moverDerecha();
        }
        if (entorno.estaPresionada('a')) {
            mago.moverIzquierda();
        }
        if (entorno.estaPresionada('w')) {
            mago.moverArriba();
        }
        if (entorno.estaPresionada('s')) {
            mago.moverAbajo();
        }

        // --- Colisión con el rectángulo de la UI (Menú) ---
        // Definir las dimensiones y posición del rectángulo del menú.
        // ¡Estos valores deben coincidir exactamente con los de Fondo.dibujarMenu()!
        int menuRectAncho = 200;
        int menuRectAlto = 560; // Coincide con Fondo.java
       
        int menuRectX = menuRectAncho / 2 + 10; // Coincide con Fondo.java
        int menuRectY =  menuRectAlto / 2 + 20; // Coincide con Fondo.java

        // Calcular los bordes del rectángulo del menú
        double rectLeft = menuRectX - menuRectAncho / 2.0;
        double rectRight = menuRectX + menuRectAncho / 2.0;
        double rectTop = menuRectY - menuRectAlto / 2.0;
        double rectBottom = menuRectY + menuRectAlto / 2.0;

        // Comprobar si el mago está colisionando con el rectángulo del menú
        boolean colliding = mago.getBordeDer() > rectLeft &&
                            mago.getBordeIzq() < rectRight &&
                            mago.getBordeInf() > rectTop &&
                            mago.getBordeSup() < rectBottom;

        // Si hay colisión, revertir la posición del mago a la posición anterior
        if (colliding) {
            mago.x = prevMagoX;
            mago.y = prevMagoY;
        }

        mago.mostrar();    // Dibuja el personaje (después de posibles reversiones de posición)
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}