public class EntityConnection extends Entity{
    private final TransitConnection connection1;
    private final TransitConnection connection2;
    private final float distance;
    private final float time;
    public EntityConnection(String ID, String modelID, TransitNode node1, TransitNode node2, float distance, float time) {
        super(ID, modelID);
        connection1 = new TransitConnection(node1, distance, time);
        connection2 = new TransitConnection(node2, distance, time);
        this.distance = distance;
        this.time = time;
    }

    public float getDistance() {
        return distance;
    }

    public float getTime() {
        return time;
    }

    public TransitConnection getConnection1() {
        return connection1;
    }

    public TransitConnection getConnection2() {
        return connection2;
    }
}
