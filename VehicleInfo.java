package vehicle;

public class VehicleInfo 
{
	public final double fuelMass;
	public final double vehicleMass;

	public double totalMass;
	
	public VehicleInfo(double fuelMass, double vehicleMass)
	{
		this.fuelMass = fuelMass;
		this.vehicleMass = vehicleMass;

		totalMass = fuelMass + vehicleMass;
	}

	public double getFuelMass()
	{
		return fuelMass;
	}

	public double getVehicleMass()
	{
		return vehicleMass;
	}

	public double getTotalMass()
	{
		return totalMass;
	}
}
