package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private Personaje mago;
    private Fondo fondo;
    private Piedras piedras;

    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);// Inicializa la ventana
        this.mago = new Personaje(entorno.ancho() / 2, entorno.alto() / 2, this.entorno); // Inicializa al mago
        this.fondo = new Fondo("imagenes/Fondo.png", this.entorno, this.mago); // Pasa el mago al constructor del Fondo
        this.piedras = new Piedras(this.entorno); // Inicializa las piedras
        
        this.entorno.iniciar();
    }

    public void tick() {
        fondo.dibujar(); // Dibuja el fondo
        fondo.dibujarMenu(); // Dibuja el menu (vida y poderes) 
        piedras.dibujar(); // Dibuja las piedras
        
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

       
        int menuRectAncho = 200;
        int menuRectAlto = 560; 
       
        int menuRectX = menuRectAncho / 2 + 10; 
        int menuRectY =  menuRectAlto / 2 + 20; 

        // Calcular los bordes del rectángulo del menú
        double rectIzq = menuRectX - menuRectAncho / 2.0;
        double rectDer = menuRectX + menuRectAncho / 2.0;
        double rectArriba = menuRectY - menuRectAlto / 2.0;
        double rectAbajo = menuRectY + menuRectAlto / 2.0;

        // Comprobar si el mago está colisionando con el rectángulo del menú
        boolean toca = mago.getBordeDer() > rectIzq &&
                            mago.getBordeIzq() < rectDer &&
                            mago.getBordeInf() > rectArriba &&
                            mago.getBordeSup() < rectAbajo;

        // Si hay colisión, revertir la posición del mago a la posición anterior
        if (toca) {
            mago.x = prevMagoX;
            mago.y = prevMagoY;
        }

        mago.mostrar();    // Dibuja el personaje 
        
        // Si toca las piedras el mago
        for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
            boolean TocaPiedra = mago.getBordeDer() > piedra.getBordeIzq() &&
                                         mago.getBordeIzq() < piedra.getBordeDer() &&
                                         mago.getBordeInf() > piedra.getBordeSup() &&
                                         mago.getBordeSup() < piedra.getBordeInf();

            if (TocaPiedra) {
                mago.x = prevMagoX;
                mago.y = prevMagoY;
                break;
            }
        }
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}