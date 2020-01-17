package ru.taranov.vacancyparser;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private final Properties properties = new Properties();

    public Config() {
        this.init();
    }

    public void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String getValue(String key) {
        return this.properties.getProperty(key);
    }
}
