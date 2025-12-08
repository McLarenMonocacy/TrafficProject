import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class InputHandler {
    //Converts keyboard inputs into actions so that the inputs can be changed later
    public enum PlayerActions{
        MoveUp,
        MoveDown,
        MoveLeft,
        MoveRight,
        MoveForward,
        MoveBackwards,
        Close
    }
    public enum InputKeyState {
        None,
        Pressed,
        Held,
        Released
    }

    public static final InputKeyState[] INPUTS = initInputsArray();
    private static InputKeyState[] initInputsArray(){
        InputKeyState[] output = new InputKeyState[PlayerActions.values().length];
        Arrays.fill(output, InputKeyState.None);
        return output;
    }
    private static final Map<PlayerActions, Integer> keyBinds = loadKeyBinds();
    private static Map<PlayerActions, Integer> loadKeyBinds(){
        Map<PlayerActions, Integer> keyBinds = new HashMap<>();
        keyBinds.put(PlayerActions.MoveUp, GLFW.GLFW_KEY_W);
        keyBinds.put(PlayerActions.MoveDown, GLFW.GLFW_KEY_S);
        keyBinds.put(PlayerActions.MoveLeft, GLFW.GLFW_KEY_A);
        keyBinds.put(PlayerActions.MoveRight, GLFW.GLFW_KEY_D);
        keyBinds.put(PlayerActions.MoveForward, GLFW.GLFW_KEY_E);
        keyBinds.put(PlayerActions.MoveBackwards, GLFW.GLFW_KEY_Q);
        keyBinds.put(PlayerActions.Close, GLFW.GLFW_KEY_ESCAPE);

        return keyBinds;
    }


    private InputHandler(){}//Non instancing class

    public static void updateInputs(Window window){
        for (PlayerActions action : PlayerActions.values()){
            updateActionState(action, GLFW.glfwGetKey(window.getWindowID(), keyBinds.get(action)));
        }
    }

    public static boolean checkAction(PlayerActions actions, InputKeyState state){
        //Returns if the given action is currently in the given state
        return InputHandler.INPUTS[actions.ordinal()] == state;
    }

    private static void updateActionState(PlayerActions action, int state){
        //Update the action state based on the new state
        switch (INPUTS[action.ordinal()]){
            case InputKeyState.None -> {
                if (state == GLFW.GLFW_PRESS) INPUTS[action.ordinal()] = InputKeyState.Pressed;
            }
            case InputKeyState.Pressed -> {
                if (state == GLFW.GLFW_PRESS) INPUTS[action.ordinal()] = InputKeyState.Held;
                else INPUTS[action.ordinal()] = InputKeyState.Released;
            }
            case InputKeyState.Held -> {
                if (state == GLFW.GLFW_RELEASE) INPUTS[action.ordinal()] = InputKeyState.Released;
            }
            case InputKeyState.Released -> {
                if (state == GLFW.GLFW_PRESS) INPUTS[action.ordinal()] = InputKeyState.Pressed;
                else INPUTS[action.ordinal()] = InputKeyState.None;
            }
        }
    }
}
