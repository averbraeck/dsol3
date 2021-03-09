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
        testDist("DistBernoulli", new DistBernoulli(this.stream, 0.25), 0.25, 0.25 * (1.0 - 0.25), 0.0, 1.0, 0.01);
        testDist("DistBeta", new DistBeta(this.stream, 1.0, 2.0), 1.0 / (1.0 + 2.0),
                (1.0 * 2.0) / ((1.0 + 2.0) * (1.0 + 2.0) * (1.0 + 2.0 + 1.0)), 0.0, 1.0, 0.01);
        testDist("DistBinomial", new DistBinomial(this.stream, 3, 0.25), 3 * 0.25, 3 * 0.25 * 0.75, 0.0, 3.0, 0.01);
        testDist("DistConstant", new DistConstant(this.stream, 12.1), 12.1, 0.0, 12.1, 12.1, 0.001);
        testDist("DistDiscreteConstant", new DistDiscreteConstant(this.stream, 14), 14, 0, 14, 14, 0.001);
        testDist("DistDiscreteUniform", new DistDiscreteUniform(this.stream, 1, 5), 3.0, (5.0 - 1.0) * (5.0 + 1.0) / 12.0, 1, 5,
                0.05);
        testDist("DistErlang", new DistErlang(this.stream, 2.0, 1), 2.0, 2.0 * 2.0, 0.0, nan, 0.05);
        testDist("DistErlang", new DistErlang(this.stream, 0.5, 4), 4.0 * 0.5, 4.0 * 0.5 * 0.5, 0.0, nan, 0.05);
        testDist("DistErlang", new DistErlang(this.stream, 0.5, 40), 40.0 * 0.5, 40.0 * 0.5 * 0.5, 0.0, nan, 0.05);
        testDist("DistExponential", new DistExponential(this.stream, 1.2), 1.2, 1.2 * 1.2, 0.0, nan, 0.01);
        testDist("DistGamma", new DistGamma(this.stream, 2.0, 4.0), 2.0 * 4.0, 2.0 * 4.0 * 4.0, 0.0, nan, 0.05);
        testDist("DistGamma", new DistGamma(this.stream, 0.999, 2.0), 0.999 * 2.0, 0.999 * 2.0 * 2.0, 0.0, nan, 0.05);
        testDist("DistGamma", new DistGamma(this.stream, 1.0, 4.0), 1.0 * 4.0, 1.0 * 4.0 * 4.0, 0.0, nan, 0.05);
        testDist("DistGeometric", new DistGeometric(this.stream, 0.1), (1 - 0.1) / 0.1, (1 - 0.1) / (0.1 * 0.1), 0.0, nan,
                0.05);
        testDist("DistGeometric", new DistGeometric(this.stream, 0.9), (1 - 0.9) / 0.9, (1 - 0.9) / (0.9 * 0.9), 0.0, nan,
                0.05);
        testDist("DistLogNormal", new DistLogNormal(this.stream, 0.0, 0.5), Math.exp(0.5 * 0.5 / 2.0),
                (Math.exp(0.5 * 0.5) - 1.0) * Math.exp(0.5 * 0.5), 0.0, nan, 0.01);
        testDist("DistLogNormal", new DistLogNormal(this.stream, 5.0, 0.5), Math.exp(5.0 + 0.5 * 0.5 / 2.0),
                (Math.exp(0.5 * 0.5) - 1.0) * Math.exp(2 * 5.0 + 0.5 * 0.5), 0.0, nan, 0.25);
        // testDist("DistNegBinomial", new DistNegBinomial(this.stream, 1, 0.1), 0.25, 0.25, 0.0, 1.0, 0.01);
        testDist("DistNormal", new DistNormal(this.stream, 0.0, 1.0), 0.0, 1.0, nan, nan, 0.01);
        testDist("DistNormal", new DistNormal(this.stream, 5.0, 2.0), 5.0, 4.0, nan, nan, 0.01);
        // testDist("DistPearson5", new DistPearson5(this.stream, 1, 0.1), 0.25, 0.25, 0.0, 1.0, 0.01);
        // testDist("DistPearson6", new DistPearson6(this.stream, 1, 0.1, 0.5), 0.25, 0.25, 0.0, 1.0, 0.01);
        // testDist("DistPoisson", new DistPoisson(this.stream, 23.21), 0.25, 0.25, 0.0, 1.0, 0.01);
        testDist("DistTriangular", new DistTriangular(this.stream, 1, 4, 9), (1 + 4 + 9) / 3.0,
                (1 * 1 + 4 * 4 + 9 * 9 - 1 * 4 - 1 * 9 - 4 * 9) / 18.0, 1.0, 9.0, 0.01);
        testDist("DistUniform", new DistUniform(this.stream, 0, 1), 0.5, 1.0 / 12.0, 0.0, 1.0, 0.01);
        // testDist("DistWeibull", new DistWeibull(this.stream, 0.4, 1.5), 0.25, 0.25, 0.0, 1.0, 0.01);
    }

    /**
     * @param name String; the name of the distribution to test
     * @param dist Dist; the distribution to test
     * @param expectedMean double; the expected mean of a large number of samples
     * @param expectedVariance double; the expected variance of a large number of samples; test the standard deviations
     * @param expectedMin double; the expected lowest value of a large number of samples, or NaN if unbounded
     * @param expectedMax double; the expected highest value of a large number of samples, or NaN if unbounded
     * @param precision double; the precision for mean, standard deviation, min and max
     */
    @SuppressWarnings("checkstyle:parameternumber")
    private void testDist(final String name, final Dist dist, final double expectedMean, final double expectedVariance,
            final double expectedMin, final double expectedMax, final double precision)
    {
        Tally tally = new Tally("distTally " + dist.toString());
        tally.initialize();
        if (dist instanceof DistContinuous)
        {
            for (int i = 0; i < 100000; i++)
            {
                double d = ((DistContinuous) dist).draw();
                if (!Double.isNaN(expectedMin))
                {
                    assertTrue(name + " min", d >= expectedMin);
                }
                if (!Double.isNaN(expectedMax))
                {
                    assertTrue(name + " max", d <= expectedMax);
                }
                tally.ingest(d);
            }
        }
        else
        {
            for (int i = 0; i < 100000; i++)
            {
                double d = 1.0 * ((DistDiscrete) dist).draw();
                if (!Double.isNaN(expectedMin))
                {
                    assertTrue(name + " min", d >= expectedMin);
                }
                if (!Double.isNaN(expectedMax))
                {
                    assertTrue(name + " max", d <= expectedMax);
                }
                tally.ingest(d);
            }
        }
        assertEquals(name + " mean", expectedMean, tally.getPopulationMean(), precision);
        assertEquals(name + " stdev", Math.sqrt(expectedVariance), tally.getPopulationStDev(), precision);
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
        DistErlang dist = new DistErlang(this.stream, 2.5, 3);
        assertEquals(this.stream, dist.getStream());
        assertEquals(3, dist.getK(), 0.0001);
        assertEquals(2.5, dist.getScale(), 0.0001);
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

        dist = new DistErlang(this.stream, 2.5, 30);
        value = dist.draw();
        assertTrue(value > 0);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(null, 2.0, 1);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, 1.0, 0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, -1.0, 5);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistErlang(DistributionTest.this.stream, 0.0, 5);
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
     * Test the Geometric distribution.
     */
    @Test
    public void testGeometric()
    {
        this.stream = new MersenneTwister(10L);
        for (double p = 0.1; p <= 1.0; p += 0.1)
        {
            DistGeometric dist = new DistGeometric(this.stream, p);
            assertEquals(this.stream, dist.getStream());
            assertEquals(p, dist.getP(), 0.0001);
            assertTrue(dist.toString().contains("Geometric"));
            assertTrue(dist.toString().contains("" + p));
            long value = dist.draw();
            assertTrue(value >= 0);
            assertEquals(0.0, dist.probability(-1), 0.01);
            for (int k = 0; k < 10; k++)
            {
                assertEquals(Math.pow(1 - p, k) * p, dist.probability(k), 0.01);
            }
        }

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGeometric(DistributionTest.this.stream, 0.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGeometric(DistributionTest.this.stream, -1.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistGeometric(DistributionTest.this.stream, 1.01);
            }
        }, IllegalArgumentException.class);
    }

    /**
     * Test the LogNormal distribution.
     */
    @Test
    public void testLogNormal()
    {
        this.stream = new MersenneTwister(10L);
        DistLogNormal dist = new DistLogNormal(this.stream, 1.5, 0.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(1.5, dist.getMu(), 0.0001);
        assertEquals(0.5, dist.getSigma(), 0.0001);
        assertTrue(dist.toString().contains("LogNormal"));
        assertTrue(dist.toString().contains("1.5"));
        assertTrue(dist.toString().contains("0.5"));
        double value = dist.draw();
        assertTrue(value > 0);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(lnpdf(1.5, 0.5, 0.5), dist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(lnpdf(1.5, 0.5, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(lnpdf(1.5, 0.5, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(lnpdf(1.5, 0.5, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(lnpdf(1.5, 0.5, 8.0), dist.getProbabilityDensity(8.0), 0.0001);

        dist = new DistLogNormal(this.stream, 0.0, 2.5);
        value = dist.draw();
        assertTrue(value > 0);
        assertEquals(0.0, dist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(lnpdf(0.0, 2.5, 0.5), dist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(lnpdf(0.0, 2.5, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(lnpdf(0.0, 2.5, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(lnpdf(0.0, 2.5, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(lnpdf(0.0, 2.5, 8.0), dist.getProbabilityDensity(8.0), 0.0001);

        assertEquals(1, even(0));
        assertEquals(-1, even(1));
        assertEquals(1, even(2));
        assertEquals(0.0, erf(0.0), 0.01);
        assertEquals(0.52049987781, erf(0.5), 0.001);
        assertEquals(-0.52049987781, erf(-0.5), 0.001);
        assertEquals(0.84270079295, erf(1.0), 0.001);

        assertEquals(0.0, dist.getCumulativeProbability(-1.0), 0.0001);
        assertEquals(0.0, dist.getCumulativeProbability(0.0), 0.0001);
        assertEquals(lncdf(0.0, 2.5, 0.5), dist.getCumulativeProbability(0.5), 0.1);
        assertEquals(lncdf(0.0, 2.5, 1.0), dist.getCumulativeProbability(1.0), 0.1);
        assertEquals(lncdf(0.0, 2.5, 2.0), dist.getCumulativeProbability(2.0), 0.1);
        assertEquals(lncdf(0.0, 2.5, 4.0), dist.getCumulativeProbability(4.0), 0.1);
        assertEquals(lncdf(0.0, 2.5, 8.0), dist.getCumulativeProbability(8.0), 0.1);

        assertEquals(0.5, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(0.5)), 0.05);
        assertEquals(1.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(1.0)), 0.05);
        assertEquals(2.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(2.0)), 0.05);
        assertEquals(4.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(4.0)), 0.05);
        assertEquals(8.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(8.0)), 0.05);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistLogNormal(null, 1.0, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistLogNormal(DistributionTest.this.stream, 2.0, 0.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistLogNormal(DistributionTest.this.stream, 2.0, -1.0);
            }
        }, IllegalArgumentException.class);
    }

    /** z-values. */
    private static final double[] Z_VALUES = new double[] {-4.26, 0.00001, -4.25, 0.00001, -4.24, 0.00001, -4.23, 0.00001,
            -4.22, 0.00001, -4.21, 0.00001, -4.2, 0.00001, -4.19, 0.00001, -4.18, 0.00001, -4.17, 0.00002, -4.16, 0.00002,
            -4.15, 0.00002, -4.14, 0.00002, -4.13, 0.00002, -4.12, 0.00002, -4.11, 0.00002, -4.1, 0.00002, -4.09, 0.00002,
            -4.08, 0.00002, -4.07, 0.00002, -4.06, 0.00002, -4.05, 0.00003, -4.04, 0.00003, -4.03, 0.00003, -4.02, 0.00003,
            -4.01, 0.00003, -4, 0.00003, -3.99, 0.00003, -3.98, 0.00003, -3.97, 0.00004, -3.96, 0.00004, -3.95, 0.00004, -3.94,
            0.00004, -3.93, 0.00004, -3.92, 0.00004, -3.91, 0.00005, -3.9, 0.00005, -3.89, 0.00005, -3.88, 0.00005, -3.87,
            0.00005, -3.86, 0.00006, -3.85, 0.00006, -3.84, 0.00006, -3.83, 0.00006, -3.82, 0.00007, -3.81, 0.00007, -3.8,
            0.00007, -3.79, 0.00008, -3.78, 0.00008, -3.77, 0.00008, -3.76, 0.00008, -3.75, 0.00009, -3.74, 0.00009, -3.73,
            0.0001, -3.72, 0.0001, -3.71, 0.0001, -3.7, 0.00011, -3.69, 0.00011, -3.68, 0.00012, -3.67, 0.00012, -3.66, 0.00013,
            -3.65, 0.00013, -3.64, 0.00014, -3.63, 0.00014, -3.62, 0.00015, -3.61, 0.00015, -3.6, 0.00016, -3.59, 0.00017,
            -3.58, 0.00017, -3.57, 0.00018, -3.56, 0.00019, -3.55, 0.00019, -3.54, 0.0002, -3.53, 0.00021, -3.52, 0.00022,
            -3.51, 0.00022, -3.5, 0.00023, -3.49, 0.00024, -3.48, 0.00025, -3.47, 0.00026, -3.46, 0.00027, -3.45, 0.00028,
            -3.44, 0.00029, -3.43, 0.0003, -3.42, 0.00031, -3.41, 0.00032, -3.4, 0.00034, -3.39, 0.00035, -3.38, 0.00036, -3.37,
            0.00038, -3.36, 0.00039, -3.35, 0.0004, -3.34, 0.00042, -3.33, 0.00043, -3.32, 0.00045, -3.31, 0.00047, -3.3,
            0.00048, -3.29, 0.0005, -3.28, 0.00052, -3.27, 0.00054, -3.26, 0.00056, -3.25, 0.00058, -3.24, 0.0006, -3.23,
            0.00062, -3.22, 0.00064, -3.21, 0.00066, -3.2, 0.00069, -3.19, 0.00071, -3.18, 0.00074, -3.17, 0.00076, -3.16,
            0.00079, -3.15, 0.00082, -3.14, 0.00084, -3.13, 0.00087, -3.12, 0.0009, -3.11, 0.00094, -3.1, 0.00097, -3.09, 0.001,
            -3.08, 0.00104, -3.07, 0.00107, -3.06, 0.00111, -3.05, 0.00114, -3.04, 0.00118, -3.03, 0.00122, -3.02, 0.00126,
            -3.01, 0.00131, -3, 0.00135, -2.99, 0.00139, -2.98, 0.00144, -2.97, 0.00149, -2.96, 0.00154, -2.95, 0.00159, -2.94,
            0.00164, -2.93, 0.00169, -2.92, 0.00175, -2.91, 0.00181, -2.9, 0.00187, -2.89, 0.00193, -2.88, 0.00199, -2.87,
            0.00205, -2.86, 0.00212, -2.85, 0.00219, -2.84, 0.00226, -2.83, 0.00233, -2.82, 0.0024, -2.81, 0.00248, -2.8,
            0.00256, -2.79, 0.00264, -2.78, 0.00272, -2.77, 0.0028, -2.76, 0.00289, -2.75, 0.00298, -2.74, 0.00307, -2.73,
            0.00317, -2.72, 0.00326, -2.71, 0.00336, -2.7, 0.00347, -2.69, 0.00357, -2.68, 0.00368, -2.67, 0.00379, -2.66,
            0.00391, -2.65, 0.00402, -2.64, 0.00415, -2.63, 0.00427, -2.62, 0.0044, -2.61, 0.00453, -2.6, 0.00466, -2.59,
            0.0048, -2.58, 0.00494, -2.57, 0.00508, -2.56, 0.00523, -2.55, 0.00539, -2.54, 0.00554, -2.53, 0.0057, -2.52,
            0.00587, -2.51, 0.00604, -2.5, 0.00621, -2.49, 0.00639, -2.48, 0.00657, -2.47, 0.00676, -2.46, 0.00695, -2.45,
            0.00714, -2.44, 0.00734, -2.43, 0.00755, -2.42, 0.00776, -2.41, 0.00798, -2.4, 0.0082, -2.39, 0.00842, -2.38,
            0.00866, -2.37, 0.00889, -2.36, 0.00914, -2.35, 0.00939, -2.34, 0.00964, -2.33, 0.0099, -2.32, 0.01017, -2.31,
            0.01044, -2.3, 0.01072, -2.29, 0.01101, -2.28, 0.0113, -2.27, 0.0116, -2.26, 0.01191, -2.25, 0.01222, -2.24,
            0.01255, -2.23, 0.01287, -2.22, 0.01321, -2.21, 0.01355, -2.2, 0.0139, -2.19, 0.01426, -2.18, 0.01463, -2.17, 0.015,
            -2.16, 0.01539, -2.15, 0.01578, -2.14, 0.01618, -2.13, 0.01659, -2.12, 0.017, -2.11, 0.01743, -2.1, 0.01786, -2.09,
            0.01831, -2.08, 0.01876, -2.07, 0.01923, -2.06, 0.0197, -2.05, 0.02018, -2.04, 0.02068, -2.03, 0.02118, -2.02,
            0.02169, -2.01, 0.02222, -2, 0.02275, -1.99, 0.0233, -1.98, 0.02385, -1.97, 0.02442, -1.96, 0.025, -1.95, 0.02559,
            -1.94, 0.02619, -1.93, 0.0268, -1.92, 0.02743, -1.91, 0.02807, -1.9, 0.02872, -1.89, 0.02938, -1.88, 0.03005, -1.87,
            0.03074, -1.86, 0.03144, -1.85, 0.03216, -1.84, 0.03288, -1.83, 0.03362, -1.82, 0.03438, -1.81, 0.03515, -1.8,
            0.03593, -1.79, 0.03673, -1.78, 0.03754, -1.77, 0.03836, -1.76, 0.0392, -1.75, 0.04006, -1.74, 0.04093, -1.73,
            0.04182, -1.72, 0.04272, -1.71, 0.04363, -1.7, 0.04457, -1.69, 0.04551, -1.68, 0.04648, -1.67, 0.04746, -1.66,
            0.04846, -1.65, 0.04947, -1.64, 0.0505, -1.63, 0.05155, -1.62, 0.05262, -1.61, 0.0537, -1.6, 0.0548, -1.59, 0.05592,
            -1.58, 0.05705, -1.57, 0.05821, -1.56, 0.05938, -1.55, 0.06057, -1.54, 0.06178, -1.53, 0.06301, -1.52, 0.06426,
            -1.51, 0.06552, -1.5, 0.06681, -1.49, 0.06811, -1.48, 0.06944, -1.47, 0.07078, -1.46, 0.07215, -1.45, 0.07353,
            -1.44, 0.07493, -1.43, 0.07636, -1.42, 0.0778, -1.41, 0.07927, -1.4, 0.08076, -1.39, 0.08226, -1.38, 0.08379, -1.37,
            0.08534, -1.36, 0.08691, -1.35, 0.08851, -1.34, 0.09012, -1.33, 0.09176, -1.32, 0.09342, -1.31, 0.0951, -1.3,
            0.0968, -1.29, 0.09853, -1.28, 0.10027, -1.27, 0.10204, -1.26, 0.10383, -1.25, 0.10565, -1.24, 0.10749, -1.23,
            0.10935, -1.22, 0.11123, -1.21, 0.11314, -1.2, 0.11507, -1.19, 0.11702, -1.18, 0.119, -1.17, 0.121, -1.16, 0.12302,
            -1.15, 0.12507, -1.14, 0.12714, -1.13, 0.12924, -1.12, 0.13136, -1.11, 0.1335, -1.1, 0.13567, -1.09, 0.13786, -1.08,
            0.14007, -1.07, 0.14231, -1.06, 0.14457, -1.05, 0.14686, -1.04, 0.14917, -1.03, 0.15151, -1.02, 0.15386, -1.01,
            0.15625, -1, 0.15866, -0.99, 0.16109, -0.98, 0.16354, -0.97, 0.16602, -0.96, 0.16853, -0.95, 0.17106, -0.94,
            0.17361, -0.93, 0.17619, -0.92, 0.17879, -0.91, 0.18141, -0.9, 0.18406, -0.89, 0.18673, -0.88, 0.18943, -0.87,
            0.19215, -0.86, 0.19489, -0.85, 0.19766, -0.84, 0.20045, -0.83, 0.20327, -0.82, 0.20611, -0.81, 0.20897, -0.8,
            0.21186, -0.79, 0.21476, -0.78, 0.2177, -0.77, 0.22065, -0.76, 0.22363, -0.75, 0.22663, -0.74, 0.22965, -0.73,
            0.2327, -0.72, 0.23576, -0.71, 0.23885, -0.7, 0.24196, -0.69, 0.2451, -0.68, 0.24825, -0.67, 0.25143, -0.66,
            0.25463, -0.65, 0.25785, -0.64, 0.26109, -0.63, 0.26435, -0.62, 0.26763, -0.61, 0.27093, -0.6, 0.27425, -0.59,
            0.2776, -0.58, 0.28096, -0.57, 0.28434, -0.56, 0.28774, -0.55, 0.29116, -0.54, 0.2946, -0.53, 0.29806, -0.52,
            0.30153, -0.51, 0.30503, -0.5, 0.30854, -0.49, 0.31207, -0.48, 0.31561, -0.47, 0.31918, -0.46, 0.32276, -0.45,
            0.32636, -0.44, 0.32997, -0.43, 0.3336, -0.42, 0.33724, -0.41, 0.3409, -0.4, 0.34458, -0.39, 0.34827, -0.38,
            0.35197, -0.37, 0.35569, -0.36, 0.35942, -0.35, 0.36317, -0.34, 0.36693, -0.33, 0.3707, -0.32, 0.37448, -0.31,
            0.37828, -0.3, 0.38209, -0.29, 0.38591, -0.28, 0.38974, -0.27, 0.39358, -0.26, 0.39743, -0.25, 0.40129, -0.24,
            0.40517, -0.23, 0.40905, -0.22, 0.41294, -0.21, 0.41683, -0.2, 0.42074, -0.19, 0.42465, -0.18, 0.42858, -0.17,
            0.43251, -0.16, 0.43644, -0.15, 0.44038, -0.14, 0.44433, -0.13, 0.44828, -0.12, 0.45224, -0.11, 0.4562, -0.1,
            0.46017, -0.09, 0.46414, -0.08, 0.46812, -0.07, 0.4721, -0.06, 0.47608, -0.05, 0.48006, -0.04, 0.48405, -0.03,
            0.48803, -0.02, 0.49202, -0.01, 0.49601, 0, 0.5, 0.01, 0.50399, 0.02, 0.50798, 0.03, 0.51197, 0.04, 0.51595, 0.05,
            0.51994, 0.06, 0.52392, 0.07, 0.5279, 0.08, 0.53188, 0.09, 0.53586, 0.1, 0.53983, 0.11, 0.5438, 0.12, 0.54776, 0.13,
            0.55172, 0.14, 0.55567, 0.15, 0.55962, 0.16, 0.56356, 0.17, 0.56749, 0.18, 0.57142, 0.19, 0.57535, 0.2, 0.57926,
            0.21, 0.58317, 0.22, 0.58706, 0.23, 0.59095, 0.24, 0.59483, 0.25, 0.59871, 0.26, 0.60257, 0.27, 0.60642, 0.28,
            0.61026, 0.29, 0.61409, 0.3, 0.61791, 0.31, 0.62172, 0.32, 0.62552, 0.33, 0.6293, 0.34, 0.63307, 0.35, 0.63683,
            0.36, 0.64058, 0.37, 0.64431, 0.38, 0.64803, 0.39, 0.65173, 0.4, 0.65542, 0.41, 0.6591, 0.42, 0.66276, 0.43, 0.6664,
            0.44, 0.67003, 0.45, 0.67364, 0.46, 0.67724, 0.47, 0.68082, 0.48, 0.68439, 0.49, 0.68793, 0.5, 0.69146, 0.51,
            0.69497, 0.52, 0.69847, 0.53, 0.70194, 0.54, 0.7054, 0.55, 0.70884, 0.56, 0.71226, 0.57, 0.71566, 0.58, 0.71904,
            0.59, 0.7224, 0.6, 0.72575, 0.61, 0.72907, 0.62, 0.73237, 0.63, 0.73565, 0.64, 0.73891, 0.65, 0.74215, 0.66,
            0.74537, 0.67, 0.74857, 0.68, 0.75175, 0.69, 0.7549, 0.7, 0.75804, 0.71, 0.76115, 0.72, 0.76424, 0.73, 0.7673, 0.74,
            0.77035, 0.75, 0.77337, 0.76, 0.77637, 0.77, 0.77935, 0.78, 0.7823, 0.79, 0.78524, 0.8, 0.78814, 0.81, 0.79103,
            0.82, 0.79389, 0.83, 0.79673, 0.84, 0.79955, 0.85, 0.80234, 0.86, 0.80511, 0.87, 0.80785, 0.88, 0.81057, 0.89,
            0.81327, 0.9, 0.81594, 0.91, 0.81859, 0.92, 0.82121, 0.93, 0.82381, 0.94, 0.82639, 0.95, 0.82894, 0.96, 0.83147,
            0.97, 0.83398, 0.98, 0.83646, 0.99, 0.83891, 1, 0.84134, 1.01, 0.84375, 1.02, 0.84614, 1.03, 0.84849, 1.04, 0.85083,
            1.05, 0.85314, 1.06, 0.85543, 1.07, 0.85769, 1.08, 0.85993, 1.09, 0.86214, 1.1, 0.86433, 1.11, 0.8665, 1.12,
            0.86864, 1.13, 0.87076, 1.14, 0.87286, 1.15, 0.87493, 1.16, 0.87698, 1.17, 0.879, 1.18, 0.881, 1.19, 0.88298, 1.2,
            0.88493, 1.21, 0.88686, 1.22, 0.88877, 1.23, 0.89065, 1.24, 0.89251, 1.25, 0.89435, 1.26, 0.89617, 1.27, 0.89796,
            1.28, 0.89973, 1.29, 0.90147, 1.3, 0.9032, 1.31, 0.9049, 1.32, 0.90658, 1.33, 0.90824, 1.34, 0.90988, 1.35, 0.91149,
            1.36, 0.91309, 1.37, 0.91466, 1.38, 0.91621, 1.39, 0.91774, 1.4, 0.91924, 1.41, 0.92073, 1.42, 0.9222, 1.43,
            0.92364, 1.44, 0.92507, 1.45, 0.92647, 1.46, 0.92785, 1.47, 0.92922, 1.48, 0.93056, 1.49, 0.93189, 1.5, 0.93319,
            1.51, 0.93448, 1.52, 0.93574, 1.53, 0.93699, 1.54, 0.93822, 1.55, 0.93943, 1.56, 0.94062, 1.57, 0.94179, 1.58,
            0.94295, 1.59, 0.94408, 1.6, 0.9452, 1.61, 0.9463, 1.62, 0.94738, 1.63, 0.94845, 1.64, 0.9495, 1.65, 0.95053, 1.66,
            0.95154, 1.67, 0.95254, 1.68, 0.95352, 1.69, 0.95449, 1.7, 0.95543, 1.71, 0.95637, 1.72, 0.95728, 1.73, 0.95818,
            1.74, 0.95907, 1.75, 0.95994, 1.76, 0.9608, 1.77, 0.96164, 1.78, 0.96246, 1.79, 0.96327, 1.8, 0.96407, 1.81,
            0.96485, 1.82, 0.96562, 1.83, 0.96638, 1.84, 0.96712, 1.85, 0.96784, 1.86, 0.96856, 1.87, 0.96926, 1.88, 0.96995,
            1.89, 0.97062, 1.9, 0.97128, 1.91, 0.97193, 1.92, 0.97257, 1.93, 0.9732, 1.94, 0.97381, 1.95, 0.97441, 1.96, 0.975,
            1.97, 0.97558, 1.98, 0.97615, 1.99, 0.9767, 2, 0.97725, 2.01, 0.97778, 2.02, 0.97831, 2.03, 0.97882, 2.04, 0.97932,
            2.05, 0.97982, 2.06, 0.9803, 2.07, 0.98077, 2.08, 0.98124, 2.09, 0.98169, 2.1, 0.98214, 2.11, 0.98257, 2.12, 0.983,
            2.13, 0.98341, 2.14, 0.98382, 2.15, 0.98422, 2.16, 0.98461, 2.17, 0.985, 2.18, 0.98537, 2.19, 0.98574, 2.2, 0.9861,
            2.21, 0.98645, 2.22, 0.98679, 2.23, 0.98713, 2.24, 0.98745, 2.25, 0.98778, 2.26, 0.98809, 2.27, 0.9884, 2.28,
            0.9887, 2.29, 0.98899, 2.3, 0.98928, 2.31, 0.98956, 2.32, 0.98983, 2.33, 0.9901, 2.34, 0.99036, 2.35, 0.99061, 2.36,
            0.99086, 2.37, 0.99111, 2.38, 0.99134, 2.39, 0.99158, 2.4, 0.9918, 2.41, 0.99202, 2.42, 0.99224, 2.43, 0.99245,
            2.44, 0.99266, 2.45, 0.99286, 2.46, 0.99305, 2.47, 0.99324, 2.48, 0.99343, 2.49, 0.99361, 2.5, 0.99379, 2.51,
            0.99396, 2.52, 0.99413, 2.53, 0.9943, 2.54, 0.99446, 2.55, 0.99461, 2.56, 0.99477, 2.57, 0.99492, 2.58, 0.99506,
            2.59, 0.9952, 2.6, 0.99534, 2.61, 0.99547, 2.62, 0.9956, 2.63, 0.99573, 2.64, 0.99585, 2.65, 0.99598, 2.66, 0.99609,
            2.67, 0.99621, 2.68, 0.99632, 2.69, 0.99643, 2.7, 0.99653, 2.71, 0.99664, 2.72, 0.99674, 2.73, 0.99683, 2.74,
            0.99693, 2.75, 0.99702, 2.76, 0.99711, 2.77, 0.9972, 2.78, 0.99728, 2.79, 0.99736, 2.8, 0.99744, 2.81, 0.99752,
            2.82, 0.9976, 2.83, 0.99767, 2.84, 0.99774, 2.85, 0.99781, 2.86, 0.99788, 2.87, 0.99795, 2.88, 0.99801, 2.89,
            0.99807, 2.9, 0.99813, 2.91, 0.99819, 2.92, 0.99825, 2.93, 0.99831, 2.94, 0.99836, 2.95, 0.99841, 2.96, 0.99846,
            2.97, 0.99851, 2.98, 0.99856, 2.99, 0.99861, 3, 0.99865, 3.01, 0.99869, 3.02, 0.99874, 3.03, 0.99878, 3.04, 0.99882,
            3.05, 0.99886, 3.06, 0.99889, 3.07, 0.99893, 3.08, 0.99896, 3.09, 0.999, 3.1, 0.99903, 3.11, 0.99906, 3.12, 0.9991,
            3.13, 0.99913, 3.14, 0.99916, 3.15, 0.99918, 3.16, 0.99921, 3.17, 0.99924, 3.18, 0.99926, 3.19, 0.99929, 3.2,
            0.99931, 3.21, 0.99934, 3.22, 0.99936, 3.23, 0.99938, 3.24, 0.9994, 3.25, 0.99942, 3.26, 0.99944, 3.27, 0.99946,
            3.28, 0.99948, 3.29, 0.9995, 3.3, 0.99952, 3.31, 0.99953, 3.32, 0.99955, 3.33, 0.99957, 3.34, 0.99958, 3.35, 0.9996,
            3.36, 0.99961, 3.37, 0.99962, 3.38, 0.99964, 3.39, 0.99965, 3.4, 0.99966, 3.41, 0.99968, 3.42, 0.99969, 3.43,
            0.9997, 3.44, 0.99971, 3.45, 0.99972, 3.46, 0.99973, 3.47, 0.99974, 3.48, 0.99975, 3.49, 0.99976, 3.5, 0.99977,
            3.51, 0.99978, 3.52, 0.99978, 3.53, 0.99979, 3.54, 0.9998, 3.55, 0.99981, 3.56, 0.99981, 3.57, 0.99982, 3.58,
            0.99983, 3.59, 0.99983, 3.6, 0.99984, 3.61, 0.99985, 3.62, 0.99985, 3.63, 0.99986, 3.64, 0.99986, 3.65, 0.99987,
            3.66, 0.99987, 3.67, 0.99988, 3.68, 0.99988, 3.69, 0.99989, 3.7, 0.99989, 3.71, 0.9999, 3.72, 0.9999, 3.73, 0.9999,
            3.74, 0.99991, 3.75, 0.99991, 3.76, 0.99992, 3.77, 0.99992, 3.78, 0.99992, 3.79, 0.99992, 3.8, 0.99993, 3.81,
            0.99993, 3.82, 0.99993, 3.83, 0.99994, 3.84, 0.99994, 3.85, 0.99994, 3.86, 0.99994, 3.87, 0.99995, 3.88, 0.99995,
            3.89, 0.99995, 3.9, 0.99995, 3.91, 0.99995, 3.92, 0.99996, 3.93, 0.99996, 3.94, 0.99996, 3.95, 0.99996, 3.96,
            0.99996, 3.97, 0.99996, 3.98, 0.99997, 3.99, 0.99997, 4, 0.99997, 4.01, 0.99997, 4.02, 0.99997, 4.03, 0.99997, 4.04,
            0.99997, 4.05, 0.99997, 4.06, 0.99998, 4.07, 0.99998, 4.08, 0.99998, 4.09, 0.99998, 4.1, 0.99998, 4.11, 0.99998,
            4.12, 0.99998, 4.13, 0.99998, 4.14, 0.99998, 4.15, 0.99998, 4.16, 0.99998, 4.17, 0.99998, 4.18, 0.99999, 4.19,
            0.99999, 4.2, 0.99999, 4.18, 0.99999, 4.19, 0.99999, 4.2, 0.99999, 4.21, 0.99999, 4.22, 0.99999, 4.23, 0.99999,
            4.24, 0.99999, 4.25, 0.99999, 4.26, 0.99999, 4.27, 0.99999, 4.28, 0.99999, 4.29, 0.99999, 4.3, 0.99999, 4.31,
            0.99999, 4.32, 0.99999, 4.33, 0.99999, 4.34, 0.99999};

    /**
     * Test the Normal distribution.
     */
    @Test
    public void testNormal()
    {
        this.stream = new MersenneTwister(10L);
        DistNormal dist = new DistNormal(this.stream, 5.0, 0.5);
        assertEquals(this.stream, dist.getStream());
        assertEquals(5.0, dist.getMu(), 0.0001);
        assertEquals(0.5, dist.getSigma(), 0.0001);
        assertTrue(dist.toString().contains("Normal"));
        assertTrue(dist.toString().contains("5.0"));
        assertTrue(dist.toString().contains("0.5"));

        DistNormal stdDist = new DistNormal(this.stream, 0.0, 1.0);
        assertEquals(1.0 / Math.sqrt(2.0 * Math.PI), stdDist.getProbabilityDensity(0.0), 0.0001);
        assertEquals(0.5, stdDist.getCumulativeProbability(0.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, 0.5), stdDist.getProbabilityDensity(0.5), 0.0001);
        assertEquals(normpdf(0.0, 1.0, 1.0), stdDist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, 2.0), stdDist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, 4.0), stdDist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, 8.0), stdDist.getProbabilityDensity(8.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, -0.5), stdDist.getProbabilityDensity(-0.5), 0.0001);
        assertEquals(normpdf(0.0, 1.0, -1.0), stdDist.getProbabilityDensity(-1.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, -2.0), stdDist.getProbabilityDensity(-2.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, -4.0), stdDist.getProbabilityDensity(-4.0), 0.0001);
        assertEquals(normpdf(0.0, 1.0, -8.0), stdDist.getProbabilityDensity(-8.0), 0.0001);

        assertEquals(1.96, stdDist.getInverseCumulativeProbability(0.975), 0.001);
        assertEquals(-1.96, stdDist.getInverseCumulativeProbability(0.025), 0.001);
        assertEquals(0.975, stdDist.getCumulativeProbability(1.96), 0.001);
        assertEquals(0.025, stdDist.getCumulativeProbability(-1.96), 0.001);

        assertEquals(0.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(0.0)), 0.0001);
        assertEquals(0.5, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(0.5)), 0.0001);
        assertEquals(1.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(1.0)), 0.0001);
        assertEquals(2.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(2.0)), 0.0001);
        assertEquals(4.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(4.0)), 0.0001);
        assertEquals(-0.5, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(-0.5)), 0.0001);
        assertEquals(-1.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(-1.0)), 0.0001);
        assertEquals(-2.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(-2.0)), 0.0001);
        assertEquals(-4.0, stdDist.getInverseCumulativeProbability(stdDist.getCumulativeProbability(-4.0)), 0.0001);

        dist = new DistNormal(this.stream, 5.0, 2.0);
        assertEquals(0.5, dist.getCumulativeProbability(5.0), 0.0001);
        assertEquals(normpdf(5.0, 2.0, 1.0), dist.getProbabilityDensity(1.0), 0.0001);
        assertEquals(normpdf(5.0, 2.0, 2.0), dist.getProbabilityDensity(2.0), 0.0001);
        assertEquals(normpdf(5.0, 2.0, 4.0), dist.getProbabilityDensity(4.0), 0.0001);
        assertEquals(normpdf(5.0, 2.0, 8.0), dist.getProbabilityDensity(8.0), 0.0001);
        assertEquals(normpdf(5.0, 2.0, 10.0), dist.getProbabilityDensity(10.0), 0.0001);

        assertEquals(normcdf(5.0, 2.0, 0.5), dist.getCumulativeProbability(0.5), 0.0001);
        assertEquals(normcdf(5.0, 2.0, 1.0), dist.getCumulativeProbability(1.0), 0.0001);
        assertEquals(normcdf(5.0, 2.0, 2.0), dist.getCumulativeProbability(2.0), 0.0001);
        assertEquals(normcdf(5.0, 2.0, 4.0), dist.getCumulativeProbability(4.0), 0.0001);
        assertEquals(normcdf(5.0, 2.0, 8.0), dist.getCumulativeProbability(8.0), 0.0001);
        assertEquals(normcdf(5.0, 2.0, 10.0), dist.getCumulativeProbability(10.0), 0.0001);

        assertEquals(0.5, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(0.5)), 0.0001);
        assertEquals(1.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(1.0)), 0.0001);
        assertEquals(2.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(2.0)), 0.0001);
        assertEquals(4.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(4.0)), 0.0001);
        assertEquals(8.0, dist.getInverseCumulativeProbability(dist.getCumulativeProbability(8.0)), 0.0001);

        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistNormal(null, 1.0, 2.0);
            }
        }, NullPointerException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistNormal(DistributionTest.this.stream, 2.0, 0.0);
            }
        }, IllegalArgumentException.class);
        Try.testFail(new Try.Execution()
        {
            @Override
            public void execute() throws Throwable
            {
                new DistNormal(DistributionTest.this.stream, 2.0, -1.0);
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
     * @param scale double; parameter 2, 1/rate
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double distErlang(final int k, final double scale, final double x)
    {
        double lambda = 1.0 / scale;
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

    /**
     * Calculate probability density of LogNormal(mu, sigma) for value x. From:
     * https://en.wikipedia.org/wiki/Log-normal_distribution.
     * @param mu double; shape parameter
     * @param sigma double; scale parameter
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double lnpdf(final double mu, final double sigma, final double x)
    {
        return (1.0 / (x * sigma * Math.sqrt(2.0 * Math.PI)))
                * Math.exp(-Math.pow((Math.log(x) - mu), 2) / (2.0 * sigma * sigma));
    }

    /**
     * @param x double
     * @return 1 is even; -1 if not even
     */
    private static int even(final int x)
    {
        return ((int) 2.0 * Math.floor(x / 2.0)) == x ? 1 : -1;
    }

    /**
     * Approximates erf(z).
     * @param z double; the value to calculate erf for
     * @return erf(z)
     */
    public static double erf(final double z)
    {
        double zpos = Math.abs(z);
        double d = zpos;
        for (int i = 1; i < 64; i++)
        {
            double term = Math.pow(zpos, 2 * i + 1) / ((2 * i + 1) * fac(i));
            d += even(i) * term;
            if (term < 1E-16)
            {
                break;
            }
        }
        if (z < 0)
        {
            d = -d;
        }
        return (2.0 / Math.sqrt(Math.PI)) * d;
    }

    /**
     * Calculate cumulative probability density of LogNormal(mu, sigma) for value x. From:
     * https://en.wikipedia.org/wiki/Log-normal_distribution.
     * @param mu double; shape parameter
     * @param sigma double; scale parameter
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double lncdf(final double mu, final double sigma, final double x)
    {
        return 0.5 + 0.5 * erf((Math.log(x) - mu) / (Math.sqrt(2.0) * sigma));
    }

    /**
     * Calculate probability density of Normal(mu, sigma) for value x. From: https://en.wikipedia.org/wiki/Normal_distribution.
     * @param mu double; shape parameter
     * @param sigma double; scale parameter
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double normpdf(final double mu, final double sigma, final double x)
    {
        return (1.0 / (sigma * Math.sqrt(2.0 * Math.PI))) * Math.exp(-0.5 * Math.pow((x - mu) / sigma, 2));
    }

    /**
     * Calculate cumulative probability density of Normal(mu, sigma) for value x. From:
     * https://en.wikipedia.org/wiki/Normal_distribution.
     * @param mu double; shape parameter
     * @param sigma double; scale parameter
     * @param x double; value
     * @return probability density of DistBeta(a, b) for value x
     */
    public static double normcdf(final double mu, final double sigma, final double x)
    {
        return 0.5 + 0.5 * erf((x - mu) / (Math.sqrt(2.0) * sigma));
    }

}
