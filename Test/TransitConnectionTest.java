import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TransitConnectionTest {

    private TransitNode node1;
    private TransitNode node2;
    private TransitConnection connection;

    @BeforeEach
    void setUp() {
        node1 = new TransitNode("Station_A");
        node2 = new TransitNode("Station_B");
        connection = new TransitConnection(node2, 10.5f, 25.0f);
    }

    @Test
    void testConnectionCreation() {
        assertNotNull(connection, "Connection should be created");
    }

    @Test
    void testGetConnectedNode() {
        assertEquals(node2, connection.getConnectedNode(), "Should return correct connected node");
    }

    @Test
    void testGetDistance() {
        assertEquals(10.5f, connection.getDistance(), 0.001f, "Should return correct distance");
    }

    @Test
    void testGetTravelTime() {
        assertEquals(25.0f, connection.getTravelTime(), 0.001f, "Should return correct travel time");
    }

    @Test
    void testZeroDistance() {
        TransitConnection zeroDistConn = new TransitConnection(node2, 0.0f, 10.0f);
        assertEquals(0.0f, zeroDistConn.getDistance(), 0.001f, "Should handle zero distance");
    }

    @Test
    void testZeroTime() {
        TransitConnection zeroTimeConn = new TransitConnection(node2, 10.0f, 0.0f);
        assertEquals(0.0f, zeroTimeConn.getTravelTime(), 0.001f, "Should handle zero time");
    }

    @Test
    void testNegativeDistance() {
        TransitConnection negDistConn = new TransitConnection(node2, -10.0f, 25.0f);
        assertEquals(-10.0f, negDistConn.getDistance(), 0.001f, "Current implementation allows negative distance");
    }

    @Test
    void testNegativeTime() {
        TransitConnection negTimeConn = new TransitConnection(node2, 10.0f, -25.0f);
        assertEquals(-25.0f, negTimeConn.getTravelTime(), 0.001f, "Current implementation allows negative time");
    }

    @Test
    void testNullConnectedNode() {
        TransitConnection nullNodeConn = new TransitConnection(null, 10.0f, 25.0f);
        assertNull(nullNodeConn.getConnectedNode(), "Should handle null connected node");
    }

    @Test
    void testLargeDistanceValue() {
        TransitConnection largeConn = new TransitConnection(node2, Float.MAX_VALUE, 25.0f);
        assertEquals(Float.MAX_VALUE, largeConn.getDistance(), "Should handle large float values");
    }

    @Test
    void testLargeTimeValue() {
        TransitConnection largeConn = new TransitConnection(node2, 10.0f, Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, largeConn.getTravelTime(), "Should handle large float values");
    }

    @Test
    void testVerySmallValues() {
        TransitConnection tinyConn = new TransitConnection(node2, 0.001f, 0.001f);
        assertEquals(0.001f, tinyConn.getDistance(), 0.0001f, "Should handle very small positive values");
        assertEquals(0.001f, tinyConn.getTravelTime(), 0.0001f, "Should handle very small positive values");
    }

    @Test
    void testConnectionImmutability() {
        assertEquals(node2, connection.getConnectedNode(), "Connected node should not change");
        assertEquals(10.5f, connection.getDistance(), 0.001f, "Distance should not change");
        assertEquals(25.0f, connection.getTravelTime(), 0.001f, "Travel time should not change");
    }
}