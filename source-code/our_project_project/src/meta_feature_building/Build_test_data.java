package meta_feature_building;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Build_test_data {
	
	/**
	 * Builds some easy data to use for debugging
	 * @param args
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
	{
		String name = "houseTest3";
		int start = 1203898814;
		int sa_dur_min = 50;
		int sa_dur_dif = 50;
		int sa_in_between = 800;
		int sb_dur_min = 100;
		int sb_dur_dif = 100;
		int sb_in_between = 1000;
		PrintWriter writer = new PrintWriter(name+".sensors", "UTF-8");
		Random rand = new Random();
		for(int i =0; i<10; i++){
			for(int j = 0; j<2; j++){
				writer.write(start + "\t");
				System.out.println("start A" + j + " " + (start-1203898814));
				int duration = sa_dur_dif+sa_dur_min;
				writer.write(duration + "\t");
				start = start+duration+sa_in_between;
				writer.write(j + "\n");
			}
			start = start+3000;
			for(int j = 2; j<4; j++){
				writer.write(start + "\t");
				System.out.println("start B" + j + " " + (start-1203898814));
				// Use below if you want more randomness in the test data
//				int duration = rand.nextInt(sb_dur_dif)+sb_dur_min;
				int duration = sb_dur_dif+sb_dur_min;
				writer.write(duration + "\t");
				start = start+duration+sb_in_between;
				writer.write(j + "\n");
			}
			start = start+3000;
		}
		writer.close();
	}
}
