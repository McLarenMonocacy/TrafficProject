import java.nio.channels.FileLock;
import java.util.LinkedList;
import java.util.Queue;

public class TransitConnection {
    public static final float WAIT_TIME = 5;

    public TransitConnection( TransitNode connectedNode, float distance, float time ){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.time = time;
        this.exitQueue = new LinkedList<>();
        waitingVehicles = new LinkedList<>();
    }

    Queue<Commuter> exitQueue;
    private final TransitNode connectedNode;
    private final float distance;
    private final float time;
    private Queue<TransitVehicle> waitingVehicles;
    private TransitVehicle vehicleReadyToPickup;
    private float currentVehicleWaitTime;


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
        if (vehicleReadyToPickup == null){
            return; //There is no vehicle
        }
        currentVehicleWaitTime = Float.MAX_VALUE;
        Commuter commuterToAdd = exitQueue.peek();
        while (commuterToAdd != null) {
            if (vehicleReadyToPickup.addPassenger(commuterToAdd)){
                commuterToAdd.addTravelDistance(distance);
                exitQueue.poll(); //Commuter was added so remove it from the queue
                commuterToAdd = exitQueue.peek(); //Get the next commuter to work on
            }
            else break;
        }
        connectedNode.receiveCommuters(vehicleReadyToPickup);
        vehicleReadyToPickup = null;
        checkIfVehicleCanPickUp();
    }

    public void addToQueue(Commuter commuter){
        exitQueue.offer(commuter);
    }

    public void receiveVehicle(TransitVehicle vehicle){
        waitingVehicles.add(vehicle);
        checkIfVehicleCanPickUp();
    }

    private void checkIfVehicleCanPickUp(){
        //Loads a vehicle to pickup commuters if there isn't already a vehicle doing that
        if (vehicleReadyToPickup == null && !waitingVehicles.isEmpty()){
            vehicleReadyToPickup = waitingVehicles.poll();
            currentVehicleWaitTime = SimulationEngine.refToSelf.getCurrentTime();
        }
    }

    public int getNumbOfWaitingVehicles(){
        if (vehicleReadyToPickup == null){
            return 0;
        }
        else {
            return waitingVehicles.size();
        }
    }

    public float getCurrentVehicleWaitTime() {
        return currentVehicleWaitTime;
    }
}
