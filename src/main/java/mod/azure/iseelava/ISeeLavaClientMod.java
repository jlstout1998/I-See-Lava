package mod.azure.iseelava;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class ISeeLavaClientMod implements ClientModInitializer {
	public static final String ID = "iseelava";

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putFluid(Fluids.LAVA, RenderType.TRANSLUCENT);
		BlockRenderLayerMap.putFluid(Fluids.FLOWING_LAVA, RenderType.TRANSLUCENT);

		FabricLoader.getInstance().getModContainer(ID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(modResource("translucent_lava"), container, ResourcePackActivationType.DEFAULT_ENABLED);
		});
	}

	public static ResourceLocation modResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
}
