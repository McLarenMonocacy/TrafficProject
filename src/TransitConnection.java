import java.util.Queue;

public class TransitConnection {
    public TransitConnection( String id, float distance, float time ){
        this.idOfConnectedNode = id;
        this.distance = distance;
        this.time = time;
    }

    Queue<Commuter> transitConnection;
    private String idOfConnectedNode;
    private float distance;
    private float time;


    public float getDistance() {
        return distance;
    }

    public float getTravelTime() {
        return time;
    }

    public String getConnectedNode() {
        return idOfConnectedNode;
    }
}
