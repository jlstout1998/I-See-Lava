package mod.azure.iseelava.mixin.sodium;

import mod.azure.iseelava.LavaConfig;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.light.LightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadViewMutable;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer")
public class SodiumFluidRendererMixin {

    @Shadow
    @Final
    private int[] quadColors;

    @Inject(method = "updateQuad", at = @At("TAIL"))
    private void iseelava$modifyLavaAlpha(
        ModelQuadViewMutable quad,
        LevelSlice level,
        BlockPos pos,
        LightPipeline lighter,
        Direction dir,
        ModelQuadFacing facing,
        float brightness,
        ColorProvider<FluidState> colorProvider,
        FluidState fluidState,
        CallbackInfo ci
    ) {
        float opacity = LavaConfig.OPACITY;
 
        // Nothing to do for fully opaque lava or other fluids.
        if (opacity >= 1f || !fluidState.getType().isSame(Fluids.LAVA)) {
            return;
        }
 
        int[] colors = this.quadColors;
 
        for (int i = 0; i < 4; i++) {
            int color = colors[i]; // ARGB
 
            int alpha = ColorARGB.unpackAlpha(color);
            int newAlpha = Math.clamp((int) (alpha * opacity), 0, 255);
 
            colors[i] = ColorARGB.withAlpha(color, newAlpha);
        }
    }
}
