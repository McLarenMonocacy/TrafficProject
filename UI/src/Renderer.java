import org.joml.Vector2i;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

public class Renderer {
    private final SceneRenderer sceneRenderer;
    private final GUIRender guiRender;
    public Renderer(Window window){
        GL.createCapabilities();
        // Set the clear color (black)
        GL46.glClearColor(0.0f, 0.0f, 0.0f,0f);
        // Enable depth testing and backface culling
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);

        sceneRenderer = new SceneRenderer();
        guiRender = new GUIRender(window);
    }

    public void init() throws Exception{

    }

    public void render(Window window, Scene scene){
        clear();
        sceneRenderer.render(scene);
        guiRender.render(scene);
    }

    public void cleanup(){
        sceneRenderer.cleanup();
        guiRender.cleanup();
    }

    public void clear(){
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public void resize(Vector2i size){
        guiRender.resize(size);
    }
}
