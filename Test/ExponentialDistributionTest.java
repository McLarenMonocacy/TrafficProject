import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExponentialDistributionTest {

    @RepeatedTest(1000)
    void sample() {
        int sampleSize = 9999; //how many samples to take
        double marginOfError = 0.05; //how close to one is acceptable

        // This tests if the distribution averages out to zero
        ExponentialDistribution ed = new ExponentialDistribution(1);

        double expectedValue = 1/ed.getLambda();
        double preaverage = 0;
        for (int i = 0; i < sampleSize; i++) {
            preaverage += ed.sample();
        }
        double average = (preaverage/sampleSize);

        assertTrue((average > (expectedValue-marginOfError))&&(average < (expectedValue+marginOfError)),"Either unlucky or the exponential distribution doesn't average to expectedValue anymore");
    }
}