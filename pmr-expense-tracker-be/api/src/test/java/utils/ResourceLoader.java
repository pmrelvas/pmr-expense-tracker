package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {
    public static String getStringFromFile(String filePath) throws IOException {
        try (InputStream stream = ClassLoader.getSystemResourceAsStream(filePath)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
