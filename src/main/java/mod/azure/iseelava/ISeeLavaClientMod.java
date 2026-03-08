package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
// import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
// import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Client-side initializer for the ISeeLava mod.
 */
public class ISeeLavaClientMod implements ClientModInitializer {
	public static final String ID = "iseelava";
	private static final Identifier TRANSLUCENT_LAVA_RP = Identifier.fromNamespaceAndPath(ID, "translucent_lava");

	@Override
	public void onInitializeClient() {
		// Make lava translucent by assigning it to the translucent render layer
		// ChunkSectionLayerMap.putFluids(ChunkSectionLayer.TRANSLUCENT, Fluids.LAVA, Fluids.FLOWING_LAVA);

		 // Register the built-in resource pack for translucent lava
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(ID);
		modContainer.ifPresent(container -> ResourceLoader.registerBuiltinPack(TRANSLUCENT_LAVA_RP, container, PackActivationType.DEFAULT_ENABLED));

		// Register a FluidRenderHandler for lava using alpha textures
		FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, Fluids.FLOWING_LAVA, new SimpleFluidRenderHandler(new Identifier("iseelava", "block/lava_still"), new Identifier("iseelava", "block/lava_flow")));
	}
}
