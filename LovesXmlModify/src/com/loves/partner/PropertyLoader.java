package com.loves.partner;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

	private static PropertyLoader propertyLoader;
	
	PropertyLoader() {
	}
	
	public static PropertyLoader getInstance() {
		if (propertyLoader == null) {
			propertyLoader = new PropertyLoader();
		}
		
		return propertyLoader;
	}
	
	public Properties getExternalProperties() throws IOException {
		Properties properties = new Properties();
		//String packName = "/";
		//packName = "" + packName.replace('.', '/') + "ExternalSettings.properties";
		String packName = "ExternalSettings.properties";
		Properties contextProperties = new Properties();
		//Class clazz = getClass();
		contextProperties.load(getClass().getClassLoader().getResourceAsStream(packName));

		properties = contextProperties;

		return properties;
	}
}
