package mod.azure.iseelava;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LavaConfig {
    // 0.0 = fully invisible, 1.0 = fully opaque
    public static float OPACITY = 0.55f;

    // Config file stored in the standard Fabric config directory.
    private static final File CONFIG_FILE = new File("config/iseelava_config.json");
    
    private static final Gson GSON = new Gson();

    /**
     * Loads configuration from disk.
     * If the file does not exist or is malformed, defaults are preserved.
     */
    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) return;
        
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json != null && json.has("opacity")) {
                OPACITY = json.get("opacity").getAsFloat();
            }
        } catch (IOException e) {
            // Log instead of crashing — config errors should not break the game.
            e.printStackTrace();
        }
    }

    /**
     * Saves current configuration values to disk.
     */
    public static void saveConfig() {
        // Ensure config directory exists before writing.
        CONFIG_FILE.getParentFile().mkdirs();
        
        JsonObject json = new JsonObject();
        json.addProperty("opacity", OPACITY);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
