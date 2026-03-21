package mod.azure.iseelava;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigScreen extends Screen {
    private AbstractSliderButton opacitySlider;

    public ModConfigScreen() {
        super(Component.translatable("Lava Config"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Create the opacity slider (range 0.0 - 1.0)
        opacitySlider = new AbstractSliderButton(centerX - 100, centerY - 20, 200, 20, Component.translatable("Opacity"), LavaConfig.OPACITY) {
            @Override
            protected void updateMessage() {
                // Update the opacity in LavaConfig
                LavaConfig.OPACITY = (float) this.getValue();
                LavaConfig.saveConfig(); // Save to config file
            }

            @Override
            protected void applyValue() {
                LavaConfig.OPACITY = (float) this.getValue(); // Update the opacity value
                LavaConfig.saveConfig(); // Save the updated value
            }
        };
        this.addRenderableWidget(opacitySlider);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor matrices, int mouseX, int mouseY, float delta) {
        this.extractBackground(matrices);
        super.extractRenderState(matrices, mouseX, mouseY, delta);
        opacitySlider.extractRenderState(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
