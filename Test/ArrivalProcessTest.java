import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Unit tests for ArrivalProcess class
class ArrivalProcessTest {

    @Test
    void testConstructor() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(0.5, "Jefferson", "DC_Downtown");

        // Check initial values
        assertEquals("Jefferson", ap.getSourceNodeID());
        assertEquals("DC_Downtown", ap.getDestinationNodeID());
        assertEquals(0.0, ap.getNextArrivalTime(), 0.001);
    }

    @Test
    void testGenerateNextCommuter() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(0.5, "Myersville", "Rockville");

        // Generate first commuter
        Commuter c1 = ap.generateNextCommuter();

        // Check commuter properties
        assertNotNull(c1);
        assertEquals("Rockville", c1.getDestination());
        assertEquals(0.0f, c1.getStartTime(), 0.001f);

        // Next arrival time should be positive
        assertTrue(ap.getNextArrivalTime() > 0.0);
    }

    @Test
    void testMultipleArrivals() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(1.0, "Frederick", "Bethesda");

        // Generate multiple commuters
        Commuter c1 = ap.generateNextCommuter();
        double time1 = ap.getNextArrivalTime();

        Commuter c2 = ap.generateNextCommuter();
        double time2 = ap.getNextArrivalTime();

        Commuter c3 = ap.generateNextCommuter();
        double time3 = ap.getNextArrivalTime();

        // Times should be increasing
        assertTrue(time1 > 0.0);
        assertTrue(time2 > time1);
        assertTrue(time3 > time2);

        // Each commuter should have correct destination
        assertEquals("Bethesda", c1.getDestination());
        assertEquals("Bethesda", c2.getDestination());
        assertEquals("Bethesda", c3.getDestination());
    }

    @Test
    void testArrivalTimesAreRandom() {
        // Create two identical processes
        ArrivalProcess ap1 = new ArrivalProcess(0.5, "NodeA", "NodeB");
        ArrivalProcess ap2 = new ArrivalProcess(0.5, "NodeA", "NodeB");

        // Generate arrivals
        ap1.generateNextCommuter();
        ap2.generateNextCommuter();

        // Times should be different
        assertNotEquals(ap1.getNextArrivalTime(), ap2.getNextArrivalTime());
    }

    @Test
    void testAdvanceArrival() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(0.8, "Source", "Dest");

        // Advance without generating commuter
        double time1 = ap.getNextArrivalTime();
        ap.advanceArrival();
        double time2 = ap.getNextArrivalTime();

        // Time should have advanced
        assertTrue(time2 > time1);
    }

    @Test
    void testCommuterStartTimes() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(0.5, "NodeX", "NodeY");

        // Generate commuters and check start times match arrival times
        Commuter c1 = ap.generateNextCommuter();
        assertEquals(0.0f, c1.getStartTime(), 0.001f);

        double expectedTime2 = ap.getNextArrivalTime();
        Commuter c2 = ap.generateNextCommuter();
        assertEquals(expectedTime2, c2.getStartTime(), 0.001f);

        double expectedTime3 = ap.getNextArrivalTime();
        Commuter c3 = ap.generateNextCommuter();
        assertEquals(expectedTime3, c3.getStartTime(), 0.001f);
    }

    @Test
    void testHighArrivalRate() {
        ArrivalProcess ap = new ArrivalProcess(10.0, "BusyNode", "DC");

        // Generate several arrivals
        ap.generateNextCommuter();
        double time1 = ap.getNextArrivalTime();
        ap.generateNextCommuter();
        double time2 = ap.getNextArrivalTime();

        // Average inter-arrival should be small (1/lambda = 0.1)
        double interArrival = time2 - time1;
        assertTrue(interArrival < 1.0); // Should be much less than 1
    }

    @Test
    void testLowArrivalRate() {
        // Low lambda means long inter-arrival times
        ArrivalProcess ap = new ArrivalProcess(0.1, "QuietNode", "DC");

        // Generate arrival
        ap.generateNextCommuter();
        double time1 = ap.getNextArrivalTime();

        // Next arrival should be relatively far in future
        // Expected value is 1/lambda = 10
        assertTrue(time1 > 0.0);
    }

    @Test
    void testSourceDestinationPreserved() {
        // Test that node IDs are preserved correctly
        ArrivalProcess ap = new ArrivalProcess(1.0, "StartHere", "EndThere");

        // Generate many commuters
        for (int i = 0; i < 10; i++) {
            Commuter c = ap.generateNextCommuter();
            assertEquals("EndThere", c.getDestination());
        }

        // Source should never change
        assertEquals("StartHere", ap.getSourceNodeID());
    }
}