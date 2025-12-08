import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public class Mesh {
    private final int vaoID;
    private final int vertexCount;
    private final List<Integer> vboIDList;
    private final float[] vertexPositions;
    private final int[] indices;

    public Mesh(float[] vertexPositions, float[] textureCoordinate, int[] indices){
        this.vertexCount = indices.length;
        this.vertexPositions = vertexPositions.clone();
        this.indices = indices;
        vboIDList = new LinkedList<>();

        //Create VAO
        vaoID = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vaoID);

        //Create positions VBO
        int vboID = GL46.glGenBuffers();
        vboIDList.add(vboID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        //Preparing the position data for OpenGL
        FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(vertexPositions.length);
        positionsBuffer.put(0,vertexPositions);
        //Load the position data into the VBO
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, positionsBuffer, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(0);
        //               Location of data, 3 numb per positon (x,y,z), numb is a float,
        GL46.glVertexAttribPointer(0,3,GL46.GL_FLOAT, false, 0, 0);


        //Create textureCoordinate VBO
        vboID = GL46.glGenBuffers();
        vboIDList.add(vboID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        //Preparing the textureCoordinate data for OpenGL
        FloatBuffer textureCoordinateBuffer = MemoryUtil.memCallocFloat(textureCoordinate.length);
        textureCoordinateBuffer.put(0,textureCoordinate);
        //Load the position data into the VBO
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, textureCoordinateBuffer, GL46.GL_STATIC_DRAW);
        GL46.glEnableVertexAttribArray(1);
        //               Location of data, 2 numb per positon (x,y), numb is a float,
        GL46.glVertexAttribPointer(1,2,GL46.GL_FLOAT, false, 0, 0);

        //Create indices VBO
        vboID = GL46.glGenBuffers();
        vboIDList.add(vboID);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vboID);
        //Preparing the indice data for OpenGL
        IntBuffer indicesBuffer = MemoryUtil.memCallocInt(indices.length);
        indicesBuffer.put(0, indices);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, indicesBuffer, GL46.GL_STATIC_DRAW);


        //Unbind VBO and VAO
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);

        //Frees the reserved memory
        MemoryUtil.memFree(positionsBuffer);
        MemoryUtil.memFree(textureCoordinateBuffer);
        MemoryUtil.memFree(indicesBuffer);
    }

    public int getVaoID(){
        return vaoID;
    }
    public int getVertexCount(){
        return vertexCount;
    }
    public float[] getVertexPositions() {
        return vertexPositions;
    }
    public int[] getIndices() {
        return indices;
    }

    public void cleanup(){
        vboIDList.forEach(GL46::glDeleteBuffers); // Delete all the VBOs
        GL46.glDeleteVertexArrays(vaoID); // Delete the VAO
    }
}
