package vehicle;

public class VehicleInfo 
{
	public final String name;

	public final double fuelMass;
	public final double vehicleMass;
	public final double totalMass;
	
	public VehicleInfo(String name, double fuelMass, double vehicleMass)
	{
		this.name = name;
		this.fuelMass = fuelMass;
		this.vehicleMass = vehicleMass;

		totalMass = fuelMass + vehicleMass;
	}

	public String getVehicleName()
	{
		return name;
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
