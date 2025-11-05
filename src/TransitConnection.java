import java.util.Queue;

public class TransitConnection {
    public TransitConnection( TransitNode connectedNode, float distance, float time ){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.time = time;
    }

    Queue<Commuter> exitQueue;
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

    public void departVehicle(){
        TransitVehicle vehicle = new TransitVehicle(5);
        for (int i = 0; i < exitQueue.size(); i++) {
            Commuter commuterToAdd = exitQueue.peek();
            if (vehicle.addPassenger(commuterToAdd)){
                exitQueue.poll();
            }
        }



        connectedNode.receiveCommuters(vehicle);
    }

    public void addToQueue(Commuter commuter){
        exitQueue.offer(commuter);
    }
}
