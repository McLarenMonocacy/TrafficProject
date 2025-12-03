import org.lwjgl.glfw.GLFW;

public class Engine{

    private final ProgramLogic programLogic;
    private final Window window;
    private final Renderer renderer;
    private final Scene scene;

    int targetUps = 60;
    int targetFps = 60;
    public Engine(int width, int height, String title, boolean vSync, ProgramLogic programLogic){

        window = new Window(width, height, title, vSync);
        this.programLogic = programLogic;
        renderer = new Renderer(window);
        scene = new Scene(window.getWindowSize());
        programLogic.init(window,scene,renderer);
        GLFW.glfwSetWindowMaximizeCallback(window.getWindowID(), this::callbackWindowMaximize);
        GLFW.glfwSetWindowSizeCallback(window.getWindowID(), this::callbackWindowSize);
    }

    public Engine() throws Exception{
        this(640,360,"Unnamed window", true, new ProgramLogic());
    }

    public void start (){
        run();
    }

    private void cleanup(){
        programLogic.cleanup();
        renderer.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void run(){
        long endTime = System.currentTimeMillis();
        float timeU = 1000f/targetUps;
        float timeR = 0;
        if (targetFps > 0) timeR = 1000f/targetFps;

        float deltaUpdate = 0;
        float deltaFps = 0;

        long lastUpdateTime = endTime;

        GUIInstance guiInstance = scene.getGuiInstance();
        while (!window.shouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            long diffTimeMillis = now - endTime; // The difference between now and the last time
            deltaUpdate += (diffTimeMillis) / timeU;
            deltaFps += (diffTimeMillis) / timeR;

            //TODO: fix lag causing an input spike, also on a different computer the movement speed is different
            if (targetFps <= 0 || deltaFps >= 1){
                //Every frame get inputs
                window.getMouseInput().input(window);
                boolean inputConsumer = guiInstance != null && guiInstance.handleGUIInput(scene,window);
                //TODO: move input actions into deltaUpdate (i.e. only gather the inputs here), this should (?) fix the previous todo.
                programLogic.input(window, scene, diffTimeMillis, inputConsumer);
            }

            if (deltaUpdate >= 1){
                // Runs one update cycle
                programLogic.update(window, scene, now-lastUpdateTime);
                lastUpdateTime = now;
                deltaUpdate -= 1;
            }


            if (targetFps <= 0 || deltaFps >= 1){
                // Render frame
                renderer.render(window, scene);
                while (deltaFps >= 1)
                    deltaFps -= 1;
                window.update();
            }

            endTime = now;
        }
        cleanup();
    }

    private void resize() {
        scene.resizeProjection(window.getWindowSize());
        renderer.resize(window.getWindowSize());
    }


    private void callbackWindowMaximize(long windowID, boolean bool) {
        resize();
    }

    private void callbackWindowSize(long l, int i, int i1) {
        resize();
    }
}
