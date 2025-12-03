import imgui.*;
import imgui.internal.ImGuiContext;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL46;

import java.nio.ByteBuffer;

public class GUIRender {
    private GUIMesh guiMesh;
    private GLFWKeyCallback prevKeyCallBack;
    private Vector2f scale;
    private final Shader shader;
    private Texture texture;
    private UniformMap uniformMap;
    private final Window window;

    public GUIRender(Window window) {
        shader = new ShaderGUI();
        this.window = window;
        createUniforms();
        createUIResources(window);
        setupKeyCallBack(window);
    }

    private void createUIResources(Window window) {
        ImGuiContext context = ImGui.createContext();
        ImGuiIO imGuiIO = ImGui.getIO();
        imGuiIO.setIniFilename(null);
        imGuiIO.setDisplaySize(window.getWindowSize().x, window.getWindowSize().y);

        //Create font texture
        ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ByteBuffer buffer = fontAtlas.getTexDataAsRGBA32(width, height);
        texture = new Texture(width.get(), height.get(), buffer);
        fontAtlas.setTexID(texture.getTextureID());

        guiMesh = new GUIMesh();
    }

    private void createUniforms() {
        uniformMap = new UniformMap(shader.getProgramID());
        uniformMap.createUniform("scale");
        uniformMap.createUniform("textureSampler"); // I don't think this does anything
        scale = new Vector2f();
    }

    private void setupKeyCallBack(Window window) {
        prevKeyCallBack = GLFW.glfwSetKeyCallback(window.getWindowID(), this::callbackKey);
        GLFW.glfwSetCharCallback(window.getWindowID(), this::callbackChar);
    }

    private void callbackKey(long windowID, int key, int scancode, int action, int mods) {
        window.callbackKey(windowID, key, scancode, action, mods);
        ImGuiIO io = ImGui.getIO();
        if (!io.getWantCaptureKeyboard()) {
            return;
        }
        if (action == GLFW.GLFW_PRESS) {
            io.addKeyEvent(Bindings.getImKey(key), true);
        } else if (action == GLFW.GLFW_RELEASE) {
            io.addKeyEvent(Bindings.getImKey(key), false);
        }
    }

    private void callbackChar(long windowID, int c) {
        ImGuiIO io = ImGui.getIO();
        if (!io.getWantCaptureKeyboard()) {
            return;
        }
        io.addInputCharacter(c);
    }

    public void resize(Vector2i size) {
        ImGuiIO imGuiIO = ImGui.getIO();
        imGuiIO.setDisplaySize(size.x, size.y);
    }

    public void render(Scene scene) {
        GUIInstance guiInstance = scene.getGuiInstance();
        if (guiInstance == null) {
            return;
        }
        guiInstance.drawGUI();

        shader.start();


        //Temp setting change
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendEquation(GL46.GL_FUNC_ADD);
        GL46.glBlendFuncSeparate(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA, GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);

        //learn about theses
        //GL46.glDisable(GL46.GL_STENCIL_TEST);
        //GL46.glEnable(GL46.GL_SCISSOR_TEST);

        //GUI VAO
        GL46.glBindVertexArray(guiMesh.getVAOID());

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, guiMesh.getVerticesVBO());
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, guiMesh.getIndicesVBO());

        ImGuiIO io = ImGui.getIO();
        scale.x = 2.0f / io.getDisplaySizeX();
        scale.y = -2.0f / io.getDisplaySizeY();
        uniformMap.setUniform("scale", scale);
        uniformMap.setUniform("textureSampler", 0); // I don't think this does anything

        ImDrawData drawData = ImGui.getDrawData();


        for (int i = 0; i < drawData.getCmdListsCount(); i++) {
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, drawData.getCmdListVtxBufferData(i), GL46.GL_STREAM_DRAW);
            GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, drawData.getCmdListIdxBufferData(i), GL46.GL_STREAM_DRAW);

            for (int j = 0; j < drawData.getCmdListCmdBufferSize(i); j++) {
                final long textureId= drawData.getCmdListCmdBufferTextureId(i, j);
                final int elemCount = drawData.getCmdListCmdBufferElemCount(i, j);
                final int idxBufferOffset = drawData.getCmdListCmdBufferIdxOffset(i, j);
                final int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(i, j);
                final int indices = idxBufferOffset * ImDrawData.sizeOfImDrawIdx();


                GL46.glBindTexture(GL46.GL_TEXTURE_2D, (int) textureId);

                //Not sure how the two differ
                //GL46.glDrawElements(GL46.GL_TRIANGLES, elemCount, GL46.GL_UNSIGNED_SHORT, indices);
                GL46.glDrawElementsBaseVertex(GL46.GL_TRIANGLES, elemCount, GL46.GL_UNSIGNED_SHORT, indices, vtxOffset);
            }
        }

        //Reverting temp setting change
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glDisable(GL46.GL_BLEND);
    }


    public void cleanup() {
        shader.cleanup();
        texture.cleanup();
        if (prevKeyCallBack != null) {
            prevKeyCallBack.free();
        }
    }
}
