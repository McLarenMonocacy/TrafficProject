import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        TransitMap map = getTransitMap();
        SimulationEngine sim = new SimulationEngine(map.saveNodes(), 999999);
        String outputData = sim.run();
        CSVConversion.stringToFile("OUTPUTDATA.TXT", outputData);
        System.out.println(SimulationEngine.refToSelf.finishedCommuters.size());

    }



    private static void oldMain(){

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


        System.out.println("Traveled " + commuter.getTravelDistance() + "Miles");

    }

    private static TransitMap getTransitMap() {
        TransitNode node1 = new TransitNode("Fairlane");
        TransitNode node2 = new TransitNode("Mark's Burg");
        TransitNode node3 = new TransitNode("Clown town");
        TransitNode node4 = new TransitNode("Jolly Rodger bay");
        TransitNode node5 = new TransitNode("Big apple");
        TransitNode node6 = new TransitNode("Small apple");

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