import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TransitNode {
    public TransitNode (String id){
        id = this.id;
    }

    String id;
    List< TransitConnection > connectionList;
    Map< String, List< String > > travelTable;



    public boolean addConnection(TransitNode node, float distance, float time){
        connectionList.add( new TransitNode.TransitConnection( node.id, distance, time));
        return true;
    }

    public void receiveCommuters( TransitVehicle vehicle ){
        /* while vehicle is not empty
        remove a commuter (Idk in what data structure the commuters in the vehicle will be stored )
        check his transitConnection
        place commuter in the appropriate transitConnect queue
         */
    }

    public String getTransitNodeId(){
        return id;
    }
    public List< TransitConnection > getConnectedNodes(){
        return connectionList;
    }
}

