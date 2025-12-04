import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector2f;
import java.util.Arrays;

public class GUI implements GUIInstance {
    public enum ButtonID {
        ADD,
        SUB,
        MOVE,
        CONNECT,
        QUERY,
        SAVE
    }

    private class Button {
        int buttonTextureID;
        private final ImVec2 offUV0;
        private final ImVec2 offUV1;
        private final ImVec2 onUV0;
        private final ImVec2 onUV1;
        boolean isPressed = false;

        Button(String buttonTextureFilePath, ImVec4 offUVs, ImVec4 onUVs) {
            this.buttonTextureID = textureCache.createTexture(buttonTextureFilePath).getTextureID();
            //Off state
            this.offUV0 = new ImVec2(offUVs.x, offUVs.y);
            this.offUV1 = new ImVec2(offUVs.z, offUVs.w);

            //On state
            this.onUV0 = new ImVec2(onUVs.x, onUVs.y);
            this.onUV1 = new ImVec2(onUVs.z, onUVs.w);
        }

        void toggle() {
            isPressed = !isPressed;
        }

        void set(boolean bool) {
            isPressed = bool;
        }

        ImVec2 getUV0() {
            if (isPressed) {
                return onUV0;
            } else {
                return offUV0;
            }
        }

        ImVec2 getUV1() {
            if (isPressed) {
                return onUV1;
            } else {
                return offUV1;
            }
        }
    }

    private static final String TEXTURE_BUTTON = "UI/res/texture/button.bmp";

    private final TextureCache textureCache;
    private final Button[] buttons = new Button[ButtonID.values().length];

    private final ImBoolean showQueryWindow = new ImBoolean(false);
    private Entity queriedEntity;
    private Entity lastQueriedEntity;
    private boolean queriedEntityChanged = false;
    private final ImString queryWindowTextInputField1 = new ImString();
    private final ImString queryWindowTextInputField2 = new ImString();

    public GUI(Scene scene) {
        scene.setGuiInstance(this);
        textureCache = scene.getTextureCache();
        Arrays.fill(buttons, null);

        buttons[ButtonID.ADD.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0f, 0.00f, 0.25f, 0.25f), new ImVec4(0.25f, 0.00f, 0.50f, 0.25f));
        buttons[ButtonID.SUB.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0f, 0.25f, 0.25f, 0.50f), new ImVec4(0.25f, 0.25f, 0.50f, 0.50f));
        buttons[ButtonID.MOVE.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0f, 0.50f, 0.25f, 0.75f), new ImVec4(0.25f, 0.50f, 0.50f, 0.75f));
        buttons[ButtonID.CONNECT.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0f, 0.75f, 0.25f, 1.00f), new ImVec4(0.25f, 0.75f, 0.50f, 1.00f));

        buttons[ButtonID.QUERY.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0.5f, 0.00f, 0.75f, 0.25f), new ImVec4(0.75f, 0.00f, 1f, 0.25f));
        buttons[ButtonID.SAVE.ordinal()] = new Button(TEXTURE_BUTTON, new ImVec4(0.5f, 0.25f, 0.75f, 0.50f), new ImVec4(0.75f, 0.25f, 1f, 0.50f));

    }

    public void drawGUI() {
        ImGuiIO io = ImGui.getIO();

        //Get display size for UI size math
        Vector2f displaySize = new Vector2f(io.getDisplaySizeX(), io.getDisplaySizeY());
        float buttonSize = displaySize.y / 8;
        // Remove the padding around image buttons
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0, 0);

        //Start of the sub window
        ImGui.newFrame();
        ImGui.begin("Should not see this", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDecoration);
        ImGui.setWindowSize(buttonSize, buttonSize * buttons.length);
        ImGui.setWindowPos(0, 0);

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) continue;
            ImGui.setCursorPos(0, buttonSize * i);
            imGuiImageButton("button_" + i, buttons[i], buttonSize, buttonSize);
            if (buttons[i].isPressed) {
                for (Button otherButton : buttons) {
                    otherButton.set(false);
                }
                buttons[i].set(true);
            }
        }

        ImGui.end();
        if (queriedEntity != null) {
            //Check if the queriedEntity is a new entity
            if (lastQueriedEntity == null || lastQueriedEntity != queriedEntity) {
                queriedEntityChanged = true;
                showQueryWindow.set(true);
            }
            lastQueriedEntity = queriedEntity;

            if (showQueryWindow.get()) {
                ImGui.begin("Edit Values", showQueryWindow, ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize);
                ImGui.setWindowSize(buttonSize * 6, buttonSize * 3);
                if (queriedEntity.getClass() == EntityNode.class) {
                    //Node information
                    EntityNode entity = (EntityNode) queriedEntity;
                    if (queriedEntityChanged) {
                        queryWindowTextInputField1.set(entity.node.getID());
                        queriedEntityChanged = false;
                    }

                    ImGui.text("Node id: " + entity.node.getID());
                    //## hides the label
                    if (ImGui.inputText("##NodeID", queryWindowTextInputField1, ImGuiInputTextFlags.EnterReturnsTrue)) {
                        ImGui.setKeyboardFocusHere(-1);
                        if (!queryWindowTextInputField1.get().isEmpty()) { //Ignore empty inputs
                            entity.node.setId(queryWindowTextInputField1.get());
                        }
                    }
                } else if (queriedEntity.getClass() == EntityConnection.class) {
                    //NodeConnection Information
                    EntityConnection entity = (EntityConnection) queriedEntity;
                    if (queriedEntityChanged) {
                        queryWindowTextInputField1.set(entity.connection1.getDistance());
                        queryWindowTextInputField2.set(entity.connection1.getTravelTime());
                        queriedEntityChanged = false;
                    }

                    ImGui.text("Distance: " + entity.connection1.getDistance());
                    if (ImGui.inputText("##Distance", queryWindowTextInputField1, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.EnterReturnsTrue)) {
                        ImGui.setKeyboardFocusHere(-1);
                        if (!queryWindowTextInputField1.get().isEmpty()) { //Empty input gets ignored
                            float newDistance = Float.parseFloat(queryWindowTextInputField1.get());
                            if (newDistance < 0)
                                newDistance *= -1; //No negative numbers, just remove the negative sign as it is user input
                            entity.connection1.setDistance(newDistance);
                            entity.connection2.setDistance(newDistance);
                        }
                    }
                    ImGui.text("Travel Time: " + entity.connection1.getTravelTime());
                    if (ImGui.inputText("##TravelTime", queryWindowTextInputField2, ImGuiInputTextFlags.CharsDecimal | ImGuiInputTextFlags.EnterReturnsTrue)) {
                        ImGui.setKeyboardFocusHere(-1);
                        if (!queryWindowTextInputField2.get().isEmpty()) { //Empty input gets ignored
                            float newTime = Float.parseFloat(queryWindowTextInputField2.get());
                            if (newTime < 0)
                                newTime *= -1; //No negative numbers, just remove the negative sign as it is user input
                            entity.connection1.setTravelTime(newTime);
                            entity.connection2.setTravelTime(newTime);
                        }
                    }
                }
                ImGui.end();
            }
        }
        if (getActiveMode() == ButtonID.SAVE){
            ImGui.begin("Save Map?", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
            //ImGui.setWindowSize(buttonSize,buttonSize*3);
            ImGui.setWindowPos(displaySize.x/2, displaySize.y/2);
            if (ImGui.button("Save slot 1")){
                saveMap("save1.save");
            }
            if (ImGui.button("Save slot 2")){
                saveMap("save2.save");
            }
            if (ImGui.button("Save slot 3")){
                saveMap("save3.save");
            }
            if (ImGui.button("Save slot 4")){
                saveMap("save4.save");
            }
            if (ImGui.button("Save slot 5")){
                saveMap("save5.save");
            }
            ImGui.end();
        }

        ImGui.render(); // Calculate the data into drawData
        ImGui.popStyleVar();
    }

    public boolean handleGUIInput (Scene scene, Window window){
        ImGuiIO io = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getCurrentPos();
        io.addMousePosEvent(mousePos.x, mousePos.y);
        io.addMouseButtonEvent(0, mouseInput.getMouseState(1) == MouseInput.State.HELD);
        io.addMouseButtonEvent(1, mouseInput.getMouseState(2) == MouseInput.State.HELD);

        return io.getWantCaptureMouse() || io.getWantCaptureKeyboard();
    }

    private void imGuiImageButton (String buttonID, Button button,float sizeX, float sizeY){
        if (ImGui.imageButton(buttonID, button.buttonTextureID, new ImVec2(sizeX, sizeY), button.getUV0(), button.getUV1())) {
            button.toggle();
        }
    }

    public ButtonID getActiveMode () {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isPressed) {
                return ButtonID.values()[i];
            }
        }
        return null;
    }

    public void setQueriedEntity (Entity entity){
        queriedEntity = entity;
    }
    private void saveMap(String filename){

    }
}
