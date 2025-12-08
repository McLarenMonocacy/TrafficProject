import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;
import java.util.LinkedList;

public class DynamicPathModel {
    //TODO: create a Mesh dynamically to connect two nodes along a curve
    private Vector3f left;
    private Vector3f right;
    private float scale;
    private Vector3f angle = new Vector3f(0,0,-1);
    private Model model = null;
    TextureCache textureCache;

    public DynamicPathModel(Vector3f left, Vector3f right, float scale, TextureCache textureCache){
        //Creates a tube mesh that starts and ends at different points
        this.left = left;
        this.right = right;
        this.scale = scale;
        this.textureCache = textureCache;
        calcMesh();
    }


    public void setLeft(Vector3f position){
        this.left = new Vector3f(position);
        calcMesh();
    }
    public void setRight(Vector3f position) {
        this.right = new Vector3f(position);
        calcMesh();
    }
    public void setScale(float scale) {
        this.scale = scale;
        calcMesh();
    }
    public Model getModel() {
        return model;
    }
    public String getModelID(){
        return model.getID();
    }

    private void calcAngle(){
        //Creates an angle vector based on the angle of the line that runs though the start and end points
        //returns an angle vector of (0,0,-1) if both the start and end points are the same
        angle = left.sub(right,new Vector3f());
        if (angle.equals(0,0,0)){
            angle = new Vector3f(0,0,-1);
            return;
        }
        angle.normalize();
    }
    private Vector3f genPoint(Vector3f origin, Vector3f offset){
        return origin.add(offset, new Vector3f());
    }
    private void calcMesh(){
        //Calculates a new mesh and then updates the model accordingly
        int meshResolution = Bindings.DYNAMIC_PATH_MODEL_RESOLUTION;
        calcAngle(); //Ensure the angle is correctly updated
        Vector3f offset = angle.orthogonalize(angle, new Vector3f());
        offset.mul(scale);
        Quaternionf rotation = new Quaternionf().fromAxisAngleDeg(angle,360f/meshResolution);

        //Generate Vertices in two sets, left and right.
        Vector3f[] startVertices = new Vector3f[meshResolution];
        Vector3f[] endVertices = new Vector3f[meshResolution];
        for (int i = 0; i < meshResolution; i++) {
            startVertices[i] = genPoint(left, offset);
            endVertices[i] = genPoint(right, offset);
            offset.rotate(rotation);
        }

        //combine these vertices into one set, with the first half being the left set and the second half being the right set
        float[] meshVertices = new float[(meshResolution*3)*2]; //3 floats per vertex position, 1 vertex per resolution, 2 sets of vertices due to top and bottom
        for (int i = 0; i < meshResolution; i++) {
            int index = i*3;
            meshVertices[index] = startVertices[i].x;
            meshVertices[index+1] = startVertices[i].y;
            meshVertices[index+2] = startVertices[i].z;
            index = (i+meshResolution)*3;
            meshVertices[index] = endVertices[i].x;
            meshVertices[index+1] = endVertices[i].y;
            meshVertices[index+2] = endVertices[i].z;
        }

        //Generate Indices
        int[] meshIndices = new int[(meshResolution*3)*2]; //3 vertices per triangle, 2 triangles per quad face, 1 face per resolution
        for (int i = 0; i < meshResolution; i++) {
            //Creates the indices for a quad for every side of the tube
            int index = i*6;
            //The start vertex set are vertices in the range [0, meshResolution)
            //The end vertex set are vertices in the range [meshResolution, 2*meshResolution)
            //Triangle 1
            meshIndices[index] = i + meshResolution; // part of end vertex set
            meshIndices[index+1] = Utils.arrayWrapAround(i + 1, meshResolution); // part of start vertex set
            meshIndices[index+2] = i; // part of start vertex set
            //Triangle 2
            meshIndices[index+3] = Utils.arrayWrapAround(i + 1, meshResolution); // part of start vertex set
            meshIndices[index+4] = i + meshResolution; // part of end vertex set
            meshIndices[index+5] = Utils.arrayWrapAround(i + meshResolution + 1, meshResolution,meshResolution*2); // part of end vertex set
        }

        //Generate UV Coordinates
        //The generated UV coordinates wrap a texture around the tube except for the last quad
        //instead of reaching the end of the texture, it instead displays the entirety of the texture before it.
        //The fix would be to create duplicate vertices for the first of each set
        float [] meshUV = new float[meshResolution*2*2]; // 2 sets of vertices, 2 numbers per coordinate
        for (int i = 0; i < meshUV.length; i++) {
            if (i%2 == 0){ // X coordinate
                float num = i%(meshResolution*2); // This should go from 0 to meshResolution*2 twice
                meshUV[i] = num/(meshResolution*2);
            }
            else{  // Y coordinate
                if (i < meshResolution*2) meshUV[i] = 0; //First half of vertices which is the left set
                else meshUV[i] = 1; //Second half of vertices which is the right set
            }
        }
        updateModel(new Mesh(meshVertices,meshUV, meshIndices));
    }

    private void genModel(Mesh mesh){
        textureCache.createTexture(Bindings.TEX_BLANK);
        Material mat = new Material();
        mat.getMeshList().add(mesh);
        mat.setTexturePath(Bindings.TEX_BLANK);
        LinkedList<Material> matList = new LinkedList<>();
        matList.add(mat);

        model = new Model(Utils.getUniqueID(), matList);
    }
    private void updateModel(Mesh mesh){
        if (model == null) genModel(mesh);
        else {
            model.getMateriaList().getFirst().getMeshList().removeFirst().cleanup();
            model.getMateriaList().getFirst().getMeshList().addFirst(mesh);
        }
    }

    public void cleanup(){
        model.cleanup();
    }


}
