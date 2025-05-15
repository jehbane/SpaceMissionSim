
public class Main 
{
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
		// TODO: some example code
	}
}
