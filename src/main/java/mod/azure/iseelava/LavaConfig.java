package mod.azure.iseelava;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * Holds the configuration for lava opacity and the currently processed fluid.
 */
public class LavaConfig {

    /** Opacity factor for lava (0.0 = fully transparent, 1.0 = fully opaque) */
    public static final float OPACITY = 0.5f; // adjust as desired

    /** Current fluid being rendered (set during tesselation) */
    public static FluidState currentFluidState = null;

    /** Utility to check if a fluid is lava */
    public static boolean isLava(FluidState fluid) {
        return fluid != null && fluid.getType() == Fluids.LAVA;
    }
}
