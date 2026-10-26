package mod.azure.iseelava;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.network.chat.Component;

public class ModConfigScreen extends Screen {
    // Slider controlling lava opacity in real-time.
    private AbstractSliderButton opacitySlider;

    // Set when the opacity changed and chunks need to be rebuilt.
    private boolean dirty;

    public ModConfigScreen() {
        // Uses translation key (better for localization later)
        super(Component.translatable("Lava Config"));
    }

    @Override
    protected void init() {
        // Center of the screen
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Create Slider
        int sliderWidth = 200;
        int sliderX = centerX - (sliderWidth / 2);
        int sliderY = centerY - 20;
        
        // Slider value is normalized (0.0–1.0)
        opacitySlider = new AbstractSliderButton(sliderX, sliderY, sliderWidth, 20, Component.empty(), LavaConfig.OPACITY) {
            {
                updateMessage();
            }
            @Override
            protected void updateMessage() {
                // Called continuously while dragging.
                setMessage(Component.translatable("Opacity: " + Math.round(this.value * 100) + "%"));
            }
            @Override
            protected void applyValue() {
                // Called when the slider is released.
                LavaConfig.OPACITY = (float) this.value;

                // Force renderer update so lava changes instantly.
                dirty = true;
            }
        };
        this.addRenderableWidget(opacitySlider);

        // Create Button
        int buttonWidth = 200;
        int buttonX = centerX - (buttonWidth / 2);
        int buttonY = centerY + 20;
        
        // Standard "Done" button to close the screen.
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose()).bounds(buttonX, buttonY, buttonWidth, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        flushRebuild();
    }

    /**
     * Forces the world renderer to refresh.
     * This is required because fluid rendering is cached per chunk rebuild.
     */
    private flushRebuild() {
        if (dirty) {
            dirty = false;
            Minecraft.getInstance().levelExtractor.allChanged();
        }
    }

    @Override
    public void onClose() {
        flushRebuild();
        // Save config when the config screen is closed.
        LavaConfig.saveConfig();
        super.onClose();
    }
}
