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
            target = "Lnet/minecraft/client/renderer/block/FluidRenderer;addFace"
        ),
        index = 20,
        remap = true
    )
    private int iseelava$modifyColor(int color) {
        
        FluidState fluidState = LavaConfig.currentFluidState;
        if (fluidState == null || fluidState.getType() != Fluids.LAVA) {
            return color;
        }

        int alpha = ARGB.alpha(color);
        int r = ARGB.red(color);
        int g = ARGB.green(color);
        int b = ARGB.blue(color);

        alpha = (int)(alpha * LavaConfig.OPACITY);

        return ARGB.color(alpha, r, g, b);
    }
}
