import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public class Main extends Canvas
{
	public static final long serialVersionUID = 1L;
	
	public static final double G = 6.67e-11;
	public static final double EARTH_GRAVITY = 9.81; // gravity measured in m/s^2
	public static final double MARS_GRAVITY = 3.73;
	public static final double LOW_EARTH_ORBIT = 400; // altitude measured in km
	public static final double LOW_MARS_ORBIT = 200;
	
	public static double xPos;
	public static double yPos;
	public static double zPos;
	public static double xVelocity;
	public static double yVelocity;
	public static double zVelocity;
	public static double xAcceleration;
	public static double yAcceleration;
	public static double zAcceleration;
	public static double xJerk;
	public static double yJerk;
	public static double zJerk;
	
	public static double yaw;
	public static double pitch;
	public static double roll;
	public static double bank;
	
	public static double deltaV;
	public static double deltaT;
	public static double exhaustVelo;
	public static double vehicleMass;
	public static double fuelMass;
	public static double specificImpulse;
	
	public static boolean enabled = true;
	public static boolean debugging = false;
	
	public static void main(String[] args)
	{
		Main main = new Main();
		main.setMinimumSize(new Dimension(0, 0));
		main.setMaximumSize(new Dimension(1000, 1000));
		main.setPreferredSize(new Dimension(480, 360));
		
		JFrame frame = new JFrame("Space Sim");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(main, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Graphics g = main.getGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(50, 50, main.getWidth(), main.getHeight());
	}
}
