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
        // Check that a node of the same ID isn't already in the map
        for (TransitNode n : nodes){
            if (n.getID().equals(node.getID())){
                return false;
            }
        }
        nodes.add(node);
        return true;
    }

    public boolean addConnection(TransitNode node1, TransitNode node2, float distance, float time){
        if (node1.getID().equals(node2.getID())) return false; //Nodes are the same

        boolean node1Exists = false;
        boolean node2Exists = false;

        for (TransitNode node : nodes){
            // Check that both nodes are in the map first
            if (!node1Exists && node.getID().equals(node1.getID())) node1Exists = true;
            if (!node2Exists && node.getID().equals(node2.getID())) node2Exists = true;

            if (node1Exists && node2Exists) break;
        }
        if (!node2Exists || !node1Exists) return false;

        boolean connection1 = node1.addConnection(node2, distance, time);
        boolean connection2 = node2.addConnection(node1, distance, time);
        if (connection1 && connection2) return true;
        else if (connection1 || connection2) throw new RuntimeException("One way connection detected");
        return false;
    }

    public List<TransitNode> getNodes(){
        return nodes;
    }

    public TransitNode getNode(String nodeID){
        //Returns a node matching the nodeID, null if non is found
        for (TransitNode node : nodes){
            if (nodeID.equals(node.getID())) return node;
        }
        return null;
    }


    private static class CalcPathClass{ // Could use a better name
        private final List<String> path;
        private float pathTime;

        public CalcPathClass(){
            this.path = new LinkedList<>();
            this.pathTime = 0;
        }
        public CalcPathClass(CalcPathClass calcPath){
            this.path = new LinkedList<>(calcPath.path);
            this.pathTime = calcPath.pathTime;
        }
        public void addNodeToPath(String nodeID, float connectionTime){
            path.add(nodeID);
            pathTime += connectionTime;
        }
    }

    public Map<String,Map<String,List<String>>> genPathTables(){
        Map<String, Map<String, List<String>>> pathTables = new HashMap<>(); //Table holding tables
        for (TransitNode startNode : nodes){
            Map<String, List<String>> travelTable = new HashMap<>(); //Table holding travel Destinations
            for (TransitNode destNode : nodes){
                if (startNode.getID().equals(destNode.getID())) continue;
                travelTable.put(destNode.getID(),calcShortestPath(startNode, destNode.getID()));
            }
            startNode.setTravelTable(travelTable); // store the travel table in the node
            System.out.println("Finished travel table for " + startNode.getID());
            pathTables.put(startNode.getID(), travelTable);
        }
        return pathTables;
    }

    private List<String> calcShortestPath(TransitNode startNode, String targetNodeID){
        CalcPathClass newPath = new CalcPathClass();
        newPath.addNodeToPath(startNode.getID(), 0f);
        CalcPathClass calcPath = calcShortestPath(newPath, startNode, targetNodeID);
        if (calcPath == null){
            throw new RuntimeException("Couldn't path to node");
        }
        return calcPath.path;
    }

    private CalcPathClass calcShortestPath(CalcPathClass currentPath, TransitNode currentNode, String targetNodeID){
        //This function should return a valid path to a targetNode or null if it finds a dead-end

        CalcPathClass shortestPath = null;
        for (TransitConnection connection : currentNode.getConnections()){
            boolean inPath = false;
            for (String nodeID : currentPath.path){
                if (nodeID.equals(connection.getConnectedNode().getID())){
                    inPath = true;
                    break;
                }
            }
            if (inPath){ //Check if the node we are checking is already in the path
                continue; // Skip the node
            }
            CalcPathClass nextPath = new CalcPathClass(currentPath);
            nextPath.addNodeToPath(connection.getConnectedNode().getID(), connection.getTravelTime());
            if (connection.getConnectedNode().getID().equals(targetNodeID)){ //Check if the node we are checking is the destination
                return nextPath; //Return the valid path
            }
            CalcPathClass calcPath = calcShortestPath(nextPath, connection.getConnectedNode(), targetNodeID);
            if (calcPath == null) continue; //The node we are checking only contains dead ends thus this node is a dead end
            if (shortestPath == null) shortestPath = calcPath; //There is not currently a valid path so set it to the found valid path
            else if (calcPath.pathTime < shortestPath.pathTime) shortestPath = calcPath; //The found valid path is shorter than the previous valid path
        }
        return shortestPath;
    }


    public String saveNodes(){
        StringBuilder builder = new StringBuilder();
        for (TransitNode node : nodes){
            builder.append(node.getID());
            builder.append(",");
        }
        builder.append("\n");
        for (TransitNode node : nodes){
            builder.append(node.getID());
            builder.append(",");
            for (TransitConnection connection : node.getConnections()){
                builder.append(connection.getConnectedNode().getID());
                builder.append(",");
                builder.append(connection.getDistance());
                builder.append(",");
                builder.append(connection.getTravelTime());
                builder.append(",");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    public static TransitMap loadNodes(String data){
        TransitMap outputMap = new TransitMap();
        String[] dataBits = data.split("\n");
        String[] firstLine = dataBits[0].split(",");
        for (String string : firstLine){
            outputMap.addNode(new TransitNode(string));
        }
        for (int i = 1; i < dataBits.length; i++) {
            String[] line = dataBits[i].split(",");
            TransitNode lineNode = outputMap.getNode(line[0]);
            for (int j = 3; j < line.length; j += 3) {
                String connectedNodeID = line[j-2];
                float distance = Float.parseFloat(line[j-1]);
                float time = Float.parseFloat(line[j]);
                outputMap.addConnection(lineNode, outputMap.getNode(connectedNodeID),distance,time);
            }
        }
        return outputMap;
    }


}
