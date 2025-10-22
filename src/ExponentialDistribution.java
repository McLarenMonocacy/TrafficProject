import java.util.Random;

// Generates samples from an exponential distribution.
public class ExponentialDistribution extends RandomDistribution {
    private double lambda; // The 'rate' parameter
    private Random random; //

    public ExponentialDistribution(double lambda) {
        this.lambda = lambda;
        this.random = new Random();
    }

    @Override
    public double sample() {
        // Uses the function method from page 54
        return -(1.0 / lambda) * Math.log(random.nextDouble());
    }
}
