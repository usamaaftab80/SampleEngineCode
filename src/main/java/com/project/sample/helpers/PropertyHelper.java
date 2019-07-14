package com.project.sample.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyHelper {
	private static PropertyHelper instance;
	private Properties properties;
	private Logger LOGGER = Logger.getLogger(PropertyHelper.class.getName());
	private String environment;

	// private constructor for singleton pattern
	private PropertyHelper() {
		try {			
			this.environment = System.getProperty("environment", "dev");
			LOGGER.info("Environment flag set to: " + this.environment);
			InputStream input = PropertyHelper.class.getClassLoader().getResourceAsStream("config.properties");
			properties = new Properties();
			properties.load(input);
			input.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to load Property Helper", e);
		}
	}

	/**
	 * Singleton pattern is used. The properties variable is parsed once and
	 * cached (for speed of access), since some of its values may be attached to
	 * every HTTP header the server returns.
	 * 
	 * @return PropertyHelper instance
	 */
	public static PropertyHelper getInstance() {
		if (instance == null) {
			instance = new PropertyHelper();
		}
		return instance;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(this.environment + "." + propertyName);
	}

}
