public class ATransitConnection {
    //TODO: remove and replace with a traffic simulation Connection
    ATransitNode connectedNode;
    float distance;
    float travelTime;

    public ATransitConnection(ATransitNode connectedNode, float distance, float travelTime){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.travelTime = travelTime;
    }
}
