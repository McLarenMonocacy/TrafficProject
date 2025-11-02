// Manages the creation of new Commuters based on an arrival rate.
public class ArrivalProcess {

    private ExponentialDistribution distribution;

    private double nextArrivalTime;

    private String destination;

    public ArrivalProcess(double lambda, String destination) {
        this.distribution = new ExponentialDistribution(lambda);
        this.destination = destination;

        // Start the simulation at time 0 with the first arrival
        this.nextArrivalTime = 0.0;
    }

    public Commuter generateNextCommuter() {
        // Get the time for this arrival event
        double arrivalEventTime = this.nextArrivalTime;

        // Calculate the next arrival time
        this.nextArrivalTime = arrivalEventTime + distribution.sample();


        return new Commuter(this.destination, (float)arrivalEventTime);
    }

    public double getNextArrivalTime() {
        return this.nextArrivalTime;
    }
}