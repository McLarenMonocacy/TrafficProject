import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        TransitMap map = getTransitMap();
        SimulationEngine.init(map.saveNodes(), 999999);
        String outputData = SimulationEngine.run();
        CSVConversion.stringToFile("OUTPUTDATA.TXT", outputData);
        System.out.println(SimulationEngine.finishedCommuters.size());


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