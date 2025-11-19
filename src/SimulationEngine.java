import java.util.LinkedList;
import java.util.List;

public class SimulationEngine {
    private static float currentTime;
    private static float runTime;
    private static TransitMap transitMap;
    private static ArrivalProcess arrivals;
    public static final List<Commuter> finishedCommuters = new LinkedList<>();

    private SimulationEngine() {
        throw new AssertionError("Cannot instantiate static class");
    }

    public static void init(String nodeData, float runTime) {
        currentTime = 0;
        SimulationEngine.runTime = runTime;
        finishedCommuters.clear();
        transitMap = TransitMap.loadNodes(nodeData);
        transitMap.genPathTables();
        arrivals = new ArrivalProcess(1, transitMap);
    }

    static String run() {
        for (TransitNode node : transitMap.getNodes()) {
            for (TransitConnection connection : node.getConnections()) {
                connection.receiveVehicle(new TransitVehicle(5));
            }
        }

        while (currentTime < runTime) {
            //TODO: should we use floats or doubles for time purposes?
            float nextArrival = (float) arrivals.getNextArrivalTime();
            NodeEvent nextNodeEvent = checkNodeEvents();

            if (nextNodeEvent == null) {
                nextNodeEvent = new NodeEvent(Float.MAX_VALUE, 0, null);
            }

            if (nextArrival < nextNodeEvent.eventTime) {
                currentTime = nextArrival;
                arrivals.generateNextCommuter();
            } else {
                switch (nextNodeEvent.eventType) {
                    case 0:
                        nextNodeEvent.affectedConnection.departVehicle();
                        currentTime = nextNodeEvent.eventTime;
                        break;
                }
            }
        }

        StringBuilder outputData = new StringBuilder();
        for (Commuter commuter : finishedCommuters) {
            float commuterTime = commuter.getEndTime() - commuter.getStartTime();
            outputData.append(String.format("%.2f", commuterTime)).append(",");
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
                if (connection.getNumbOfWaitingVehicles() > 0) {
                    float eventTime = connection.getCurrentVehicleWaitTime() + TransitConnection.WAIT_TIME;
                    if (output == null) {
                        output = new NodeEvent(eventTime, 0, connection);
                    } else if (eventTime < output.eventTime) {
                        output = new NodeEvent(eventTime, 0, connection);
                    }
                }
            }
        }
        return output;
    }

    private static class NodeEvent {
        float eventTime;
        int eventType;
        TransitConnection affectedConnection;

        NodeEvent(float eventTime, int eventType, TransitConnection affectedConnection) {
            this.affectedConnection = affectedConnection;
            this.eventTime = eventTime;
            this.eventType = eventType;
        }
    }
}