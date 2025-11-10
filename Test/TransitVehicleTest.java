import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TransitVehicleTest {

    private TransitVehicle vehicle;
    private Commuter commuter1;
    private Commuter commuter2;

    @BeforeEach
    void setUp() {
        vehicle = new TransitVehicle(2);
        commuter1 = new Commuter("anything", 0f);
        commuter2 = new Commuter("anything", 0f);
    }

    @Test
    void testAddPassengerToEmptyVehicle() {
        assertTrue(vehicle.addPassenger(commuter1), "Should successfully add passenger to empty vehicle");
    }

    @Test
    void testAddPassengerUpToCapacity() {
        assertTrue(vehicle.addPassenger(commuter1), "Should add first passenger");
        assertTrue(vehicle.addPassenger(commuter2), "Should add second passenger up to capacity");
    }

    @Test
    void testAddPassengerExceedingCapacity() {
        vehicle.addPassenger(commuter1);
        vehicle.addPassenger(commuter2);
        Commuter commuter3 = new Commuter("anything", 0f);
        assertFalse(vehicle.addPassenger(commuter3), "Should not add passenger when at capacity");
    }

    @Test
    void testRemovePassengerFromEmptyVehicle() {
        assertNull(vehicle.removePassenger(), "Removing from empty vehicle should return null");
    }

    @Test
    void testRemovePassengerFIFOOrder() {
        vehicle.addPassenger(commuter1);
        vehicle.addPassenger(commuter2);

        assertEquals(commuter1, vehicle.removePassenger(), "Should remove first passenger added");
        assertEquals(commuter2, vehicle.removePassenger(), "Should remove second passenger added");
    }

    @Test
    void testAddAfterRemoval() {
        vehicle.addPassenger(commuter1);
        vehicle.addPassenger(commuter2);
        vehicle.removePassenger();

        Commuter commuter3 = new Commuter("anything", 0f);
        assertTrue(vehicle.addPassenger(commuter3), "Should be able to add after removing a passenger");
    }

    @Test
    void testZeroCapacityVehicle() {
        TransitVehicle zeroCapVehicle = new TransitVehicle(0);
        assertFalse(zeroCapVehicle.addPassenger(commuter1), "Zero capacity vehicle should not accept passengers");
    }

    @Test
    void testNegativeCapacityVehicle() {
        TransitVehicle negCapVehicle = new TransitVehicle(-1);
        assertFalse(negCapVehicle.addPassenger(commuter1), "Negative capacity vehicle should not accept passengers");
    }

    @Test
    void testMultipleRemovalsUntilEmpty() {
        vehicle.addPassenger(commuter1);
        vehicle.removePassenger();
        assertNull(vehicle.removePassenger(), "Should return null when removing from empty vehicle");
    }

    @Test
    void testAddNullPassenger() {
        assertTrue(vehicle.addPassenger(null), "Current implementation allows null passengers");
        assertNull(vehicle.removePassenger(), "Should retrieve null passenger");
    }
}