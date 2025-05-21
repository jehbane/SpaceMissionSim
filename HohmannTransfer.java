package orbit;

public class HohmannTransfer 
{
	public static final double G = 6.67e-11;
	
	public static final double EARTH_GRAVITY = 9.81; // gravity measured in m/s^2
	public static final double EARTH_GRAVITATIONAL_PARAM = 3.986e5; // measured in km^3/s^2
	public static final double EARTH_MASS = 0;
	public static final double EARTH_RADIUS = 6371; // measured in km
	public static final double EARTH_ORBITAL_RADIUS = 1.496e8;
	
	public static final double MARS_GRAVITY = 3.73;
	public static final double MARS_GRAVITATIONAL_PARAM = 4.282e4;
	public static final double MARS_MASS = 0;
	public static final double MARS_RADIUS = 3390; // measured in km
	public static final double MARS_ORBITAL_RADIUS = 2.279e8;
	
	public static final double SUN_GRAVITATIONAL_PARAM = 1.327e11;
	
	public final double lowEarthOrbit;
	public final double lowMarsOrbit;
	
	public HohmannTransfer(int leo, int lmo)
	{
		this.lowEarthOrbit = leo;
		this.lowMarsOrbit = lmo;
	}
	
	public double getEscapeDeltaV()
	{
		double orbitalVelocity = Math.sqrt(EARTH_GRAVITATIONAL_PARAM / (EARTH_RADIUS + lowEarthOrbit)); // vehicle's orbital velocity around Earth
		double earthOrbitalVelocity = Math.sqrt(SUN_GRAVITATIONAL_PARAM / EARTH_ORBITAL_RADIUS); // Earth's orbital velocity around the Sun
		double semiMajorAxis = (EARTH_ORBITAL_RADIUS + MARS_ORBITAL_RADIUS) / 2;
		double perihelionVelocity = Math.sqrt((SUN_GRAVITATIONAL_PARAM) * ((2 / EARTH_ORBITAL_RADIUS) - (1 / semiMajorAxis)));
		double hyperbolicSpeed = perihelionVelocity - earthOrbitalVelocity;
		double escapeVelocity = Math.sqrt(orbitalVelocity * orbitalVelocity + hyperbolicSpeed * hyperbolicSpeed);
		double deltaV = escapeVelocity - orbitalVelocity;
		return deltaV * 1000D; // convert to SI units
	}
	
	public double getInsertionDeltaV()
	{
		double marsOrbitalVelocity = Math.sqrt(SUN_GRAVITATIONAL_PARAM / MARS_ORBITAL_RADIUS);
		double semiMajorAxis = (EARTH_ORBITAL_RADIUS + MARS_ORBITAL_RADIUS) / 2;
		double arrivalVelocity = Math.sqrt((SUN_GRAVITATIONAL_PARAM) * ((2 / MARS_ORBITAL_RADIUS) - (1 / semiMajorAxis)));
		double hyperbolicSpeed = Math.abs(arrivalVelocity - marsOrbitalVelocity);
		double orbitalVelocity = Math.sqrt(MARS_GRAVITATIONAL_PARAM / (MARS_RADIUS + lowMarsOrbit));
		double periapsisVelocity = Math.sqrt((hyperbolicSpeed * hyperbolicSpeed) + ((2 * MARS_GRAVITATIONAL_PARAM) / (MARS_RADIUS + lowMarsOrbit)));
		double deltaV = periapsisVelocity - orbitalVelocity;
		return deltaV * 1000D;
	}

	// gravity assists
	// Oberth effect
	// slingshots

}
