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
    /**
     * Stores the current FluidState being rendered.
     *
     * NOTE:
     * This relies on the fact that tesselate() is currently called synchronously.
     * If Mojang parallelizes chunk rendering further, this could become unsafe.
     */
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

        // Only apply effect to lava
        if (fluidState == null || !fluidState.getType().isSame(Fluids.LAVA)) {
            return color;
        }

        // Scale alpha channel based on config.
        // Keeps RGB untouched to preserve vanilla lighting/color behavior.
        int alpha = Math.clamp((int)(ARGB.alpha(color) * LavaConfig.OPACITY), 0, 255);

        return ARGB.color(alpha, color & 0xFFFFFF);
    }
}
