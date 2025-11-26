import java.util.LinkedList;
import java.util.Queue;

public class TransitConnection {
    public TransitConnection( TransitNode connectedNode, float distance, float time ){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.time = time;
        this.exitQueue = new LinkedList<>();
        this.waitingVehicles = new LinkedList<>();
    }

    private Queue<Commuter> exitQueue;
    private final TransitNode connectedNode;
    private final float distance;
    private final float time;

    private final Queue<TransitVehicle> waitingVehicles;
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
    }

    public void receiveVehicle(TransitVehicle vehicle){
        waitingVehicles.add(vehicle);
        checkIfVehicleCanPickUp();
    }

    private void checkIfVehicleCanPickUp(){
        //Loads a vehicle to pickup commuters if there isn't already a vehicle doing that
        if (vehicleReadyToPickup == null && !waitingVehicles.isEmpty()){
            vehicleReadyToPickup = waitingVehicles.poll();
            currentVehicleWaitTime = SimulationEngine.getCurrentTime();
        }
    }

    public int getNumbOfWaitingVehicles(){
        if (vehicleReadyToPickup == null){
            return 0;
        }
        else {
            //Add one to account for the vehicle that is ready to pickup commuters already
            return waitingVehicles.size() + 1;
        }
    }

    public float getCurrentVehicleWaitTime() {
        return currentVehicleWaitTime;
    }
}
