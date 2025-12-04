import java.nio.IntBuffer;
import java.util.logging.Logger;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private long windowID;
    private final String windowTitle;
    private final Vector2i windowSize;
    private int vSyncSetting;
    private MouseInput mouseInput;

    private static final Logger logger = Logger.getLogger(Window.class.toString());

    public Window(int width, int height, String title, boolean vSync){
        windowTitle = title;
        windowSize = new Vector2i(width,height);
        if (vSync){ vSyncSetting = 1;}
        else { vSyncSetting = 0;}
        init();
        mouseInput = new MouseInput(windowID);
    }

    private void init() {
        //TODO: set the following env var
        //__GL_THREADED_OPTIMIZATIONS=0

        // Start up GLFW so we can use it
        if ( !GLFW.glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");

        // Configure basic GLFW settings
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
        //GLFW.glfwWindowHint(GLFW.GLFW_SCALE_TO_MONITOR, GLFW.GLFW_TRUE); // scale the window to the monitor?

        // Create the window
        windowID = GLFW.glfwCreateWindow(windowSize.x, windowSize.y, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);
        if ( windowID == MemoryUtil.NULL ) throw new RuntimeException("Failed to create the GLFW window");
        // Set up callbacks
        GLFW.glfwSetKeyCallback(windowID, this::callbackKey);
        //GLFW.glfwSetFramebufferSizeCallback(windowID, this::callbackFrameBufferSize);
        GLFW.glfwSetWindowContentScaleCallback(windowID, this::callbackWindowContentScale);
        GLFW.glfwSetErrorCallback(this::callbackError);

        //Window settings
        GLFW.glfwSetWindowSizeLimits(windowID, 640, 360, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);
        //GLFW.glfwSetWindowAspectRatio(windowID,16,9);

        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size, monitor resolution and center the window
            GLFW.glfwGetWindowSize(windowID, pWidth, pHeight);
            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(windowID, (videoMode.width() - pWidth.get(0)) / 2, (videoMode.height() - pHeight.get(0)) / 2);
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(windowID);
        // Enable v-sync based on setting
        GLFW.glfwSwapInterval(vSyncSetting);
        // Show the window
        GLFW.glfwShowWindow(windowID);
    }



    public boolean shouldClose(){
        return GLFW.glfwWindowShouldClose(windowID);
    }

    public void update(){
        // Swap to the rendered frame
        GLFW.glfwSwapBuffers(windowID);
    }
    public void pollEvents(){
        GLFW.glfwPollEvents(); // Gather user input
    }

    public void cleanup(){
        Callbacks.glfwFreeCallbacks(windowID);
        GLFW.glfwDestroyWindow(windowID);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    //Getters
    public Vector2i getWindowSize(){
        return windowSize;
    }
    public MouseInput getMouseInput(){
        return mouseInput;
    }
    public long getWindowID() {
        return windowID;
    }
    public boolean isKeyPressed(int keyCode){
        return GLFW.glfwGetKey(windowID, keyCode) == GLFW.GLFW_PRESS;
    }

    private void callbackFrameBufferSize(long windowID, int width, int height){
        GL46.glViewport(0,0,width,height);
        windowSize.x = width;
        windowSize.y = height;
    }
    public void callbackKey(long windowID, int key, int scancode, int action, int mods){
        //May be overridden
    }
    private void callbackError(int errorCode, long msgPtr){
        logger.severe(String.format("Error code %1$s, msg %2$s", errorCode, MemoryUtil.memUTF8(msgPtr)));
    }
    private void callbackWindowContentScale(long window, float xScale, float yScale){
    }


}
