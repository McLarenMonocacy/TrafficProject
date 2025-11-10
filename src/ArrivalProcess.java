// Manages arrival of Commuters at a specific transit node
public class ArrivalProcess {
    private ExponentialDistribution distribution;
    private double nextArrivalTime;
    private String sourceNodeID; // Where commuters arrive
    private String destinationNodeID; // Where they want to go

    // Constructor with arrival rate and node IDs
    public ArrivalProcess(double lambda, String sourceNodeID, String destinationNodeID) {
        this.distribution = new ExponentialDistribution(lambda);
        this.sourceNodeID = sourceNodeID;
        this.destinationNodeID = destinationNodeID;
        this.nextArrivalTime = 0.0; // First arrival at time 0
    }

    // Generate next commuter and update next arrival time
    public Commuter generateNextCommuter() {
        double currentArrival = this.nextArrivalTime;

        // Schedule next arrival
        this.nextArrivalTime = currentArrival + distribution.sample();

        // Create and return new commuter
        return new Commuter(this.destinationNodeID, (float)currentArrival);
    }

    // Get when next arrival will occur
    public double getNextArrivalTime() {
        return this.nextArrivalTime;
    }

    // Get where commuters arrive
    public String getSourceNodeID() {
        return this.sourceNodeID;
    }

    // Get where commuters want to go
    public String getDestinationNodeID() {
        return this.destinationNodeID;
    }

    // Advance to next arrival without generating commuter
    public void advanceArrival() {
        this.nextArrivalTime += distribution.sample();
    }
}
