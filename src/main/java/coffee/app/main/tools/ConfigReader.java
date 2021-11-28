package coffee.app.main.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {
    public static Properties read(String fileName) throws CoffeeException {
        Properties props = new Properties();
        BufferedReader appConfigPath = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName))));
        try {
            props.load(appConfigPath);
        } catch (IOException e) {
            throw new CoffeeException("Cannot read application configuration: " + appConfigPath + fileName);
        }
        return props;
    }
}
