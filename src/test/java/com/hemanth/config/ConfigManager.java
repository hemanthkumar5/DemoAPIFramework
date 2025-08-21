package com.hemanth.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton Configuration Manager for centralized configuration management
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    private static final String CONFIG_FILE = "config/qa.properties";
    
    // Private constructor to prevent instantiation
    private ConfigManager() {
        loadProperties();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load properties from configuration file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Configuration file not found: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }
    
    /**
     * Get property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    /**
     * Get API timeout
     */
    public int getApiTimeout() {
        return Integer.parseInt(getProperty("api.timeout", "30000"));
    }
    
    /**
     * Get JWT secret
     */
    public String getJwtSecret() {
        return getProperty("jwt.secret");
    }
    
    /**
     * Get JWT expiration time
     */
    public long getJwtExpiration() {
        return Long.parseLong(getProperty("jwt.expiration", "3600000")); // 1 hour default
    }
    
    /**
     * Get test data file path
     */
    public String getTestDataPath() {
        return getProperty("testdata.path", "testdata/");
    }
    
    /**
     * Get mock server port
     */
    public int getMockServerPort() {
        return Integer.parseInt(getProperty("mock.server.port", "8080"));
    }
    
    /**
     * Get mock server enabled flag
     */
    public boolean isMockServerEnabled() {
        return Boolean.parseBoolean(getProperty("mock.server.enabled", "false"));
    }
    
    /**
     * Reload configuration
     */
    public void reload() {
        loadProperties();
    }
    
    /**
     * Get all properties as Properties object
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }
}
