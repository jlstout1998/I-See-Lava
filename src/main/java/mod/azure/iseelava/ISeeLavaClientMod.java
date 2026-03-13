package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

/**
 * Client-side initializer for the ISeeLava mod.
 */
public class ISeeLavaClientMod implements ClientModInitializer {
	public static final String ID = "iseelava";
	private static final Identifier TRANSLUCENT_LAVA_RP = Identifier.fromNamespaceAndPath(ID, "translucent_lava");

	@Override
	public void onInitializeClient() {		
		 // Register the built-in resource pack for translucent lava
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(ID);
		modContainer.ifPresent(container -> ResourceLoader.registerBuiltinPack(TRANSLUCENT_LAVA_RP, container, PackActivationType.DEFAULT_ENABLED));

		// Register lava with a translucent fluid model and fallback handler
        FluidModel.Unbaked model = new FluidModel.Unbaked(
                Identifier.fromNamespaceAndPath("minecraft", "block/lava_still"),
                Identifier.fromNamespaceAndPath("minecraft", "block/lava_flow"),
                null, // no overlay
                null  // no tint
        );

        FluidRenderHandler handler = FluidRenderingRegistry.getOverride(Fluids.LAVA);
        FluidRenderingRegistry.register(Fluids.LAVA, Fluids.FLOWING_LAVA, model, handler != null ? handler : FluidRenderHandler::renderFluid);
	}
}
