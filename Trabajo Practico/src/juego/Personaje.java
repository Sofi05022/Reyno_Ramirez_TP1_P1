package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas; // Importar Herramientas para cargar imágenes

public class Personaje {
    double x;
    double y;
    double ancho;
    double alto;
    Image imagenDer;
    Image imagenIzq;
    Image imagenAtras;
    Image imagenDelantera;
    Image inicioImage; // Nueva variable para la imagen actual del personaje
    double escala;
    double velocidad;
    Entorno e;
    boolean estaApoyado;
    boolean herido;
    int vida;
    String[] poderes;

    public Personaje(double x, double y, Entorno ent) {
        this.x = x;
        this.y = y;
        this.escala = 0.16;
        this.velocidad = 2.0;

        // Cargamos las imágenes.
        this.imagenDer = Herramientas.cargarImagen("imagenes/Mago Der.png"); // Mago mirando a la derecha
        this.imagenIzq = Herramientas.cargarImagen("imagenes/Mago Izq.png"); // Mago mirando a la izquierda
        this.imagenAtras = Herramientas.cargarImagen("imagenes/MagoAtras.png"); // Mago mirando hacia arriba/atrás
        this.imagenDelantera = Herramientas.cargarImagen("imagenes/MagoDelantero.png"); // Mago mirando hacia abajo/delante

        this.e = ent;
        
        this.ancho = imagenDer.getWidth(null) * this.escala;
        this.alto = imagenDer.getHeight(null) * this.escala;

        this.estaApoyado = false;
        this.herido = false;
        this.vida = 100;
        this.poderes = new String[]{"Bola de Fuego", "Escudo Mágico", "Teletransporte"}; // Poderes iniciales

        // Establecemos la imagen inicial del personaje
        this.inicioImage = this.imagenAtras; // Empieza con el personaje hacia atras
    }

    public void mostrar() {
        // Dibujamos la imagen actual del personaje en su posición (x, y)
        this.e.dibujarImagen(inicioImage, x, y, 0, escala);
    }

    // Métodos para obtener los bordes del personaje (útiles para colisiones y límites)
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

    /**
     * Mueve el personaje hacia la derecha y actualiza su imagen.
     */
    public void moverDerecha() {
        this.x += this.velocidad;
        // Límite derecho del entorno
        if (getBordeDer() > this.e.ancho()) {
            this.x = this.e.ancho() - (this.ancho / 2);
        }
        this.inicioImage = this.imagenDer; // Cambia la imagen a la de derecha
    }

    /**
     * Mueve el personaje hacia la izquierda y actualiza su imagen.
     */
    public void moverIzquierda() {
        this.x -= this.velocidad;
        // Límite izquierdo del entorno
        if (getBordeIzq() < 0) {
            this.x = 0 + (this.ancho / 2);
        }
        this.inicioImage = this.imagenIzq; // Cambia la imagen a la de izquierda
    }

    /**
     * Mueve el personaje hacia arriba y actualiza su imagen.
     */
    public void moverArriba() {
        this.y -= this.velocidad;
        // Límite superior del entorno
        if (getBordeSup() < 0) {
            this.y = 0 + (this.alto / 2);
        }
        this.inicioImage = this.imagenAtras; // Cambia la imagen a la de atrás
    }

    /**
     * Mueve el personaje hacia abajo y actualiza su imagen.
     */
    public void moverAbajo() {
        this.y += this.velocidad;
        // Límite inferior del entorno
        if (getBordeInf() > this.e.alto()) {
            this.y = this.e.alto() - (this.alto / 2);
        }
        this.inicioImage = this.imagenDelantera; // Cambia la imagen a la delantera
    }
   
    public int getVida() {
        return vida;
    }
    
    public String[] getPoderes() {
        return poderes;
    }
}