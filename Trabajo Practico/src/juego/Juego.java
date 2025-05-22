package juego;

import entorno.Entorno;
import java.awt.Color;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	private Entorno entorno;
	private Fondo fondo;
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		// Inicializar lo que haga falta para el juego
		// ...
		
		
		// Inicia el juego!
		this.entorno.iniciar();
	}
	
	public void tick() {
		// Procesamiento de un instante de tiempo
		// ...
		fondo.dibujar();
		
		}
	
	
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
	
}
