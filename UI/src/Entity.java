import org.joml.*;

public class Entity {
    private final String ID;
    private final String modelID;
    private final Matrix4f modelMatrix;
    private final Vector3f position;
    private final Quaternionf rotation;
    private float scale; //Uniform scale
    private Vector4f tintColor;

    public Entity(String ID, String modelID){
        this.ID = ID;
        this.modelID = modelID;
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1f;
        tintColor = new Vector4f(1,1,1,1);
    }

    //Getters
    public String getID(){
        return ID;
    }
    public String getModelID(){
        return modelID;
    }
    public Matrix4f getModelMatrix(){
        return new Matrix4f(modelMatrix);
    }
    public Vector3f getPosition(){
        return new Vector3f(position);
    }
    public Quaternionf getRotation() {
        return new Quaternionf(rotation);
    }
    public float getScale() {
        return scale;
    }
    public Vector4f getTintColor(){
        return new Vector4f(tintColor);
    }

    //Setters
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
        updateModelMatrix();
    }
    public void setPosition(Vector3f entityPos) {
        position.set(entityPos);
        updateModelMatrix();
    }
    public void setRotation(float x, float y, float z, float angle){
        rotation.fromAxisAngleDeg(x,y,z,angle);
        updateModelMatrix();
    }
    public void setScale(float scale){
        this.scale = scale;
        updateModelMatrix();
    }
    public void setTintColor(Vector4f color){
        tintColor = color;
    }

    //Other methods
    private void updateModelMatrix(){
        modelMatrix.translationRotateScale(position,rotation,scale);
    }
    public void addRotation(float x, float y, float z, float angle){
        //This should rotate around the given axis
        rotation.mul(new Quaternionf().fromAxisAngleDeg(x,y,z,angle));
        updateModelMatrix();
    }

}
