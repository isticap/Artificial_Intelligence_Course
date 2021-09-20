public class bInventory
{
	private double tstep;
	private int x;
	private int y;
	
	public bInventory(double tstep, int x, int y)
    {
        this.tstep = tstep;
        this.x = x;
        this.y = y;
    }
	
	public double getTstep()
	{
		return tstep;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
    {
        return y;
    }
	
	public String toString()
    {
        return "timestep: " + tstep + "\n" + "bxAxis: " + x + "\n"
                    + "byAxis: " + y;
    }
}