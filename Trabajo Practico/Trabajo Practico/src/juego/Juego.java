package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;
import java.util.Random; 

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private Personaje mago;
    private Fondo fondo;
    private Piedras piedras;
    private Demonio[] demonios; // Un arreglo para almacenar los demonios
    private Random random; // Para generar los demonios 

    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        this.mago = new Personaje(entorno.ancho() / 2, entorno.alto() / 2, this.entorno);
        this.fondo = new Fondo("imagenes/Fondo.png", this.entorno, this.mago);
        this.piedras = new Piedras(this.entorno);
        this.random = new Random(); 

   
        int menuRectAncho = 200;
        int menuRectAlto = 560;
        int menuRectX = menuRectAncho / 2 + 10;
        int menuRectY = menuRectAlto / 2 + 20;

        // Calcular los bordes del rectángulo del menú
        double rectIzq = menuRectX - menuRectAncho / 2.0;
        double rectDer = menuRectX + menuRectAncho / 2.0;
        double rectArriba = menuRectY - menuRectAlto / 2.0;
        double rectAbajo = menuRectY + menuRectAlto / 2.0;

        // Inicializar 4 demonios en posiciones distintas y aleatorias sin colisionar con el menú
        this.demonios = new Demonio[4];
        for (int i = 0; i < demonios.length; i++) {
            double demonX, demonY;
            boolean validPosition;
            do {
                // Generar posiciones aleatorias dentro del entorno
                // Asegurarse de que el demonio no se genere parcialmente fuera de la pantalla
                demonX = random.nextDouble() * (entorno.ancho() - (mago.ancho * 2)) + mago.ancho;
                demonY = random.nextDouble() * (entorno.alto() - (mago.alto * 2)) + mago.alto;

                // Crear un demonio temporal para verificar su tamaño en la colisión
                Demonio tempDemon = new Demonio(demonX, demonY, this.entorno);

                // Comprueba si la posición colisiona con el menú
                // Usamos los métodos getBorde del demonio y los bordes calculados del menú
                validPosition = !(tempDemon.getBordeDer() > rectIzq &&
                                  tempDemon.getBordeIzq() < rectDer &&
                                  tempDemon.getBordeInf() > rectArriba &&
                                  tempDemon.getBordeSup() < rectAbajo);

                // Comprueba que no se superponga con el mago al inicio
                if (validPosition) {
                    validPosition = !(tempDemon.getBordeDer() > mago.getBordeIzq() &&
                                      tempDemon.getBordeIzq() < mago.getBordeDer() &&
                                      tempDemon.getBordeInf() > mago.getBordeSup() &&
                                      tempDemon.getBordeSup() < mago.getBordeInf());
                }

                // Comprueba que no se superponga con otras piedras al inicio
                if (validPosition) {
                    for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
                        if (tempDemon.getBordeDer() > piedra.getBordeIzq() &&
                            tempDemon.getBordeIzq() < piedra.getBordeDer() &&
                            tempDemon.getBordeInf() > piedra.getBordeSup() &&
                            tempDemon.getBordeSup() < piedra.getBordeInf()) {
                            validPosition = false;
                            break;
                        }
                    }
                }
                
                //Comprueba que no se superponga con otros demonios ya creados
                if (validPosition) {
                    for (int j = 0; j < i; j++) { // Solo compara con los demonios ya inicializados
                        if (tempDemon.getBordeDer() > demonios[j].getBordeIzq() &&
                            tempDemon.getBordeIzq() < demonios[j].getBordeDer() &&
                            tempDemon.getBordeInf() > demonios[j].getBordeSup() &&
                            tempDemon.getBordeSup() < demonios[j].getBordeInf()) {
                            validPosition = false;
                            break;
                        }
                    }
                }

            } while (!validPosition); // Repite hasta encontrar una posicion 

            demonios[i] = new Demonio(demonX, demonY, this.entorno);
        }

        this.entorno.iniciar();
    }

    public void tick() {
        fondo.dibujar();
        fondo.dibujarMenu();
        piedras.dibujar();
        
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

        // --- Colision del mago con el Menu ---
        int menuRectAncho = 200;
        int menuRectAlto = 560;
        int menuRectX = menuRectAncho / 2 + 10;
        int menuRectY = menuRectAlto / 2 + 20;

        // Calcular los bordes del menu
        double rectIzq = menuRectX - menuRectAncho / 2.0;
        double rectDer = menuRectX + menuRectAncho / 2.0;
        double rectArriba = menuRectY - menuRectAlto / 2.0;
        double rectAbajo = menuRectY + menuRectAlto / 2.0;

        if (mago.getBordeDer() > rectIzq &&
            mago.getBordeIzq() < rectDer &&
            mago.getBordeInf() > rectArriba &&
            mago.getBordeSup() < rectAbajo) {
            mago.x = prevMagoX;
            mago.y = prevMagoY;
        }

        // --- Colision del mago con las Piedras ---
        for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
            if (mago.getBordeDer() > piedra.getBordeIzq() &&
                mago.getBordeIzq() < piedra.getBordeDer() &&
                mago.getBordeInf() > piedra.getBordeSup() &&
                mago.getBordeSup() < piedra.getBordeInf()) {
                mago.x = prevMagoX;
                mago.y = prevMagoY;
                break; 
            }
        }

        // --- Movimiento y colision de los demonios ---
        for (Demonio demonio : demonios) {
            double prevDemonX = demonio.x;
            double prevDemonY = demonio.y;

            demonio.perseguir(mago); 

            // Colision del demonio con el menu
            if (demonio.getBordeDer() > rectIzq &&
                demonio.getBordeIzq() < rectDer &&
                demonio.getBordeInf() > rectArriba &&
                demonio.getBordeSup() < rectAbajo) {
                demonio.x = prevDemonX;
                demonio.y = prevDemonY;
            }
            
            // Colision del demonio con el mago para que el demonio no atraviese al mago
            if (demonio.getBordeDer() > mago.getBordeIzq() &&
                demonio.getBordeIzq() < mago.getBordeDer() &&
                demonio.getBordeInf() > mago.getBordeSup() &&
                demonio.getBordeSup() < mago.getBordeInf()) {
                demonio.x = prevDemonX;
                demonio.y = prevDemonY;
            }
            
            demonio.mostrar(); // Muestra al demonio
        }

        mago.mostrar(); 
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}
