import java.util.ArrayList;

public class Pez extends Objeto {
    // Constantes
    public static final double PASO = 3;
    public static final double DISTANCIA_MIN = 5;
    public static final double DISTANCIA_MIN_CUADRADO = 25;
    public static final double DISTANCIA_MAX = 40;
    public static final double DISTANCIA_MAX_CUADRADO = 1600;
    // Atributos
    protected double velocidadX;
    protected double velocidadY;

    // Métodos
    public Pez(double _x, double _y, double _dir) {
        posX = _x;
        posY = _y;
        velocidadX = Math.cos(_dir);
        velocidadY = Math.sin(_dir);
    }

    public double getVelocidadX() {
        return velocidadX;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    protected void ActualizarPosicion() {
        posX += PASO * velocidadX;
        posY += PASO * velocidadY;
    }

    protected boolean EnAlineacion(Pez p) {
        double distanciaCuadrado = DistanciaCuadrado(p);
        return (distanciaCuadrado < DISTANCIA_MAX_CUADRADO &&
                distanciaCuadrado > DISTANCIA_MIN_CUADRADO);
    }

    protected double DistanciaAlMuro(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        double min = Math.min(posX - muroXMin, posY - muroYMin);
        min = Math.min(min, muroXMax - posX);
        min = Math.min(min, muroYMax - posY);
        return min;
    }

    protected void Normalizar() {
        double longitud = Math.sqrt(velocidadX * velocidadX +
                velocidadY * velocidadY);
        velocidadX /= longitud;
        velocidadY /= longitud;
    }

    //////////////////////////////////////////////////Comportamiento 1//////////////////////////////////////////////////
    protected boolean EvitarMuros(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        PararEnMuro(muroXMin, muroYMin, muroXMax, muroYMax);
        double distancia = DistanciaAlMuro(muroXMin, muroYMin, muroXMax, muroYMax);
        if (distancia < DISTANCIA_MIN) {
            CambiarDireccionMuro(distancia, muroXMin, muroYMin, muroXMax, muroYMax);
            Normalizar();
            return true;
        }
        return false;
    }

    private void PararEnMuro(double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        if (posX < muroXMin) {
            posX = muroXMin;
        } else if (posY < muroYMin) {
            posY = muroYMin;
        } else if (posX > muroXMax) {
            posX = muroXMax;
        } else if (posY > muroYMax) {
            posY = muroYMax;
        }
    }

    private void CambiarDireccionMuro(double distancia, double muroXMin, double muroYMin, double muroXMax, double muroYMax) {
        if (distancia == (posX - muroXMin)) {
            velocidadX += 0.3;
        } else if (distancia == (posY - muroYMin)) {
            velocidadY += 0.3;
        } else if (distancia == (muroXMax - posX)) {
            velocidadX -= 0.3;
        } else if (distancia == (muroYMax - posY)) {
            velocidadY -= 0.3;
        }
    }

    ////////////////////////////////////////Comportamiento 2////////////////////////////////////////////////////////////
    protected boolean EvitarObstaculos(ArrayList<ZonaAEvitar> obstaculos) {
        if (!obstaculos.isEmpty()) {
            // Búsqueda del obstáculo más cercano
            ZonaAEvitar obstaculoProximo = obstaculos.get(0);
            double distanciaCuadrado = DistanciaCuadrado(obstaculoProximo);
            for (ZonaAEvitar o : obstaculos) {
                if (DistanciaCuadrado(o) < distanciaCuadrado) {
                    obstaculoProximo = o;
                    distanciaCuadrado = DistanciaCuadrado(o);
                }
            }
            if (distanciaCuadrado < (4 * obstaculoProximo.radio *
                    obstaculoProximo.radio)) {
                // Si colisiona, se calcula el vector diff
                double distancia = Math.sqrt(distanciaCuadrado);
                double diffX = (obstaculoProximo.posX - posX) / distancia;
                double diffY = (obstaculoProximo.posY - posY) / distancia;
                velocidadX = velocidadX - diffX / 2;
                velocidadY = velocidadY - diffY / 2;
                Normalizar();
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////Comportamiento 3////////////////////////////////////////////////////////////
    protected boolean EvitarPeces(Pez[] peces) {
        // Búsqueda del pez más cercano
        Pez p;
        if (!peces[0].equals(this)) {
            p = peces[0];
        } else {
            p = peces[1];
        }
        double distanciaCuadrado = DistanciaCuadrado(p);
        for (Pez pez : peces) {
            if (DistanciaCuadrado(pez) < distanciaCuadrado && !pez.equals(this)) {
                p = pez;
                distanciaCuadrado = DistanciaCuadrado(p);
            }
        }
        // Evitar
        if (distanciaCuadrado < DISTANCIA_MIN_CUADRADO) {
            double distancia = Math.sqrt(distanciaCuadrado);
            double diffX = (p.posX - posX) / distancia;
            double diffY = (p.posY - posY) / distancia;
            velocidadX = velocidadX - diffX / 4;
            velocidadY = velocidadY - diffY / 4;
            Normalizar();
            return true;
        }
        return false;
    }

    ////////////////////////////////////////Comportamiento 4////////////////////////////////////////////////////////////
    protected void CalcularDireccionMedia(Pez[] peces) {
        double velocidadXTotal = 0;
        double velocidadYTotal = 0;
        int numTotal = 0;
        for (Pez p : peces) {
            if (EnAlineacion(p)) {
                velocidadXTotal += p.velocidadX;
                velocidadYTotal += p.velocidadY;
                numTotal++;
            }
        }
        if (numTotal >= 1) {
            velocidadX = (velocidadXTotal / numTotal + velocidadX) / 2;
            velocidadY = (velocidadYTotal / numTotal + velocidadY) / 2;
            Normalizar();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void Actualizar(Pez[] peces, ArrayList<ZonaAEvitar> obstaculos, double ancho, double alto) {
        if (!EvitarMuros(0, 0, ancho, alto)) {
            if (!EvitarObstaculos(obstaculos)) {
                if (!EvitarPeces(peces)) {
                    CalcularDireccionMedia(peces);
                }
            }
        }
        ActualizarPosicion();
    }


}



