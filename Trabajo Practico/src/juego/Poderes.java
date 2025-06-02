package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Poderes {
    double x, y;
    double tamanio;
    double velocidad;
    double ancho, alto;
    double angulo; // Angulo para el movimiento de la bola de fuego o la poción de fuerza
    Image bolaImg; 
    Image pocionFuerzaImg; // Nueva imagen para la poción de fuerza
    Entorno e;
    String tipoPoder; 

    public Poderes(double x, double y, Entorno ent, int mouseXDestino, int mouseYDestino, String tipoPoder) {
        this.x = x;
        this.y = y;
        this.e = ent;
        this.tamanio = 0.5;
        this.velocidad = 5; // Velocidad de la bola de fuego o la poción de fuerza
        this.tipoPoder = tipoPoder;

        this.bolaImg = Herramientas.cargarImagen("imagenes/Explosion.png");
        this.pocionFuerzaImg = Herramientas.cargarImagen("imagenes/Pocion.png"); // Cargar imagen de la poción

        if (tipoPoder.equals("bola")) {
            if (this.bolaImg != null) {
                this.ancho = bolaImg.getWidth(null) * tamanio;
                this.alto = bolaImg.getHeight(null) * tamanio;
            } else { 
                this.ancho = 20 * tamanio; 
                this.alto = 20 * tamanio;  
            }
            // Calcular el ángulo desde la posición del mago hacia las coordenadas del mouse
            double deltaX = mouseXDestino - this.x;
            double deltaY = mouseYDestino - this.y;
            this.angulo = Math.atan2(deltaY, deltaX);
        } else if (tipoPoder.equals("fuerza")) { // Lógica para el nuevo poder "fuerza"
            if (this.pocionFuerzaImg != null) {
                this.ancho = pocionFuerzaImg.getWidth(null) * tamanio; // Ajustar tamaño de la poción
                this.alto = pocionFuerzaImg.getHeight(null) * tamanio;
            } else {
                this.ancho = 20 * tamanio; // Tamaño por defecto si la imagen no carga
                this.alto = 20 * tamanio;
            }
            // Calcular el ángulo para la poción de fuerza, igual que la bola
            double deltaX = mouseXDestino - this.x;
            double deltaY = mouseYDestino - this.y;
            this.angulo = Math.atan2(deltaY, deltaX);
        }
    }

    
    public void dibujar(Entorno ent) { 
        if (this.tipoPoder.equals("bola")) {
            if (bolaImg != null) {
                ent.dibujarImagen(bolaImg, x, y, 0, tamanio); 
            }
        } else if (this.tipoPoder.equals("fuerza")) { // Dibujar la poción de fuerza
            if (pocionFuerzaImg != null) {
                ent.dibujarImagen(pocionFuerzaImg, x, y, 0, tamanio); 
            }
        }
    }
    
    /**
     * Mueve el poder si es del tipo "bola" o "fuerza".
     */
    public void lanzarPoder() { 
        if (this.tipoPoder.equals("bola") || this.tipoPoder.equals("fuerza")) { // Ambos poderes se lanzan
            x += velocidad * Math.cos(angulo);
            y += velocidad * Math.sin(angulo);
        }
    }

    public boolean estaFueraDePantalla() { 
        return x < -ancho || x > e.ancho() + ancho || y < -alto || y > e.alto() + alto;
    }

    public boolean colisionaCon(Demonio demonio) { 
        if (demonio == null) return false; // Evitar NullPointerException
        // Detección de colisión AABB (Axis-Aligned Bounding Box)
        return this.getBordeDer() > demonio.getBordeIzq() &&
               this.getBordeIzq() < demonio.getBordeDer() &&
               this.getBordeInf() > demonio.getBordeSup() &&
               this.getBordeSup() < demonio.getBordeInf();
    }
    
    // Getters para los bordes
    public double getBordeIzq() { return x - ancho / 2; } 
    public double getBordeDer() { return x + ancho / 2; } 
    public double getBordeSup() { return y - alto / 2; } 
    public double getBordeInf() { return y + alto / 2; } 

    public double getX() { return x; } 
    
    public String getTipoPoder() { 
        return tipoPoder;
    }

    public void setX(double x) { this.x = x; } 
    public void setY(double y) { this.y = y; } 
}
