package co.edu.sena.Dentvision_Backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DentvisionApiApplication {

    public static void main(String[] args) {
        loadDotEnv();

        String envProfile = System.getProperty("SPRING_PROFILES_ACTIVE");
        if (envProfile != null && !envProfile.isBlank()
                && System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", envProfile);
        }

        SpringApplication.run(DentvisionApiApplication.class, args);
    }

    private static void loadDotEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();

                if (value == null) {
                    return;
                }

                if (System.getProperty(key) == null
                        && System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            });
        } catch (Exception ex) {
            System.out.println("[dotenv] No se pudo cargar el archivo .env: " + ex.getMessage());
        }
    }
}

