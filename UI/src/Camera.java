import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f forwardDirection; //The forwards direction
    private final Vector3f position;
    private final Vector2f rotation;
    private final Vector3f rightDirection;
    private final Vector3f upDirection;
    private final Matrix4f viewMatrix;

    public Camera(){
        forwardDirection = new Vector3f();
        position = new Vector3f();
        rightDirection = new Vector3f();
        rotation = new Vector2f();
        upDirection = new Vector3f();
        viewMatrix = new Matrix4f();
    }
    public void addRotation(float x, float y) {
        rotation.add(x, y);
        //Limits horizontal rotation to -180 to 180 degrees
        if (rotation.y > Math.PI) rotation.y -= (float) (Math.PI*2);
        else if (rotation.y < -Math.PI) rotation.y += (float) (Math.PI*2);

        //Limits horizontal rotation to -90 to 90 degrees (i.e. cannot flip the camera upside down)
        float halfPI = (float) (Math.PI/2);
        if (rotation.x > halfPI) rotation.x = halfPI;
        else if (rotation.x < -halfPI) rotation.x = -halfPI;
        recalculate();
    }

    //Getters
    public Vector3f getPosition() {
        return new Vector3f(position);
    }
    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }
    public Matrix4f getInverseViewMatrix(){
        Matrix4f output = new Matrix4f();
        viewMatrix.invert(output);
        return output;
    }

    //Movement
    public void moveForward(float distance) {
        viewMatrix.positiveZ(forwardDirection).negate().mul(distance);
        position.add(forwardDirection);
        recalculate();
    }
    public void moveBackwards(float distance) {
        viewMatrix.positiveZ(forwardDirection).negate().mul(distance);
        position.sub(forwardDirection);
        recalculate();
    }
    public void moveUp(float distance) {
        viewMatrix.positiveY(upDirection).mul(distance);
        position.add(upDirection);
        recalculate();
    }
    public void moveDown(float distance) {
        viewMatrix.positiveY(upDirection).mul(distance);
        position.sub(upDirection);
        recalculate();
    }
    public void moveLeft(float distance) {
        viewMatrix.positiveX(rightDirection).mul(distance);
        position.sub(rightDirection);
        recalculate();
    }
    public void moveRight(float distance) {
        viewMatrix.positiveX(rightDirection).mul(distance);
        position.add(rightDirection);
        recalculate();
    }
    public void moveCamera(Vector3f movementVector){
        //Currently moves camera irrespective of rotation
        position.add(movementVector);
        recalculate();
    }


    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }

    //Setters
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }
    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }


}
