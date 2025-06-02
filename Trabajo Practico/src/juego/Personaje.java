package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas; 
import java.awt.Color; 
import java.util.Random;

public class Personaje {
    double x;
    double y;
    double ancho;
    double alto;
    Image imagenDer;
    Image imagenIzq;
    Image imagenAtras;
    Image imagenFrente;
    Image inicioImage; 
    double escala;
    double velocidad;
    Entorno e;
    boolean estaApoyado;
    boolean herido;
    int vida; 
    String[] poderes; 
    private boolean ultimaDireccionDerecha = true; // true = derecha, false = izquierda
    private int puntuacion = 0;

    public Personaje(double x, double y, Entorno ent) {
        this.x = x;
        this.y = y;
        this.escala = 0.16;
        this.velocidad = 2.0; // Velocidad ajustada para el mago

        // Cargamos las imagenes.
        this.imagenDer = Herramientas.cargarImagen("imagenes/Mago Der.png"); 
        this.imagenIzq = Herramientas.cargarImagen("imagenes/Mago Izq.png"); 
        this.imagenAtras = Herramientas.cargarImagen("imagenes/MagoAtras.png"); 
        this.imagenFrente = Herramientas.cargarImagen("imagenes/MagoFrente.png"); 

        this.e = ent;

        // Calculamos ancho y alto una vez que las imágenes se han cargado
        this.ancho = imagenDer.getWidth(null) * this.escala;
        this.alto = imagenDer.getHeight(null) * this.escala;
        this.herido = false;
        this.vida = 100; // Vida inicial del mago
        this.poderes = new String[]{"Bola de Fuego", "Escudo Mágico", "Teletransporte"}; // Poderes adquiridos 

     // Empieza con el mago mirando hacia delante
        this.inicioImage = this.imagenFrente; 
    }

    public void mostrar() {
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

    // Los movimientos del mago y su actualizacion segun su movimiento
    public void moverDerecha() {
        this.x += this.velocidad;
        if (getBordeDer() > this.e.ancho()) {
            this.x = this.e.ancho() - (this.ancho / 2);
        }
        this.inicioImage = this.imagenDer;
        this.ultimaDireccionDerecha = true;
    }

    public void moverIzquierda() {
        this.x -= this.velocidad;
        if (getBordeIzq() < 0) {
            this.x = 0 + (this.ancho / 2);
        }
        this.inicioImage = this.imagenIzq; 
        this.ultimaDireccionDerecha = false;
    }

    public void moverArriba() {
        this.y -= this.velocidad;
        if (getBordeSup() < 0) {
            this.y = 0 + (this.alto / 2);
        }
        this.inicioImage = this.imagenAtras; 
    }
    
    public void moverAbajo() {
        this.y += this.velocidad;
        if (getBordeInf() > this.e.alto()) {
            this.y = this.e.alto() - (this.alto / 2);
        }
        this.inicioImage = this.imagenFrente; 
    }

    public int getVida() {
        return vida;
    }
    
    public void perderVida() {
        this.vida -= 5; // Reduce la vida en 5
    }
    
    public int getVidas() {
    	return vida;
    }

    public String[] getPoderes() {
        return poderes;
    }
    
    public boolean getUltimaDireccion() {
        return ultimaDireccionDerecha;
    }
    
    public int getPuntuacion() {
        return puntuacion;
    }

    public void sumarPuntos(int puntos) {
        this.puntuacion += puntos;
    }

    public void setVidas(int cantidad) {
        this.vida = cantidad;
    }

   
    public boolean colisionaCon(Piedras.PiedraIndividual piedra) {
        if (piedra == null) return false;
        return this.getBordeDer() > piedra.getBordeIzq() &&
               this.getBordeIzq() < piedra.getBordeDer() &&
               this.getBordeInf() > piedra.getBordeSup() &&
               this.getBordeSup() < piedra.getBordeInf();
    }

    
    public boolean colisionaCon(Demonio demonio) {
        if (demonio == null) return false;
        return this.getBordeDer() > demonio.getBordeIzq() &&
               this.getBordeIzq() < demonio.getBordeDer() &&
               this.getBordeInf() > demonio.getBordeSup() &&
               this.getBordeSup() < demonio.getBordeInf();
    }
}
