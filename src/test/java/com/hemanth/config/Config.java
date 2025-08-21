package com.hemanth.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    // why static - no need to create a Config object to access or initialize it.
    private static final Properties props = new Properties();

    static {
        String env = System.getProperty("env", "qa");

        String path = "/config/" + env + ".properties";

        try (InputStream is = Config.class.getResourceAsStream(path)) {
            if (is == null) throw new IllegalStateException("Missing config:" + path);
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String baseUri() {
        return props.getProperty("base.uri");
    }

    public static boolean apiKeyEnabled() {
        return Boolean.parseBoolean(props.getProperty("api.key.enabled"));
    }

    public static String apiKeyHeader() {
        return props.getProperty("api.key.header");
    }

    public static String apiKeyValue() {
        return props.getProperty("api.key.value");
    }

    public static  String authType(){
        return props.getProperty("auth.type","none");
    }

    // Basic
    public static String basicUser(){
        return props.getProperty("auth.basic.username");
    }

    public static String basicPassword(){
        return props.getProperty("auth.basic.password");
    }

    // Bearer
    public static String bearerToken(){
        return props.getProperty("auth.bearer.token");
    }

    public static String cookieName(){
        return props.getProperty("auth.cookie.name");
    }
    public static String cookieValue(){
        return props.getProperty("auth.cookie.value");
    }

}
