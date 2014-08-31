/*
 * Created on Mar 20, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * @author Peter Jacobs To change the template for this generated type comment go to Window - Preferences - Java - Code
 *         Generation - Code and Comments
 */
public final class DistributionsBenchmark
{
    /**
     * constructs a new StreamBenchmark.
     */
    private DistributionsBenchmark()
    {
        super();
        // unreachable code
    }

    /**
     * benchmarks a stream by drawing 1000000 double values
     * @param continuousDistribution the continuousDistribution to test
     * @return the execution time in milliseconds
     */
    public static long benchmark(final DistContinuous continuousDistribution)
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            continuousDistribution.draw();
        }
        return System.currentTimeMillis() - startTime;
    }

    /**
     * benchmarks a stream by drawing 1000000 double values
     * @param discreteDistribution the discreteDistribution to test
     * @return the execution time in milliseconds
     */
    public static long benchmark(final DistDiscrete discreteDistribution)
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            discreteDistribution.draw();
        }
        return System.currentTimeMillis() - startTime;
    }

    /**
     * executes the benchmark
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        StreamInterface stream = new Java2Random();
        System.out.println("DistBernoulli : " + DistributionsBenchmark.benchmark(new DistBernoulli(stream, 1.0)));
        System.out.println("DistBeta : " + DistributionsBenchmark.benchmark(new DistBeta(stream, 1.0, 2.0)));
        System.out.println("DistBinomial : " + DistributionsBenchmark.benchmark(new DistBinomial(stream, 3L, 0.23)));
        System.out.println("DistConstant : " + DistributionsBenchmark.benchmark(new DistConstant(stream, 0.23)));
        System.out.println("DistDiscreteConstant : "
                + DistributionsBenchmark.benchmark(new DistDiscreteConstant(stream, 14)));
        System.out.println("DistDiscreteUniform : "
                + DistributionsBenchmark.benchmark(new DistDiscreteUniform(stream, 0, 1)));
        System.out.println("DistErlang : " + DistributionsBenchmark.benchmark(new DistErlang(stream, 1, 0.1)));
        System.out.println("DistExponential : " + DistributionsBenchmark.benchmark(new DistExponential(stream, 0.1)));
        System.out.println("DistGamma : " + DistributionsBenchmark.benchmark(new DistGamma(stream, 0.1, 0.5)));
        System.out.println("DistGeometric : " + DistributionsBenchmark.benchmark(new DistGeometric(stream, 0.1)));
        System.out.println("DistLogNormal : " + DistributionsBenchmark.benchmark(new DistLogNormal(stream, 10, 1.0)));
        System.out
                .println("DistNegBinomial : " + DistributionsBenchmark.benchmark(new DistNegBinomial(stream, 1, 0.1)));
        System.out.println("DistNormal : " + DistributionsBenchmark.benchmark(new DistNormal(stream, 1, 0.1)));
        System.out.println("DistPearson5 : " + DistributionsBenchmark.benchmark(new DistPearson5(stream, 1, 0.1)));
        System.out.println("DistPearson6 : " + DistributionsBenchmark.benchmark(new DistPearson6(stream, 1, 0.1, 0.5)));
        System.out.println("DistPoisson : " + DistributionsBenchmark.benchmark(new DistPoisson(stream, 23.21)));
        System.out.println("DistTriangular : " + DistributionsBenchmark.benchmark(new DistTriangular(stream, 1, 4, 9)));
        System.out.println("DistUniform: " + DistributionsBenchmark.benchmark(new DistUniform(stream, 0, 1)));
        System.out.println("DistWeibull: " + DistributionsBenchmark.benchmark(new DistWeibull(stream, 0.4, 1.5)));

    }
}
