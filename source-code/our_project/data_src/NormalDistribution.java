package data;

public class NormalDistribution
{
	public final float mu, sigma;

	/**
	 * Models a list of values as a normal distribution.
	 * @param values An array of numerical values.
	 */
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
	
	/**
	 * Calculates the KL divergence from this normal distribution to another one.
	 * Inspired from http://www.allisons.org/ll/MML/KL/Normal/
	 * @param other The normal distribution to compare this distribution with.
	 * @return The Kullback-Leibler distance from this normal distribution to another one.
	 */
	public float KLDivergence(NormalDistribution other)
	{
		return (float) ((Math.pow(this.mu - other.mu, 2) + Math.pow(this.sigma, 2) - Math.pow(other.sigma, 2)) / (2 * Math.pow(other.sigma, 2)) + Math.log(other.sigma / this.sigma));
	}
}
