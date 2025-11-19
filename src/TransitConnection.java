import java.util.LinkedList;
import java.util.Queue;

public class TransitConnection {
    public TransitConnection( TransitNode connectedNode, float distance, float time ){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.time = time;
        this.exitQueue = new LinkedList<>();
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
        Commuter commuterToAdd = exitQueue.peek();
        while (commuterToAdd != null) {
            if (vehicle.addPassenger(commuterToAdd)){
                commuterToAdd.addTravelDistance(distance);
                exitQueue.poll(); //Commuter was added so remove it from the queue
                commuterToAdd = exitQueue.peek(); //Get the next commuter to work on
            }
            else break;
        }
        connectedNode.receiveCommuters(vehicle);
    }

    public void addToQueue(Commuter commuter){
        exitQueue.offer(commuter);
        //TODO: CHANGE: FOR NOW INSTANTLY DEPART THE COMMUTER
        departVehicle();
    }
}
