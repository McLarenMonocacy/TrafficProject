import java.util.LinkedList;
import java.util.Queue;

public class TransitConnection {
    private static final float WAIT_TIME = .5f;

    public TransitConnection( TransitNode connectedNode, float distance, float travelTime){
        this.connectedNode = connectedNode;
        this.distance = distance;
        this.travelTime = travelTime;
        this.exitQueue = new LinkedList<>();
        this.waitingVehicles = new LinkedList<>();
        this.vehiclesInTransit = new LinkedList<>();
    }

    private final Queue<Commuter> exitQueue;
    private final TransitNode connectedNode;
    private float distance;
    private float travelTime;

    private final Queue<TransitVehicle> waitingVehicles;
    private TransitVehicle vehicleReadyToPickup;
    private float currentVehicleDepartTime;
    private final Queue<TransitVehicle> vehiclesInTransit;


    public float getDistance() {
        return distance;
    }

    public float getTravelTime() {
        return travelTime;
    }

    public TransitNode getConnectedNode() {
        return connectedNode;
    }

    public boolean loadCommuterOntoVehicle(){
        //Returns true if at least one commuter boarded the vehicle
        if (vehicleReadyToPickup == null){
            return false; //There is no vehicle
        }
        boolean output = false;
        Commuter commuterToAdd = exitQueue.peek();
        while (commuterToAdd != null) {
            if (vehicleReadyToPickup.addPassenger(commuterToAdd)){
                commuterToAdd.addTravelDistance(distance);
                exitQueue.poll(); //Commuter was added so remove it from the queue
                currentVehicleDepartTime += WAIT_TIME;
                output = true;
                commuterToAdd = exitQueue.peek(); //Get the next commuter to work on
            }
            else break;
        }
        return output;
    }
    public void departVehicle(){
        if (vehicleReadyToPickup == null){
            System.out.println("WE SHOULDN'T BE HERE");
            return; //There is no vehicle
        }
        currentVehicleDepartTime = Float.MAX_VALUE;
        vehicleReadyToPickup.setArrivalTimeToNextNode(SimulationEngine.getCurrentTime() + travelTime);
        vehiclesInTransit.add(vehicleReadyToPickup);
        vehicleReadyToPickup = null;
        checkIfVehicleCanPickUp();
    }
    public void vehicleReachedDestination(){
        connectedNode.receiveCommuters(vehiclesInTransit.poll());
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
            currentVehicleDepartTime = SimulationEngine.getCurrentTime();
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

    public float getCurrentVehicleDepartTime() {
        return currentVehicleDepartTime;
    }

    public Queue<TransitVehicle> getVehiclesInTransit() {
        return vehiclesInTransit;
    }

    public void setDistance(float distance){
        this.distance = distance;
    }

    public void setTravelTime(float travelTime) {
        this.travelTime = travelTime;
    }
}
