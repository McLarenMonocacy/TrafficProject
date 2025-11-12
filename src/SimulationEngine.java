import java.util.LinkedList;
import java.util.List;

public class SimulationEngine {
    public static SimulationEngine refToSelf;
    private float currentTime;
    private float runTime;
    private final TransitMap transitMap;
    private final ArrivalProcess arrivals;

    public final List<Commuter> finishedCommuters;

    public SimulationEngine(String nodeData, float runTime){
        currentTime = 0;
        this.runTime = runTime;
        finishedCommuters = new LinkedList<>();
        transitMap = TransitMap.loadNodes(nodeData);
        transitMap.genPathTables();
        arrivals = new ArrivalProcess(1,transitMap);
        refToSelf = this;
    }

    public String run(){
        while (currentTime < runTime){
            //TODO: should we use floats or doubles for time purposes?
            float nextArrival = (float) arrivals.getNextArrivalTime();
            //checkNodeEvents(); somehow determine if the next event should be a node event

            //Act the most recent event
            if (nextArrival < 99999999){//TODO: replace 99999999 with the time of the next node event
                currentTime = nextArrival;
                arrivals.generateNextCommuter();
            }
            else {
                //Act on the next node event
            }

        }
        StringBuilder outputData = new StringBuilder();
        for (Commuter commuter : finishedCommuters){
            float commuterTime = commuter.getStartTime() - commuter.getEndTime();
            outputData.append(commuterTime).append(" : ").append(commuter.getDestination()).append("\n");
        }



        return outputData.toString();
    }

    private class NodeEvent{
        float eventTime;
        int eventType; //Use an enum? what events will a node even have other than the coming and going of vehicles
        TransitNode affectedNode;

        NodeEvent(float eventTime, int eventType, TransitNode affectedNode){
            this.affectedNode = affectedNode;
            this.eventTime = eventTime;
            this.eventType = eventType;
        }
    }

    private NodeEvent checkNodeEvents(){
        for (TransitNode node : transitMap.getNodes()){
            for (TransitConnection connection : node.getConnections()){
                //Check if a departure can be made
            }
        }
        //somehow return the event that needs done
        return null;
    }

    public float getCurrentTime() {
        return currentTime;
    }
}
