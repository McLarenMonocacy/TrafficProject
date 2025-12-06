public class ShaderGUI extends Shader{
    public ShaderGUI(){
    super("UI/res/shader/GUI.vs", "UI/res/shader/GUI.fs");
}

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
