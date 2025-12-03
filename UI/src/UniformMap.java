import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class UniformMap {
    private int programID;
    private Map<String, Integer> uniforms;

    public UniformMap(int programID){
        this.programID = programID;
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName){
        int uniformLocation = GL46.glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0){
            throw new RuntimeException("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value){
        try (MemoryStack stack = MemoryStack.stackPush()){
            GL46.glUniformMatrix4fv(getUniformLocation(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }
    public void setUniform(String uniformName, int value){
        GL46.glUniform1i(getUniformLocation(uniformName), value);
    }
    public void setUniform(String uniformName, Vector4f value){
        GL46.glUniform4f(getUniformLocation(uniformName), value.x, value.y, value.z, value.w);
    }
    public void setUniform(String uniformName, Vector2f value) {
        GL46.glUniform2f(getUniformLocation(uniformName), value.x, value.y);
    }

    private int getUniformLocation(String uniformName){
        Integer uniformLocation = uniforms.get(uniformName);
        if (uniformLocation == null) throw new RuntimeException("Could not find uniform: " + uniformName);
        return uniformLocation;
    }

}
