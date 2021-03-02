import javax.swing.*;

public class Aplicacion {
    public static void main(String[] args) {
        // Creación de la ventana
        JFrame ventana = new JFrame();
        ventana.setTitle("Banco de peces");
        ventana.setSize(1000, 800);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        // Creación del contenido
        OceanoPantalla panel = new OceanoPantalla();
        ventana.setContentPane(panel);
        // Visualización
        ventana.setVisible(true);
        panel.Ejecutar();
    }
}
