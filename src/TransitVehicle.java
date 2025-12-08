import java.util.LinkedList;
import java.util.Queue;

public class TransitVehicle {
    private int maxCapacity;
    private Queue<Commuter> passengers;
    private String prevStop = "";
    private float arrivalTimeToNextNode = Float.MAX_VALUE;

    public TransitVehicle (int maxCapacity, String startingStop){
        this.maxCapacity = maxCapacity;
        passengers = new LinkedList<>();
        prevStop = startingStop;
    }
    public TransitVehicle (int maxCapacity){
        this(maxCapacity, "");
    }
    public TransitVehicle (){
        this(1);
    }

    public boolean addPassenger(Commuter commuter){
        if (passengers.size() < maxCapacity){
            passengers.add(commuter);
            return true;
        }
        return false;
    }
    public Commuter removePassenger(){
        return passengers.poll();
    }

    //Get and Set functions
    public void setPrevStop(String prevStop) {
        this.prevStop = prevStop;
    }
    public String getPrevStop() {
        return prevStop;
    }

    public void setArrivalTimeToNextNode(float time){
        arrivalTimeToNextNode = time;
    }

    public float getArrivalTimeToNextNode() {
        return arrivalTimeToNextNode;
    }
}
