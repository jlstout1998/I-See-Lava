package mod.azure.iseelava;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LavaConfig {
    // Opacity value for lava (0.0 to 1.0)
    public static float OPACITY = 0.55f;

    private static final File CONFIG_FILE = new File("config/iseelava_config.json");
    
    private static final Gson GSON = new Gson();

    // Load the configuration from the file
    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                JsonObject json = Gson.fromJson(reader, JsonObject.class);
                // Load the opacity value
                OPACITY = json.has("opacity") ? json.get("opacity").getAsFloat() : 1.0f;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save the opacity value to the configuration file
    public static void saveConfig() {
        JsonObject json = new JsonObject();
        json.addProperty("opacity", OPACITY);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Gson.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
