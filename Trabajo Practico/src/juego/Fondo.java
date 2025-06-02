package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Color; 

public class Fondo {
    Image Fondo;
    Entorno e;
    private Personaje mago; 
    Image imagenMenu; 
    private int demoniosRestantes;
    private int puntuacion;
    private String poderActivoNombre = null;
    private int usosPoderRestantes = 0;
    private Image imagenGanado;
    private Image imagenPerdido;

    public Fondo(String ruta, Entorno ent, Personaje mago) { 
        this.Fondo = entorno.Herramientas.cargarImagen("imagenes/Fondo.png");
        this.e = ent;
        this.mago = mago; 
        this.imagenMenu = entorno.Herramientas.cargarImagen("imagenes/FondoMenu.png"); 
        
        this.imagenGanado = Herramientas.cargarImagen("imagenes/FondoWin.png"); 
        this.imagenPerdido = Herramientas.cargarImagen("imagenes/FondoLose.png");
    }

    public void dibujar() {
        e.dibujarImagen(Fondo, e.ancho() / 2, e.alto() / 2, 0, (double) e.ancho() / Fondo.getWidth(null));
    }

    public void dibujarMenu() {
        // Definir las dimensiones y posición del rectángulo del menu
        int menuRectAncho = 200;
        int menuRectAlto = 560; 
        int menuRectX = menuRectAncho / 2 + 10;
        int menuRectY = menuRectAlto / 2 + 20; 
        e.dibujarImagen(imagenMenu, menuRectX, menuRectY, 0, 1.0);

        // --- Mostrar la vida del mago ---
        e.cambiarFont("Segoe UI Emoji", 16, Color.YELLOW); // Fuente para emojis
        e.escribirTexto("❤️ " + mago.getVida(), menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 40);
        
        // --- Mostrar la puntuación ---
        e.cambiarFont("Impact", 16, Color.WHITE); // Fuente para texto general
        e.escribirTexto("Puntos: " + puntuacion, menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 60);

        // --- Mostrar los poderes como botones ---
        e.cambiarFont("Segoe UI Emoji", 16, Color.WHITE); // Fuente para emojis
        e.escribirTexto("✨Poderes:", menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 90);
        
        // Texto "Selecciona un poder" movido antes de los botones
        e.cambiarFont("Impact", 14, Color.GRAY);
        e.escribirTexto("Selecciona un poder", menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 110);
        
        // Botón Bola de Fuego (movido a la derecha y ajustado Y)
        e.dibujarRectangulo(menuRectX - menuRectAncho / 2 + 75, menuRectY - menuRectAlto / 2 + 135, 100, 20, 0, Color.BLUE);
        e.cambiarFont("Impact", 14, Color.WHITE); // Fuente para texto general
        e.escribirTexto("Bola de Fuego", menuRectX - menuRectAncho / 2 + 30, menuRectY - menuRectAlto / 2 + 140);

        // Botón Fuerza (antes Escudo Mágico)
        e.dibujarRectangulo(menuRectX - menuRectAncho / 2 + 75, menuRectY - menuRectAlto / 2 + 165, 100, 20, 0, Color.BLUE);
        e.cambiarFont("Impact", 14, Color.WHITE); // Fuente para texto general
        e.escribirTexto("Fuerza", menuRectX - menuRectAncho / 2 + 30, menuRectY - menuRectAlto / 2 + 170); // Cambiado a "Fuerza"
        
        // Mostrar poder activo y usos restantes (Y ajustado)
        if (poderActivoNombre != null && usosPoderRestantes > 0) {
            e.cambiarFont("Impact", 14, Color.YELLOW); // Resaltar poder activo
            e.escribirTexto("Activo: " + (poderActivoNombre.equals("bola") ? "Bola de Fuego" : "Fuerza"), menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 195); // Cambiado a "Fuerza"
            e.escribirTexto("Usos: " + usosPoderRestantes, menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 215);
        }

        // Mostrar demonios restantes (Y ajustado)
        e.cambiarFont("Segoe UI Emoji", 16, Color.WHITE); // Fuente para emojis
        e.escribirTexto("👿 " + demoniosRestantes, menuRectX - menuRectAncho / 2 + 25, menuRectY - menuRectAlto / 2 + 245); 
    }

    public void setDemoniosRestantes(int demoniosRestantes) {
        this.demoniosRestantes = demoniosRestantes;
    }
    
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setPoderActivo(String poderActivoNombre, int usosPoderRestantes) {
        this.poderActivoNombre = poderActivoNombre;
        this.usosPoderRestantes = usosPoderRestantes;
    }

    public void actualizarDatos(int vidas, int demoniosMatados, int puntuacion, String poderActivo, int usosPoderRestantes) {
        this.demoniosRestantes = demoniosMatados; 
        this.puntuacion = puntuacion;
        this.poderActivoNombre = poderActivo;
        this.usosPoderRestantes = usosPoderRestantes;
    }
    
    public void dibujarPantallaFinJuego(boolean ganado) {
        if (ganado && imagenGanado != null) {
            e.dibujarImagen(imagenGanado, e.ancho() / 2, e.alto() / 2, 0, 1.0);
        } else if (!ganado && imagenPerdido != null) {
            e.dibujarImagen(imagenPerdido, e.ancho() / 2, e.alto() / 2, 0, 1.0);
        }

        // Mostrar el mensaje de texto superpuesto
        e.cambiarFont("Impact", 40, ganado ? Color.GREEN : Color.RED);
        String mensaje = ganado ? "¡GANASTE!" : "GAME OVER!";
        e.escribirTexto(mensaje, e.ancho() / 2 - 100, e.alto() / 2);
     // Mostrar el mensaje secundario: "Para volver a jugar presiona r"
        String mensajeReinicio = "Para volver a jugar presiona r";
        e.cambiarFont("Impact", 20, Color.WHITE); // Un poco más pequeño y en blanco
        // Posicionar debajo del mensaje principal, ajustando la coordenada Y
        e.escribirTexto(mensajeReinicio, e.ancho() / 2 - 150, e.alto() / 2 + 50);  
        
        
    }
}

