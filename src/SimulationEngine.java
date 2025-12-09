import java.util.LinkedList;
import java.util.List;

public final class SimulationEngine {
    private enum NodeEventType{
        NULL_EVENT,
        TRY_DEPART,
        ARRIVED
    }
    private static float currentTime;
    private static float runTime;
    private static TransitMap transitMap;
    private static ArrivalProcess arrivals;
    //TODO: maybe set to private with access functions
    public static List<Commuter> finishedCommuters;
    private static boolean wasInitRun = false;

    private SimulationEngine(){
        throw new RuntimeException("Everything is static so instancing makes no sense");
    }

    public static void init(String nodeData, float runTime, float arrivalRate) {
        currentTime = 0;
        SimulationEngine.runTime = runTime;
        finishedCommuters = new LinkedList<>();
        transitMap = TransitMap.loadNodes(nodeData);
        transitMap.genPathTables();
        arrivals = new ArrivalProcess(arrivalRate, transitMap);
        wasInitRun = true;
    }

    static String run() {
        System.out.println("Starting simulation");
        if (!wasInitRun) throw new RuntimeException("Init was never ran");
        for (TransitNode node : transitMap.getNodes()) {
            for (TransitConnection connection : node.getConnections()) {
                connection.receiveVehicle(new TransitVehicle(500, node.getID()));
            }
        }

        while (currentTime < runTime) {
            //TODO: should we use floats or doubles for time purposes?
            float nextArrival = (float) arrivals.getNextArrivalTime();
            NodeEvent nextNodeEvent = checkNodeEvents();

            if (nextNodeEvent == null) {
                nextNodeEvent = new NodeEvent(Float.MAX_VALUE, NodeEventType.NULL_EVENT, null);
            }

            if (nextArrival < nextNodeEvent.eventTime) {
                currentTime = nextArrival;
                arrivals.generateNextCommuter();
            } else {
                switch (nextNodeEvent.eventType) {
                    case TRY_DEPART: //Vehicle tries to depart
                        currentTime = nextNodeEvent.eventTime;
                        //Check if any more commuters can be picked up
                        if (nextNodeEvent.affectedConnection.loadCommuterOntoVehicle()){
                            break;
                        }
                        //if not do this \/
                        nextNodeEvent.affectedConnection.departVehicle();
                        break;
                    case ARRIVED:
                        currentTime = nextNodeEvent.eventTime;
                        nextNodeEvent.affectedConnection.vehicleReachedDestination();
                        break;
                }
            }
        }

        StringBuilder outputData = new StringBuilder();
        for (Commuter commuter : finishedCommuters) {
            float commuterTime = commuter.getEndTime() - commuter.getStartTime();
            //Commuter ID, Start time, End time, Time diff, Distance
            outputData.append(commuter.getId()).append(',').append(String.format("%.2f", commuter.getStartTime())).append(',').append(String.format("%.2f", commuter.getEndTime())).append(',').append(String.format("%.2f", commuterTime)).append(',').append(String.format("%.2f", commuter.getTravelDistance())).append(',');
            for (String path : commuter.getPath()) {
                outputData.append(path).append(",");
            }
            outputData.append("\n");
        }

        return outputData.toString();
    }

    public static float getCurrentTime() {
        return currentTime;
    }

    private static NodeEvent checkNodeEvents() {
        NodeEvent output = null;
        for (TransitNode node : transitMap.getNodes()) {
            for (TransitConnection connection : node.getConnections()) {
                //Checks if vehicle can depart
                if (connection.getNumbOfWaitingVehicles() > 0) {
                    float eventTime = connection.getCurrentVehicleDepartTime();
                    if (output == null || eventTime < output.eventTime) {
                        output = new NodeEvent(eventTime, NodeEventType.TRY_DEPART, connection);
                    }
                }
                //Checks if vehicle reached destination
                TransitVehicle vehicleInTransit = connection.getVehiclesInTransit().peek();
                if (vehicleInTransit != null){
                    if (output == null || vehicleInTransit.getArrivalTimeToNextNode() < output.eventTime) {
                        output = new NodeEvent(vehicleInTransit.getArrivalTimeToNextNode(), NodeEventType.ARRIVED, connection);
                    }
                }
            }
        }
        return output;
    }

    private static class NodeEvent {
        float eventTime;
        NodeEventType eventType;
        TransitConnection affectedConnection;

        NodeEvent(float eventTime, NodeEventType eventType, TransitConnection affectedConnection) {
            this.affectedConnection = affectedConnection;
            this.eventTime = eventTime;
            this.eventType = eventType;
        }
    }
}
