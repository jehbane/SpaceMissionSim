package simMain;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.*;
import javax.swing.*;
import gui.ConfigPanel;
import gui.DebugPanel;
import gui.GUIRendering;
public class Main {
    public static final double G            = 6.67430e-11;
    public static final double EARTH_RADIUS = 6.371e6;
    public static final double MARS_RADIUS  = 3.3895e6;
    public static final double g0           = 9.80665;
    public static double xPos = 0, yPos = 0;
    public static double xVel = 0, yVel = 0;
    public static double vehicleMass = 1e4, fuelMass = 5e3, isp = 300;
    public static boolean useMars = false;
    private static ScheduledExecutorService executor;
    private static final Logger LOG = Logger.getLogger("SpaceSim");
    public static void main(String[] args) throws IOException {
        Handler fh = new FileHandler("spacesim.log", true);
        fh.setFormatter(new SimpleFormatter());
        LOG.addHandler(fh);
        LOG.setLevel(Level.INFO);
        for (String arg : args) {
            if (arg.startsWith("--dry="))  vehicleMass = Double.parseDouble(arg.substring(6));
            if (arg.startsWith("--fuel=")) fuelMass    = Double.parseDouble(arg.substring(7));
            if (arg.startsWith("--isp="))  isp         = Double.parseDouble(arg.substring(6));
            if (arg.equals("--mars"))      useMars     = true;
        }
        LOG.info(String.format("Start: dry=%.0fkg, fuel=%.0fkg, isp=%.0fs, planet=%s",
                vehicleMass, fuelMass, isp, useMars ? "Mars" : "Earth"));
        SwingUtilities.invokeLater(Main::buildAndShowGui);
    }
    private static void buildAndShowGui() {
        JFrame frame = new JFrame("Interplanetary Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        ConfigPanel configPanel = new ConfigPanel();
        DebugPanel debugPanel   = new DebugPanel();
        gui.GUIRendering renderPanel = new gui.GUIRendering(debugPanel);
        JToolBar tool = new JToolBar();
        JButton btnPlay  = new JButton("▶");
        JButton btnPause = new JButton("❚❚");
        JButton btnReset = new JButton("⟲");
        tool.add(btnPlay);
        tool.add(btnPause);
        tool.add(btnReset);
        tool.addSeparator();
        JLabel lblSpeed = new JLabel("Speed:");
        JSlider speed   = new JSlider(1, 500, 100);
        speed.setMajorTickSpacing(99);
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);
        tool.add(lblSpeed);
        tool.add(speed);
        JPanel left = new JPanel(new BorderLayout());
        left.add(configPanel, BorderLayout.NORTH);
        left.add(debugPanel,   BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, renderPanel);
        split.setDividerLocation(260);
        frame.add(tool, BorderLayout.NORTH);
        frame.add(split, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Runnable simStep = () -> {
            double dt      = 0.1 * (speed.getValue() / 100.0);
            double radius  = useMars ? MARS_RADIUS : EARTH_RADIUS;
            double mu      = G * (useMars ? 6.4171e23 : 5.972e24);
            double r       = Math.hypot(xPos, yPos) + radius;
            double thrust  = isp * g0;
            if (fuelMass > 0) {
                double accel = thrust / (vehicleMass + fuelMass);
                double ax    = -xPos / r * accel;
                double ay    = -yPos / r * accel;
                xVel += ax * dt;
                yVel += ay * dt;
                fuelMass -= accel * 0.1 * dt;
                if (fuelMass < 0) fuelMass = 0;
            }
            xPos += xVel * dt;
            yPos += yVel * dt;
            renderPanel.updateState(xPos, yPos, xVel, yVel, fuelMass);
            if (debugPanel.logSteps.isSelected()) {
                LOG.fine(String.format("dt=%.3f, pos=(%.1f,%.1f), vel=(%.2f,%.2f), fuel=%.1f",
                        dt, xPos, yPos, xVel, yVel, fuelMass));
            }
        };
        btnPlay.addActionListener(e -> {
            if (executor == null || executor.isShutdown()) {
                executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(simStep, 0, 100, TimeUnit.MILLISECONDS);
            }
            renderPanel.startSimulation();
        });
        btnPause.addActionListener(e -> {
            if (executor != null) executor.shutdown();
            renderPanel.pauseSimulation();
        });
        btnReset.addActionListener(e -> {
            if (executor != null) executor.shutdown();
            double altitude = configPanel.useMars()
                                ? 200e3    // LMO
                                : 400e3;   // LEO
            xPos = (configPanel.useMars() ? Main.MARS_RADIUS : Main.EARTH_RADIUS) + altitude;
            yPos = 0;
            double mu = Main.G * (configPanel.useMars() ? 6.4171e23 : 5.972e24);
            double vCirc = Math.sqrt(mu / xPos);
            xVel = 0;
            yVel = vCirc;
            fuelMass = configPanel.getFuelMass();
            isp      = configPanel.getIsp();
            useMars  = configPanel.useMars();
            renderPanel.resetSimulation();
            renderPanel.updateState(xPos, yPos, xVel, yVel, fuelMass);
            renderPanel.startSimulation();
        });
        speed.addChangeListener(e -> renderPanel.setSpeedFactor(speed.getValue()));
    }
}
