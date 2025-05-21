package gui;
import javax.swing.*;
import orbit.HohmannTransfer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import simMain.Main;
public class GUIRendering extends JPanel {
    private final Timer timer;
    private double xPos, yPos, xVel, yVel, fuel;
    private double speed = 1.0, zoom = 1.0;
    private int panX = 0, panY = 0;
    private Point lastDrag;
    private final LinkedList<Point> trail = new LinkedList<>();
    private final DebugPanel debugOpts;
    public GUIRendering(DebugPanel debugOpts) {
        this.debugOpts = debugOpts;
        setBackground(Color.BLACK);
        timer = new Timer(25, e -> repaint());
        addMouseWheelListener(e -> {
            zoom *= (e.getWheelRotation() < 0) ? 1.1 : 0.9;
            repaint();
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastDrag = e.getPoint();
                if (SwingUtilities.isRightMouseButton(e)) {
                    panX = panY = 0; zoom = 1.0;
                    repaint();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                panX += p.x - lastDrag.x;
                panY += p.y - lastDrag.y;
                lastDrag = p;
                repaint();
            }
        });
    }
    public void startSimulation() { if (!timer.isRunning()) timer.start(); }
    public void pauseSimulation() { if (timer.isRunning()) timer.stop(); }
    public void resetSimulation() {
        pauseSimulation();
        xPos = yPos = xVel = yVel = fuel = 0;
        trail.clear();
        zoom = 1.0; panX = panY = 0;
        repaint();
    }
    public void setSpeedFactor(int val) { speed = val / 100.0; }
    public void updateState(double x, double y, double vx, double vy, double f) 
    {
        xPos = x; yPos = y; xVel = vx; yVel = vy; fuel = f;
        if (debugOpts.showTrail.isSelected()) 
        {
            trail.add(new Point((int)x, (int)y));
            if (trail.size() > 300) trail.removeFirst();
        }
    }
    @Override
    protected void paintComponent(Graphics g0) 
    {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        int w = getWidth(), h = getHeight();
        g.setTransform(new AffineTransform());
        g.setColor(getBackground());
        g.fillRect(0, 0, w, h);
        double planetRadius   = Main.useMars ? Main.MARS_RADIUS : Main.EARTH_RADIUS;
        Color  planetColor    = Main.useMars ? new Color(210, 90,  60)  // Mars‐like
                                             : new Color( 80, 130, 200); // Earth‐like
        double worldCamRadius = planetRadius * 5;   // show ±5×R on screen
        double scale = (Math.min(w, h) * 0.8 / 2) / worldCamRadius; // 80% of half-screen
        AffineTransform at = new AffineTransform();
        at.translate(w/2 + panX, h/2 + panY);
        at.scale(zoom * scale, zoom * scale);
        g.setTransform(at);
        int pr = (int)(planetRadius);
        g.setColor(planetColor);
        g.fillOval(-pr, -pr, pr*2, pr*2);
        g.setColor(planetColor.darker());
        g.setStroke(new BasicStroke(1f / (float)(zoom*scale)));
        g.drawOval(-pr, -pr, pr*2, pr*2);
        if (debugOpts.showTrail.isSelected()) 
        {
            g.setColor(Color.GRAY);
            Point prev = null;
            for (Point p : trail) 
            {
                if (prev != null) 
                {
                    g.drawLine(prev.x, prev.y, p.x, p.y);
                }   
                prev = p;
            }
        }
        double vx = xVel, vy = yVel;
        double vmag = Math.hypot(vx, vy);
        double heading = Math.atan2(vy, vx);
        Polygon ship = new Polygon();
        ship.addPoint( +10,   0);
        ship.addPoint( -6,  +6);
        ship.addPoint( -6,  -6);
        AffineTransform shipXform = new AffineTransform();
        shipXform.translate(xPos, yPos);
        shipXform.rotate(heading);
        shipXform.scale(1.0/scale/zoom, 1.0/scale/zoom); // keep ship size constant
        Shape shipShape = shipXform.createTransformedShape(ship);
        g.setColor(Color.WHITE);
        g.fill(shipShape);
        g.setColor(Color.BLACK);
        g.draw(shipShape);
        g.setTransform(new AffineTransform());
        g.setColor(Color.GREEN);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        int line = 15;
        g.drawString(String.format("Planet: %s", Main.useMars ? "Mars" : "Earth"), 10, line);
        g.drawString(String.format("Pos: (%.0e, %.0e)m", xPos, yPos), 10, line+=15);
        g.drawString(String.format("Vel: (%.1f, %.1f)m/s", xVel, yVel), 10, line+=15);
        g.drawString(String.format("Fuel: %.1f kg", fuel), 10, line+=15);
        g.drawString(String.format("Zoom: %.2f×", zoom), 10, line+=15);
        if (debugOpts.showHud.isSelected()) 
        {
            g.drawString(String.format("Step count: %d", trail.size()), 10, line+=15);
            g.drawString(String.format("Scale: %.2e m/px", 1.0/(scale*zoom)), 10, line+=15);
        }
        HohmannTransfer transfer = new HohmannTransfer(300, 250);
        g.drawString(String.format("Escape Delta-V: %s", transfer.getEscapeDeltaV()), 10, line += 15);
        g.drawString(String.format("Insertion Delta-V: %s", transfer.getInsertionDeltaV()), 10, line += 15);
    }
}
