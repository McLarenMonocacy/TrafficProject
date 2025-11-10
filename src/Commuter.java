// Represents a person commuting through the transit system
public class Commuter {
    private final String destination;
    private final float startTime;
    private float endTime;
    private float travelDistance;

    // Create new commuter with destination and start time
    public Commuter(String destination, float startTime) {
        this.destination = destination;
        this.startTime = startTime;
        this.endTime = -1.0f; // Not yet finished
        this.travelDistance = 0.0f;
    }

    // Record when commuter finishes
    public void setEndTime(float time) {
        this.endTime = time;
    }

    // Add distance traveled
    public void addTravelDistance(float distance) {
        this.travelDistance += distance;
    }

    // Get destintion node ID
    public String getDestination() {
        return destination;
    }

    // Get start time
    public float getStartTime() {
        return startTime;
    }

    // Get end time
    public float getEndTime() {
        return endTime;
    }

    // Get total distance traveled
    public float getTravelDistance() {
        return travelDistance;
    }

    // Calculate passage time (wait + travel)
    public float getPassageTime() {
        if (endTime < 0) return -1.0f; // Not finished yet
        return endTime - startTime;
    }
}