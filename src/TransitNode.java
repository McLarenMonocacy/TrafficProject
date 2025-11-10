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
        Commuter commuter = null;
         while ( (commuter = vehicle.removePassenger()) != null ){
             String nextStop = travelTable.get( commuter.getDestination() ).get(1); //The ID of the next stop along the path
            for ( TransitConnection connection : connectionList){ // Get the connection that leads to the next stop
                if ( nextStop.equals( connection.getConnectedNode().id ) ){
                    connection.addToQueue(commuter);
                    break;
                }
            }
        }
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

