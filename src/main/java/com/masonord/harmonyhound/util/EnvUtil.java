package com.masonord.harmonyhound.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvUtil.class);

    public static String getValue(String property) {
        String result;
        try (InputStream input = new FileInputStream("./src/main/resources/application.properties")) {
            Properties prop = new Properties();

            prop.load(input);
            result = prop.getProperty(property);
        } catch (IOException e) {
            LOGGER.atError().setMessage(e.getMessage()).log();
            throw new RuntimeException(e);
        }

        LOGGER.atInfo().setMessage("The property {} has been got successfully").addArgument(property).log();
        return result;
    }
}
