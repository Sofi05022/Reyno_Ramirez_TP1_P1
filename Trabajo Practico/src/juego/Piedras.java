package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Image; 

public class Piedras {
     Entorno e;
     PiedraIndividual[] piedrasArray;
     Image imagenPiedra;
     int ANCHO_PIEDRA = 50;
     int ALTO_PIEDRA = 50;

    public class PiedraIndividual {
        double x;
        double y;
        double ancho;
        double alto;

        public PiedraIndividual(double x, double y, double ancho, double alto) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
        }

        // Métodos para obtener los bordes de la piedra 
        public double getBordeDer() {
            return this.x + (this.ancho / 2);
        }

        public double getBordeIzq() {
            return this.x - (this.ancho / 2);
        }

        public double getBordeSup() {
            return this.y - (this.alto / 2);
        }

        public double getBordeInf() {
            return this.y + (this.alto / 2);
        }
    }

    public Piedras(Entorno ent) {
        this.e = ent;
        this.piedrasArray = new PiedraIndividual[5];
        this.imagenPiedra = Herramientas.cargarImagen("imagenes/Roca.png"); 
        
        piedrasArray[0] = new PiedraIndividual(e.ancho() / 2 + 100, e.alto() / 2 - 150, ANCHO_PIEDRA, ALTO_PIEDRA);
        piedrasArray[1] = new PiedraIndividual(e.ancho() / 2 - 150, e.alto() / 2 + 50, ANCHO_PIEDRA, ALTO_PIEDRA);
        piedrasArray[2] = new PiedraIndividual(e.ancho() / 2 + 250, e.alto() / 2 + 100, ANCHO_PIEDRA, ALTO_PIEDRA);
        piedrasArray[3] = new PiedraIndividual(e.ancho() / 2 - 50, e.alto() / 2 + 200, ANCHO_PIEDRA, ALTO_PIEDRA);
        piedrasArray[4] = new PiedraIndividual(e.ancho() / 2 + 300, e.alto() / 2 - 50, ANCHO_PIEDRA, ALTO_PIEDRA);
    }

    public void dibujar() {
        for (PiedraIndividual piedra : piedrasArray) {
        	e.dibujarImagen(imagenPiedra, piedra.x, piedra.y, 0, 1.0);
        }
    }
    
    public PiedraIndividual[] getPiedrasArray() {
        return piedrasArray;
    }
}
