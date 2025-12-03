import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    public static final String DEFAULT_TEXTURE = "UI/res/texture/error.png";

    private final Map<String, Texture> textureMap;

    public TextureCache(){
        textureMap = new HashMap<>();
        textureMap.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE));
    }

    public void cleanup(){
        textureMap.values().forEach(Texture::cleanup);
    }

    public Texture createTexture(String filePath){
        //Create the texture if it is not present already
        return textureMap.computeIfAbsent(filePath, Texture::new);
    }

    public Texture getTexture(String filePath){
        //Returns the texture, if it's not found, returns the default
        Texture texture = null;
        if (filePath != null) {
            texture = textureMap.get(filePath);
        }
        if (texture == null){
            texture = textureMap.get(DEFAULT_TEXTURE);
        }
        return texture;
    }
}
