import imgui.ImDrawData;
import org.lwjgl.opengl.GL46;

public class GUIMesh {
    private int indicesVBO;
    private int VAOID;
    private int verticesVBO;

    public GUIMesh(){
        VAOID = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(VAOID);

        verticesVBO = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, verticesVBO);
        GL46.glEnableVertexAttribArray(0);
        GL46.glVertexAttribPointer(0,2,GL46.GL_FLOAT, false, ImDrawData.sizeOfImDrawVert(), 0);
        GL46.glEnableVertexAttribArray(1);
        GL46.glVertexAttribPointer(1,2,GL46.GL_FLOAT, false, ImDrawData.sizeOfImDrawVert(), 8);
        GL46.glEnableVertexAttribArray(2);
        GL46.glVertexAttribPointer(2,4,GL46.GL_UNSIGNED_BYTE, true, ImDrawData.sizeOfImDrawVert(), 16);

        indicesVBO = GL46.glGenBuffers();

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
    }

    public void cleanup(){
        GL46.glDeleteBuffers(indicesVBO);
        GL46.glDeleteBuffers(verticesVBO);
        GL46.glDeleteVertexArrays(VAOID);
    }

    //Getters
    public int getVAOID() {
        return VAOID;
    }
    public int getVerticesVBO() {
        return verticesVBO;
    }
    public int getIndicesVBO() {
        return indicesVBO;
    }
}
