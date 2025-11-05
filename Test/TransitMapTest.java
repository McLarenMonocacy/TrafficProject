import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransitMapTest {

    @org.junit.jupiter.api.Test
    void addNode() {
    }

    @org.junit.jupiter.api.Test
    void addConnection() {
    }

    @org.junit.jupiter.api.Test
    void getNodes() {
    }

    @org.junit.jupiter.api.Test
    void getNode() {
    }

    @org.junit.jupiter.api.Test
    void genPathTables() {
    }

    @org.junit.jupiter.api.Test
    void saveNodes() {
    }

    @org.junit.jupiter.api.Test
    void loadNodes() {
        TransitMap map = getTransitMap();

        Map<String, Map<String, List<String>>> pathTables = map.genPathTables();
        int nodeCount = 0;
        for (TransitNode node : map.getNodes()) nodeCount++;


        int mapCount = 0;
        pathTables.forEach((key, value) -> {
            boolean test = false;
            for (TransitNode node : map.getNodes()) {
                if (node.getID().equals(key)){
                    test = true;
                    break;
                }
            }
            assertTrue(test, "Generated pathTables includes non existent node");
            value.forEach((key2, value2)->{
                for (TransitNode node : map.getNodes()) {
                    assertTrue(key.equals(value2.getFirst()), "Generated paths in the path table does not start with origin of path");
                }
            });
        });

        String savaData = map.saveNodes();
        assertFalse(savaData.isEmpty(), "Save data is empty");

        TransitMap newMap = TransitMap.loadNodes(savaData);
        Map<String, Map<String, List<String>>> newPathTables = map.genPathTables();

        assertEquals(pathTables, newPathTables, "The path tables changed after saving and loading");

        //Check that the new TransitMap has all the same nodes and connections as the nodes in the old TransitMap
        for (TransitNode oldNode : map.getNodes()){
            boolean hasNode = false;
            for (TransitNode newNode : newMap.getNodes()){
                if (oldNode.getID().equals(newNode.getID())){
                    for (TransitConnection oldConnection : oldNode.getConnections()){
                        boolean hasConnection = false;
                        for (TransitConnection newConnection : newNode.getConnections()){
                            if (oldConnection.getConnectedNode().getID().equals(newConnection.getConnectedNode().getID())){
                                hasConnection = true;
                                break;
                            }
                        }
                        assertTrue(hasConnection, "Node missing connection after saving and loading");

                    }
                    hasNode = true;
                    break;
                }
            }
            assertTrue(hasNode, "Node in original map does not exist after saving and loading");
        }
            
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