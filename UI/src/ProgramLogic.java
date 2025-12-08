import org.joml.*;

public class ProgramLogic{


    public enum EntityType{
        //todo: maybe move to the entity class
        DOT,
        SUZANNE,
        CUBE,
        MAP,
        PATH
    }

    //Variables used for the selection of nodes
    Vector4f prevEntityColor;
    Entity prevEntity = null;
    Entity heldEntity = null;

    Entity selectedEntity1 = null;
    Vector4f selectedEntity1Color;

    DynamicPathModelCollection dyModelCollection;

    private GUI gui;

    public void init(Window window, Scene scene, Renderer renderer){
        gui = new GUI(scene);

        scene.addModel(ModelLoader.loadModel(EntityType.DOT.name(), "UI/res/model/dot.obj",scene.getTextureCache()));
        //scene.addModel(ModelLoader.loadModel(EntityType.SUZANNE.name(), "res/model/suzanne.obj",scene.getTextureCache()));
        //scene.addModel(ModelLoader.loadModel(EntityType.CUBE.name(), "res/model/cube.obj",scene.getTextureCache()));
        scene.addModel(ModelLoader.loadModel(EntityType.MAP.name(), "UI/res/model/map.obj",scene.getTextureCache()));

        dyModelCollection = new DynamicPathModelCollection();



        //The map plane
        Entity entityTemp = new Entity(EntityType.MAP.name(), EntityType.MAP.name());
        entityTemp.setPosition(0,0,-10);
        entityTemp.setRotation(1,0,0,90);
        entityTemp.setScale(100);
        scene.addEntity(entityTemp);
    }
    public void input(Window window, Scene scene, long diffTimeMillis, boolean consumed){
        if (consumed) return;
        InputHandler.updateInputs(window);
        Camera camera = scene.getCamera();

        //Inputs regarding the mouse
        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.getMouseState(2) == MouseInput.State.HELD){
            Vector2f displacementVector = mouseInput.getDisplacementVector();
            displacementVector = displacementVector.mul(Bindings.MOUSE_SENSITIVITY);
            camera.addRotation(displacementVector.x, displacementVector.y);
        }

        if (gui.getActiveMode() != GUI.ButtonID.CONNECT){
            if (selectedEntity1 != null){
                selectedEntity1.setTintColor(selectedEntity1Color);
                selectedEntity1 = null;
            }
        }


        if (gui.getActiveMode() == GUI.ButtonID.SUB || gui.getActiveMode() == GUI.ButtonID.MOVE || gui.getActiveMode() == GUI.ButtonID.CONNECT) {
            //Get appropriate ray filter
            String[] filter;
            if (gui.getActiveMode() == GUI.ButtonID.SUB) filter = new String[]{EntityType.DOT.name(), EntityType.PATH.name()};
            else filter = new String[]{EntityType.DOT.name()};

            Ray ray = new Ray(Ray.mouseRay(mouseInput, window, scene),camera.getPosition(),scene, filter);
            if (ray.isRayHit()){
                Entity entityHit = ray.getEntityHit();
                //Possible optimization is to do nothing if the entity hit is the same as prevEntity however as of current this would cause selected entities to not highlight while they are still the prevEntity
                if (prevEntity != entityHit || prevEntity == selectedEntity1){
                    //deal with prevEntity
                    if (prevEntity != null){
                        if (prevEntity == selectedEntity1) prevEntity.setTintColor(Bindings.COLOR_SELECTED);
                        else prevEntity.setTintColor(prevEntityColor);
                        prevEntity = null;
                    }

                    //Setup next prevEntity
                    if (entityHit == selectedEntity1) {
                        entityHit.setTintColor(Bindings.COLOR_HOVERED_AND_SELECTED);
                    }
                    else {
                        prevEntityColor = ray.getEntityHit().getTintColor();
                        entityHit.setTintColor(Bindings.COLOR_HOVERED);
                    }
                    prevEntity = entityHit;
                }
            }
            else if (prevEntity != null){
                if (prevEntity == selectedEntity1) prevEntity.setTintColor(Bindings.COLOR_SELECTED);
                else prevEntity.setTintColor(prevEntityColor);
                prevEntity = null;
            }

        }

        if (mouseInput.getMouseState(1) == MouseInput.State.PRESSED){
            //Process the right mouse click depending on the Active mode
            //TODO: convert to a switch statement
            if (gui.getActiveMode() == GUI.ButtonID.ADD){
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.MAP.name()});
                if (ray.isRayHit()){
                    EntityNode dot = new EntityNode(EntityType.DOT.name(), EntityType.DOT.name(), "New node");
                    Vector3f pos = ray.getRayHitPosition();
                    dot.setPosition(pos.x,pos.y,pos.z);
                    dot.setTintColor(Utils.randomColor(0.4f, 0.9f));
                    scene.addEntity(dot);
                }
            }
            else if (gui.getActiveMode() == GUI.ButtonID.SUB) {
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.DOT.name(), EntityType.PATH.name()});
                if (ray.isRayHit()){
                    if (ray.getEntityHit().getID().equals(EntityType.DOT.name())){
                        scene.removeEntity(ray.getEntityHit());
                        dyModelCollection.removeDynamicPathModel(ray.getEntityHit(), scene);
                    }
                    else if (ray.getEntityHit().getID().equals(EntityType.PATH.name())) {
                        dyModelCollection.removeDynamicPathModel(ray.getEntityHit().getModelID(),scene);
                    }
                }
            }
            else if (gui.getActiveMode() == GUI.ButtonID.MOVE) {
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.DOT.name()});
                if (ray.isRayHit()){
                    heldEntity = ray.getEntityHit();
                }
            }
            else if (gui.getActiveMode() == GUI.ButtonID.CONNECT) {
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.DOT.name()});
                if (ray.isRayHit()){
                    if (selectedEntity1 == ray.getEntityHit()){
                        selectedEntity1.setTintColor(selectedEntity1Color);
                        selectedEntity1 = null;
                    }
                    else if (selectedEntity1 == null){
                        selectedEntity1 = ray.getEntityHit();
                        if (prevEntity == selectedEntity1) selectedEntity1Color = new Vector4f(prevEntityColor);
                        else selectedEntity1Color = selectedEntity1.getTintColor();
                        selectedEntity1.setTintColor(Bindings.COLOR_SELECTED);
                    }
                    else {
                        if (selectedEntity1.getClass() == EntityNode.class && ray.getEntityHit().getClass() == EntityNode.class) {
                            dyModelCollection.addDynamicPathModel((EntityNode) selectedEntity1, (EntityNode) ray.getEntityHit(), 0.25f, scene);
                        }
                            selectedEntity1.setTintColor(selectedEntity1Color);
                            selectedEntity1 = null;
                    }

                }

            } else if (gui.getActiveMode() == GUI.ButtonID.QUERY) {
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.DOT.name(), EntityType.PATH.name()});
                if (ray.isRayHit()){
                    if (ray.getEntityHit().getClass() == EntityNode.class || ray.getEntityHit().getClass() == EntityConnection.class){
                        gui.setQueriedEntity(ray.getEntityHit());
                    }
                }
            }
        }
        if (mouseInput.getMouseState(1) == MouseInput.State.HELD){
            if (gui.getActiveMode() == GUI.ButtonID.MOVE && heldEntity != null){
                Vector3f rayVector = Ray.mouseRay(mouseInput, window, scene);
                Ray ray = new Ray(rayVector,camera.getPosition(),scene,new String[]{EntityType.MAP.name()});
                if (ray.isRayHit()){
                    heldEntity.setPosition(ray.getRayHitPosition());
                    dyModelCollection.updateEntityPosition(heldEntity);
                }

            }
        }
        if (mouseInput.getMouseState(1) == MouseInput.State.RELEASED){
            heldEntity = null;
        }

    }

    public void update(Window window, Scene scene, long diffTimeMillis){
        //float deltaTime = diffTimeMillis / 1000f; //Todo: possibly unnecessary but could be useful for clarity sake
        Camera camera = scene.getCamera();
        float moveDistance = Bindings.MOVE_SPEED*diffTimeMillis;

        if(InputHandler.checkAction(InputHandler.PlayerActions.MoveUp, InputHandler.InputKeyState.Held)){
            camera.moveUp(moveDistance);
        }
        else if (InputHandler.checkAction(InputHandler.PlayerActions.MoveDown, InputHandler.InputKeyState.Held)){
            camera.moveDown(moveDistance);
        }
        if (InputHandler.checkAction(InputHandler.PlayerActions.MoveLeft, InputHandler.InputKeyState.Held)){
            camera.moveLeft(moveDistance);
        }
        else if (InputHandler.checkAction(InputHandler.PlayerActions.MoveRight, InputHandler.InputKeyState.Held)){
            camera.moveRight(moveDistance);
        }
        if (InputHandler.checkAction(InputHandler.PlayerActions.MoveForward, InputHandler.InputKeyState.Held)){
            camera.moveForward(moveDistance);
        }
        else if (InputHandler.checkAction(InputHandler.PlayerActions.MoveBackwards, InputHandler.InputKeyState.Held)){
            camera.moveBackwards(moveDistance);
        }

    }

    public void cleanup(){

    }


}
