package data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.correlation.Covariance;

public class NormalDistribution
{
	private final static double EQ_THRESHOLD = 0.001;
	
	private RealVector mu, sigma;
	private RealMatrix covariance;

	/**
	 * Models a list of values as a normal distribution.
	 * @param values A list of numerical, double values.
	 */
	public NormalDistribution(double[][] values)
	{
		calculateCharacteristics(values);
	}
	
	/**
	 * Models a list of values as a normal distribution.
	 * @param values A list of numerical, integer values.
	 */
	public NormalDistribution(int[][] values)
	{
		// Value transformation into doubles:		
		double[][] valuesDouble = new double[values.length][values[0].length];
		
		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < values[i].length; j++)
			{
				valuesDouble[i][j] = (double) values[i][j];
			}
		}
		
		calculateCharacteristics(valuesDouble);
	}
	
	private void calculateCharacteristics(double[][] values)
	{
		int dimensions = values[0].length; // Number of dimensions of the future (multivariate) normal distribution.
		
		RealMatrix valuesMatrix = new Array2DRowRealMatrix(values);
		
		// Distribution characteristics calculation:		
		covariance = (new Covariance(valuesMatrix)).getCovarianceMatrix();
		
		double[] muVector    = new double[dimensions];
		double[] sigmaVector = new double[dimensions];
		
		long[] sum = new long[dimensions];
		
		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < dimensions; j++)
			{
				sum[j] += values[i][j];
			}
		}
		
		for (int i = 0; i < dimensions; i++)
		{
			muVector[i] = (double) sum[i] / values.length;
		}
		
		double diff[] = new double[dimensions];
		
		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < dimensions; j++)
			{
				diff[j] += Math.pow(values[i][j] - muVector[j], 2);
			}
		}
		
		for (int i = 0; i < dimensions; i++)
		{
			sigmaVector[i] = Math.sqrt(diff[i] / values.length);
		}
		
		mu    = new ArrayRealVector(muVector);
		sigma = new ArrayRealVector(sigmaVector);
	}
	
	/**
	 * Calculates the Kullback-Leibler divergence from this normal distribution to another one.
	 * Inspired from https://en.wikipedia.org/wiki/Multivariate_normal_distribution
	 * @param other The normal distribution to compare this distribution with.
	 * @return The KL distance from this normal distribution to another one.
	 */
	public float KLDivergence(NormalDistribution other)
	{
		double output = 0;
		
		double determinantThis  = new LUDecomposition(this.covariance).getDeterminant();
		double determinantOther = new LUDecomposition(other.covariance).getDeterminant();
				
		RealMatrix covarianceInvOther;
		
		try
		{	
			covarianceInvOther = (new LUDecomposition(other.covariance)).getSolver().getInverse();
		}
		catch (SingularMatrixException a)
		{			
			if (this.mu.getDistance(other.mu) <= EQ_THRESHOLD)
			{
				System.out.println("exact same distribution");
				
				return 0.0f;
			}
			
			return Float.POSITIVE_INFINITY;
		}
		output += covarianceInvOther.multiply(this.covariance).getTrace();
		
		RealMatrix diffMeans = new Array2DRowRealMatrix(other.mu.subtract(this.mu).toArray());
		
		output += diffMeans.transpose().multiply(covarianceInvOther).multiply(diffMeans).getTrace();
		
		output -= mu.getDimension();		
		
		output -= Math.log(determinantThis / determinantOther);
		
		return (float) (output * 0.5);
	}
	
	/**
	 * Calculates the level of overlap between this normal distribution and another one.
	 * @param other Another normal distribution.
	 * @return A value in the range [0, 1] indicating the level of overlap between both distributions.
	 */
	public float overlapLevel(NormalDistribution other)
	{
		double sum = 0;
		
		for (int i = 0; i < mu.getDimension(); i++)
		{
			// Points where both distributions yield the same value:
			
			List<Double> m = new ArrayList<Double>(); 
			
			m.add(Double.NEGATIVE_INFINITY);
			
			// Calculation of the set of overlapping points:
			
			double a = Math.pow(other.sigma.getEntry(i), 2) - Math.pow(this.sigma.getEntry(i), 2);
			
			if (Math.abs(a) <= EQ_THRESHOLD)
			{
				// a = 0, so we cannot solve the quadratic equation.
				
				if (Math.abs(this.mu.getEntry(i) - other.mu.getEntry(i)) <= EQ_THRESHOLD)
				{
					// Same distribution:
					
					return 1;
				}
				else
				{
					// Equal variances, different means:
					
					m.add((this.mu.getEntry(i) + other.mu.getEntry(i)) / 2.0);
				}
			}
			else
			{
				// a != 0, so we can solve the quadratic equation.
				
				double b = 2 * (Math.pow(this.sigma.getEntry(i), 2) * other.mu.getEntry(i) - Math.pow(other.sigma.getEntry(i), 2) * this.mu.getEntry(i));
				double c = + Math.pow(this.sigma.getEntry(i), 2)  * Math.pow(other.mu.getEntry(i), 2)
						   - Math.pow(other.sigma.getEntry(i), 2) * Math.pow(this.mu.getEntry(i),  2)
						   + 2 * Math.pow(this.sigma.getEntry(i), 2) * Math.pow(other.sigma.getEntry(i), 2) * Math.log(other.sigma.getEntry(i) / this.sigma.getEntry(i));
	
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
		
		return (float) sum / mu.getDimension();
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
		return 0.5 * Erf.erf((a - mu.getEntry(d)) / (sigma.getEntry(d) * Math.sqrt(2)), (b - mu.getEntry(d)) / (sigma.getEntry(d) * Math.sqrt(2)));
	}
}
