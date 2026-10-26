package mod.azure.iseelava.mixin;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    @WrapOperation(
        method = "tesselate",
        at = @At("HEAD", value = "INVOKE", target = "Lnet/minecraft/util/ARGB;scaleRGB(IF)I")
    )
    private int iseelava$modifyLavaColor(int color, float scale, Operation<Integer> original, @Local(argsOnly = true) FluidState fluidState) {
        float opacity = LavaConfig.OPACITY;
 
        // Skip all work for fully opaque lava and for every other fluid.
        if (opacity < 1f && fluidState.getType().isSame(Fluids.LAVA)) {
            // Keeps RGB untouched to preserve vanilla lighting/color behavior.
            int alpha = Math.clamp((int) (ARGB.alpha(color) * opacity), 0, 255);
            color = ARGB.color(alpha, color & 0xFFFFFF);
        }
 
        return original.call(color, scale);
    }
}
