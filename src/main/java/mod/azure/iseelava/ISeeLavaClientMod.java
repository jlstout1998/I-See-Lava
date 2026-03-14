package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
// import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
// import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
// import net.minecraft.client.renderer.block.FluidModel;
// import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
// import net.minecraft.world.level.material.Fluids;

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

/*
		// Register lava with a translucent fluid model and fallback handler
		FluidModel.Unbaked model = new FluidModel.Unbaked(
        		new Material(Identifier.fromNamespaceAndPath("translucent_lava", "block/lava_still")),
        		new Material(Identifier.fromNamespaceAndPath("translucent_lava", "block/lava_flow")),
       			null,   // overlay material
        		null    // tint source
		);

		FluidRenderHandler handler = FluidRenderingRegistry.getOverride(Fluids.LAVA);
		if (handler == null) {
    		handler = FluidRenderingRegistry.get(Fluids.LAVA);
		}
		FluidRenderingRegistry.register(Fluids.LAVA, Fluids.FLOWING_LAVA, model, handler);
*/
	}
}
