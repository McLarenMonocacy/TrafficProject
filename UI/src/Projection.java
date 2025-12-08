import org.joml.Matrix4f;
import org.joml.Vector2i;

public class Projection {
    private static final float FOV = (float) Math.toRadians(70d); //Field of View
    private static final float Z_FAR = 1000f; // Far clipping distance
    private static final float Z_NEAR = 0.01f; // Near clipping distance

    private final Matrix4f projMatrix;

    public Projection(Vector2i dimensions){
        projMatrix = new Matrix4f();
        updateProjMatrix(dimensions);
    }
    public void updateProjMatrix (Vector2i dimensions){
        projMatrix.setPerspective(FOV,(float) dimensions.x/dimensions.y, Z_NEAR, Z_FAR);
    }
    public Matrix4f getProjMatrix(){
        return projMatrix;
    }
    public Matrix4f getInverseProjMatric(){
        Matrix4f output = new Matrix4f();
        projMatrix.invert(output);
        return output;
    }
}
