import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;;

public class OceanoPantalla extends JPanel implements MouseListener, PropertyChangeListener{
    protected Oceano oceano;
    protected Timer timer;

    public OceanoPantalla() {
        this.setBackground(new Color(150, 255, 255));
        this.addMouseListener(this);
    }
    public void Ejecutar() {
        oceano = new Oceano(250, this.getWidth(), getHeight());
        oceano.agregarChangeListener(this);
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                oceano.ActualizarOceano();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(tarea, 0, 15);
    }
    protected void DibujarPez(Pez p, Graphics g) {
        g.drawLine((int) p.posX, (int) p.posY, (int) (p.posX - 10 * p.velocidadX), (int) (p.posY - 10 * p.velocidadY));
    }

    protected void DibujarObstaculo(ZonaAEvitar o, Graphics g) {
        g.drawOval((int) (o.posX - o.radio), (int) (o.posY - o.radio), (int) o.radio * 2, (int) o.radio * 2);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Pez p : oceano.peces) {
            DibujarPez(p, g);
        }
        for (ZonaAEvitar o : oceano.obstaculos) {
            DibujarObstaculo(o, g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        oceano.AgregarObstaculo(e.getX(), e.getY(), 10);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
