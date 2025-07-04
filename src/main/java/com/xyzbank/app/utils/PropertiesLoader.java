package com.xyzbank.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private static Properties configProperties;
    private static Properties testDataProperties; // New static field for test data

    // Private constructor to prevent instantiation
    private PropertiesLoader() {
    }


    private static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + filePath);
                throw new RuntimeException("Property file not found: " + filePath);
            }
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Error loading properties from " + filePath + ": " + ex.getMessage());
            throw new RuntimeException("Failed to load property file: " + filePath, ex);
        }
        return properties;
    }


    public static void loadConfigProperties() {
        if (configProperties == null) {
            configProperties = loadProperties("config.properties");
        }
    }


    public static void loadTestDataProperties() {
        if (testDataProperties == null) {
            testDataProperties = loadProperties("testdata.properties");
        }
    }


    public static String getConfigProperty(String key) {
        loadConfigProperties(); // Ensure properties are loaded
        String value = configProperties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }


    public static String getConfigProperty(String key, String defaultValue) {
        loadConfigProperties(); // Ensure properties are loaded
        return configProperties.getProperty(key, defaultValue);
    }


    public static String getTestDataProperty(String key) {
        loadTestDataProperties(); // Ensure properties are loaded
        String value = testDataProperties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in testdata.properties");
        }
        return value;
    }


    public static String getTestDataProperty(String key, String defaultValue) {
        loadTestDataProperties();
        return testDataProperties.getProperty(key, defaultValue);
    }
}