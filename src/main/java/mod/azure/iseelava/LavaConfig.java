package mod.azure.iseelava;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class LavaConfig {
    // 0.0 = fully invisible, 1.0 = fully opaque
    public static final float DEFAULT_OPACITY = 0.55f;

    // Mutable runtime value
    public static volatile float OPACITY = DEFAULT_OPACITY;

    // Config file stored in the standard Fabric config directory.
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("iseelava_config.json");
    
    private static final Gson GSON = new Gson();

    /**
     * Loads configuration from disk.
     * If the file does not exist or is malformed, defaults are preserved.
     */
    public static void loadConfig() {
        if (!Files.exist(CONFIG_FILE)) return;
        
        try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json != null && json.has("opacity")) {
                float value = json.get("opacity").getAsFloat();
                OPACITY = Float.isFinite(value) ? Math.clamp(value, 0f, 1f) : DEFAULT_OPACITY;
            } else {
                OPACITY = DEFAULT_OPACITY;
            }
        } catch (IOException | RuntimeException e) {
            // Log instead of crashing — config errors should not break the game.
            e.printStackTrace();
            OPACITY = DEFAULT_OPACITY;
        }
    }

    /**
     * Saves current configuration values to disk.
     */
    public static void saveConfig() {
        // Ensure config directory exists before writing.        
        JsonObject json = new JsonObject();
        json.addProperty("opacity", OPACITY);

        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            Path temp = CONFIG_FILE.resolveSibling(CONFIG_FILE.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(temp)) {
                GSON.toJson(json, writer);
            }
            try {
                Files.move(temp, CONFIG_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicFailed) {
                Files.move(temp, CONFIG_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
