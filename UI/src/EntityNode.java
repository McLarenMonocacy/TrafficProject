public class EntityNode extends Entity{
    TransitNode node;
    public EntityNode(String ID, String modelID, String nodeName) {
        super(ID, modelID);
        node = new TransitNode(nodeName);

    }
}
