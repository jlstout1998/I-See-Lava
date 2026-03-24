package mod.azure.iseelava.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer")
public interface DefaultFluidRendererAccessor {

    @Accessor("quadColors")
    int[] getQuadColors();
}
