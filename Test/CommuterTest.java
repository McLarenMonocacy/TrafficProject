import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CommuterTest {

    @Test
    void testConstructorAndDefaults() {
        String destination = "Nodes5";
        float startTime = 10.0f;

        Commuter commuter = new Commuter(destination, startTime);

        assertEquals(destination, commuter.getDestination(), "Destination should match constructor input");
        assertEquals(startTime, commuter.getStartTime(), "Start time should match constructor input");
        assertEquals(-1.0f, commuter.getEndTime(), "End time should default to -1.0");
        assertEquals(0.0f, commuter.getTravelDistance(), "Initial distance should be 0.0");
    }

    @Test
    void testPassageTimeCalculation() {
        Commuter commuter = new Commuter("End", 10.0f);

        //Test before finish
        assertEquals(-1.0f, commuter.getPassageTime(), "Passage time should be -1.0 if trip is not finished ");

        //Test after finish
        commuter.setEndTime(25.5f);
        float expectedTime = 25.5f - 10.0f;

        assertEquals(expectedTime, commuter.getPassageTime(), 0.001, "Passage time should be (EndTime - StartTime)");

    }

    @Test
    void testDistanceAccumulation() {
        Commuter commuter = new Commuter("End", 0.0f);

        commuter.addTravelDistance(5.5f);
        assertEquals(5.5f, commuter.getTravelDistance(), 0.001);

        commuter.addTravelDistance(2.0f);
        assertEquals(7.5f, commuter.getTravelDistance(), 0.001);
    }

    @Test
    void testPathTracking() {
        Commuter commuter = new Commuter("End", 0.0f);

        commuter.advancePath("NodeA");
        commuter.advancePath("NodeB");

        List<String> path = commuter.getPath();

        assertEquals(2, path.size());
        assertEquals("NodeA", path.get(0));
        assertEquals("NodeB", path.get(1));

    }


}
