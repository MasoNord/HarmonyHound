package com.masonord.harmonyhound.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvUtil {
    public static String getValue(String property) {
        String result;
        try (InputStream input = new FileInputStream("./src/main/resources/application.properties")) {
            Properties prop = new Properties();

            prop.load(input);
            result = prop.getProperty(property);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
