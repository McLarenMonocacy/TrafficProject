import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransitMap {

    private final List<TransitNode> nodes;

    public TransitMap(){
        nodes = new LinkedList<>();
    }

    public boolean addNode (TransitNode node){

        for (TransitNode n : nodes){
            if (n.getID().equals(node.getID())){
                return false;
            }
        }
        nodes.add(node);
        return true;
    }

    public boolean addConnection(TransitNode node1, TransitNode node2, float distance, float time){
        boolean connection1 = node1.addConnection(node2, distance, time);
        boolean connection2 = node2.addConnection(node1, distance, time);
        if (connection1 && connection2) return true;
        else if (connection1 || connection2) throw new RuntimeException("One way connection detected");
        return false;
    }

    public List<TransitNode> getNodes(){
        return nodes;
    }


    private class CalcPathClass{
        private List<String> path = null;
        private float pathTime = 0;

        public CalcPathClass(){

        }
        public CalcPathClass(CalcPathClass calcPath){
            this.path = new LinkedList<>(calcPath.path);
            this.pathTime = calcPath.pathTime;
        }
        public void addNodeToPath(String nodeID, float connectionTime){
            if (path == null){
                path = new LinkedList<>();
                pathTime = 0;
            }
            path.add(nodeID);
            pathTime += connectionTime;
        }
    }

    public Map<String,Map<String,List<String>>> genPathTables(){
        Map<String, Map<String, List<String>>> pathTables = new HashMap<>(); //Table holding tables
        for (TransitNode startNode : nodes){
            Map<String, List<String>> traveTable = new HashMap<>(); //Table holding travel Destinations
            for (TransitNode destNode : nodes){
                if (startNode.getID().equals(destNode.getID())) continue;
                traveTable.put(destNode.getID(),calcShortestPath(startNode, destNode.getID()));
            }
            pathTables.put(startNode.getID(), traveTable);
        }
        return pathTables;
    }

    private List<String> calcShortestPath(TransitNode startNode, String targetNodeID){
        CalcPathClass newPath = new CalcPathClass();
        newPath.addNodeToPath(startNode.getID(), 0f);
        CalcPathClass calcPath = calcShortestPath(newPath, startNode, targetNodeID);
        if (calcPath == null) throw new RuntimeException("Couldn't path to node");
        return calcPath.path;
    }

    private CalcPathClass calcShortestPath(CalcPathClass currentPath, TransitNode currentNode, String targetNodeID){
        //This function should return a valid path to a targetNode or null if it finds a dead-end

        CalcPathClass shortestPath = null;
        for (TransitNode nextNode : currentNode.getConnections()){
            boolean inPath = false;
            for (String nodeID : currentPath.path){
                if (nodeID.equals(nextNode.getID())){
                    inPath = true;
                    break;
                }
            }
            if (inPath){ //Check if the node we are checking is already in the path
                continue; // Skip the node
            }
            CalcPathClass nextPath = new CalcPathClass(currentPath);
            nextPath.addNodeToPath(nextNode.getID(), nextNode.getTime(currentNode.getID()));
            if (nextNode.getID().equals(targetNodeID)){ //Check if the node we are checking is the destination
                return nextPath; //Return the valid path
            }
            CalcPathClass calcPath = calcShortestPath(nextPath, nextNode, targetNodeID);
            if (calcPath == null) continue; //The node we are checking only contains dead ends thus this node is a dead end
            if (shortestPath == null) shortestPath = calcPath; //There is not currently a valid path so set it to the found valid path
            else if (calcPath.pathTime < shortestPath.pathTime) shortestPath = calcPath; //The found valid path is shorter than the previous valid path
        }
        if (shortestPath == null) return null;
        return shortestPath;
    }


    public void saveNodes(){

    }
    public void loadNodes(String dataPath){

    }


}
