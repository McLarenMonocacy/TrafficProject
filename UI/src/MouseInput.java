import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class MouseInput {
    //TODO: could this be combined into inputHandler

    public enum State{
        NONE,
        PRESSED,
        HELD,
        RELEASED;
    }

    private final Vector2f currentPos;
    private final Vector2f previousPos;
    private final Vector2f displacementVector;
    private boolean inWindow;
    private final State[] mouseButtons = new State[8];

    public MouseInput(long windowID){
        previousPos = new Vector2f(-1,-1);
        currentPos = new Vector2f();
        displacementVector = new Vector2f();
        inWindow = false;
        Arrays.fill(mouseButtons, State.NONE);

        GLFW.glfwSetCursorPosCallback(windowID,this::callbackCursorPos);
        GLFW.glfwSetCursorEnterCallback(windowID, this::callbackCursorEnterWindow);
        GLFW.glfwSetMouseButtonCallback(windowID, this::callbackMouseButton);
    }

    public void pollMouseButtons(Window window){
        //Polls mouse button 1 through 8
        int i = 0;
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_1));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_2));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_3));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_4));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_5));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_6));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_7));
        setButtonState(i++,GLFW.glfwGetMouseButton(window.getWindowID(),GLFW.GLFW_MOUSE_BUTTON_8));
    }

    private void setButtonState(int index, int state){
        switch (mouseButtons[index]){
            case NONE -> {
                if (state == GLFW.GLFW_PRESS) mouseButtons[index] = State.PRESSED;
            }
            case PRESSED -> {
                if (state == GLFW.GLFW_PRESS) mouseButtons[index] = State.HELD;
                else mouseButtons[index] = State.RELEASED;
            }
            case HELD -> {
                if (state == GLFW.GLFW_RELEASE) mouseButtons[index] = State.RELEASED;
            }
            case RELEASED -> {
                if (state == GLFW.GLFW_PRESS) mouseButtons[index] = State.PRESSED;
                else mouseButtons[index] = State.NONE;
            }
        }
    }

    private void callbackCursorPos(long windowID, double xPos, double yPos) {
        currentPos.x = (float) xPos;
        currentPos.y = (float) yPos;
    }
    private void callbackCursorEnterWindow(long windowID, boolean inWindow){
        this.inWindow = inWindow;
    }
    private void callbackMouseButton(long windowID, int button, int action, int mode){
        //Scroll wheel?
    }

    //Getters
    public Vector2f getCurrentPos(){
        return currentPos;
    }
    public Vector2f getDisplacementVector(){
        return displacementVector;
    }
    public State getMouseState(int mouseButton){
        //Valid mouse buttons are 1 through 8
        if (mouseButton > 0 && mouseButton < 9){
            return mouseButtons[mouseButton-1];
        }
        return null;
    }

    public void input(Window window){
        pollMouseButtons(window);
        displacementVector.x = 0;
        displacementVector.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow){

            float deltaX = currentPos.x - previousPos.x;
            float deltaY = currentPos.y - previousPos.y;
            boolean rotateX = (deltaX != 0);
            boolean rotateY = (deltaY != 0);
            if (rotateX){
                displacementVector.y = deltaX;
            }
            if (rotateY){
                displacementVector.x = deltaY;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

}
