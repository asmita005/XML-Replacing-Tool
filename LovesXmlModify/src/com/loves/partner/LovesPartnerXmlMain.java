package com.loves.partner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;


public class LovesPartnerXmlMain {
	private static String INPUT_DIR_PATH;
	private static String OUTPUT_DIR_PATH;
	private static boolean wantLogs;
	private static Logger log =null;
	private static File[] xmlFiles = null;




	public static void getDirectory()
	{
		// This block of code retrieves the information from properties file and assigns them to respective variables
		try {

			INPUT_DIR_PATH=PropertyLoader.getInstance().getExternalProperties().getProperty("INPUT_FOLDER_PATH");
			OUTPUT_DIR_PATH=PropertyLoader.getInstance().getExternalProperties().getProperty("OUTPUT_FOLDER_PATH");
			wantLogs=Boolean.parseBoolean(PropertyLoader.getInstance().getExternalProperties().getProperty("WANTLOGS"));

			if(wantLogs){										// if wantlogs flag is true in properties file 
				CreateLogger.getInstance().creatingLogger();   // then only logger is created
				log=CreateLogger.getLogger();
			}

			checkXMLFiles(INPUT_DIR_PATH);


		}

		catch(IOException ex) {
			if(wantLogs){
				log.info("Exception : "+ ex); 
			}
			ex.printStackTrace();
		} catch (Exception e) {
			if(wantLogs){
				log.info("Exception : "+ e);
			}
			
			e.printStackTrace();
		}
	}

	public static void checkXMLFiles(String inputDir) {

		File dir = new File(inputDir);
		
		String files;

		if (dir.isDirectory()) {
			xmlFiles = dir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File folder, String name) {
					return name.toLowerCase().endsWith(".xml");
				}
			});
		}
		
		else{
			if(wantLogs){
				log.info("Input Directory not present!!"); 
			}
			 
		}

		for (int i = 0; i < xmlFiles.length; i++)      // checks for file name containing vendor and send it to xmlParser
		{

			files = xmlFiles[i].getName();
			if (files.contains("vendor") || files.contains("VENDOR"))
			{
				if(wantLogs){
					log.info(files); 
				}
				String filePath=INPUT_DIR_PATH+"\\"+files;
				try {
					XmlParser.getInstance().readAndReplace(filePath, wantLogs, INPUT_DIR_PATH);
				} catch (Exception e) {
					if(wantLogs){
						log.info("Exception : "+ e); 
					}
					e.printStackTrace();
				} 

			}
		}
			moveXmlToOutputDir();
		
		
		
	}
	
	//this method moves all the .xml files in INPUT_DIT to OUTPUT_DIR
	public static void moveXmlToOutputDir() {
		
		File outDir = new File(OUTPUT_DIR_PATH);
		File inDir = new File(INPUT_DIR_PATH);
		File[] xmlFiles1 = null ;
		String files;

		if (outDir.isDirectory()) {
			if (inDir.isDirectory()) {
				xmlFiles1 = inDir.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File folder, String name) {
						return name.toLowerCase().endsWith(".xml");
					}
				});
			}
			for (int i = 0; i < xmlFiles1.length; i++) 
	 		  {
				 files = xmlFiles1[i].getName();
 		    	
 		    	try {
 		    		
	       		    	
 		    		Path srcPath = Paths.get(INPUT_DIR_PATH ,files);
		      		    	
		      		Path destPath = Paths.get(OUTPUT_DIR_PATH,files);
		      		    	
		      		Files.move(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
      		   			
				} catch (IOException e) {
					if(wantLogs){
						log.info("Exception : "+ e); 
					}
					e.printStackTrace();
				}
	 		   
	 		  }
		}
		else{
			if(wantLogs){
				log.info("Output Directory not present!!"); 
			}
			 
		}
}


	public static void main(String [] args) {


		getDirectory();

	}
}


