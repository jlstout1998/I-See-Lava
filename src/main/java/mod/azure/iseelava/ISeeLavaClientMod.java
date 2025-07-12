package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Client-side initializer for the ISeeLava mod.
 */
public class ISeeLavaClientMod implements ClientModInitializer {
	public static final String ID = "iseelava";
	private static final Identifier TRANSLUCENT_LAVA_RP = Identifier.of(ID, "translucent_lava");

	@Override
	public void onInitializeClient() {
		// Make lava translucent by assigning it to the translucent render layer
		BlockRenderLayerMap.putFluids(RenderLayer.getTranslucent(), Fluids.LAVA, Fluids.FLOWING_LAVA);

		 // Register the built-in resource pack for translucent lava
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(ID);
		modContainer.ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(TRANSLUCENT_LAVA_RP, container, ResourcePackActivationType.DEFAULT_ENABLED));
	}
}
