import org.lwjgl.opengl.GL46;

import java.util.Collection;
import java.util.List;

public class SceneRenderer {
    private final Shader shader;
    private UniformMap uniformMap;

    public SceneRenderer(){
        shader = new ShaderFlatColor();
        createUniforms();
    }

    private void createUniforms(){
        uniformMap = new UniformMap(shader.getProgramID());
        uniformMap.createUniform("projectionMatrix");
        uniformMap.createUniform("modelMatrix");
        uniformMap.createUniform("textureSampler"); //I don't think this does anything
        uniformMap.createUniform("cameraMatrix");
        uniformMap.createUniform("material.diffuse");
        uniformMap.createUniform("material.tintColor");
    }

    public void cleanup(){
        shader.cleanup();
    }

    public void render(Scene scene){
        shader.start(); // Enable the shader
        uniformMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
        uniformMap.setUniform("textureSampler", 0); // I don't think this does anything
        uniformMap.setUniform("cameraMatrix", scene.getCamera().getViewMatrix());

        //Draw the meshes
        Collection<Model> models = scene.getModelMap().values();
        TextureCache textureCache = scene.getTextureCache();
        for (Model model : models){
            List<Entity> entities = model.getEntitiesList();
            for (Material material : model.getMateriaList()){
                uniformMap.setUniform("material.diffuse", material.getDiffuseColor());
                Texture texture = textureCache.getTexture(material.getTexturePath());
                GL46.glActiveTexture(GL46.GL_TEXTURE0);
                texture.bind();

                for (Mesh mesh : material.getMeshList()){
                    GL46.glBindVertexArray(mesh.getVaoID());
                    for (Entity entity : entities){
                        uniformMap.setUniform("modelMatrix", entity.getModelMatrix());
                        uniformMap.setUniform("material.tintColor", entity.getTintColor());
                        GL46.glDrawElements(GL46.GL_TRIANGLES,mesh.getVertexCount(),GL46.GL_UNSIGNED_INT,0);
                    }
                }
            }
        }

        //Cleanup: unload last mesh and disable the shader
        GL46.glBindVertexArray(0);
        shader.stop();
    }
}
