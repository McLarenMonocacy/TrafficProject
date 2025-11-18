import java.util.LinkedList;
import java.util.List;

public class SimulationEngine {
    public static SimulationEngine refToSelf; //TODO: REMOVE
    private float currentTime;
    private final float runTime;
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
        for (TransitNode node : transitMap.getNodes()){
            for (TransitConnection connection : node.getConnections()){
                connection.receiveVehicle(new TransitVehicle(500, node.getID()));
            }
        }


        while (currentTime < runTime){
            //TODO: should we use floats or doubles for time purposes?
            float nextArrival = (float) arrivals.getNextArrivalTime();
            NodeEvent nextNodeEvent = checkNodeEvents();

            if (nextNodeEvent == null) nextNodeEvent = new NodeEvent(Float.MAX_VALUE ,0,null);
            //Act the most recent event
            if (nextArrival < nextNodeEvent.eventTime){
                currentTime = nextArrival;
                arrivals.generateNextCommuter();
            }
            else {
                switch (nextNodeEvent.eventType){
                    case 0:
                        nextNodeEvent.affectedConnection.departVehicle();
                        currentTime = nextNodeEvent.eventTime;
                }
            }

        }
        StringBuilder outputData = new StringBuilder();
        for (Commuter commuter : finishedCommuters){
            float commuterTime = commuter.getEndTime() - commuter.getStartTime();
            outputData.append(String.format("%.2f",commuterTime)).append(",");
            for (String path : commuter.getPath()){
                outputData.append(path).append(",");
            }
            outputData.append("\n");
        }



        return outputData.toString();
    }

    private class NodeEvent{
        float eventTime;
        int eventType; //Use an enum? what events will a node even have other than the coming and going of vehicles
        TransitConnection affectedConnection;

        NodeEvent(float eventTime, int eventType, TransitConnection affectedConnection){
            this.affectedConnection = affectedConnection;
            this.eventTime = eventTime;
            this.eventType = eventType;
        }
    }

    private NodeEvent checkNodeEvents(){
        NodeEvent output = null;
        for (TransitNode node : transitMap.getNodes()){
            for (TransitConnection connection : node.getConnections()){
                if (connection.getNumbOfWaitingVehicles() > 0){
                    float eventTime = connection.getCurrentVehicleWaitTime() + TransitConnection.WAIT_TIME;
                    if (output == null){
                        output = new NodeEvent(eventTime, 0, connection);
                    }
                    else if (eventTime < output.eventTime){
                        output = new NodeEvent(eventTime, 0, connection);
                    }
                }
            }
        }
        //somehow return the event that needs done
        return output;
    }

    public float getCurrentTime() {
        return currentTime;
    }
}
