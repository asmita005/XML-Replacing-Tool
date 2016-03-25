package com.loves.partner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser
{
	private static Logger log = CreateLogger.getLogger();
	private static boolean wantLogs;
	private static XmlParser xmlParser;
	private static boolean parsingError = false;
	private static SAXException excep = null ;
	private static IOException excep1 = null ;
	private static String INPUT_DIR_PATH;
		
	// Method to create and return instance of the class
	public static XmlParser getInstance() {
		if (xmlParser == null) {
			xmlParser = new XmlParser();
		}
					
		return xmlParser;
	}
	
    public void readAndReplace(String xmlFilename, boolean wantLog, String InputDirPath) {
    	INPUT_DIR_PATH = InputDirPath;
    	wantLogs = wantLog;
        File source = new File(xmlFilename);
        
        // This block of code writes in log file if the xml file does not exist
        if(!source.exists())
        {
            System.out.println("File:"+source+ " does not exist");
            if(wantLogs){
				log.info("XML file does not exist!!"); 
			}
        }

       
        

        try {
			
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(source);
			

			
			//use item(0) to get the first node with tage name "LFA1"
			Node LFA1 = document.getElementsByTagName("LFA1").item(0);

			NodeList nodes = LFA1.getChildNodes();
			
			
			for (int i = 0; i < (nodes.getLength()); i++) {
				
				Node element = nodes.item(i);
				
				
				if ("item".equals(element.getNodeName())) {

					NodeList nodes1 = element.getChildNodes();
					

					for (int j = 0; j < (nodes1.getLength()); j++) {

						Node element1 = nodes1.item(j);
						
						// remove CITY2
						if ("CITY2".equals(element1.getNodeName())) {
							element.removeChild(element1);
							Element city2 = document.createElement("CITY2");
							element.appendChild(city2);
				        }}}

			}

			// write the DOM object to the file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			 File target = new File(xmlFilename+".new");
			StreamResult streamResult = new StreamResult(target);
			transformer.transform(domSource, streamResult);

			System.out.println("The XML File was ");

		} catch (ParserConfigurationException e) {
			
			if(wantLogs){
				log.info("Exception : "+ e); 
			}
			e.printStackTrace();
		} catch (TransformerException e) {
			if(wantLogs){
				log.info("Exception : "+ e); 
			}
			e.printStackTrace();
		} catch (IOException e) {
			parsingError=true;
			excep1 = e ;
			if(wantLogs){
				log.info("Exception : "+ e); 
			}
			e.printStackTrace();
		} catch (SAXException e) {
			parsingError=true;
			excep = e ;
			if(wantLogs){
				log.info("Exception : "+ e); 
			}
			e.printStackTrace();
		} finally{
	        
			
		}
        deleteAndReplace(xmlFilename);
        
    }
    
    // This method replaces the original xml file with modified xml
    public void deleteAndReplace(String xmlFile)
    {
    	if(!parsingError){
	    	try{
	    		File file = new File(xmlFile);
	    		File file1 = new File(xmlFile+".new");
	    		Files.move(file1.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    	} catch(Exception e){
	    		if(wantLogs){
					log.info("Exception : "+ e); 
				}
	    		e.printStackTrace();
	    	}

    	}
    	else{
    	
  		    	try{
  		    		String ERROR_DIR_PATH = INPUT_DIR_PATH + "\\Unprocessed" ;
  		    		File file = new File(xmlFile);
  		    		
  		    	
  			    	File fol = new File(ERROR_DIR_PATH); 
  			    	if(!fol.exists()){
  			    		fol.mkdir();
  			    	}
  			    	
  	  		    	Path destPath1 = Paths.get(ERROR_DIR_PATH,file.getName());
  	  		    	
  		    		Files.copy(file.toPath(), destPath1, StandardCopyOption.REPLACE_EXISTING);
  		    		System.gc();
  		    		file.delete();
  		    		String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");
  		   
  		    		String logFile = ERROR_DIR_PATH + "\\" + fileNameWithOutExt + ".log" ;
  		    		PrintWriter writer = new PrintWriter(logFile, "UTF-8");
	  		    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  		    	Date date = new Date();
	  		    	writer.println(dateFormat.format(date));
	  		    	writer.println(excep);
	  		    	writer.println(excep1);
	  		    	writer.close();
	  		    	
	  		    }catch(IOException e){
		    		if(wantLogs){
						log.info("Exception : "+ e); 
					}
		    		e.printStackTrace();
		    	}
    	}
    }
}