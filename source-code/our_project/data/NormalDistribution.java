package data;

import java.util.*;
import org.apache.commons.math3.special.*;

public class NormalDistribution
{
	private final static double EQ_THRESHOLD = 0.001;
	
	public final float mu, sigma;

	/**
	 * Models a list of values as a normal distribution.
	 * @param values A list of numerical values.
	 */
	public NormalDistribution(List<Integer> values)
	{
		long sum = 0;
		
		for (int value: values)
		{
			sum += value;
		}
		
		mu = (float) sum / values.size();
		
		float diff = 0;
		
		for (int value: values)
		{
			diff += Math.pow(value - mu, 2);
		}
		
		sigma = (float) Math.sqrt(diff / values.size());
	}
	
	public NormalDistribution(float mu, float sigma)
	{
		this.mu    = mu;
		this.sigma = sigma;
	}
	
	/**
	 * Calculates the Kullback-Leibler divergence from this normal distribution to another one.
	 * Inspired from http://stats.stackexchange.com/questions/7440/kl-divergence-between-two-univariate-gaussians
	 * @param other The normal distribution to compare this distribution with.
	 * @return The KL distance from this normal distribution to another one.
	 */
	public float KLDivergence(NormalDistribution other)
	{
		return (float) ((Math.pow(this.mu - other.mu, 2) + Math.pow(this.sigma, 2)) / (2 * Math.pow(other.sigma, 2)) + Math.log(other.sigma / this.sigma) - 0.5);
	}
	
	/**
	 * Calculates the level of overlap between this normal distribution and another one.
	 * @param other Another normal distribution.
	 * @return A value in the range [0, 1] indicating the level of overlap between both distributions.
	 */
	public float overlapLevel(NormalDistribution other)
	{
		// Points where both distributions yield the same value:
		
		List<Double> m = new ArrayList<Double>(); 
		
		m.add(Double.NEGATIVE_INFINITY);
		
		// Calculation of the set of overlapping points:
		
		double a = Math.pow(other.sigma, 2) - Math.pow(this.sigma, 2);
		
		if (Math.abs(a) <= EQ_THRESHOLD)
		{
			// a = 0, so we cannot solve the quadratic equation.
			
			if (Math.abs(this.mu - other.mu) <= EQ_THRESHOLD)
			{
				// Same distribution:
				
				return 1;
			}
			else
			{
				// Equal variances, different means:
				
				m.add((this.mu + other.mu) / 2.0);
			}
		}
		else
		{
			// a != 0, so we can solve the quadratic equation.
			
			double b = 2 * (Math.pow(this.sigma, 2) * other.mu - Math.pow(other.sigma, 2) * this.mu);
			double c = + Math.pow(this.sigma, 2)  * Math.pow(other.mu, 2)
					   - Math.pow(other.sigma, 2) * Math.pow(this.mu,  2)
					   + 2 * Math.pow(this.sigma, 2) * Math.pow(other.sigma, 2) * Math.log(other.sigma / this.sigma);

			double r = Math.pow(b, 2) - 4 * a * c;
			
			if (Math.abs(r) <= EQ_THRESHOLD)
			{
				// Unique solution for m:
				
				m.add((- b) / (2 * a));
			}
			else if (r > 0)
			{
				// Double solution for m:
				
				m.add((- b - Math.sqrt(r)) / (2 * a));
				m.add((- b + Math.sqrt(r)) / (2 * a));
			}
			else
			{
				// No solution for m (distributions too far away, not enough precision):
				
				return 0;
			}
		}
		
		m.add(Double.POSITIVE_INFINITY);
		
		// Area addition:
		
		double overlap = 0;
		
		for (int i = 0; i < m.size() - 1; i++)
		{
			double thisIntegral  = this.definiteIntegral(m.get(i),  m.get(i + 1));
			double otherIntegral = other.definiteIntegral(m.get(i), m.get(i + 1));
			
			overlap += Math.min(thisIntegral, otherIntegral);
		}
		
		return (float) overlap;
	}
	
	/**
	 * Returns the definite integral of this normal distribution for the range [a, b].
	 * @param a Starting point.
	 * @param b Ending point.
	 * @return The definite integral of this normal distribution for the range [a, b].
	 */
	public double definiteIntegral(double a, double b)
	{
		return 0.5 * Erf.erf((a - mu) / (sigma * Math.sqrt(2)), (b - mu) / (sigma * Math.sqrt(2)));
	}
}
