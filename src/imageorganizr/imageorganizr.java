package imageorganizr;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class imageorganizr {
	
	public static final SimpleDateFormat givenDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat targetDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	

	
	public static void main (String[] args) {
		
		System.out.println();	//only for readability

		System.out.println("Please enter the path of the directory that shall be organized.");
		File sourceDirectory = new File("");
		
		Scanner sc = new Scanner(System.in);
		
		while(sc.hasNext()) {		//enter source directory
			
			String directoryInput = sc.next();
			sourceDirectory = new File(directoryInput);
			
			if (sourceDirectory.exists()) {
				break;
				
			} else {
				System.out.println("This path is does not exist, please enter a valid path");
			}
		} 
		
		if (!sourceDirectory.exists()) {	//if source directory has not been entered
			System.out.println("No source directory entered. Program finished.");
			System.out.println();		
			return;
		}
	
		System.out.println();		
		if (sortingAlgorithm(sourceDirectory)) {
			System.out.println("The directory " + sourceDirectory + " has been successfully organized.");
			
		} else {
			System.out.println("An error occurred! Program terminated");
			
		}
		
		System.out.println();		
	}
	
	
	public static boolean sortingAlgorithm (File sourceDirectory) {
		
		/*	String home = System.getProperty("user.home");
		*	File outputDirectory = new File(home + "/Desktop/organizedOutput");
		*	System.out.println("Created the output directory: " + outputDirectory.mkdir());
		*/
		File actualWorkingDirectory = sourceDirectory;
		File overviewDirectory = new File(sourceDirectory.toString() + "/overview");
		Date lastFileDate = new Date(0);
		int directoryCounter = 1;	// counter of new directories
		int fileCounter = 0;		// counter of files moved in each directory
		
		for (Iterator<File> iter =  FileUtils.iterateFiles(sourceDirectory, new SuffixFileFilter(".jpg"), null); iter.hasNext();) {
			File actualFile = iter.next();
			
			int dateEndIndex = actualFile.getName().lastIndexOf("_");
			String actualFileDateString = actualFile.getName().substring(dateEndIndex - 14, dateEndIndex);
			Date actualFileDate = new Date();
			
	        try {
	            actualFileDate = givenDateFormat.parse(actualFileDateString);
	        } 
	        catch (ParseException e) {
	            ;
	        }
			
			if(actualFileDate.getTime() > lastFileDate.getTime() + 3000) {
				if (!lastFileDate.equals(new GregorianCalendar(1970, 1, 1))) System.out.println("Moved " + fileCounter + " Files into the new directory \"" + actualWorkingDirectory + "\"");
				fileCounter = 0;
				
				if (!copyFileToDirectory(actualFile, overviewDirectory)) return false;
				System.out.println("Copied file " + actualFile.getName() + " to the directory \"" + overviewDirectory + "\"");
				
				String renameString = "/Alarm" + directoryCounter++ + ", " + targetDateFormat.format(actualFileDate);
								
				new File(overviewDirectory.toString() + "/" + actualFile.getName()).renameTo(new File(overviewDirectory.toString() + renameString + ".jpg"));
				actualWorkingDirectory = new File(sourceDirectory.toString() + renameString);
				
			}
			lastFileDate = actualFileDate;
			if(!moveFileToDirectory(actualFile, actualWorkingDirectory)) return false;
			
			fileCounter++;
		}
		
		return true;
	}
	
	public static boolean copyFileToDirectory (File file, File directory) {
		try {
			FileUtils.copyFileToDirectory(file, directory, true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean moveFileToDirectory (File file, File directory) {
		try {
			FileUtils.moveFileToDirectory(file, directory, true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}