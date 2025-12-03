import org.joml.Vector4f;

import java.util.LinkedList;
import java.util.List;

public class Material {

    public static final Vector4f DEFAULT_COLOR = new Vector4f(0,0,0,1);

    private final List<Mesh> meshList;
    private String texturePath;
    private Vector4f diffuseColor;

    public Material(){
        meshList = new LinkedList<>();
        texturePath = TextureCache.DEFAULT_TEXTURE;
        diffuseColor = new Vector4f(0,0,0,1);
    }

    public void cleanup(){
        meshList.forEach(Mesh::cleanup);
    }

    //Getters
    public List<Mesh> getMeshList(){
        return meshList;
    }
    public String getTexturePath(){
        return texturePath;
    }
    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    //Setters
    public void setTexturePath(String texturePath){
        this.texturePath = texturePath;
    }
    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }
}
