package mod.azure.iseelava;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigScreen extends Screen {
    // Slider controlling lava opacity in real-time.
    private AbstractSliderButton opacitySlider;

    public ModConfigScreen() {
        // Uses translation key (better for localization later)
        super(Component.translatable("Lava Config"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Slider value is normalized (0.0–1.0)
        opacitySlider = new AbstractSliderButton(centerX - 100, centerY - 20, 200, 20, Component.translatable("Opacity"), LavaConfig.OPACITY) {
            @Override
            protected void updateMessage() {
                // Called continuously while dragging.
                LavaConfig.OPACITY = (float) this.value;

                // Force renderer update so lava changes instantly.
                updateLavaOpacity();
            }

            @Override
            protected void applyValue() {
                // Called when the slider is released.
                LavaConfig.OPACITY = (float) this.value;

                // Persist change to disk.
                LavaConfig.saveConfig();

                // Force renderer update so lava changes instantly.
                updateLavaOpacity();
            }
        };
        this.addRenderableWidget(opacitySlider);

        // Standard "Done" button to close the screen.
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose()).bounds(centerX - 100, centerY + 20, 200, 20).build());
    }
    
    /**
     * Forces the world renderer to refresh.
     * This is required because fluid rendering is cached per chunk rebuild.
     */
    private void updateLavaOpacity() {
        Minecraft.getInstance().levelRenderer.allChanged();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor matrices, int mouseX, int mouseY, float delta) {
        this.extractBackground(matrices, mouseX, mouseY, delta);
        super.extractRenderState(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
        super.onClose();
    }
}
