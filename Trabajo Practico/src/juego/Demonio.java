package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;


public class Demonio {
	
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
	
    public Demonio(double x, double y, Entorno ent) {
        this.x = x;
        this.y = y;
        this.escala = 0.16;
        this.velocidad = 1.5; // Velocidad del demonio, un poco más lenta que el mago
        this.e = ent;

        // Cargamos las imágenes.
        this.imagenDer = Herramientas.cargarImagen("imagenes/DemonDer.png"); 
        this.imagenIzq = Herramientas.cargarImagen("imagenes/DemonIzq.png"); 
        this.imagenAtras = Herramientas.cargarImagen("imagenes/DemonAtras.png"); 
        this.imagenFrente = Herramientas.cargarImagen("imagenes/DemonFrente.png"); 
        
        this.ancho = imagenDer.getWidth(null) * this.escala;
        this.alto = imagenDer.getHeight(null) * this.escala;

        this.estaApoyado = false;
        this.herido = false;
        this.vida = 100;
        this.inicioImage = this.imagenFrente; 
    }
    
    public void mostrar() {
        this.e.dibujarImagen(inicioImage, x, y, 0, escala);
    }
    
    
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
    
  
    public void moverDerecha() {
        this.x += this.velocidad;
        if (getBordeDer() > this.e.ancho()) {
            this.x = this.e.ancho() - (this.ancho / 2);
        }
        this.inicioImage = this.imagenDer;
    }

    public void moverIzquierda() {
        this.x -= this.velocidad;
        if (getBordeIzq() < 0) {
            this.x = 0 + (this.ancho / 2);
        }
        this.inicioImage = this.imagenIzq; 
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
    
    public void perseguir(Personaje mago) {
        double deltaX = mago.x - this.x;
        double deltaY = mago.y - this.y;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            double moveX = (deltaX / distance) * velocidad;
            double moveY = (deltaY / distance) * velocidad;

            this.x += moveX;
            this.y += moveY;

            // Actualizar la imagen del demonio según la dirección principal del movimiento
            if (Math.abs(moveX) > Math.abs(moveY)) { // Movimiento predominantemente horizontal
                if (moveX > 0) {
                    this.inicioImage = this.imagenDer;
                } else {
                    this.inicioImage = this.imagenIzq;
                }
            } else { // Movimiento predominantemente vertical
                if (moveY > 0) {
                    this.inicioImage = this.imagenFrente;
                } else {
                    this.inicioImage = this.imagenAtras;
                }
            }
        }
    }
}