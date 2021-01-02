package nl.tudelft.simulation.jstats.streams;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

/**
 * The test script for the Stream class.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class StreamTest
{
    /**
     * Test the Random Number Generator classes for double.
     */
    @Test
    public final void testStreamDouble()
    {
        System.out.println("\nDOUBLE");
        int nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            double min = 1.0;
            double max = 0.0;
            for (int i = 0; i < nr; i++)
            {
                double value = stream.nextDouble();
                Assert.assertTrue(value >= 0.0);
                Assert.assertTrue(value <= 1.0);
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            System.out.println(String.format("%-18s double sum=%6d, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    (long) sum, min, max));
            assertEquals(0.5, sum / (1.0 * nr), 0.01);
            assertEquals(0.0, min, 0.01);
            assertEquals(1.0, max, 0.01);
        }
    }

    /**
     * Test the Random Number Generator classes for float.
     */
    @Test
    public final void testStreamFloat()
    {
        System.out.println("\nFLOAT");
        int nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            float sum = 0.0f;
            float min = 1.0f;
            float max = 0.0f;
            for (int i = 0; i < nr; i++)
            {
                float value = stream.nextFloat();
                Assert.assertTrue(value >= 0.0f);
                Assert.assertTrue(value <= 1.0f);
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            System.out.println(String.format("%-18s float sum=%6d, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    (long) sum, min, max));
            assertEquals(0.5f, sum / (1.0f * nr), 0.01);
            assertEquals(0.0f, min, 0.01);
            assertEquals(1.0f, max, 0.01);
        }
    }

    /**
     * Test the Random Number Generator classes for int.
     */
    @Test
    public final void testStreamInt()
    {
        System.out.println("\nINT");
        int nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < nr; i++)
            {
                int value = stream.nextInt();
                sum += value / (1.0 * Integer.MAX_VALUE);
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            double avg = (1.0 * sum) / (1.0d * nr);
            double dMin = (1.0d * min) / (1.0d * Integer.MAX_VALUE);
            double dMax = (1.0d * max) / (1.0d * Integer.MAX_VALUE);
            System.out.println(String.format("%-18s int avg=%10.8f, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    avg, dMin, dMax));
            assertEquals(0.0, avg, 0.01);
            assertEquals(-1.0, dMin, 0.01);
            assertEquals(1.0, dMax, 0.01);
        }
    }

    /**
     * Test the Random Number Generator classes for int.
     */
    @Test
    public final void testStreamIntEqualBits()
    {
        System.out.println("\nINT EQUAL NUMBER OF BITS");
        int nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            int NRBINS = 32;
            int[] bins = new int[NRBINS];
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < nr; i++)
            {
                int value = stream.nextInt();
                sum += value / (1.0 * Integer.MAX_VALUE);
                min = Math.min(min, value);
                max = Math.max(max, value);
                for (int j = 0; j < NRBINS; j++)
                {
                    if ((value & 1) == 1)
                    {
                        bins[j]++;
                    }
                    value = value >>> 1;
                }
            }
            double avg = (1.0 * sum) / (1.0d * nr);
            double dMin = (1.0d * min) / (1.0d * Integer.MAX_VALUE);
            double dMax = (1.0d * max) / (1.0d * Integer.MAX_VALUE);
            System.out.println(String.format("%-18s int avg=%10.8f, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    avg, dMin, dMax));
            for (int i = 0; i < NRBINS; i++)
            {
                double frac = 1.0 * bins[i] / (1.0 * nr);
                System.out.println(String.format("    fraction in bin %2d = %10.8f", i, frac));
            }
            for (int i = 0; i < NRBINS; i++)
            {
                double frac = 1.0 * bins[i] / (1.0 * nr);
                assertEquals(0.5, frac, 0.01);
            }
        }
    }

    /**
     * Test the Random Number Generator classes for int.
     */
    @Test
    public final void testStreamInt0to10()
    {
        System.out.println("\nINT 0 - 10");
        int nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            long sum = 0L;
            int NRBINS = 10;
            int[] bins = new int[NRBINS];
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < nr; i++)
            {
                int value = stream.nextInt(0, 9);
                bins[value]++;
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            double avg = (1.0 * sum) / (1.0d * nr);
            System.out.println(
                    String.format("%-18s int avg=%10.8f, min=%2d, max=%2d", stream.getClass().getSimpleName(), avg, min, max));
            for (int i = 0; i < NRBINS; i++)
            {
                double perc = 100.0 * bins[i] / (1.0 * nr);
                System.out.println(String.format("    number in bin %2d = %8.5f perc", i, perc));
            }
            for (int i = 0; i < NRBINS; i++)
            {
                double perc = 100.0 * bins[i] / (1.0 * nr);
                assertEquals(10.0, perc, 0.2);
            }
            assertEquals(4.5, avg, 0.01);
            assertEquals(0, min);
            assertEquals(9, max);
        }
    }

    /**
     * Test the Random Number Generator classes for boolean.
     */
    @Test
    public final void testStreamBoolean()
    {
        System.out.println("\nBOOLEAN");
        int nr = 100000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            for (int i = 0; i < nr; i++)
            {
                boolean value = stream.nextBoolean();
                sum += value ? 1.0 : 0.0;
            }
            double avg = sum / (1.0 * nr);
            System.out.println(String.format("%-18s boolean avg = %8.6f", stream.getClass().getSimpleName(), avg));
            assertEquals(0.5, avg, 0.01);
        }
    }

    /**
     * Test the Random Number Generator classes for long.
     */
    @Test
    public final void testStreamLong()
    {
        System.out.println("\nLONG");
        long nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (int i = 0; i < nr; i++)
            {
                long value = stream.nextLong();
                sum += value / (1.0d * Long.MAX_VALUE);
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            double avg = (1.0d * sum) / (1.0d * nr);
            double dMin = (1.0d * min) / (1.0d * Long.MAX_VALUE);
            double dMax = (1.0d * max) / (1.0d * Long.MAX_VALUE);
            System.out.println(String.format("%-18s long avg=%10.8f, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    avg, dMin, dMax));
            assertEquals(0.0, avg, 0.01);
            assertEquals(-1.0, dMin, 0.01);
            assertEquals(1.0, dMax, 0.01);
        }
    }

    /**
     * Test the Random Number Generator classes for long.
     */
    @Test
    public final void testStreamLongEqualBits()
    {
        System.out.println("\nLONG EQUAL NUMBER OF BITS");
        long nr = 1000000;
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (StreamInterface stream : streams)
        {
            double sum = 0.0;
            int NRBINS = 64;
            long[] bins = new long[NRBINS];
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (int i = 0; i < nr; i++)
            {
                long value = stream.nextLong();
                sum += value / (1.0d * Long.MAX_VALUE);
                min = Math.min(min, value);
                max = Math.max(max, value);
                for (int j = 0; j < NRBINS; j++)
                {
                    if ((value & 1) == 1)
                    {
                        bins[j]++;
                    }
                    value = value >>> 1;
                }
            }
            double avg = (1.0d * sum) / (1.0d * nr);
            double dMin = (1.0d * min) / (1.0d * Long.MAX_VALUE);
            double dMax = (1.0d * max) / (1.0d * Long.MAX_VALUE);
            System.out.println(String.format("%-18s long avg=%10.8f, min=%10.8f, max=%10.8f", stream.getClass().getSimpleName(),
                    avg, dMin, dMax));
            for (int i = 0; i < NRBINS; i++)
            {
                double frac = 1.0 * bins[i] / (1.0 * nr);
                System.out.println(String.format("    fraction in bin %2d = %10.8f", i, frac));
            }
            for (int i = 0; i < NRBINS; i++)
            {
                double frac = 1.0d * bins[i] / (1.0 * nr);
                assertEquals(0.5, frac, 0.01);
            }
        }
    }

}
