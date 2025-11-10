import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransitNode {
    public TransitNode (String id){
        this.id = id;
        connectionList = new LinkedList<>();
    }

    private final String id;
    private final List< TransitConnection > connectionList;
    private Map<String, List<String>> travelTable;



    public boolean addConnection(TransitNode node, float distance, float time){
        for (TransitConnection connection : connectionList){
            if (node.getID().equals(connection.getConnectedNode().getID())) return false;
        }
        connectionList.add( new TransitConnection( node, distance, time));
        return true;
    }

    public void receiveCommuters( TransitVehicle vehicle ){
        /* while vehicle is not empty
        remove a commuter (Idk in what data structure the commuters in the vehicle will be stored )
        check his transitConnection
        place commuter in the appropriate transitConnect queue
         */
    }

    public String getID(){
        return id;
    }
    public List< TransitConnection > getConnections(){
        return connectionList;
    }
    public Map<String, List<String>> getTravelTable(){
        return travelTable;
    }
    public void setTravelTable(Map<String, List<String>> travelTable){
        this.travelTable = travelTable;
    }
}

