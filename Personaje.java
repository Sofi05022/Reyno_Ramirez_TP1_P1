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
    int vidas; 
    String[] poderes;
    boolean ultimaDireccion;

    public Personaje(double x, double y, Entorno ent) {
        this.x = x;
        this.y = y;
        this.escala = 0.16;
        this.velocidad = 3.0;

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
        this.vidas = 100; // Vida inicial del mago
        this.poderes = new String[]{"🔥Bola de Fuego", "🛡️Escudo Mágico", "🌀Teletransporte"}; // Poderes adquiridos 

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
    public boolean getUltimaDireccion() {
    	return ultimaDireccion;
    }
    // Los movimientos del mago y su actualizacion segun su movimiento
    public void moverDerecha() {
        this.x += this.velocidad;
        ultimaDireccion = false;
        if (getBordeDer() > this.e.ancho()) {
            this.x = this.e.ancho() - (this.ancho / 2);
        }
        this.inicioImage = this.imagenDer; 
    }
 
    public void moverIzquierda() {
        this.x -= this.velocidad;
        ultimaDireccion = true;
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
  
    public String[] getPoderes() {
        return poderes;
    }
    // Metodos de vidas
    public void perderVida() {
    	this.vidas--;
    }
	public int getVidas() {
		// TODO Auto-generated method stub
		return this.vidas;
	}

	public void setVidas(int cantidad) {
		this.vidas=cantidad;
		// TODO Auto-generated method stub
		
	}

}
