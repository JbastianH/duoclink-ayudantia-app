package cl.duoc.msapuntes.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_PROJECT_ID:#{null}}")
    private String projectId;

    @Value("${FIREBASE_CLIENT_EMAIL:#{null}}")
    private String clientEmail;

    @Value("${FIREBASE_PRIVATE_KEY:#{null}}")
    private String privateKey;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // DEBUG: Print loaded values (masked)
        System.out.println("DEBUG: Loading Firebase Config...");
        System.out.println("DEBUG: Project ID: " + projectId);
        System.out.println("DEBUG: Client Email: " + clientEmail);
        if (privateKey != null) {
            System.out.println("DEBUG: Private Key Length: " + privateKey.length());
            System.out.println("DEBUG: Private Key Starts With: " + privateKey.substring(0, Math.min(20, privateKey.length())));
            int dotIndex = privateKey.indexOf('.');
            if (dotIndex != -1) {
                System.out.println("DEBUG: Found dot at index: " + dotIndex);
            }
        } else {
            System.out.println("DEBUG: Private Key is NULL");
        }

        if (projectId == null || clientEmail == null || privateKey == null) {
            // Fallback to application default credentials if env vars are not set
            // This is useful for local development with Google Cloud SDK
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp();
            }
            return FirebaseApp.getInstance();
        }

        // Fix private key format (replace escaped newlines)
        // Also remove surrounding quotes if present (common in .env files)
        if (privateKey.startsWith("\"") && privateKey.endsWith("\"")) {
            privateKey = privateKey.substring(1, privateKey.length() - 1);
        }
        
        String formattedPrivateKey = privateKey.replace("\\n", "\n");

        String jsonCredentials = String.format(
                "{" +
                "  \"type\": \"service_account\"," +
                "  \"project_id\": \"%s\"," +
                "  \"private_key_id\": \"\"," + // Optional
                "  \"private_key\": \"%s\"," +
                "  \"client_email\": \"%s\"," +
                "  \"client_id\": \"\"," + // Optional
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\"," +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\"," +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\"," +
                "  \"client_x509_cert_url\": \"\"" + // Optional
                "}",
                projectId, formattedPrivateKey.replace("\n", "\\n"), clientEmail
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonCredentials.getBytes(StandardCharsets.UTF_8))))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}
