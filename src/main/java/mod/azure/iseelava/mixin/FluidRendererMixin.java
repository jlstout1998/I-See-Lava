package mod.azure.iseelava.mixin;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

    @ModifyArg(
        method = "tesselate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ARGB;scaleRGB(IF)I"
        ),
        index = 0
    )
    private int modifyLavaColor(int color, FluidState fluidState) {

        // Only modify lava fluids
        if (fluidState == null || !fluidState.isSourceOfType(Fluids.LAVA)) {
            return color;
        }

        int alpha = Math.clamp((int)(ARGB.alpha(color) * LavaConfig.OPACITY), 0, 255);

        return ARGB.color(alpha, color & 0xFFFFFF);
    }
}
