import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Ray {
    //TODO: use bounding boxes to filter out unnecessary triangle checks
    private final Vector3f rayVector;
    private final Vector3f rayOrigin;
    private final String[] filter;
    private final Scene scene;

    private boolean rayHit = false;
    private float rayHitDistance = Float.MAX_VALUE;
    private Vector3f rayHitPosition = null;
    private Entity entityHit = null;


    public Ray(Vector3f rayVector, Vector3f rayOrigin, Scene scene){
        // Ray with no filter
        this(rayVector,rayOrigin,scene,new String[0]);

    }
    public Ray(Vector3f rayVector, Vector3f rayOrigin, Scene scene, String[] entityIDFilter){
        this.rayVector = new Vector3f(rayVector);
        this.rayOrigin = new Vector3f(rayOrigin);
        this.scene = scene;
        this.filter = entityIDFilter;

        castRay();
    }

    private void castRay(){
        Entity outputEntityHit = null;
        float outputHitDistance = Float.MAX_VALUE;
        Vector3f outputHitPos = null;

        for (Model model : scene.getModelMap().values()){
            for (Entity entity : model.getEntitiesList()) {
                //Filter out entities that are not in the filter
                boolean inFilter = false;
                if (filter.length > 0){ //There is at least 1 item to filter for
                    for(String filterID : filter){
                        if (entity.getID().equals(filterID)) { // Found in filter
                            inFilter = true;
                            break;
                        }
                    }
                }
                else { //There is no filter
                    inFilter = true;
                }
                if (!inFilter){
                    continue;
                }
                Matrix4f modelMatrix = new Matrix4f(entity.getModelMatrix());
                for (Material mat : model.getMateriaList()) {
                    for (Mesh mesh : mat.getMeshList()) {
                        float[] vertexPositions = mesh.getVertexPositions();
                        int[] indices = mesh.getIndices();
                        for (int i = 2; i < indices.length; i += 3) {
                            //Checks if a ray intersects a triangle returns null if triangle was not intersected
                            //For the math: https://www.desmos.com/3d/dpf53kfnla

                            //Vertex 1 transformed into world space
                            int index1 = indices[i - 2] * 3;
                            int index2 = indices[i - 2] * 3 + 1;
                            int index3 = indices[i - 2] * 3 + 2;
                            Vector4f a1 = new Vector4f(vertexPositions[index1], vertexPositions[index2], vertexPositions[index3],1);
                            a1.mul(modelMatrix);
                            Vector3f triVert1 = new Vector3f();
                            a1.xyz(triVert1);
                            //Vertex 2 transformed into world space
                            index1 = indices[i - 1] * 3;
                            index2 = indices[i - 1] * 3 + 1;
                            index3 = indices[i - 1] * 3 + 2;
                            Vector4f a2 = new Vector4f(vertexPositions[index1], vertexPositions[index2], vertexPositions[index3],1);
                            a2.mul(modelMatrix);
                            Vector3f triVert2 = new Vector3f();
                            a2.xyz(triVert2);
                            //Vertex 3 transformed into world space
                            index1 = indices[i] * 3;
                            index2 = indices[i] * 3 + 1;
                            index3 = indices[i] * 3 + 2;
                            Vector4f a3 = new Vector4f(vertexPositions[index1], vertexPositions[index2], vertexPositions[index3],1);
                            a3.mul(modelMatrix);
                            Vector3f triVert3 = new Vector3f();
                            a3.xyz(triVert3);

                            //Each segment must have its data stored in a new Vector3f object before proceeding
                            Vector3f tempVec1 = new Vector3f();
                            Vector3f tempVec2 = new Vector3f();
                            Vector3f tempVec3 = new Vector3f();

                            //Finds the normal vector of the triangle
                            //This segment does this v without changing any of the base Vector3f data
                            //Vector3f triangleNormal = triVert2.sub(triVert1).cross(triVert3.sub(triVert1)).normalize();
                            triVert1.sub(triVert3,tempVec1);
                            triVert2.sub(triVert3,tempVec2);
                            tempVec1.cross(tempVec2,tempVec3);
                            tempVec3.normalize();
                            Vector3f triangleNormal = new Vector3f(tempVec3);

                            //Finds the distance the ray traveled before hitting the plane the triangle lies on
                            float denominator = triangleNormal.dot(rayVector);
                            if (denominator > -0.001) {
                                // Escape collision check on this triangle because the ray is very close to parallel or is from behind
                                continue;
                            }
                            float d = triangleNormal.dot(triVert1); // I'm not sure what d in the math equation actually represents (distance to origin?)
                            float hitDistance = (d-triangleNormal.dot(rayOrigin))/denominator;

                            //Finds the point in world space where the ray intersected the plane the triangle lies on
                            //This segment does this v without changing any Vector3f data
                            //Vector3f hitPoint = rayOrigin.add(rayVector.mul(hitDistance));
                            rayVector.mul(hitDistance, tempVec1);
                            rayOrigin.add(tempVec1,tempVec2);
                            Vector3f hitPoint = new Vector3f(tempVec2);

                            //Tests if the hit point is inside the triangle being tests
                            //These segments do this v without changing and Vector3f data
                            //boolean sideTest1 = triVert2.sub(triVert1).cross(hitPoint.sub(triVert1)).dot(triangleNormal) >= 0;
                            triVert2.sub(triVert1,tempVec1);
                            hitPoint.sub(triVert1, tempVec2);
                            tempVec1.cross(tempVec2,tempVec3);
                            boolean sideTest1 = tempVec3.dot(triangleNormal) >= 0;
                            triVert3.sub(triVert2,tempVec1);
                            hitPoint.sub(triVert2, tempVec2);
                            tempVec1.cross(tempVec2,tempVec3);
                            boolean sideTest2 = tempVec3.dot(triangleNormal) >= 0;
                            triVert1.sub(triVert3,tempVec1);
                            hitPoint.sub(triVert3, tempVec2);
                            tempVec1.cross(tempVec2,tempVec3);
                            boolean sideTest3 = tempVec3.dot(triangleNormal) >= 0;

                            boolean insideTriangle = sideTest1 && sideTest2 && sideTest3;

                            if (insideTriangle){ //Ray hit the triangle

                                if (hitDistance < outputHitDistance){ //Ray hit a closer triangle
                                    rayHit = true;
                                    outputHitDistance = hitDistance;
                                    outputEntityHit = entity;
                                    outputHitPos = hitPoint;
                                }
                            }
                        }
                    }
                }
            }
        }

        rayHitPosition = outputHitPos;
        entityHit = outputEntityHit;
        rayHitDistance = outputHitDistance;
    }

    //Getters
    public Entity getEntityHit() {
        return entityHit;
    }
    public float getRayHitDistance() {
        return rayHitDistance;
    }
    public Vector3f getRayHitPosition() {
        return new Vector3f(rayHitPosition);
    }
    public boolean isRayHit() {
        return rayHit;
    }

    //Static methods
    public static Vector3f mouseRay(MouseInput mouseInput, Window window, Scene scene){
        //return a normalized directional vector pointing towards the mouse pointer from the camera (the camera position is the origin)

        // Normalize mouse position to [-1:1] space
        float mouseNormX = (mouseInput.getCurrentPos().x*2)/window.getWindowSize().x - 1f;
        float mouseNormY = 1f - (mouseInput.getCurrentPos().y*2)/window.getWindowSize().y; // Y is flipped so positive y points up

        //Screen space
        Vector4f rayScreenSpace = new Vector4f(mouseNormX, mouseNormY, -1,1);
        //View space
        Matrix4f viewProjection = scene.getProjection().getInverseProjMatric();
        Vector4f rayViewSpace = rayScreenSpace.mul(viewProjection);
        rayViewSpace.z = -1; // Only need to un project x and y
        rayViewSpace.w = 1; // RE: above

        //World space
        Matrix4f worldProjection = scene.getCamera().getInverseViewMatrix();
        Vector3f rayWorldSpace = new Vector3f();
        rayViewSpace.mul(worldProjection).xyz(rayWorldSpace);
        rayWorldSpace.sub(scene.getCamera().getPosition());
        rayWorldSpace.normalize();
        return rayWorldSpace;
    }


}
