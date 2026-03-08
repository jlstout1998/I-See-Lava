package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer; // OPTIONAL
import net.minecraft.client.resources.model.ModelBakery; // OPTIONAL
import net.minecraft.client.resources.model.sprite.SpriteGetter; // OPTIONAL
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
		// Make lava translucent by assigning it to the translucent render layer
		FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, Fluids.FLOWING_LAVA, new SimpleFluidRenderHandler(ModelBakery.LAVA_STILL, ModelBakery.LAVA_FLOW) {
				// OPTIONAL - force translucent render layer
				@Override
				public ChunkSectionLayer reloadTextures(SpriteGetter spriteGetter) {
					super.reloadTextures(spriteGetter);
					return ChunkSectionLayer.TRANSLUCENT;
				}
			}
		);
		
		 // Register the built-in resource pack for translucent lava
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(ID);
		modContainer.ifPresent(container -> ResourceLoader.registerBuiltinPack(TRANSLUCENT_LAVA_RP, container, PackActivationType.DEFAULT_ENABLED));
	}
}
