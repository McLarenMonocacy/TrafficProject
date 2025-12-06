import org.lwjgl.opengl.GL46;

public abstract class Shader {
    private final int programID;
    private int vertexID;
    private int fragmentID;

    public Shader(String Vert, String Frag){
        programID = GL46.glCreateProgram();
        if (programID == 0) throw new RuntimeException("Could not create program for shader");
        vertexID = loadShader(Vert, GL46.GL_VERTEX_SHADER);
        fragmentID = loadShader(Frag, GL46.GL_FRAGMENT_SHADER);
        GL46.glAttachShader(programID, vertexID);
        GL46.glAttachShader(programID, fragmentID);
        bindAttributes();
        GL46.glLinkProgram(programID);
        GL46.glValidateProgram(programID);
        //GL46.glDetachShader(programID,vertexID);
        //GL46.glDetachShader(programID,fragmentID);
        //GL46.glDeleteShader(vertexID);
        //GL46.glDeleteShader(fragmentID);
    }

    public void start(){
        GL46.glUseProgram(programID);
    }

    public void stop(){
        GL46.glUseProgram(0);
    }

    public void cleanup(){
        stop();
        if (programID != 0){
            GL46.glDeleteProgram(programID);
        }
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        GL46.glBindAttribLocation(programID, attribute, variableName);
    }

    private static int loadShader (String filePath, int type){
        String shaderSource = Utils.readFileToString(filePath);

        int shaderID = GL46.glCreateShader(type);
        if (shaderID == 0) throw new RuntimeException("Didn't create shader ID");
        GL46.glShaderSource(shaderID, shaderSource);
        GL46.glCompileShader(shaderID);
        if (GL46.glGetShaderi(shaderID, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE){
            throw new RuntimeException("Couldn't compile the shader " + filePath + ":\n" + GL46.glGetShaderInfoLog(shaderID, 1024));
        }
        return shaderID;
    }

    public int getProgramID(){
        return programID;
    }
}
