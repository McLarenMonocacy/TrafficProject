import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    private int textureID;
    private final String filePath;

    public Texture(int width, int height, ByteBuffer buffer){
        this.filePath = "";
        generateTexture(width, height, buffer);

    }

    public Texture(String filePath){
        try (MemoryStack stack = MemoryStack.stackPush()){
            this.filePath = filePath;
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Load the image file into a byte buffer
            ByteBuffer buffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channels, 4);
            if (buffer == null) throw new RuntimeException("Failed to load file: " + filePath);
            int width = widthBuffer.get();
            int height = heightBuffer.get();

            generateTexture(width, height, buffer);

            // Release the memory buffer
            STBImage.stbi_image_free(buffer);
        }
    }

    public void bind(){
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);
    }

    public void cleanup(){
        GL46.glDeleteTextures(textureID);
    }

    private void generateTexture(int width, int height, ByteBuffer buffer){
        textureID = GL46.glGenTextures();

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1); //TODO: Look this up for more information on what it is exactly doing
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
    }

    public String getFilePath(){
        return filePath;
    }
    public int getTextureID(){
        return textureID;
    }
}
