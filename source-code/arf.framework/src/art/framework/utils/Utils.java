package art.framework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
		}
		catch (IOException e) {
			System.out.println("[ERROR] Failed to write to file " + fileName);
			e.printStackTrace();
		}
	}

	public static void saveContent(String fileName, String content) {
		saveContent(fileName, content, false /* append */);
	}

	public static Map<String, String> readExamples(String file) {
		Map<String, String> examples = new HashMap<String, String>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.replace("[", "").replace("]", "");
				String[] exmapleInfo = line.split(":");

				examples.put(exmapleInfo[1] /* game play */, exmapleInfo[0] /*
																			 * class
																			 * label
																			 */);
			}

		}
		catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (Exception e) {
					System.err.println("COuldn't close file " + file);
					e.printStackTrace();
				}
			}
		}

		return examples;
	}

	/**
	 * creates directory if it doesn't exist yet
	 * 
	 * @param dirName
	 */
	public static void createDirectory(String dirName) {
		boolean success= new File(dirName).mkdirs();
//		if (!outputDir.exists()) {
//			outputDir.mkdir();
//		}
	}

	public static void resetDirectory(String dirName) {
		deleteDir(dirName);
		File outputDir = new File(dirName);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
	}

	public static boolean deleteDir(String dirName) {
		File dir = new File(dirName);
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]).getAbsolutePath());
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty if it had files/subdirs - try to delete it
		boolean deleted = false;
		try {
			dir.delete();
			deleted = true;
		}
		catch (Exception e) {
			// File permission problems are caught here.
			System.err.println("Error deleting dir " + dirName);
			e.printStackTrace();
		}

		return deleted;
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

		}
		catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		catch (IOException e) {
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

		}
		catch (FileNotFoundException e) {
			System.out.println("[ERROR] Failed to read from file: " + file);
			System.out.println(e);
			System.exit(1);
		}
		catch (IOException e) {
			System.out.println("[ERROR] Unexpected exception while reading from file: " + file);
			System.out.println(e);
			System.exit(1);
		}

		return lines;
	}

	public static void saveLines(List<String> readings, String fileName) {
		StringBuffer sb = new StringBuffer();
		for (String line : readings) {
			sb.append(line).append("\n");
		}
		Utils.saveContent(fileName, sb.toString());
	}

	public static int getDirectorySize(String path) {

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		return listOfFiles.length;
	}

	public static void copyDirectory(File sourceLocation, File targetLocation) {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			else {
				resetDirectory(targetLocation.getPath());
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		}
		else {

			InputStream in = null;
			OutputStream out = null;

			try {
				in = new FileInputStream(sourceLocation);
				out = new FileOutputStream(targetLocation);

				byte[] buf = new byte[1024];
				int len = 0;
				try {

					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Copy the bits from instream to outstream
			finally {
				if (in != null) {
					try {
						in.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void copyFile(File sourceFile, File destFile)  {
		
		try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

		    FileChannel source = null;
		    FileChannel destination = null;
	
		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    }
		    finally {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
		    }
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getDirectoryListing(String path) {

		ArrayList<String> files = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {

				if (listOfFiles[i].isFile()) {
					files.add(listOfFiles[i].getName());
					//System.out.println(files);
				}
			}
		}
		return files;
	}
	
	public static ArrayList<String> getSubDirectories(String path) {
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return new ArrayList<String>(Arrays.asList(directories));
	}
}
