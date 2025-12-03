public class EntityConnection extends Entity{
    ATransitConnection connection1;
    ATransitConnection connection2;
    public EntityConnection(String ID, String modelID, ATransitNode node1, ATransitNode node2, float distance, float time) {
        super(ID, modelID);
        connection1 = new ATransitConnection(node1, distance, time);
        connection2 = new ATransitConnection(node2, distance, time);
    }
}
