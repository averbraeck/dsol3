package nl.tudelft.simulation.jstats.distributions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.djutils.exceptions.Try;
import org.djutils.stats.summarizers.Tally;
import org.junit.Test;

import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * DistributionTest tests the correct statistics of the distributions, based on a tally of their values.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistributionTest
{
    /** the random stream to use. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    StreamInterface stream;

    /**
     * Test the distributions for the correct stats based on a tally of their values.
     */
    @Test
    public void testContinuousDistributions()
    {
        this.stream = new MersenneTwister(12L);
        double nan = Double.NaN;
        testDist("DistBernoulli", new DistBernoulli(this.stream, 0.25), 0.25, 0.25 * (1.0 - 0.25), 0.0, 1.0, nan, nan, 0.01);
        testDist("DistBeta", new DistBeta(this.stream, 1.0, 2.0), 1.0 / (1.0 + 2.0),
                (1.0 * 2.0) / ((1.0 + 2.0) * (1.0 + 2.0) * (1.0 + 2.0 + 1.0)), 0.0, 1.0, nan, nan, 0.01);
        testDist("DistBinomial", new DistBinomial(this.stream, 3, 0.25), 3 * 0.25, 3 * 0.25 * 0.75, 0.0, 3.0, nan, nan, 0.01);
        testDist("DistConstant", new DistConstant(this.stream, 12.1), 12.1, 0.0, 12.1, 12.1, nan, nan, 0.001);
        testDist("DistDiscreteConstant", new DistDiscreteConstant(this.stream, 14), 14, 0, 14, 14, nan, nan, 0.001);
        testDist("DistDiscreteUniform", new DistDiscreteUniform(this.stream, 1, 5), 3.0, (5.0 - 1.0) * (5.0 + 1.0) / 12.0, 1, 5,
                nan, nan, 0.05);
        testDist("DistErlang", new DistErlang(this.stream, 4, 1.2), 1.2, 1.2 * 1.2 / 4.0, 0.0, nan, nan, 5.0, 0.05);
        // testDist("DistErlang", new DistErlang(this.stream, 40, 1.2), 1.2, 1.2 * 1.2 / 40.0, 0.0, nan, nan, 5.0, 0.05);
        testDist("DistExponential", new DistExponential(this.stream, 1.2), 1.2, 1.2 * 1.2, 0.0, nan, nan, 10.0, 0.01);
        testDist("DistGamma", new DistGamma(this.stream, 2.0, 4.0), 2.0 * 4.0, 2.0 * 4.0 * 4.0, 0.0, nan, nan, 10.0, 0.05);
        testDist("DistGamma", new DistGamma(this.stream, 0.999, 2.0), 0.999 * 2.0, 0.999 * 2.0 * 2.0, 0.0, nan, nan, 10.0,
                0.05);
        testDist("DistGamma", new DistGamma(this.stream, 1.0, 4.0), 1.0 * 4.0, 1.0 * 4.0 * 4.0, 0.0, nan, nan, 10.0, 0.05);
        // testDist("DistGeometric", new DistGeometric(this.stream, 0.1), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistLogNormal", new DistLogNormal(this.stream, 10, 1.0), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistNegBinomial", new DistNegBinomial(this.stream, 1, 0.1), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistNormal", new DistNormal(this.stream, 1, 0.1), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistPearson5", new DistPearson5(this.stream, 1, 0.1), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistPearson6", new DistPearson6(this.stream, 1, 0.1, 0.5), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistPoisson", new DistPoisson(this.stream, 23.21), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistTriangular", new DistTriangular(this.stream, 1, 4, 9), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistUniform", new DistUniform(this.stream, 0, 1), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
        // testDist("DistWeibull", new DistWeibull(this.stream, 0.4, 1.5), 0.25, 0.25, 0.0, 1.0, nan, nan, 0.01);
    }

    /**
     * @param name String; the name of the distribution to test
     * @param dist Dist; the distribution to test
     * @param expectedMean double; the expected mean of a large number of samples
     * @param expectedVariance double; the expected variance of a large number of samples
     * @param expectedMin double; the expected lowest value of a large number of samples, or NaN if unbounded
     * @param expectedMax double; the expected highest value of a large number of samples, or NaN if unbounded
     * @param expectedMinThreshold double; the value the minimum should be under in a large number of samples, or NaN if bounded
     * @param expectedMaxThreshold double; the value the maximum shoud be over in a large number of samples, or NaN if bounded
     * @param precision double; the precision for mean, standard deviation, min and max
     */
    @SuppressWarnings("checkstyle:parameternumber")
    private void testDist(final String name, final Dist dist, final double expectedMean, final double expectedVariance,
            final double expectedMin, final double expectedMax, final double expectedMinThreshold,
            final double expectedMaxThreshold, final double precision)
    {
        Tally tally = new Tally("distTally " + dist.toString());
        tally.initialize();
        if (dist instanceof DistContinuous)
        {
            for (int i = 0; i < 100000; i++)
            {
                tally.ingest(((DistContinuous) dist).draw());
            }
        }
        else
        {
            for (int i = 0; i < 100000; i++)
            {
                tally.ingest(1.0 * ((DistDiscrete) dist).draw());
            }
        }
        assertEquals(name + " mean", expectedMean, tally.getPopulationMean(), precision);
        assertEquals(name + " stdev", Math.sqrt(expectedVariance), tally.getPopulationStDev(), precision);
        if (!Double.isNaN(expectedMinThreshold))
        {
            assertTrue(name + " min value = " + tally.getMin() + "; > threshold " + expectedMinThreshold,
                    tally.getMin() < expectedMinThreshold); // below threshold
        }
        if (!Double.isNaN(expectedMaxThreshold))
        {
            assertTrue(name + " max value = " + tally.getMax() + "; < threshold " + expectedMaxThreshold,
                    tally.getMax() > expectedMaxThreshold); // above threshold
        }
        if (!Double.isNaN(expectedMin))
        {
            assertEquals(name + " min", expectedMin, tally.getMin(), precision);
        }
        if (!Double.isNaN(expectedMax))
        {
            assertEquals(name + " max", expectedMax, tally.getMax(), precision);
        }
    }

    /**
     * Test the Bernoulli distribution.
     */
    @Test
    public void testBernoulli()
    {
        this.stream = new MersenneTwister(10L);
        DistBernoulli dist = new DistBernoulli(this.stream, 0.25);
        assertEquals(this.stream, dist.getStream());
        assertEquals(0.25, dist.getP(), 0.0001);
        assertTrue(dist.toString().contains("Bernoulli"));
        assertTrue(dist.toString().contains("0.25"));
        long value = dist.draw();
        assertTrue(value == 0 || value == 1);
        assertEquals(0.75, dist.probability(0), 0.0001);
        assertEquals(0.25, dist.probability(1), 0.0001);
        assertEquals(0.0, dist.probability(2), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBernoulli(null, 0.1);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBernoulli(DistributionTest.this.stream, -0.1);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBernoulli(DistributionTest.this.stream, 2.0);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Beta distribution.
     */
    @Test
    public void testBeta()
    {
        this.stream = new MersenneTwister(10L);
        DistBeta dist = new DistBeta(this.stream, 1.5, 2.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(1.5, dist.getAlpha1(), 0.0001);
        assertEquals(2.5, dist.getAlpha2(), 0.0001);
        assertTrue(dist.toString().contains("Beta"));
        assertTrue(dist.toString().contains("1.5"));
        assertTrue(dist.toString().contains("2.5"));
        double value = dist.draw();
        assertTrue(value >= 0 && value <= 1);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(0.0, dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(distBeta(1.5, 2.5, 0.5), dist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(distBeta(1.5, 2.5, 0.25), dist.getProbabilityDensity(0.25), 0.0001);
        assertEquals(0.0, dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(0.0, dist.getProbabilityDensity(-0.1), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBeta(null, 0.1, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBeta(DistributionTest.this.stream, -0.1, 1.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBeta(DistributionTest.this.stream, 2.0, -1.0);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Binomial distribution.
     */
    @Test
    public void testBinomial()
    {
        this.stream = new MersenneTwister(10L);
        DistBinomial dist = new DistBinomial(this.stream, 4, 0.25);
        assertEquals(this.stream, dist.getStream());
        assertEquals(0.25, dist.getP(), 0.0001);
        assertEquals(4, dist.getN());
        assertTrue(dist.toString().contains("Binomial"));
        assertTrue(dist.toString().contains("0.25"));
        assertTrue(dist.toString().contains("4"));
        long value = dist.draw();
        assertTrue(value >= 0 || value <= 4);
        assertEquals(combinations(4, 0) * Math.pow(0.25, 0) * Math.pow(0.75, 4), dist.probability(0), 0.0001);
        assertEquals(combinations(4, 1) * Math.pow(0.25, 1) * Math.pow(0.75, 3), dist.probability(1), 0.0001);
        assertEquals(combinations(4, 2) * Math.pow(0.25, 2) * Math.pow(0.75, 2), dist.probability(2), 0.0001);
        assertEquals(combinations(4, 3) * Math.pow(0.25, 3) * Math.pow(0.75, 1), dist.probability(3), 0.0001);
        assertEquals(combinations(4, 4) * Math.pow(0.25, 4) * Math.pow(0.75, 0), dist.probability(4), 0.0001);
        assertEquals(0.0, dist.probability(5), 0.0001);
        assertEquals(0.0, dist.probability(-1), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBinomial(null, 4, 0.1);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBinomial(DistributionTest.this.stream, -4, 0.1);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBinomial(DistributionTest.this.stream, 2, -0.1);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistBinomial(DistributionTest.this.stream, 2, 1.1);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Constant distribution.
     */
    @Test
    public void testConstant()
    {
        this.stream = new MersenneTwister(10L);
        DistConstant dist = new DistConstant(this.stream, 7.1);
        assertEquals(this.stream, dist.getStream());
        assertEquals(7.1, dist.getConstant(), 0.0001);
        assertTrue(dist.toString().contains("Constant"));
        assertTrue(dist.toString().contains("7.1"));
        double value = dist.draw();
        assertTrue(value == 7.1);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(0.0, dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(1.0, dist.getProbabilityDensity(7.1), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistConstant(null, 2.0);
            }
        }, NullPointerException.class);
    }

    /**
     * Test the DiscreteConstant distribution.
     */
    @Test
    public void testDiscreteConstant()
    {
        this.stream = new MersenneTwister(10L);
        DistDiscreteConstant dist = new DistDiscreteConstant(this.stream, 7);
        assertEquals(this.stream, dist.getStream());
        assertEquals(7L, dist.getConstant(), 0.0001);
        assertTrue(dist.toString().contains("DiscreteConstant"));
        assertTrue(dist.toString().contains("7"));
        long value = dist.draw();
        assertTrue(value == 7);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(0.0, dist.probability(1), 0.0001);
        assertEquals(1.0, dist.probability(7), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistDiscreteConstant(null, 2);
            }
        }, NullPointerException.class);
    }

    /**
     * Test the DiscreteUniform distribution.
     */
    @Test
    public void testDiscreteUniform()
    {
        this.stream = new MersenneTwister(10L);
        DistDiscreteUniform dist = new DistDiscreteUniform(this.stream, 1, 6);
        assertEquals(this.stream, dist.getStream());
        assertEquals(1, dist.getMin(), 0.0001);
        assertEquals(6, dist.getMax(), 0.0001);
        assertTrue(dist.toString().contains("DiscreteUniform"));
        assertTrue(dist.toString().contains("6"));
        long value = dist.draw();
        assertTrue(value >= 1 && value <= 6);
        assertEquals(0.0, dist.probability(0), 0.0001);
        assertEquals(1. / 6., dist.probability(1), 0.0001);
        assertEquals(1. / 6., dist.probability(2), 0.0001);
        assertEquals(1. / 6., dist.probability(3), 0.0001);
        assertEquals(1. / 6., dist.probability(4), 0.0001);
        assertEquals(1. / 6., dist.probability(5), 0.0001);
        assertEquals(1. / 6., dist.probability(6), 0.0001);
        assertEquals(0.0, dist.probability(7), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistDiscreteUniform(null, 2, 4);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistDiscreteUniform(DistributionTest.this.stream, 4, 2);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Erlang distribution.
     */
    @Test
    public void testErlang()
    {
        this.stream = new MersenneTwister(10L);
        DistErlang dist = new DistErlang(this.stream, 3, 2.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(3, dist.getK(), 0.0001);
        assertEquals(2.5, dist.getLambda(), 0.0001);
        assertTrue(dist.toString().contains("Erlang"));
        assertTrue(dist.toString().contains("3"));
        assertTrue(dist.toString().contains("2.5"));
        double value = dist.draw();
        assertTrue(value > 0);
        assertEquals(0.0, dist.getProbabilityDensity(-0.1), 0.0001);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(distErlang(3, 2.5, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(distErlang(3, 2.5, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(distErlang(3, 2.5, 3.0), dist.getProbabilityDensity(3.0), 0.0001);
        assertEquals(distErlang(3, 2.5, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(distErlang(3, 2.5, 5.0), dist.getProbabilityDensity(5.0), 0.0001);

        dist = new DistErlang(this.stream, 30, 2.5);
        value = dist.draw();
        assertTrue(value > 0);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(null, 1, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, 0, 1.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, 5, -1.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, 5, 0.0);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Exponential distribution.
     */
    @Test
    public void testExponential()
    {
        this.stream = new MersenneTwister(10L);
        DistExponential dist = new DistExponential(this.stream, 2.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(2.5, dist.getMean(), 0.0001);
        assertTrue(dist.toString().contains("Exponential"));
        assertTrue(dist.toString().contains("2.5"));
        double value = dist.draw();
        assertTrue(value >= 0);
        assertEquals(0.0, dist.getProbabilityDensity(-1.0), 0.0001);
        double l = 1 / 2.5;
        assertEquals(l * Math.exp(-l * 0.0), dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(l * Math.exp(-l * 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(l * Math.exp(-l * 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(l * Math.exp(-l * 3.0), dist.getProbabilityDensity(3.0), 0.0001);
        assertEquals(l * Math.exp(-l * 4.0), dist.getProbabilityDensity(4.0), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistExponential(null, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistExponential(DistributionTest.this.stream, -0.1);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistExponential(DistributionTest.this.stream, 0.0);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the Gamma distribution.
     */
    @Test
    public void testGamma()
    {
        this.stream = new MersenneTwister(10L);
        DistGamma dist = new DistGamma(this.stream, 1.5, 2.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(1.5, dist.getShape(), 0.0001);
        assertEquals(2.5, dist.getScale(), 0.0001);
        assertTrue(dist.toString().contains("Gamma"));
        assertTrue(dist.toString().contains("1.5"));
        assertTrue(dist.toString().contains("2.5"));
        double value = dist.draw();
        assertTrue(value > 0);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(distGamma(1.5, 2.5, 0.5), dist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(distGamma(1.5, 2.5, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(distGamma(1.5, 2.5, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(distGamma(1.5, 2.5, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(distGamma(1.5, 2.5, 8.0), dist.getProbabilityDensity(8.0), 0.0001);

        dist = new DistGamma(this.stream, 0.5, 2.5);
        value = dist.draw();
        assertTrue(value > 0);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(distGamma(0.5, 2.5, 0.5), dist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(distGamma(0.5, 2.5, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(distGamma(0.5, 2.5, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(distGamma(0.5, 2.5, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(distGamma(0.5, 2.5, 8.0), dist.getProbabilityDensity(8.0), 0.0001);

        dist = new DistGamma(this.stream, 100.0, 0.1);
        value = dist.draw();
        assertTrue(value > 0);

        dist = new DistGamma(this.stream, 0.9999, 0.1);
        value = dist.draw();
        assertTrue(value > 0);

        dist = new DistGamma(this.stream, 0.1, 100.0);
        value = dist.draw();
        assertTrue(value > 0);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGamma(null, 1.0, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGamma(DistributionTest.this.stream, -0.1, 1.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGamma(DistributionTest.this.stream, 2.0, -1.0);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Calculate ln(Gamma(x)). Based on https://introcs.cs.princeton.edu/java/91float/Gamma.java.html.
     * @param x double; the value for which to calculate the logarithm of the Gamma function
     * @return double; ln(Gamma(x))
     */
    public static double logGamma(final double x)
    {
        // @formatter:off
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                         + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                         +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
        // @formatter:on
    }

    /**
     * Calculate Gamma(x). Based on https://introcs.cs.princeton.edu/java/91float/Gamma.java.html.
     * @param x double; the value to calculate the Gamma function of
     * @return double; Gamma(x)
     */
    public static double gamma(final double x)
    {
        return Math.exp(logGamma(x));
    }

    /**
     * Calculate Beta(p, q). From: https://mathworld.wolfram.com/BetaFunction.html.
     * @param a double; param 1
     * @param b double; param 2
     * @return Beta(p, q)
     */
    public static double beta(final double a, final double b)
    {
        return gamma(a) * gamma(b) / gamma(a + b);
    }

    /**
     * Calculate probability density of DistBeta(a, b) for value x. From: https://mathworld.wolfram.com/BetaDistribution.html.
     * @param a double; parameter 1
     * @param b double; parameter 2
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double distBeta(final double a, final double b, final double x)
    {
        return Math.pow(1 - x, b - 1) * Math.pow(x, a - 1) / beta(a, b);
    }

    /**
     * Calculate the number of k-permutations in an n-set, or "n over k" = n! / (k! * (n-k)!)
     * @param n int; number in the set
     * @param k int; number of combinations to look for
     * @return n over k
     */
    public static double permutations(final int n, final int k)
    {
        return 1.0 * fac(n) / (1.0 * fac(n - k));
    }

    /**
     * Calculate the number of k-combinations in an n-set, or "n over k" = n! / (k! * (n-k)!)
     * @param n int; number in the set
     * @param k int; number of combinations to look for
     * @return n over k
     */
    public static double combinations(final int n, final int k)
    {
        return 1.0 * fac(n) / (1.0 * fac(k) * fac(n - k));
    }

    /**
     * Calculate fac(n) = n * (n-1) * (n-2) * ... 1, where fac(0) = 1.
     * @param n int; param
     * @return n!
     */
    public static long fac(final int n)
    {
        return n == 0 ? 1L : n * fac(n - 1);
    }

    /**
     * Calculate probability density of DistErlank(k, b) for value x. From:
     * https://mathworld.wolfram.com/ErlangDistribution.html.
     * @param k double; parameter 1
     * @param lambda double; parameter 2
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double distErlang(final int k, final double lambda, final double x)
    {
        return lambda * Math.pow(lambda * x, k - 1) * Math.exp(-lambda * x) / fac(k - 1);
    }

    /**
     * Calculate probability density of DistGamma(alpha, theta) for value x. From:
     * https://mathworld.wolfram.com/GammaDistribution.html.
     * @param alpha double; shape parameter
     * @param theta double; scale parameter
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double distGamma(final double alpha, final double theta, final double x)
    {
        return Math.pow(x, alpha - 1) * Math.exp(-x / theta) / (gamma(alpha) * Math.pow(theta, alpha));
    }

}
