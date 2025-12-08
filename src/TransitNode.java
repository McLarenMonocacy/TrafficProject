import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransitNode {
    public TransitNode (String id){
        this.id = id;
        connectionList = new LinkedList<>();
    }

    private String id;
    private final List< TransitConnection > connectionList;
    private Map<String, List<String>> travelTable;



    public boolean addConnection(TransitNode node, float distance, float time){
        for (TransitConnection connection : connectionList){
            //Check if the connection already exists
            if (node.getID().equals(connection.getConnectedNode().getID())) return false;
        }
        connectionList.add( new TransitConnection( node, distance, time));
        return true;
    }

    public void receiveCommuters( TransitVehicle vehicle ){
        Commuter commuter = null;
        //Unloads all the commuters
         while ( (commuter = vehicle.removePassenger()) != null ){
             commuter.advancePath(id);
             if (commuter.getDestination().equals(id)){ //This is the commuters destination
                 //TODO: send the commuter to some kinds of finished list
                 //TODO: in a more elegant way
                 SimulationEngine.finishedCommuters.add(commuter);
                 commuter.setEndTime(SimulationEngine.getCurrentTime());
                 continue;
             }
             String nextStop = travelTable.get( commuter.getDestination() ).get(1); //The ID of the next stop along the path
            for ( TransitConnection connection : connectionList){ // Get the connection that leads to the next stop
                if ( nextStop.equals( connection.getConnectedNode().id ) ){
                    connection.addToQueue(commuter); // Add the commuter to the queue leading to the next stop
                    break;
                }
            }
        }

         //Send the vehicle to the proper TransitConnection
         if (!vehicle.getPrevStop().isEmpty()){ //This should reject all vehicles that didn't come from a node (aka the arrival process)
             for (TransitConnection connection : connectionList){
                 if (vehicle.getPrevStop().equals(connection.getConnectedNode().getID())){
                     vehicle.setPrevStop(id);
                     connection.receiveVehicle(vehicle);
                     return;
                 }
             }
         }
    }

    public String getID(){
        return id;
    }
    public void setId(String id){
        this.id = id;
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

