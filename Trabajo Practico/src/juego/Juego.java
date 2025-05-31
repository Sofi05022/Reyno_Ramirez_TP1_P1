package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Juego extends InterfaceJuego {
	private Entorno entorno;
    private Personaje mago;
    private Fondo fondo;
    private Piedras piedras;
    private Demonio[] demonios; // Un arreglo para almacenar los demonios
    private Random random; // Para generar los demonios 
    private boolean juegoTerminado = false;
    private boolean juegoGanado = false;
    private List<Poderes> poderesActivos;
    private int totalDemoniosDisponibles = 20; // total que tendrá el juego
    private int demoniosRestantes;             // muestra en pantalla la cantidad de demonios que quedan con vida
    
    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        this.mago = new Personaje(entorno.ancho() / 2, entorno.alto() / 2, this.entorno);
        this.fondo = new Fondo("imagenes/Fondo.png", this.entorno, this.mago);
        this.piedras = new Piedras(this.entorno);
        this.random = new Random();
        this.mago.setVidas(10);
        // Inicializar 4 demonios en posiciones distintas y aleatorias sin colisionar con el menú
        this.demonios = new Demonio[4];
        this.demoniosRestantes = totalDemoniosDisponibles-demonios.length;
        this.poderesActivos = new ArrayList<>();
        for (int i = 0; i < demonios.length; i++) {
            demonios[i] = crearDemonio();
        }

        this.entorno.iniciar();

    }

    public Demonio crearDemonio() {
        Demonio nuevo;
        boolean valido;
        do { // Generar posiciones aleatorias dentro del entorno
            double x = random.nextDouble() * (entorno.ancho() - (mago.ancho * 2)) + mago.ancho;
            double y = random.nextDouble() * (entorno.alto() - (mago.alto * 2)) + mago.alto;
            nuevo = new Demonio(x, y, entorno);
            
            // Comprueba si la posición colisiona con el menú
            // Usamos los métodos getBorde del demonio y los bordes calculados del menú
            valido = !(nuevo.getBordeDer() > mago.getBordeIzq() &&
                       nuevo.getBordeIzq() < mago.getBordeDer() &&
                       nuevo.getBordeInf() > mago.getBordeSup() &&
                       nuevo.getBordeSup() < mago.getBordeInf());

            int menuRectAncho = 200;
            int menuRectAlto = 560;
            int menuRectX = menuRectAncho / 2 + 10;
            int menuRectY = menuRectAlto / 2 + 20;
            
            // Calcular los bordes del rectangulo del menú
            double rectIzq = menuRectX - menuRectAncho / 2.0;
            double rectDer = menuRectX + menuRectAncho / 2.0;
            double rectArriba = menuRectY - menuRectAlto / 2.0;
            double rectAbajo = menuRectY + menuRectAlto / 2.0;
            // Comprueba que no se superponga con el mago al inicio
            if (valido) {
                valido = !(nuevo.getBordeDer() > rectIzq &&
                           nuevo.getBordeIzq() < rectDer &&
                           nuevo.getBordeInf() > rectArriba &&
                           nuevo.getBordeSup() < rectAbajo);
            }
         // Comprueba que no se superponga con otras piedras al inicio
            if (valido) {
                for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
                    if (nuevo.getBordeDer() > piedra.getBordeIzq() &&
                        nuevo.getBordeIzq() < piedra.getBordeDer() &&
                        nuevo.getBordeInf() > piedra.getBordeSup() &&
                        nuevo.getBordeSup() < piedra.getBordeInf()) {
                        valido = false;
                        break;
                    }
                }
            }

        } while (!valido); // Repite hasta encontrar una posicion 
        return nuevo;
    }
    public void tick() {
    	// muestra la cantidad de demonios 
    	fondo.setDemoniosRestantes(demoniosRestantes);
    	fondo.dibujarMenu();
    	fondo.dibujar();
    	
        if (juegoTerminado) {
        	 entorno.cambiarFont("Impact", 40, juegoGanado ? Color.GREEN : Color.RED);
        	    String mensaje = juegoGanado ? "¡GANASTE!" : "GAME OVER!";
        	    entorno.escribirTexto(mensaje, entorno.ancho() / 2 - 100, entorno.alto() / 2);
        	    return;
        }
        if ( entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            boolean direccion = mago.getUltimaDireccion();
            poderesActivos.add(new Poderes(mago.x, mago.y, entorno, direccion));
        }

        
        fondo.dibujarMenu();
        piedras.dibujar();

        double prevMagoX = mago.x;
        double prevMagoY = mago.y;
        // Lógica de movimiento del personaje
        if (entorno.estaPresionada('d')) mago.moverDerecha();
        if (entorno.estaPresionada('a')) mago.moverIzquierda();
        if (entorno.estaPresionada('w')) mago.moverArriba();
        if (entorno.estaPresionada('s')) mago.moverAbajo();
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

        if (mago.getBordeDer() > rectIzq && mago.getBordeIzq() < rectDer &&
            mago.getBordeInf() > rectArriba && mago.getBordeSup() < rectAbajo) {
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
        for (int i = 0; i < demonios.length; i++) {
            Demonio demonio = demonios[i];
            if (demonio == null) continue;

            double prevDemonX = demonio.x;
            double prevDemonY = demonio.y;

            demonio.perseguir(mago);
            // Colision del demonio con el menu
            if (demonio.getBordeDer() > rectIzq && demonio.getBordeIzq() < rectDer &&
                demonio.getBordeInf() > rectArriba && demonio.getBordeSup() < rectAbajo) {
                demonio.x = prevDemonX;
                demonio.y = prevDemonY;
            }
            // Colision del demonio con el mago para que el demonio no atraviese al mago
            if (demonio.getBordeDer() > mago.getBordeIzq() &&
                demonio.getBordeIzq() < mago.getBordeDer() &&
                demonio.getBordeInf() > mago.getBordeSup() &&
                demonio.getBordeSup() < mago.getBordeInf()) {

                mago.perderVida();
                System.out.println("Vidas restantes: " + mago.getVidas());

                if (mago.getVidas() <= 0) {
                    juegoTerminado = true;
                    return;
                }

                // Reemplazar demonio solo si quedan disponibles
                if (demoniosRestantes > 0) {
                    demonios[i] = crearDemonio();
                    demoniosRestantes--;
                } else {
                    demonios[i] = null; // ya no se reemplaza
                }
                continue;
            }

            demonio.mostrar(); // mostrar al demonio
        }
        for (int i = 0; i < poderesActivos.size(); i++) {
            Poderes poder = poderesActivos.get(i);
            poder.lanzarBola();
            poder.dibujarBola(entorno);

            // Colisión con demonios
            for (int j = 0; j < demonios.length; j++) {
            	if (demonios[j] != null && poder.colisionaCon(demonios[j])) {
                    poderesActivos.remove(i); //eliminar demonio
                    i--; //ajustar indice
                    // Reemplazar si quedan demonios por aparecer
                    if (demoniosRestantes > 0) {
                        demonios[j] = crearDemonio();
                        demoniosRestantes--;
                    
                    }break;
            	}
            }
        } 
        
        if (todosDemoniosMuertos() && demoniosRestantes == 0 && !juegoGanado) {
            juegoGanado = true;
            juegoTerminado = true;
        }
        mago.mostrar(); 
    }
    public boolean todosDemoniosMuertos (){
    	for (Demonio demonio : demonios) {
            if (demonio != null) return false;
        	}
        return true;
        }
    public static void main(String[] args) {
        new Juego();
    }
}


          