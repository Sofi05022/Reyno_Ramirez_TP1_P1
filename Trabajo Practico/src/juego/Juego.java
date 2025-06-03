package juego;

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
    private List<Poderes> poderesActivos; // Esta lista ahora contendrá tanto bolas de fuego como pociones de fuerza
    private int demoniosEliminadosDesdeUltimaRecompensa = 0;
    private Recompensa recompensaActiva = null; 


    // Variables para el manejo de poderes con usos
    private String poderActivo = null; // ("bola", "fuerza", or null)
    private int usosPoderRestantes = 0; 
    private final int MAX_USOS_PODER_BOLA = 25; // Usos para la bola de fuego
    private final int MAX_USOS_PODER_FUERZA = 25; // Usos para el poder de fuerza (pociones)

    // Nuevas variables para el control de demonios
    private int demoniosMuertosContador = 0; 
    private int demoniosMatados = 0;         // Para mostrar en el menú (demonios eliminados por el jugador)

    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
       
        reiniciarJuego(); // Inicializa el juego al inicio
        this.entorno.iniciar();
    }

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        this.mago = new Personaje(entorno.ancho() / 2, entorno.alto() / 2, this.entorno);
        this.fondo = new Fondo("imagenes/Fondo.png", this.entorno, this.mago); // Se pasa el entorno y mago al constructor de Fondo
        this.piedras = new Piedras(this.entorno);
        this.random = new Random();
        this.mago.setVidas(100); // Mago inicia con 100 de vida
        this.mago.sumarPuntos(-mago.getPuntuacion()); // Resetear puntuación a 0
        
        this.demonios = new Demonio[4]; // Array para 4 demonios activos simultáneamente
        this.poderesActivos = new ArrayList<>();
        this.juegoTerminado = false;
        this.juegoGanado = false;
        this.demoniosEliminadosDesdeUltimaRecompensa = 0;
        this.recompensaActiva = null;
        this.poderActivo = null;
        this.usosPoderRestantes = 0;
        this.demoniosMuertosContador = 0; 
        this.demoniosMatados = 0;         

        // Generar los 4 demonios iniciales
        for (int i = 0; i < demonios.length; i++) {
            demonios[i] = crearDemonio();
        }
    }

    public Demonio crearDemonio() {
        Demonio nuevo;
        boolean colisionaConAlgo;
        int intentos = 0;
        final int MAX_INTENTOS = 100; 

        do {
            colisionaConAlgo = false;
            double demonioX = random.nextDouble() * entorno.ancho();
            double demonioY = random.nextDouble() * entorno.alto();
            nuevo = new Demonio(demonioX, demonioY, entorno); 

            // Definir las dimensiones y posición del rectángulo del menú para evitar colisiones
            int menuRectAncho = 200;
            int menuRectAlto = 560;
            int menuRectX = menuRectAncho / 2 + 10; 
            int menuRectY = menuRectAlto / 2 + 20;  

            double rectIzq = menuRectX - menuRectAncho / 2.0;
            double rectDer = menuRectX + menuRectAncho / 2.0;
            double rectArriba = menuRectY - menuRectAlto / 2.0;
            double rectAbajo = menuRectY + menuRectAlto / 2.0;

            // Comprobar colisión con el menú
            if (nuevo.getBordeDer() > rectIzq && nuevo.getBordeIzq() < rectDer &&
                nuevo.getBordeInf() > rectArriba && nuevo.getBordeSup() < rectAbajo) {
                colisionaConAlgo = true;
            }

            // Comprobar colisión con el mago
            if (!colisionaConAlgo && mago != null && nuevo.colisionaCon(mago)) {
                colisionaConAlgo = true;
            }

            // Comprobar colisión con las piedras
            if (!colisionaConAlgo && piedras != null) {
                for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
                    if (piedra != null && nuevo.colisionaCon(piedra)) {
                        colisionaConAlgo = true;
                        break;
                    }
                }
            }
            
            // Comprobar colisión con otros Demonios ya existentes
            if (!colisionaConAlgo) {
                for (Demonio otroDemonio : this.demonios) { 
                    if (otroDemonio != null && otroDemonio != nuevo && nuevo.colisionaCon(otroDemonio)) {
                        colisionaConAlgo = true;
                        break;
                    }
                }
            }

            intentos++;
            if (intentos > MAX_INTENTOS) {
                System.err.println("No se pudo encontrar una posición válida para el demonio después de " + MAX_INTENTOS + " intentos.");
                return null; // Retornar null si no se puede colocar
            }

        } while (colisionaConAlgo);
        return nuevo;
    }
    
    public void tick() {
        // Reiniciar juego al presionar 'r'
        if (entorno.sePresiono('r')) {
            reiniciarJuego();
            return; 
        }

        // Mostrar mensaje de juego terminado si aplica
        if (juegoTerminado) {
            fondo.dibujarPantallaFinJuego(juegoGanado); // Llama al nuevo método en Fondo
        	return; // Detener la lógica del juego si ha terminado
        }
        
        // Actualizar y dibujar el fondo y el menú
    	fondo.actualizarDatos(mago.getVidas(), demoniosMatados, mago.getPuntuacion(), poderActivo, usosPoderRestantes);
    	fondo.dibujar();
    	fondo.dibujarMenu();
    	
        // Manejo del clic del mouse para la selección y disparo de poderes
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mouseX = entorno.mouseX();
            int mouseY = entorno.mouseY();

            // Dimensiones y posición del menú para evitar colisiones de clic
            int menuRectAncho = 200;
            int menuRectAlto = 560;
            int menuRectX = menuRectAncho / 2 + 10;
            int menuRectY = menuRectAlto / 2 + 20;
            
            // Lógica para seleccionar "Bola de Fuego"
            if (mouseX >= (menuRectX - menuRectAncho / 2 + 30) && mouseX <= (menuRectX - menuRectAncho / 2 + 30 + 100) &&
                mouseY >= (menuRectY - menuRectAlto / 2 + 135) && mouseY <= (menuRectY - menuRectAlto / 2 + 135 + 20)) {
                
                if (poderActivo == null || usosPoderRestantes <= 0 || !poderActivo.equals("bola")) { 
                    poderActivo = "bola";
                    usosPoderRestantes = MAX_USOS_PODER_BOLA;
                }
            }
            // Lógica para seleccionar "Fuerza"
            else if (mouseX >= (menuRectX - menuRectAncho / 2 + 30) && mouseX <= (menuRectX - menuRectAncho / 2 + 30 + 100) &&
                mouseY >= (menuRectY - menuRectAlto / 2 + 165) && mouseY <= (menuRectY - menuRectAlto / 2 + 165 + 20)) {
                
                if (poderActivo == null || usosPoderRestantes <= 0 || !poderActivo.equals("fuerza")) { 
                    poderActivo = "fuerza"; 
                    usosPoderRestantes = MAX_USOS_PODER_FUERZA; 
                }
            }
            // Lógica para lanzar el poder (si hay uno activo y se hace clic fuera del menú)
            else if (poderActivo != null && usosPoderRestantes > 0) {
                // Calcular los bordes del área del menú para evitar lanzar poderes al hacer clic en el menú
                double rectIzq = menuRectX - menuRectAncho / 2.0;
                double rectDer = menuRectX + menuRectAncho / 2.0;
                double rectArriba = menuRectY - menuRectAlto / 2.0;
                double rectAbajo = menuRectY + menuRectAlto / 2.0; 

                // Si el clic no fue en el menú, se lanza el poder
                if (!(mouseX >= rectIzq && mouseX <= rectDer && mouseY >= rectArriba && mouseY <= rectAbajo)) {
                    if (poderActivo.equals("bola")) {
                        poderesActivos.add(new Poderes(mago.x, mago.y, entorno, mouseX, mouseY, "bola"));
                        usosPoderRestantes--; 
                    } else if (poderActivo.equals("fuerza")) { // Lanzar poción de fuerza
                        poderesActivos.add(new Poderes(mago.x, mago.y, entorno, mouseX, mouseY, "fuerza"));
                        usosPoderRestantes--; // Consume un uso al lanzar la poción
                    }
                }
            }
        }

        piedras.dibujar(); // Dibujar las piedras

        // Guardar la posición anterior del mago para revertir movimientos en caso de colisión
        double prevMagoX = mago.x;
        double prevMagoY = mago.y;
        
        // Movimiento del mago según las teclas presionadas
        if (entorno.estaPresionada('d')) mago.moverDerecha();
        if (entorno.estaPresionada('a')) mago.moverIzquierda();
        if (entorno.estaPresionada('w')) mago.moverArriba();
        if (entorno.estaPresionada('s')) mago.moverAbajo();
        
        // Revertir movimiento del mago si colisiona con el menú
        int menuRectAncho = 200;
        int menuRectAlto = 560;
        int menuRectX = menuRectAncho / 2 + 10;
        int menuRectY = menuRectAlto / 2 + 20;
        double rectIzq = menuRectX - menuRectAncho / 2.0;
        double rectDer = menuRectX + menuRectAncho / 2.0;
        double rectArriba = menuRectY - menuRectAlto / 2.0;
        double rectAbajo = menuRectY + menuRectAlto / 2.0;

        if (mago.getBordeDer() > rectIzq && mago.getBordeIzq() < rectDer &&
            mago.getBordeInf() > rectArriba && mago.getBordeSup() < rectAbajo) {
            mago.x = prevMagoX;
            mago.y = prevMagoY;
        }
        
        // Revertir movimiento del mago si colisiona con las piedras
        if (piedras != null) {
            for (Piedras.PiedraIndividual piedra : piedras.getPiedrasArray()) {
                if (piedra != null && mago.colisionaCon(piedra)) {
                    mago.x = prevMagoX;
                    mago.y = prevMagoY;
                    break;
                }
            }
        }
        
        // Lógica para los demonios
        for (int i = 0; i < demonios.length; i++) {
            Demonio demonio = demonios[i];
            if (demonio == null) continue; // Saltar si el demonio es nulo

            double prevDemonX = demonio.x;
            double prevDemonY = demonio.y;

            demonio.perseguir(mago); // Los demonios persiguen al mago

            // Revertir movimiento del demonio si colisiona con el menú
            if (demonio.getBordeDer() > rectIzq && demonio.getBordeIzq() < rectDer &&
                demonio.getBordeInf() > rectArriba && demonio.getBordeSup() < rectAbajo) {
                demonio.x = prevDemonX;
                demonio.y = prevDemonY;
            }
            
            // Colisión del demonio con el mago
            if (demonio.colisionaCon(mago)) {
                mago.perderVida(); 
                if (mago.getVidas() <= 0) {
                    juegoTerminado = true;
                    juegoGanado = false;
                    return; // Terminar el juego
                }
                
                demonios[i] = null; // El demonio es eliminado
                demoniosMuertosContador++; // Incrementar contador para reemplazo
                demoniosMatados++; // Incrementar contador de demonios eliminados por el jugador
                continue; 
            }
            demonio.mostrar(); // Dibujar el demonio
        }
        
        // Reemplazar demonios eliminados (en pares o individualmente si hay slots vacíos)
        // Se asegura que siempre haya 4 demonios activos si el juego no ha terminado.
        for (int i = 0; i < demonios.length; i++) {
            if (demonios[i] == null && !juegoTerminado) { // Si hay un slot vacío y el juego no ha terminado
                Demonio nuevoDemonio = crearDemonio();
                if (nuevoDemonio != null) {
                    demonios[i] = nuevoDemonio; // Añadir el nuevo demonio
                }
            }
        }
        // Reiniciar el contador de demonios muertos para el reemplazo si se han rellenado los slots
        if (demoniosMuertosContador > 0) {
            demoniosMuertosContador = 0;
        }


        // Lógica para los poderes activos (bolas de fuego y pociones de fuerza)
        for (int i = 0; i < poderesActivos.size(); i++) {
            Poderes poder = poderesActivos.get(i);
            
            poder.lanzarPoder(); // Mover el poder (bola o poción)
            poder.dibujar(entorno); // Dibujar el poder

            // Colisión del poder con los demonios
            for (int j = 0; j < demonios.length; j++) {
                if (demonios[j] != null && poder.colisionaCon(demonios[j])) {
                    int puntosGanados = 10; // Puntos base
                    if (poder.getTipoPoder().equals("fuerza")) {
                        puntosGanados = 20; // Doble de puntos si es poción de fuerza
                    }
                    
                    poderesActivos.remove(i); // Eliminar el poder (bola o poción)
                    i--; // Ajustar el índice debido a la eliminación
                    
                    mago.sumarPuntos(puntosGanados); // Sumar puntos al mago
                    demoniosEliminadosDesdeUltimaRecompensa++; // Para la recompensa
                    demoniosMatados++; // Incrementar contador de demonios eliminados
                    demonios[j] = null; // Eliminar el demonio
                    break; // Salir del bucle interno, el poder ya colisionó
                }
            }
            // Si el poder sale de la pantalla, se elimina
            if (i >= 0 && i < poderesActivos.size() && poderesActivos.get(i).estaFueraDePantalla()) {
                poderesActivos.remove(i);
                i--; // Ajustar el índice
            }
        } 
        
        // Si los usos del poder activo se agotan, se desactiva
        if (usosPoderRestantes <= 0 && poderActivo != null) {
            poderActivo = null; // Desactivar el poder
        }
        
        // Lógica para la recompensa
        if (demoniosEliminadosDesdeUltimaRecompensa >= 5) {
            // Generar una posición aleatoria para la recompensa, evitando el área del menú
            double recompensaX = random.nextDouble() * (entorno.ancho() - 220) + 210; 
            double recompensaY = random.nextDouble() * (entorno.alto() - 40) + 20; 
            recompensaActiva = new Recompensa(recompensaX, recompensaY);
            demoniosEliminadosDesdeUltimaRecompensa = 0; // Resetear contador
        }

        if (recompensaActiva != null) {
            recompensaActiva.dibujar(entorno);
            // Colisión del mago con la recompensa
            if (recompensaActiva.colisionaCon(mago) && !recompensaActiva.isRecolectada()) { 
                mago.sumarPuntos(50); // Sumar puntos al mago
                recompensaActiva.setRecolectada(true); 
                recompensaActiva = null; // Eliminar la recompensa
            }
        }
        
        // Condiciones de fin de juego
        if (mago.getVidas() <= 0) {
            juegoTerminado = true;
            juegoGanado = false;
        }

        if (mago.getPuntuacion() >= 350 && !juegoGanado) {
            juegoGanado = true;
            juegoTerminado = true;
        }

        mago.mostrar(); // Dibujar al mago
    }

    public static void main(String[] args) {
        new Juego();
    }
}
