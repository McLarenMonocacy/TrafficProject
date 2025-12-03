import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class Scene {

    private final Map<String, Model> modelMap;
    private final TextureCache textureCache;
    private final Projection projection;
    private final Camera camera;
    private GUIInstance guiInstance;

    public Scene(Vector2i dimensions){
        modelMap = new HashMap<>();
        projection = new Projection(dimensions);
        textureCache = new TextureCache();
        camera = new Camera();
    }

    public void addEntity(Entity entity){
        String modelID = entity.getModelID();
        Model model = modelMap.get(modelID);
        if (model == null) throw new RuntimeException("Could not find model: " + modelID);
        model.getEntitiesList().add(entity);
    }
    public void removeEntity(Entity entity){
        modelMap.get(entity.getModelID()).getEntitiesList().remove(entity);
    }

    public void addModel(Model model){
        modelMap.put(model.getID(),model);
    }
    public void removeModel(Model model){
        modelMap.remove(model.getID()).cleanup();
    }

    public void cleanup(){
        modelMap.values().forEach(Model::cleanup);
    }

    public void resizeProjection(Vector2i dimensions){
        projection.updateProjMatrix(dimensions);
    }

    //Getters
    public Map<String, Model> getModelMap(){
        return modelMap;
    }
    public Projection getProjection(){
        return projection;
    }
    public TextureCache getTextureCache(){
        return textureCache;
    }
    public Camera getCamera(){
        return camera;
    }
    public GUIInstance getGuiInstance() {
        return guiInstance;
    }

    //Setter
    public void setGuiInstance(GUIInstance guiInstance) {
        this.guiInstance = guiInstance;
    }
}
