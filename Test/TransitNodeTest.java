import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class TransitNodeTest {

    private TransitNode node1;
    private TransitNode node2;
    private TransitNode node3;

    @BeforeEach
    void setUp() {
        node1 = new TransitNode("Station_A");
        node2 = new TransitNode("Station_B");
        node3 = new TransitNode("Station_C");
    }

    @Test
    void testNodeCreation() {
        assertEquals("Station_A", node1.getID(), "Node ID should match constructor parameter");
    }

    @Test
    void testAddConnection() {
        assertTrue(node1.addConnection(node2, 5.0f, 10.0f), "Should successfully add connection");
    }

    @Test
    void testAddDuplicateConnection() {
        node1.addConnection(node2, 5.0f, 10.0f);
        assertFalse(node1.addConnection(node2, 3.0f, 8.0f), "Should not add duplicate connection to same node");
    }

    @Test
    void testAddMultipleConnections() {
        assertTrue(node1.addConnection(node2, 5.0f, 10.0f), "Should add first connection");
        assertTrue(node1.addConnection(node3, 8.0f, 15.0f), "Should add second connection");
        assertEquals(2, node1.getConnections().size(), "Should have 2 connections");
    }

    @Test
    void testGetConnectionsEmptyNode() {
        assertNotNull(node1.getConnections(), "Connections list should not be null");
        assertTrue(node1.getConnections().isEmpty(), "New node should have no connections");
    }

    @Test
    void testGetConnectionsReturnsCorrectList() {
        node1.addConnection(node2, 5.0f, 10.0f);
        List<TransitConnection> connections = node1.getConnections();
        assertEquals(1, connections.size(), "Should have 1 connection");
        assertEquals(node2, connections.get(0).getConnectedNode(), "Connection should point to node2");
    }

    @Test
    void testAddConnectionWithZeroDistance() {
        assertTrue(node1.addConnection(node2, 0.0f, 10.0f), "Should allow zero distance");
    }

    @Test
    void testAddConnectionWithZeroTime() {
        assertTrue(node1.addConnection(node2, 5.0f, 0.0f), "Should allow zero time");
    }

    @Test
    void testAddConnectionWithNegativeValues() {
        assertTrue(node1.addConnection(node2, -5.0f, -10.0f), "Current implementation allows negative values");
    }

    @Test
    void testSetAndGetTravelTable() {
        Map<String, List<String>> travelTable = new HashMap<>();
        node1.setTravelTable(travelTable);
        // We didn't implement getters yet , but this tests that setTravelTable doesn't throw exceptions
        assertDoesNotThrow(() -> node1.setTravelTable(travelTable));
    }

    @Test
    void testSetNullTravelTable() {
        assertDoesNotThrow(() -> node1.setTravelTable(null), "Should handle null travel table");
    }

    @Test
    void testAddSelfConnection() {
        assertTrue(node1.addConnection(node1, 0.0f, 0.0f), "Current implementation allows self-connections");
    }

    @Test
    void testNodeIDWithSpecialCharacters() {
        TransitNode specialNode = new TransitNode("Station-1_Test");
        assertEquals("Station-1_Test", specialNode.getID(), "Should handle special characters in ID");
    }

    @Test
    void testNodeIDWithEmptyString() {
        TransitNode emptyNode = new TransitNode("");
        assertEquals("", emptyNode.getID(), "Should handle empty string ID");
    }

    @Test
    void testConnectionOrderPreserved() {
        node1.addConnection(node2, 5.0f, 10.0f);
        node1.addConnection(node3, 8.0f, 15.0f);

        List<TransitConnection> connections = node1.getConnections();
        assertEquals(node2, connections.get(0).getConnectedNode(), "First connection should be node2");
        assertEquals(node3, connections.get(1).getConnectedNode(), "Second connection should be node3");
    }
}