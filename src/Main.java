import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String saveFile = "nodemap.dat";

        TransitMap map = getTransitMap();


        Map<String, Map<String, List<String>>> pathTables = map.genPathTables();
        for (TransitNode node : map.getNodes()){
            System.out.println(node.getID() + ":" + pathTables.get(node.getID()));
            System.out.println(node.getID() + ":" + node.getTravelTable());
        }
        String savaData = map.saveNodes();
        CSVConversion.stringToFile(saveFile, savaData);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        TransitMap newMap = TransitMap.loadNodes(CSVConversion.fileToString(saveFile));
        Map<String, Map<String, List<String>>> newPathTables = newMap.genPathTables();
        for (TransitNode node : newMap.getNodes()){
            System.out.println(node.getID() + ":" + newPathTables.get(node.getID()));
            System.out.println(node.getID() + ":" + node.getTravelTable());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(pathTables.equals(newPathTables));

        TransitNode startNode = new TransitNode("0");
        map.addNode(startNode);
        map.addConnection(startNode, map.getNodes().getFirst(), 1f, 1f);
        map.genPathTables();
        Commuter commuter = new Commuter("6", 0f);
        TransitVehicle bus = new TransitVehicle(5);
        bus.addPassenger(commuter);
        startNode.receiveCommuters(bus);


    }





    private static TransitMap getTransitMap() {
        TransitNode node1 = new TransitNode("1");
        TransitNode node2 = new TransitNode("2");
        TransitNode node3 = new TransitNode("3");
        TransitNode node4 = new TransitNode("4");
        TransitNode node5 = new TransitNode("5");
        TransitNode node6 = new TransitNode("6");

        TransitMap map = new TransitMap();

        map.addNode(node1);
        map.addNode(node2);
        map.addNode(node3);
        map.addNode(node4);
        map.addNode(node5);
        map.addNode(node6);

        map.addConnection(node1,node2,1f,1f);
        map.addConnection(node1,node3,1f,1f);
        map.addConnection(node2,node3,1f,1f);
        map.addConnection(node3,node4,3f,3f);
        map.addConnection(node2,node6,9f,9f);
        map.addConnection(node4,node5,1f,1f);
        map.addConnection(node4,node6,1f,1f);
        map.addConnection(node5,node6,1f,1f);
        return map;
    }
}