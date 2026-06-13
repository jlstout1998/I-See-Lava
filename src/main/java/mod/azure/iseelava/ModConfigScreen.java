package mod.azure.iseelava;

import mod.azure.iseelava.LavaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.minecraft.client.renderer.state.level.LevelRenderState;

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
        // ATTEMPT TO REWRITE allChanged()
        if (this.level != null) {
            this.level.clearTintCaches();
            Options options = this.minecraft.options;
            boolean ambientOcclusion = options.ambientOcclusion().get();
            boolean cutoutLeaves = options.cutoutLeaves().get();
            ModelManager modelManager = this.minecraft.getModelManager();
            SectionCompiler sectionCompiler = new SectionCompiler(
                ambientOcclusion,
                cutoutLeaves,
                modelManager.getBlockStateModelSet(),
                modelManager.getFluidStateModelSet(),
                this.minecraft.getBlockColors()
            );
            if (this.sectionRenderDispatcher == null) {
                this.sectionRenderDispatcher = new SectionRenderDispatcher(Util.backgroundExecutor(), this.renderBuffers, sectionCompiler, this.sectionOcclusionGraph::schedulePropagationFrom);
            } else {
                this.sectionRenderDispatcher.setCompiler(sectionCompiler);
            }
            
            this.cloudRenderer.markForRebuild();
            LeavesBlock.setCutoutLeaves(cutoutLeaves);
            if (this.viewArea != null) {
                this.viewArea.releaseAllBuffers();
            }
            
            this.sectionRenderDispatcher.clearCompileQueue();
            this.viewArea = new ViewArea(this.sectionRenderDispatcher, this.level.getMinY(), this.level.getMaxY(), this.level.getMinSectionY(), this.level.getMaxSectionY(), options.getEffectiveRenderDistance(), this.sectionOcclusionGraph);
            this.sectionOcclusionGraph.waitAndReset(this.viewArea);
            this.clearVisibleSections();
            Camera camera = this.minecraft.gameRenderer.mainCamera();
            this.viewArea.repositionCamera(SectionPos.of(camera.position()));
        }
    }

    @Override
    public void onClose() {
        // Save config when the config screen is closed.
        LavaConfig.saveConfig();
        super.onClose();
    }
}
