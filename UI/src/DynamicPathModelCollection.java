import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DynamicPathModelCollection {
    //This class handles the dynamicPathModels as connections between entities
    private final List<DynamicPathModel> modelList;
    private final List<Entity> entityList;
    private final Map<DynamicPathModel, Entity> leftEntity;
    private final Map<DynamicPathModel, Entity> rightEntity;

    public DynamicPathModelCollection(){
        modelList = new LinkedList<>();
        entityList = new LinkedList<>();
        leftEntity = new HashMap<>();
        rightEntity = new HashMap<>();
    }

    public void addDynamicPathModel(EntityNode left, EntityNode right, float scale, Scene scene){
        DynamicPathModel model = new DynamicPathModel(left.getPosition(), right.getPosition(), scale, scene.getTextureCache());

        modelList.add(model);
        scene.addModel(model.getModel());
        Entity entity = new EntityConnection(ProgramLogic.EntityType.PATH.name(), model.getModelID(), left.getNode(), right.getNode(), 0f, 0f);
        entity.setTintColor(Utils.randomColor(0.4f,0.9f));
        entityList.add(entity);
        scene.addEntity(entity);

        leftEntity.put(model, left);
        rightEntity.put(model, right);
    }
    public void removeDynamicPathModel(DynamicPathModel dyModel, Scene scene){
        //Tries to remove the dynamicPathModel from the collection, if it doesn't exist in the collection it does nothing
        removeDynamicPathModel(dyModel.getModelID(), scene);
    }
    public void removeDynamicPathModel(Entity entity, Scene scene){
        //Tries to remove the dynamicPathModel from the collection, if it doesn't exist in the collection it does nothing
        List<DynamicPathModel> modelsToRemove = new LinkedList<>();
        for (DynamicPathModel model : modelList){
            if (leftEntity.get(model) == entity){
                modelsToRemove.add(model);
            }
            if (rightEntity.get(model) == entity){
                modelsToRemove.add(model);
            }
        }
        for (DynamicPathModel model : modelsToRemove){
            removeDynamicPathModel(model,scene);
        }
    }
    public void removeDynamicPathModel(String dyModelID, Scene scene){
        //Tries to remove the dynamicPathModel from the collection, if it doesn't exist in the collection it does nothing
        DynamicPathModel dyModelToRemove = null;
        for (DynamicPathModel dyModel : modelList){
            if (dyModel.getModelID().equals(dyModelID)){
                dyModelToRemove = dyModel;
                break;
            }
        }
        if (dyModelToRemove != null){
            Entity dyEntityToRemove = null;
            for (Entity entity : entityList){
                if (entity.getModelID().equals(dyModelToRemove.getModelID())){
                    dyEntityToRemove = entity;
                }
            }
            if (dyEntityToRemove != null){
                scene.removeEntity(dyEntityToRemove);
                entityList.remove(dyEntityToRemove);
                scene.removeModel(dyModelToRemove.getModel());
                leftEntity.remove(dyModelToRemove);
                rightEntity.remove(dyModelToRemove);
                modelList.remove(dyModelToRemove);
            }
        }
    }

    public List<DynamicPathModel> getModelList() {
        return modelList;
    }

    public void updateEntityPosition(Entity entity){
        for (DynamicPathModel model : modelList){
            if (leftEntity.get(model) == entity){
                model.setLeft(entity.getPosition());
            }
            if (rightEntity.get(model) == entity){
                model.setRight(entity.getPosition());
            }
        }

    }
}
