package mod.azure.iseelava;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.Minecraft;
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
                LavaConfig.OPACITY = (float) this.value;
                LavaConfig.saveConfig(); // Save to config file

                // Trigger a screen update to re-render lava with new opacity
                updateLavaOpacity();
            }

            @Override
            protected void applyValue() {
                LavaConfig.OPACITY = (float) this.value; // Update the opacity value
                LavaConfig.saveConfig(); // Save the updated value

                // Trigger a screen update to re-render lava with new opacity
                updateLavaOpacity();
            }
        };
        this.addRenderableWidget(opacitySlider);
    }

    // This function forces the lava opacity to be re-rendered immediately
    private void updateLavaOpacity() {
        // We need to notify the game to update the lava rendering based on the new opacity value.
        // Trigger a re-rendering of the fluid (lava) state with the updated opacity value.
        
        // Since the opacity is applied to lava through the `FluidRendererMixin`, we can simply mark it as dirty
        // or trigger a refresh in the game world (this is more efficient than resetting the entire screen).
        Minecraft.getInstance().levelRenderer.allChanged(); // Notify that something changed in the world
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor matrices, int mouseX, int mouseY, float delta) {
        this.extractBackground(matrices, mouseX, mouseY, delta);
        super.extractRenderState(matrices, mouseX, mouseY, delta);
        opacitySlider.extractRenderState(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
