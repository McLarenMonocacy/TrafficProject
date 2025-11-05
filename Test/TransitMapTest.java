import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransitMapTest {

    @org.junit.jupiter.api.Test
    void testAddNodeAddConnectionGetNodeGetNodes() {
        TransitMap map = new TransitMap();
        TransitNode node1 = new TransitNode("1");
        TransitNode node2 = new TransitNode("2");

        assertEquals(0, map.getNodes().size(), "Map not empty when created");
        assertNull(map.getNode("Not in map"), "getNode() found a node that wasn't added to the map");

        map.addNode(node1);

        assertEquals(1, map.getNodes().size(), "Map does not contain exactly 1 node when only 1 is added");
        assertEquals(node1, map.getNode(node1.getID()), "Node added to map not found via getNode");

        map.addNode(node2);

        assertEquals(2, map.getNodes().size(), "Map does not contain exactly 2 nodes when only 2 are added");
        assertEquals(node2, map.getNode(node2.getID()), "Node added to map not found via getNode");

        map.addConnection(node1, node2, 4f, 8f);

        TransitNode connectedToNode1 = node1.getConnections().getFirst().getConnectedNode();
        TransitNode connectedToNode2 = node2.getConnections().getFirst().getConnectedNode();

        assertEquals(node2, connectedToNode1, "Second node not connected properly to first node");
        assertNotEquals(node1, connectedToNode1, "First node was connected to itself");
        assertEquals(node1, connectedToNode2, "First node not connected properly to second node");
        assertNotEquals(node2, connectedToNode2, "Second node was connected to itself");
    }

    @org.junit.jupiter.api.Test
    void testSaveLoadAndGenPathTables() {
        TransitMap map = getTransitMap();

        Map<String, Map<String, List<String>>> pathTables = map.genPathTables();
        int nodeCount = 0;
        for (TransitNode node : map.getNodes()) nodeCount++;

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
                    assertEquals(key, value2.getFirst(), "Generated paths in the path table does not start with origin of path");
                }
            });
        });

        String savaData = map.saveNodes();
        assertFalse(savaData.isEmpty(), "Save data is empty");

        TransitMap newMap = TransitMap.loadNodes(savaData);
        Map<String, Map<String, List<String>>> newPathTables = map.genPathTables();

        //Check that the path tables for both maps are the same
        assertEquals(pathTables, newPathTables, "The path tables changed after saving and loading");

        //Check that the new TransitMap has all the same nodes and connections as the nodes in the old TransitMap
        int newNodeCount = 0;
        for (TransitNode newNode : newMap.getNodes()) newNodeCount++;
        assertEquals(nodeCount, newNodeCount, "Map has different number of nodes after saving and loading");


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

    @Test
    void testGenPathTables(){
        TransitMap map = getTransitMap();
        Map<String, Map<String, List<String>>> pathTables = map.genPathTables();

        String expected = "{1={2=[1, 2], 3=[1, 3], 4=[1, 3, 4], 5=[1, 3, 4, 5], 6=[1, 3, 4, 6]}, 2={1=[2, 1], 3=[2, 3], 4=[2, 3, 4], 5=[2, 3, 4, 5], 6=[2, 6]}, 3={1=[3, 1], 2=[3, 2], 4=[3, 4], 5=[3, 4, 5], 6=[3, 4, 6]}, 4={1=[4, 3, 1], 2=[4, 3, 2], 3=[4, 3], 5=[4, 5], 6=[4, 6]}, 5={1=[5, 4, 3, 1], 2=[5, 4, 3, 2], 3=[5, 4, 3], 4=[5, 4], 6=[5, 6]}, 6={1=[6, 4, 3, 1], 2=[6, 2], 3=[6, 4, 3], 4=[6, 4], 5=[6, 5]}}";
        assertEquals(expected, pathTables.toString());
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