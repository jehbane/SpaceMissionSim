package render;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
public class GUIRendering extends JPanel {
    private Timer timer;
    private double time;
    private double speed = 1.0;
    public GUIRendering() {
        setBackground(Color.BLACK);
        timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time += 0.025 * speed;
                repaint();
            }
        });
    }

    //Initialize simulation data here
    public void init() {
        time = 0;
    }
    public void startSimulation() {
        if (!timer.isRunning()) timer.start();
    }
    public void pauseSimulation() {
        if (timer.isRunning()) timer.stop();
    }
    public void resetSimulation() {
        pauseSimulation();
        time = 0;
        repaint();
    }
    public void setSpeed(double value) {
        this.speed = value / 50.0; // normalize slider
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        // Draw central body (e.g., Earth)
        int radius = Math.min(w, h) / 10;
        g.setColor(Color.BLUE);
        g.fillOval(w/2 - radius, h/2 - radius, radius*2, radius*2);
        double orbitRadius = Math.min(w, h) / 3.0;
        double angle = time;
        int x = (int)(w/2 + orbitRadius * Math.cos(angle));
        int y = (int)(h/2 + orbitRadius * Math.sin(angle));
        g.setColor(Color.WHITE);
        g.fillOval(x - 5, y - 5, 10, 10);
    }
}
