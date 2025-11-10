import java.util.LinkedList;
import java.util.Queue;

public class TransitVehicle {
    private int maxCapacity;
    private Queue<Commuter> passengers = new LinkedList<>();
    public TransitVehicle (int maxCapacity){
        this.maxCapacity = maxCapacity;
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


}
