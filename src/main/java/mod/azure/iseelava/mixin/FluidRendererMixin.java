package mod.azure.iseelava.mixin;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    private FluidState capturedFluid;

    @Inject(
        method = "tesselate",
        at = @At("HEAD")
    )
    private void captureFluidState(
        BlockAndTintGetter level,
        BlockPos pos,
        FluidRenderer.Output output,
        BlockState blockState,
        FluidState fluidState,
        CallbackInfo ci
    ) {
        this.capturedFluid = fluidState;
    }

    @ModifyArg(
        method = "tesselate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ARGB;scaleRGB(IF)I"
        ),
        index = 0
    )
    private int modifyLavaColor(int color) {

        FluidState fluidState = this.capturedFluid;

        // Only modify lava fluids
        if (fluidState == null || !fluidState.getType().isSame(Fluids.LAVA)) {
            return color;
        }

        /*
        // Dampen Bright Sides
        float opacity = LavaConfig.OPACITY;
        int r = (int)(ARGB.red(color) * opacity);
        int g = (int)(ARGB.green(color) * opacity);
        int b = (int)(ARGB.blue(color) * opacity);
        int a = Math.clamp((int)(ARGB.alpha(color) * opacity), 0, 255);
        
        return ARGB.color(a, r, g, b);
        */

        // Orginal Vanilla Code Look
        int alpha = Math.clamp((int)(ARGB.alpha(color) * LavaConfig.OPACITY * 2), 0, 255);

        return ARGB.color(alpha, color & 0xFFFFFF);
    }
}
