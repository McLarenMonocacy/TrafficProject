public class EntityNode extends Entity{
    private TransitNode node;
    public EntityNode(String ID, String modelID, String nodeName) {
        super(ID, modelID);
        node = new TransitNode(nodeName);

    }

    public TransitNode getNode(){
        return node;
    }

    public void setNode(TransitNode node) {
        this.node = node;
    }
}
