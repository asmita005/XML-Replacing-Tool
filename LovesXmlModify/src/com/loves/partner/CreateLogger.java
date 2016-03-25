package com.loves.partner;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CreateLogger {
	
	private static CreateLogger createLogger;
	private static Logger logger;
	
	// Method to create and return instance of the class
	public static CreateLogger getInstance() {
		if (createLogger == null) {
			createLogger = new CreateLogger();
		}
		return createLogger;
	}
	
	public void creatingLogger()
	{
	setLogger(Logger.getLogger("MyLog"));  
    FileHandler fh;  

    try {  

        // This block configure the logger with handler and formatter  
        fh = new FileHandler(".\\PartnerXMlModifier.log",90000,1,true);  
        getLogger().addHandler(fh);
        
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

    } catch (SecurityException e) {  
        e.printStackTrace();  
    } catch (IOException f) {  
        f.printStackTrace();  
    }  

}
	// Get and Set methods for Logger
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CreateLogger.logger = logger;
	}
}
