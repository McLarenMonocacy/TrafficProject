import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public class ModelLoader {
    public static Model loadModel (String modelID, String filePath, TextureCache textureCache){
        return loadModel(modelID, filePath, textureCache, Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals | Assimp.aiProcess_CalcTangentSpace | Assimp.aiProcess_LimitBoneWeights | Assimp.aiProcess_PreTransformVertices);
    }
    public static Model loadModel (String modelID, String filePath, TextureCache textureCache, int flags){
        File file = new File(filePath);
        if (!file.exists()) throw new RuntimeException("Could not find " + filePath);
        String modelDir = file.getParent();
        AIScene aiScene = Assimp.aiImportFile(filePath, flags);
        if (aiScene == null) throw new RuntimeException("Could not load model: " + filePath);

        int materialCount = aiScene.mNumMaterials();
        List<Material> materialList = new LinkedList<>();
        for (int i = 0; i < materialCount; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiScene.mMaterials().get(i));
            materialList.add(processMaterial(aiMaterial, modelDir, textureCache));
        }

        int meshCount = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Material defaultMaterial = new Material();
        for (int i = 0; i < meshCount; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh);
            int materialIndex = aiMesh.mMaterialIndex();
            Material material;
            if (materialIndex >= 0 && materialIndex < materialList.size()){
                material = materialList.get(materialIndex);
            }
            else {
                material = defaultMaterial;
            }
            material.getMeshList().add(mesh);
        }

        if (!defaultMaterial.getMeshList().isEmpty()){
            materialList.add(defaultMaterial);
        }

        return new Model(modelID, materialList);
    }

    private static Material processMaterial(AIMaterial aiMaterial, String modelDir, TextureCache textureCache){
        Material material = new Material();
        try (MemoryStack stack = MemoryStack.stackPush()){
            AIColor4D color = AIColor4D.create();

            int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, color);
            if (result == Assimp.aiReturn_SUCCESS){
                material.setDiffuseColor(new Vector4f(color.r(), color.g(), color.b(), color.a()));
            }

            AIString aiTexturePath = AIString.calloc(stack);
            Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, aiTexturePath, (IntBuffer) null, null, null, null, null, null);
            String texturePath = aiTexturePath.dataString();
            if (texturePath != null && !texturePath.isEmpty()){
                material.setTexturePath(modelDir + File.separator + new File(texturePath).getName());
                textureCache.createTexture(material.getTexturePath());
                material.setDiffuseColor(material.DEFAULT_COLOR);
            }
            return material;
        }
    }

    private static Mesh processMesh (AIMesh aiMesh){
        float[] vertices = processVertices(aiMesh);
        float[] textureCoordinates = processTextureCoordinates(aiMesh);
        int[] indices = processIndices(aiMesh);

        if (textureCoordinates.length == 0){
            int elementCount = (vertices.length/3)*2;
            textureCoordinates = new float[elementCount];
        }

        return new Mesh(vertices,textureCoordinates,indices);
    }

    private static int[] processIndices(AIMesh aiMesh) {
        List<Integer> indices = new LinkedList<>();
        int faceCount = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < faceCount; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    private static float[] processTextureCoordinates(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);
        if (buffer == null) {
            return new float[]{};
        }
        float[] data = new float[buffer.remaining() * 2];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D textCoordinates = buffer.get();
            data[pos++] = textCoordinates.x();
            data[pos++] = 1 - textCoordinates.y();
        }
        return data;
    }

    private static float[] processVertices(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mVertices();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D vertexCoordinates = buffer.get();
            data[pos++] = vertexCoordinates.x();
            data[pos++] = vertexCoordinates.y();
            data[pos++] = vertexCoordinates.z();
        }
        return data;
    }
}
