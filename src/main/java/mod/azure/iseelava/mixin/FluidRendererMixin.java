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

    // Store the fluid currently being tessellated
    private FluidState currentFluid;

    /**
     * Capture the FluidState at the start of tesselation.
     */
    @Inject(
        method = "tesselate",
        at = @At("HEAD")
    )
    private void captureFluid(
        BlockAndTintGetter level,
        BlockPos pos,
        FluidRenderer.Output output,
        BlockState blockState,
        FluidState fluidState,
        CallbackInfo ci
    ) {
        this.currentFluid = fluidState;
    }

    /**
     * Modify lava alpha/opacity only.
     */
    @ModifyArg(
        method = "tesselate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ARGB;scaleRGB(IF)I"
        ),
        index = 0
    )
    private int modifyLavaColor(int color) {
        if (currentFluid == null || !currentFluid.getType().isSame(Fluids.LAVA)) {
            return color;
        }

        int alpha = Math.clamp((int)(ARGB.alpha(color) * LavaConfig.OPACITY), 0, 255);
        return ARGB.color(alpha, color & 0xFFFFFF);
    }

    /**
     * Disable back face rendering for flowing lava only.
     */
    @ModifyArg(
        method = "tesselate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/FluidRenderer;addFace(" +
                     "Lcom/mojang/blaze3d/vertex/VertexConsumer;" +
                     "FFFFFFFFFFFFII" + // placeholder for floats/ints
                     "Z)V"
        ),
        index = 22 // index of addBackFace boolean in addFace call
    )
    private boolean disableBackFaceForFlowingLava(boolean original) {
        return currentFluid != null && currentFluid.getType().isSame(Fluids.LAVA) && !currentFluid.isSource();
    }
}
