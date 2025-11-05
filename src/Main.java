import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

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


        Map<String, Map<String, List<String>>> pathTables = map.genPathTables();
        System.out.println(pathTables);
        for (TransitNode node : map.getNodes()){
            System.out.println(pathTables.get(node.getID()));
        }
        String savaData = map.saveNodes();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        TransitMap newMap = TransitMap.loadNodes(savaData);
        Map<String, Map<String, List<String>>> newPathTables = map.genPathTables();
        for (TransitNode node : newMap.getNodes()){
            System.out.println(newPathTables.get(node.getID()));
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(pathTables.equals(newPathTables));
    }
}