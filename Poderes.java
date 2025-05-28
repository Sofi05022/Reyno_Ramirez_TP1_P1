package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Poderes {
    double x, y;
    double tamanio;
    double velocidad;
    double ancho, alto;
    boolean direccion;
    Image bolaIzq;
    Image bolaDer;
    Entorno e;

    public Poderes(double x, double y, Entorno ent, boolean direccion) {
        this.x = x;
        this.y = y;
        this.e = ent;
        this.tamanio = 0.5;
        this.velocidad = 5;
        this.direccion = direccion;

        // Usa una imagen distinta para proyectil, no la explosión
        this.bolaDer = Herramientas.cargarImagen("imagenes/explosion-png.png");
        this.bolaIzq = Herramientas.cargarImagen("imagenes/explosion-png.png");

        this.ancho = bolaDer.getWidth(null) * tamanio;
        this.alto = bolaDer.getHeight(null) * tamanio;
    }

    public void dibujarBola(Entorno e) {
        if (direccion) {
            e.dibujarImagen(bolaIzq, x, y, 0, tamanio);
        } else {
            e.dibujarImagen(bolaDer, x, y, 0, tamanio);
        }
    }

    public void lanzarBola() {
        if (direccion) {
            x -= velocidad;
        } else {
            x += velocidad;
        }
    }

    public boolean estaFueraDePantalla() {
        return x < 0 || x > e.ancho();
    }

    public boolean colisionaCon(Demonio demonio) {
        return this.getBordeDer() > demonio.getBordeIzq() &&
               this.getBordeIzq() < demonio.getBordeDer() &&
               this.getBordeInf() > demonio.getBordeSup() &&
               this.getBordeSup() < demonio.getBordeInf();
    }

    public double getBordeIzq() {
        return x - ancho / 2;
    }

    public double getBordeDer() {
        return x + ancho / 2;
    }

    public double getBordeSup() {
        return y - alto / 2;
    }

    public double getBordeInf() {
        return y + alto / 2;
    }

    public double getX() {
        return x;
    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
