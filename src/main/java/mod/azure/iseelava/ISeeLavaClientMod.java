package mod.azure.iseelava;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class ISeeLavaClientMod implements ClientModInitializer {
    public static final String ID = "iseelava";
    private static final Identifier TRANSLUCENT_LAVA_RP = Identifier.fromNamespaceAndPath(ID, "translucent_lava");
    private static KeyMapping configKey;

    @Override
    public void onInitializeClient() {
        // Register the built-in resource pack for translucent lava
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(ID);
        modContainer.ifPresent(container -> ResourceLoader.registerBuiltinPack(TRANSLUCENT_LAVA_RP, container, PackActivationType.DEFAULT_ENABLED));

        // Load the config on mod startup
        LavaConfig.loadConfig();

        // Register the category for key mappings
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ID, "category"));

        // Register the keybinding with a category
        configKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.iseelava.config", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, category));

        // Register the ClientTickCallback to check key press on each tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.isDown()) {
                Minecraft.getInstance().setScreen(new ModConfigScreen()); // Open config screen
            }
        });
    }
}
