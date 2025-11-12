import java.util.LinkedList;
import java.util.List;

// Manages arrival of Commuters at a specific transit node
public class ArrivalProcess {
    private ExponentialDistribution distribution;
    private float nextArrivalTime;
    private final TransitMap transitMap;
    private List<TransitNode> nodes;

    // Constructor with arrival rate and node IDs
    public ArrivalProcess(double lambda, TransitMap map) {
        this.distribution = new ExponentialDistribution(lambda);
        this.transitMap = map;
        genNodeList();
        this.nextArrivalTime = 0.0f; // First arrival at time 0
    }

    // Generate next commuter and update next arrival time
    public void generateNextCommuter() {

        TransitNode startNode = nodes.get(randIndex());
        TransitNode endNode = nodes.get(randIndex());
        while (startNode.getID().equals(endNode.getID())){
            //If the start and end nodes are the same reroll the end node
            endNode = nodes.get(randIndex());
        }

        Commuter newCommuter = new Commuter(endNode.getID(), nextArrivalTime);
        TransitVehicle roboTaxi = new TransitVehicle(1);
        roboTaxi.addPassenger(newCommuter);
        startNode.receiveCommuters(roboTaxi);

        nextArrivalTime += (float) distribution.sample();
    }

    // Get when next arrival will occur
    public double getNextArrivalTime() {
        return this.nextArrivalTime;
    }

    // Advance to next arrival without generating commuter
    public void advanceArrival() {
        //TODO: what is the purpose of this function
        this.nextArrivalTime += distribution.sample();
    }

    private void genNodeList(){
        nodes = transitMap.getNodes();
    }

    public int randIndex(){
        return (int) Math.round(Math.floor(Math.random()*nodes.size()));
    }

}
