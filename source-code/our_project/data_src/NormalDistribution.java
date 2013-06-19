package data;

public class NormalDistribution
{
	public final float mu, sigma;

	public NormalDistribution(int[] values)
	{
		long sum = 0;
		
		for (int value: values)
		{
			sum += value;
		}
		
		mu = (float) sum / values.length;
		
		float diff = 0;
		
		for (int value: values)
		{
			diff += Math.pow(value - mu, 2);
		}
		
		sigma = (float) Math.sqrt(diff / values.length);
	}
}
