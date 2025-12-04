public class EntityConnection extends Entity{
    TransitConnection connection1;
    TransitConnection connection2;
    public EntityConnection(String ID, String modelID, TransitNode node1, TransitNode node2, float distance, float time) {
        super(ID, modelID);
        connection1 = new TransitConnection(node1, distance, time);
        connection2 = new TransitConnection(node2, distance, time);
    }
}
