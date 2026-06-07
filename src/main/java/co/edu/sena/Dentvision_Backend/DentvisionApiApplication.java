package co.edu.sena.Dentvision_Backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DentvisionApiApplication {

    public static void main(String[] args) {
        loadDotEnv();
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
                if (value == null) return;

                // Solo setea si no viene ya de la JVM (-D) ni del entorno real del OS
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            });

            // CORRECCIÓN: el código original leía SPRING_PROFILES_ACTIVE con
            // System.getProperty() pero dotenv lo carga con setProperty() en el paso
            // de arriba, así que esta lectura ya funciona. Sin embargo el perfil
            // debe activarse vía la propiedad estándar spring.profiles.active,
            // no con el nombre de la variable de entorno directamente.
            String profile = System.getProperty("SPRING_PROFILES_ACTIVE");
            if (profile != null && !profile.isBlank()
                    && System.getProperty("spring.profiles.active") == null) {
                System.setProperty("spring.profiles.active", profile);
            }

        } catch (Exception ex) {
            System.out.println("[dotenv] No se pudo cargar el archivo .env: " + ex.getMessage());
        }
    }
}
