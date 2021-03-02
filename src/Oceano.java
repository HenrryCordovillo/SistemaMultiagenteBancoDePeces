import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

public class Oceano {
    // Atributos
    protected Pez[] peces;
    protected ArrayList<ZonaAEvitar> obstaculos;
    protected Random generador;
    protected double ancho;
    protected double alto;
    private PropertyChangeSupport support;
    private int contador;

    // Métodos
    public void agregarChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void eliminarPropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public Oceano(int _numPeces, double _ancho, double _alto) {
        support = new PropertyChangeSupport(this);
        contador = 0;
        ancho = _ancho;
        alto = _alto;
        generador = new Random();
        obstaculos = new ArrayList();
        peces = new Pez[_numPeces];
        for (int i = 0; i < _numPeces; i++) {
            peces[i] = new Pez(generador.nextDouble() * ancho, generador.nextDouble() * alto, generador.nextDouble() * 2 * Math.PI);
        }
    }

    public void AgregarObstaculo(double _posX, double _posY, double radio) {
        obstaculos.add(new ZonaAEvitar(_posX, _posY, radio));
    }

    protected void ActualizarObstaculos() {
        for (ZonaAEvitar obstaculo : obstaculos) {
            obstaculo.Actualizar();
        }
        obstaculos.removeIf(o -> o.estaMuerto());
    }

    protected void ActualizarPeces() {
        for (Pez p : peces) {
            p.Actualizar(peces, obstaculos, ancho, alto);
        }
    }

    public void ActualizarOceano() {
        ActualizarObstaculos();
        ActualizarPeces();
        support.firePropertyChange("changed", this.contador,
                this.contador+1);
        this.contador++;
    }


}

