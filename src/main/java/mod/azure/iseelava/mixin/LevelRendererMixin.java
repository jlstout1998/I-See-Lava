@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LavaRenderReload {

    @Shadow private ClientLevel level;
    @Shadow private SectionRenderDispatcher sectionRenderDispatcher;
    @Shadow private RenderBuffers renderBuffers;
    @Shadow private SectionOcclusionGraph sectionOcclusionGraph;
    @Shadow private CloudRenderer cloudRenderer;
    @Shadow private ViewArea viewArea;

    @Shadow protected abstract void clearVisibleSections();

    @Override
    public void iseelava$reloadLavaRender() {
        if (this.level == null) return;
        this.level.clearTintCaches();
        Options options = Minecraft.getInstance().options;
        boolean ambientOcclusion = options.ambientOcclusion().get();
        boolean cutoutLeaves = options.cutoutLeaves().get();
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        SectionCompiler sectionCompiler = new SectionCompiler(
                ambientOcclusion,
                cutoutLeaves,
                modelManager.getBlockStateModelSet(),
                modelManager.getFluidStateModelSet(),
                Minecraft.getInstance().getBlockColors()
        );
        if (this.sectionRenderDispatcher == null) {
            this.sectionRenderDispatcher = new SectionRenderDispatcher(
                    Util.backgroundExecutor(),
                    this.renderBuffers,
                    sectionCompiler,
                    this.sectionOcclusionGraph::schedulePropagationFrom
            );
        } else {
            this.sectionRenderDispatcher.setCompiler(sectionCompiler);
        }

        this.cloudRenderer.markForRebuild();
        LeavesBlock.setCutoutLeaves(cutoutLeaves);
        if (this.viewArea != null) {
            this.viewArea.releaseAllBuffers();
        }

        this.sectionRenderDispatcher.clearCompileQueue();
        this.viewArea = new ViewArea(
                this.sectionRenderDispatcher,
                this.level.getMinY(),
                this.level.getMaxY(),
                this.level.getMinSectionY(),
                this.level.getMaxSectionY(),
                options.getEffectiveRenderDistance(),
                this.sectionOcclusionGraph
        );
        this.sectionOcclusionGraph.waitAndReset(this.viewArea);
        this.clearVisibleSections();
        Camera camera = Minecraft.getInstance().gameRenderer.mainCamera();
        this.viewArea.repositionCamera(SectionPos.of(camera.getPosition()));
    }
}
