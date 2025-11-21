import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Unit tests for ArrivalProcess class
class ArrivalProcessTest {

    private TransitMap testMap;

    @BeforeEach
    void setUp() {
        testMap = new TransitMap();

        TransitNode n1= new TransitNode("NodeA");
        TransitNode n2= new TransitNode("NodeB");

        testMap.addNode(n1);
        testMap.addNode(n2);

        testMap.addConnection(n1,n2,10,10);


        testMap.genPathTables();

    }

    @Test
    void testConstructor(){

        ArrivalProcess ap = new ArrivalProcess(2.0,testMap);

        assertEquals(0.0, ap.getNextArrivalTime(),0.001);

    }

    @Test
    void testGenerateNextCommuter() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(1.0, testMap);
        double initialTime= ap.getNextArrivalTime();


        ap.generateNextCommuter();

        double newTime=ap.getNextArrivalTime();

        assertTrue(newTime> initialTime,"Time should advance after generating a commuter");
    }

    @Test
    void testMultipleArrivals() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(1.0, testMap);

        double time1= ap.getNextArrivalTime();
        ap.generateNextCommuter();

        double time2=ap.getNextArrivalTime();
        ap.generateNextCommuter();
        double time3=ap.getNextArrivalTime();


        assertTrue(time2>time1);
        assertTrue(time3>time2);

    }

    @Test
    void testArrivalTimesAreRandom() {
        // Create two identical processes
        ArrivalProcess ap1 = new ArrivalProcess(0.5, testMap);
        ArrivalProcess ap2 = new ArrivalProcess(0.5, testMap);

        // Generate arrivals
        ap1.generateNextCommuter();
        ap2.generateNextCommuter();

        // Times should be different
        assertNotEquals(ap1.getNextArrivalTime(), ap2.getNextArrivalTime());
    }

    @Test
    void testAdvanceArrival() {
        // Create arrival process
        ArrivalProcess ap = new ArrivalProcess(1.0, testMap);
        double initialTime= ap.getNextArrivalTime();

        ap.advanceArrival();

        assertTrue(ap.getNextArrivalTime()>initialTime);
    }



    @Test
    void testHighArrivalRate() {
        ArrivalProcess ap = new ArrivalProcess(10.0, testMap);

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
        ArrivalProcess ap = new ArrivalProcess(0.1, testMap);

        // Generate arrival
        ap.generateNextCommuter();
        double time1 = ap.getNextArrivalTime();

        // Next arrival should be relatively far in future
        // Expected value is 1/lambda = 10
        assertTrue(time1 > 0.0);
    }


}