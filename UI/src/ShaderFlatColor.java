public class ShaderFlatColor extends Shader{
    public ShaderFlatColor(){
        super("UI/res/shader/FlatColor.vs", "UI/res/shader/FlatColor.fs");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
