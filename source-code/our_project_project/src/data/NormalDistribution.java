package data;

import java.util.*;
import org.apache.commons.math3.special.*;

public class NormalDistribution
{
	private final static double EQ_THRESHOLD = 0.001;
	
	public final float[] mu, sigma;

	/**
	 * Models a list of values as a normal distribution.
	 * @param values A list of numerical values.
	 */
	public NormalDistribution(List values)
	{
		int dimensions = 1; // Number of dimensions of the future (multivariate) normal distribution.
		
		Object example = values.get(0);
		
		if (example instanceof List)
		{
			dimensions = ((List) example).size();
		}
		
		mu    = new float[dimensions];
		sigma = new float[dimensions];
		
		long[] sum = new long[dimensions];
		
		for (Object value: values)
		{
			if (value instanceof Integer)
			{
				sum[0] += (Integer) value;
			}
			else if (value instanceof List)
			{
				for (int i = 0; i < dimensions; i++)
				{
					sum[i] += (Integer) ((List) value).get(i);
				}
			}
		}
		
		for (int i = 0; i < dimensions; i++)
		{
			mu[i] = (float) sum[i] / values.size();
		}
		
		float diff[] = new float[dimensions];
		
		for (Object value: values)
		{
			if (value instanceof Integer)
			{
				diff[0] += Math.pow((Integer) value - mu[0], 2);
			}
			else if (value instanceof List)
			{
				for (int i = 0; i < dimensions; i++)
				{
					diff[i] += Math.pow((Integer) ((List) value).get(i) - mu[i], 2);
				}
			}
		}
		
		for (int i = 0; i < dimensions; i++)
		{
			sigma[i] = (float) Math.sqrt(diff[i] / values.size());
		}
	}
	
	public NormalDistribution(float[] mu, float[] sigma)
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
		double sum = 0;
		
		for (int i = 0; i < mu.length; i++)
		{
			sum += (Math.pow(this.mu[i] - other.mu[i], 2) + Math.pow(this.sigma[i], 2)) / (2 * Math.pow(other.sigma[i], 2)) + Math.log(other.sigma[i] / this.sigma[i]) - 0.5;
		}
		
		return (float) sum;
	}
	
	/**
	 * Calculates the level of overlap between this normal distribution and another one.
	 * @param other Another normal distribution.
	 * @return A value in the range [0, 1] indicating the level of overlap between both distributions.
	 */
	public float overlapLevel(NormalDistribution other)
	{
		double sum = 0;
		
		for (int i = 0; i < mu.length; i++)
		{
			// Points where both distributions yield the same value:
			
			List<Double> m = new ArrayList<Double>(); 
			
			m.add(Double.NEGATIVE_INFINITY);
			
			// Calculation of the set of overlapping points:
			
			double a = Math.pow(other.sigma[i], 2) - Math.pow(this.sigma[i], 2);
			
			if (Math.abs(a) <= EQ_THRESHOLD)
			{
				// a = 0, so we cannot solve the quadratic equation.
				
				if (Math.abs(this.mu[i] - other.mu[i]) <= EQ_THRESHOLD)
				{
					// Same distribution:
					
					return 1;
				}
				else
				{
					// Equal variances, different means:
					
					m.add((this.mu[i] + other.mu[i]) / 2.0);
				}
			}
			else
			{
				// a != 0, so we can solve the quadratic equation.
				
				double b = 2 * (Math.pow(this.sigma[i], 2) * other.mu[i] - Math.pow(other.sigma[i], 2) * this.mu[i]);
				double c = + Math.pow(this.sigma[i], 2)  * Math.pow(other.mu[i], 2)
						   - Math.pow(other.sigma[i], 2) * Math.pow(this.mu[i],  2)
						   + 2 * Math.pow(this.sigma[i], 2) * Math.pow(other.sigma[i], 2) * Math.log(other.sigma[i] / this.sigma[i]);
	
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
			
			for (int j = 0; j < m.size() - 1; j++)
			{
				double thisIntegral  = this.definiteIntegral(m.get(j),  m.get(j + 1), i);
				double otherIntegral = other.definiteIntegral(m.get(j), m.get(j + 1), i);
				
				overlap += Math.min(thisIntegral, otherIntegral);
			}
			
			sum += overlap;
		}
		
		return (float) sum;
	}
	
	/**
	 * Returns the definite integral of this normal distribution for the range [a, b].
	 * @param a Starting point.
	 * @param b Ending point.
	 * @param d Index of the dimension.
	 * @return The definite integral of this normal distribution for the range [a, b].
	 */
	public double definiteIntegral(double a, double b, int d)
	{
		return 0.5 * Erf.erf((a - mu[d]) / (sigma[d] * Math.sqrt(2)), (b - mu[d]) / (sigma[d] * Math.sqrt(2)));
	}
}
