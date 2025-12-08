import java.util.LinkedList;
import java.util.List;

public class Model {

    private final String ID;
    private final List<Entity> entitiesList;
    private final List<Material> materialList;

    public Model(String ID, List<Material> materialList){
        this.ID = ID;
        this.materialList = materialList;
        entitiesList = new LinkedList<>();
    }

    public void cleanup(){
        materialList.forEach(Material::cleanup);
    }

    public List<Entity> getEntitiesList(){
        return entitiesList;
    }

    public String getID(){
        return ID;
    }

    public List<Material> getMateriaList(){
        return materialList;
    }
}
