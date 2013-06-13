package tl.experiments;

import java.io.File;
import java.io.IOException;

import org.ejml.simple.SimpleMatrix;
import art.experiments.WifiExperimentRunner;

public class Test {
	
	
	public static void main(String[] args) throws IOException {		
		
		System.out.println("Hi hi hi");
		
		
		SimpleMatrix n = new SimpleMatrix(2,3);
		//System.out.println(new File(".").getCanonicalPath());
		

		//WifiExperimentRunner we = new WifiExperimentRunner();
		WifiExperimentRunner.main(null);
	}

}
