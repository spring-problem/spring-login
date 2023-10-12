package com.example.springlogin.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class JwtConfig {
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/jwt/properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSecret() {
        return properties.getProperty("secret");
    }

    public static long getTokenValidTime() {
        return Long.parseLong(properties.getProperty("tokenValidTime"));
    }
}
