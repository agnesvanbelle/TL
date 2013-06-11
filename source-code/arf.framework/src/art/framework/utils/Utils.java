package art.framework.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

	public static void saveContent(String fileName, String content, boolean append) {
		try {
			File file = new File(fileName);
			File dir = file.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, append));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to write to file " + fileName);
			e.printStackTrace();
		}
	}
	
	public static void saveContent(String fileName, String content) {
		saveContent(fileName, content, false);
	}
	
	public static Map<String,String> readExamples(String file) {
		Map<String, String> examples = new HashMap<String, String>();
	
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.replace("[", "").replace("]", "");
				String[] exmapleInfo = line.split(":");
				
				examples.put(exmapleInfo[1] /*game play*/, 
						exmapleInfo[0] /*class label*/);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		
		
		return examples;
	}
	
	public static void createOutputDirectory(String dirName) {
		File outputDir = new File(dirName);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
	}
	
	public static boolean deleteDir(String dirName) {
		File dir = new File(dirName);
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]).getAbsolutePath());
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	public static List<List<String>> readRelations(String file, String delimiter) {
		List<List<String>> examples = new ArrayList<List<String>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] rulesInfo = line.split(delimiter);
				List<String> patterns = Arrays.asList(rulesInfo);
				examples.add(patterns);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		return examples;
	}


	public static String getValueSignature(String value) {
		String signature = value.replaceAll(",", "").replaceAll("\\(.+?\\)", "").replace(")", "");
		return signature;
	}

	public static List<String> readLines(String file) {
		List<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		
		
		return lines;
	}
	

	public static void saveLines(List<String> readings,
			String fileName) {
		StringBuffer sb = new StringBuffer();
		for (String line: readings) {
			sb.append(line).append("\n");
		}
		Utils.saveContent(fileName, sb.toString());
	}
}