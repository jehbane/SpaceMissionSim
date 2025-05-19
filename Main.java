import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
public class Main 
{
    public static final double G = 6.67e-11;
    public static final double EARTH_GRAVITY = 9.81;
    public static final double MARS_GRAVITY = 3.73;
    public static final double LOW_EARTH_ORBIT = 400;
    public static final double LOW_MARS_ORBIT = 200;
    public static double xPos, yPos, zPos;
    public static double xVelocity, yVelocity, zVelocity;
    public static double xAcceleration, yAcceleration, zAcceleration;
    public static double xJerk, yJerk, zJerk;
    public static double yaw, pitch, roll, bank;
    public static double deltaV, deltaT, exhaustVelo;
    public static double vehicleMass, fuelMass, specificImpulse;
    public static boolean enabled = true;
    public static boolean debugging = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Space Sim");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(800, 600));
            render.GUIRendering renderPanel = new render.GUIRendering();
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            JButton playBtn = new JButton("Play");
            JButton pauseBtn = new JButton("Pause");
            JButton resetBtn = new JButton("Reset");
            JSlider speedSlider = new JSlider(1, 100, 50);
            speedSlider.setMajorTickSpacing(25);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintLabels(true);
            toolBar.add(playBtn);
            toolBar.add(pauseBtn);
            toolBar.add(resetBtn);
            toolBar.addSeparator();
            toolBar.add(speedSlider);
            playBtn.addActionListener(e -> renderPanel.startSimulation());
            pauseBtn.addActionListener(e -> renderPanel.pauseSimulation());
            resetBtn.addActionListener(e -> renderPanel.resetSimulation());
            speedSlider.addChangeListener(e -> renderPanel.setSpeed(speedSlider.getValue()));
            frame.setLayout(new BorderLayout());
            frame.add(toolBar, BorderLayout.NORTH);
            frame.add(renderPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            renderPanel.init();
        });
    }
}
