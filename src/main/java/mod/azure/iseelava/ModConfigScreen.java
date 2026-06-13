package mod.azure.iseelava;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import mod.azure.iseelava.LavaRenderReload;
import net.minecraft.client.renderer.LevelRenderer;

public class ModConfigScreen extends Screen {
    // Slider controlling lava opacity in real-time.
    private AbstractSliderButton opacitySlider;

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
        opacitySlider = new AbstractSliderButton(sliderX, sliderY, sliderWidth, 20, Component.translatable("Opacity"), LavaConfig.OPACITY) {
            @Override
            protected void updateMessage() {
                // Called continuously while dragging.
                LavaConfig.OPACITY = (float) this.value;
            }
            @Override
            protected void applyValue() {
                // Called when the slider is released.
                LavaConfig.OPACITY = (float) this.value;

                // Force renderer update so lava changes instantly.
                updateLavaOpacity();
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
    
    /**
     * Forces the world renderer to refresh.
     * This is required because fluid rendering is cached per chunk rebuild.
     */
    private void updateLavaOpacity() {
        // Minecraft.getInstance().levelRenderer.allChanged();
        /* // ATTEMPT TO REWRITE allChanged()
        if (Minecraft.getInstance().levelRenderer instanceof LavaRenderReload reload) {
            reload.iseelava$reloadLavaRender();
        }
        */
        if (Minecraft.getInstance().levelRenderer == null) return;
        // 1. Force a render pipeline reset (this is safe and exists in your version)
        Minecraft.getInstance().levelRenderer.endFrame();
        // 2. Rebuild visible section tracking (forces rescheduling next frame)
        Minecraft.getInstance().levelRenderer.clearVisibleSections();
        // 3. Force chunk section recompilation scheduling
        if (Minecraft.getInstance().levelRenderer.visibleSections() != null) {
            for (var section : Minecraft.getInstance().levelRenderer.visibleSections()) {
                section.compileAsync(section.getRegion());
            }
        }
        if (Minecraft.getInstance().levelRenderer.nearbyVisibleSections() != null) {
            for (var section : Minecraft.getInstance().levelRenderer.nearbyVisibleSections()) {
                section.compileAsync(section.getRegion());
            }
        }
        // 4. Force GPU upload pass (ensures no delay waiting for natural flush)
        if (Minecraft.getInstance().levelRenderer.sectionRenderDispatcher() != null) {
            Minecraft.getInstance().levelRenderer.sectionRenderDispatcher().uploadTerrainBuffersToGpu();
        }
    }

    @Override
    public void onClose() {
        // Save config when the config screen is closed.
        LavaConfig.saveConfig();
        super.onClose();
    }
}
