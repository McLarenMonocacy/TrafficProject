import java.util.Queue;

public class TransitConnection {
    public TransitConnection( TransitNode connectedNode, float distance, float time ){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.time = time;
    }

    Queue<Commuter> transitConnection;
    private final TransitNode connectedNode;
    private final float distance;
    private final float time;


    public float getDistance() {
        return distance;
    }

    public float getTravelTime() {
        return time;
    }

    public TransitNode getConnectedNode() {
        return connectedNode;
    }
}
