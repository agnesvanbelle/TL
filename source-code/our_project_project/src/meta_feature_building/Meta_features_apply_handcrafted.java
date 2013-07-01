package meta_feature_building;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import data.HouseData;

public class Meta_features_apply_handcrafted {

	/**
	 * 
	 * @param houses
	 * @param different_meta_features boolean for using different meta features
	 *            or shared meta features (handcrafted meta meta features)
	 */
	public  void apply_hand_crafted_meta_features(ArrayList<HouseData> houses, boolean different_meta_features) {
		BufferedReader br = null;
		String line = "";

		for (HouseData house : houses) {
			String x = "";
			if (!different_meta_features)
				x = "1";
			String filename = house.houseName + x + ".meta_features";
			try {
				br = new BufferedReader(new FileReader(HouseData.inputDataDir + filename));
				while ((line = br.readLine()) != null) {
					String[] names = line.split("\t");
					if (names.length == 2) {
						String sensorName = names[1] + '-' + house.houseName;
						HouseData.mapSensors(sensorName, names[0] + "-" + house.houseName);
					}
					else {
						System.out.println("Something wrong with handcrafted meta features file of " + house.houseName);
						for (int i = 0; i < names.length; i++) {
							System.out.println("-" + names[i] + "-");
						}
					}
				}
			}
			catch (FileNotFoundException e) {
				System.err.println("File " + filename + " not found in apply_hand_crafted_meta_features");
				e.printStackTrace();
			}
			catch (IOException e) {
				System.err.println("Problem reading from file " + filename + " in apply_hand_crafted_meta_features");
				e.printStackTrace();
			}
			catch (Exception e) {
				System.err.println("Problem with file " + filename + " in apply_hand_crafted_meta_features");
				e.printStackTrace();
			}
			finally {
				if (br != null) {
					try {
						br.close();
					}
					catch (Exception e) {
						System.err.println("Problem closing file " + filename + " in apply_hand_crafted_meta_features");
						e.printStackTrace();
					}
				}
			}

		}
	}

}
