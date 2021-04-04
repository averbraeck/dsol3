package nl.tudelft.simulation.jstats.distributions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.djutils.exceptions.Try;
import org.junit.Test;

import nl.tudelft.simulation.jstats.distributions.empirical.DistributionEntry;
import nl.tudelft.simulation.jstats.distributions.empirical.EmpiricalDistribution;

/**
 * Test the EmpiricalDistribution class.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EmpiricalDistributionTest
{
    /**
     * Test the DistributionEntry object.
     */
    @Test
    public void testDistributionEntry()
    {
        DistributionEntry entry11 = new DistributionEntry(1.0, 0.1);
        DistributionEntry entry12 = new DistributionEntry(1.0, 0.2);
        DistributionEntry entry21 = new DistributionEntry(2.0, 0.1);
        DistributionEntry entry22 = new DistributionEntry(2.0, 0.2);
        DistributionEntry entryN2 = new DistributionEntry(null, 0.2);
        assertEquals(2.0, entry22.getValue().doubleValue(), 1E-6);
        assertEquals(0.2, entry22.getCumulativeProbability(), 1E-6);
        assertEquals(entry11, entry11);
        assertEquals(entry11.hashCode(), entry11.hashCode());
        assertEquals(entry11.toString(), entry11.toString());
        assertNotEquals(entry11, entry12);
        assertNotEquals(entry11.hashCode(), entry12.hashCode());
        assertNotEquals(entry11.toString(), entry12.toString());
        assertNotEquals(entry11, entry21);
        assertNotEquals(entry11, entry22);
        assertNotEquals(null, entry21);
        assertNotEquals(entry11, null);
        assertNotEquals(entry11, new Object());
        assertNotEquals(entryN2, entry22);
        assertNotEquals(entry22, entryN2);
        assertNotEquals(entryN2, entry21);
        assertNotEquals(entry21, entryN2);
        assertNotEquals(entryN2.hashCode(), entry12.hashCode());
        assertNotEquals(entry22.hashCode(), entryN2.hashCode());
        assertEquals(entryN2, new DistributionEntry(null, 0.2));
    }

    /**
     * Test the EmpiricalDistribution object.
     */
    @Test
    public void testHashCodeEquals()
    {
        double[] cpd = {0.1, 0.5, 0.8, 1.0};
        double[] vd = {1.0, 2.0, 3.0, 4.0};
        double[] cpd3 = {0.1, 0.4, 0.8, 1.0};
        double[] vd4 = {1.0, 2.0, 3.5, 4.0};
        EmpiricalDistribution def = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        EmpiricalDistribution def2 = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        EmpiricalDistribution def3 = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd3, false);
        EmpiricalDistribution def4 = EmpiricalDistribution.createFromCumulativeProbabilities(vd4, cpd, false);
        double[] cpdt = {0.0, 0.5, 0.8, 1.0};
        double[] vdt = {-2.0, 2.0, 3.0, 4.0};
        EmpiricalDistribution det = EmpiricalDistribution.createFromCumulativeProbabilities(vdt, cpdt, true);
        assertEquals(def, def2);
        assertEquals(def.hashCode(), def2.hashCode());
        assertEquals(def.toString(), def2.toString());
        assertEquals(def, def);
        assertNotEquals(def, det);
        assertNotEquals(def, null);
        assertNotEquals(def, new Object());
        assertNotEquals(def, def3);
        assertNotEquals(def, def4);
        assertNotEquals(def.hashCode(), def3.hashCode());
        assertNotEquals(def.toString(), def3.toString());
        assertNotEquals(def.hashCode(), def4.hashCode());
        assertNotEquals(def.toString(), def4.toString());
        assertNotEquals(def.hashCode(), det.hashCode());
        assertNotEquals(def.toString(), det.toString());
    }

    /* ******************************************************************************************************************** */
    /* ******************************** CONSTRUCT BASED ON CUMULATIVE DISTRIBUTION **************************************** */
    /* ******************************************************************************************************************** */

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and double[] values.
     */
    @Test
    public void testCreateCumulativeDoubleDoubleNotInterpolated()
    {
        double[] cpd = {0.1, 0.5, 0.8, 1.0};
        double[] vd = {1.0, 2.0, 3.0, 4.0};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        assertEquals(4, de.size());
        assertEquals(false, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.1));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntry(0.1));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getFloorEntry(1.0));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getCeilingEntry(1.0));
        assertNull(de.getFloorEntry(0.0));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(1.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        assertEquals(new DistributionEntry(1.0, 0.1), de.getPrevEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getPrevEntry(0.5));
        assertNull(de.getPrevEntry(0.1));
        assertNull(de.getPrevEntry(0.0));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getNextEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getNextEntry(0.5));
        assertNull(de.getNextEntry(1.0));
        assertNull(de.getNextEntry(1.1));
        assertNull(de.getFloorEntryForValue(0.0));
        assertNull(de.getCeilingEntryForValue(5.0));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getFloorEntryForValue(2.0));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntryForValue(2.0));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntryForValue(1.0));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntryForValue(1.0));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntryForValue(-1.0));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getFloorEntryForValue(4.0));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getFloorEntryForValue(8.0));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getCeilingEntryForValue(4.0));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getFloorEntryForValue(3.5));
        assertEquals(new DistributionEntry(4.0, 1.0), de.getCeilingEntryForValue(3.5));
        EmpiricalDistribution.createFromCumulativeProbabilities(new Double[] {10.0}, new double[] {1.0}, false);
        
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.2, 0.2, 1.0}, false);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {-0.1, 0.2, 0.5, 1.0}, false);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.2, 0.5, 1.0}, false);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.5, 0.2, 1.0}, false);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.2, 0.5, 1.001}, false);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.2, 0.5, 0.9}, false);
        });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((double[]) null, cpd, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, false); });
        Try.testFail(
                () -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.2, 1.0}, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {2, 3, 1, 4}, cpd, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {}, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {}, cpd, false); });
        Try.testFail(
                () -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {}, new double[] {}, false); });
    }

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and double[] values.
     */
    @Test
    public void testCreateCumulativeDoubleDoubleInterpolated()
    {
        double[] cpd = {0.0, 0.5, 0.8, 1.0};
        double[] vd = {-2.0, 2.0, 3.0, 4.0};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, true);
        assertEquals(4, de.size());
        assertEquals(true, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertEquals(new DistributionEntry(-2.0, 0.0), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getFloorEntry(0.8));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getCeilingEntry(0.8));
        assertNull(de.getFloorEntry(-0.1));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(-2.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);

        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.2, 0.2, 1.0}, true);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {-0.1, 0.2, 0.5, 1.0}, true);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.0, 0.5, 1.0}, true);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.5, 0.2, 1.0}, true);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.2, 0.5, 1.001}, true);
        });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.0, 0.2, 0.5, 0.9}, true);
        });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((double[]) null, cpd, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, true); });
        Try.testFail(
                () -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {0.1, 0.2, 1.0}, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {2, 3, 1, 4}, cpd, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, new double[] {}, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {}, cpd, true); });
        Try.testFail(
                () -> { EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {}, new double[] {}, true); });
        Try.testFail(() -> {
            EmpiricalDistribution.createFromCumulativeProbabilities(new double[] {1.0}, new double[] {1.0}, true);
        });
    }

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and Number[] values.
     */
    @Test
    public void testCreateCumulativeDoubleNumberNotInterpolated()
    {
        double[] cpd = {0.1, 0.5, 0.8, 1.0};
        Number[] vd = {1.0, 2.0, 3.0, 4.0};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        assertEquals(4, de.size());
        assertEquals(false, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().toArray(new Number[0]));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.1));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntry(0.1));
        assertNull(de.getFloorEntry(0.0));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(1.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((Double[]) null, cpd, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, false); });
    }

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and Number[] values.
     */
    @Test
    public void testCreateCumulativeDoubleNumberInterpolated()
    {
        double[] cpd = {0.0, 0.5, 0.8, 1.0};
        Number[] vd = {-2.0, 2.0, 3.0, 4.0};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, true);
        assertEquals(4, de.size());
        assertEquals(true, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().toArray(new Number[0]));
        assertEquals(new DistributionEntry(-2.0, 0.0), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getFloorEntry(0.8));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getCeilingEntry(0.8));
        assertNull(de.getFloorEntry(-0.1));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(-2.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((Double[]) null, cpd, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, true); });
    }

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and long[] values.
     */
    @Test
    public void testCreateCumulativeLongNumberNotInterpolated()
    {
        double[] cpd = {0.1, 0.5, 0.8, 1.0};
        long[] vd = {1L, 2L, 3L, 4L};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        assertEquals(4, de.size());
        assertEquals(false, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().stream().mapToLong(v -> v.longValue()).toArray());
        assertEquals(new DistributionEntry(1L, 0.1), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(1L, 0.1), de.getFloorEntry(0.1));
        assertEquals(new DistributionEntry(2L, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(1L, 0.1), de.getCeilingEntry(0.1));
        assertNull(de.getFloorEntry(0.0));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(1L, de.getLowestValue().longValue());
        assertEquals(4L, de.getHighestValue().longValue());
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((long[]) null, cpd, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, false); });
    }

    /**
     * Test the construction from a cumulative distribution with double[] cumulative distribution and long[] values.
     */
    @Test
    public void testCreateCumulativeLongNumberInterpolated()
    {
        double[] cpd = {0.0, 0.5, 0.8, 1.0};
        long[] vd = {-2L, 2L, 3L, 4L};
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, true);
        assertEquals(4, de.size());
        assertEquals(true, de.isInterpolated());
        assertArrayEquals(cpd, de.getCumulativeProbabilities().stream().mapToDouble(v -> v.doubleValue()).toArray(), 0.001);
        assertArrayEquals(vd, de.getValues().stream().mapToLong(v -> v.longValue()).toArray());
        assertEquals(new DistributionEntry(-2L, 0.0), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(3L, 0.8), de.getFloorEntry(0.8));
        assertEquals(new DistributionEntry(2L, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(3L, 0.8), de.getCeilingEntry(0.8));
        assertNull(de.getFloorEntry(-0.1));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(-2L, de.getLowestValue().longValue());
        assertEquals(4L, de.getHighestValue().longValue());
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((long[]) null, cpd, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (double[]) null, true); });
    }

    /**
     * Test the construction from a cumulative distribution with List&lt;Double&gt; cumulative distribution and
     * List&lt;Double&gt; values.
     */
    @Test
    public void testCreateCumulativeListNotInterpolated()
    {
        List<Double> cpd = Arrays.asList(0.1, 0.5, 0.8, 1.0);
        List<Double> vd = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, false);
        assertEquals(4, de.size());
        assertEquals(false, de.isInterpolated());
        assertEquals(cpd, de.getCumulativeProbabilities());
        assertEquals(vd, de.getValues());
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.1));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntry(0.1));
        assertNull(de.getFloorEntry(0.0));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(1.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((List<Double>) null, cpd, false); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (List<Double>) null, false); });
    }

    /**
     * Test the construction from a cumulative distribution with List&lt;Double&gt; cumulative distribution and
     * List&lt;Double&gt; values.
     */
    @Test
    public void testCreateCumulativeListInterpolated()
    {
        List<Double> cpd = Arrays.asList(0.0, 0.5, 0.8, 1.0);
        List<Double> vd = Arrays.asList(-2.0, 2.0, 3.0, 4.0);
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(vd, cpd, true);
        assertEquals(4, de.size());
        assertEquals(true, de.isInterpolated());
        assertEquals(cpd, de.getCumulativeProbabilities());
        assertEquals(vd, de.getValues());
        assertEquals(new DistributionEntry(-2.0, 0.0), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getFloorEntry(0.8));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getCeilingEntry(0.8));
        assertNull(de.getFloorEntry(-0.1));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(-2.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities((List<Double>) null, cpd, true); });
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(vd, (List<Double>) null, true); });
    }

    /**
     * Test the construction from a cumulative distribution with values from a SortedMap.
     */
    @Test
    public void testCreateCumulativeMapNotInterpolated()
    {
        SortedMap<Double, Double> map = new TreeMap<>();
        map.put(1.0, 0.1);
        map.put(2.0, 0.5);
        map.put(3.0, 0.8);
        map.put(4.0, 1.0);
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(map, false);
        assertEquals(4, de.size());
        assertEquals(false, de.isInterpolated());
        assertEquals(Arrays.asList(0.1, 0.5, 0.8, 1.0), de.getCumulativeProbabilities());
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0), de.getValues());
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getFloorEntry(0.1));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(1.0, 0.1), de.getCeilingEntry(0.1));
        assertNull(de.getFloorEntry(0.0));
        assertNull(de.getCeilingEntry(1.1));
        assertEquals(1.0, de.getLowestValue().doubleValue(), 1E-6);
        assertEquals(4.0, de.getHighestValue().doubleValue(), 1E-6);
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(null, false); });
    }

    /**
     * Test the construction from a cumulative distribution with values from a SortedMap.
     */
    @Test
    public void testCreateCumulativeMapInterpolated()
    {
        SortedMap<Double, Double> map = new TreeMap<>();
        map.put(-2.0, 0.0);
        map.put(2.0, 0.5);
        map.put(3.0, 0.8);
        map.put(4.0, 1.0);
        EmpiricalDistribution de = EmpiricalDistribution.createFromCumulativeProbabilities(map, true);
        assertEquals(4, de.size());
        assertEquals(true, de.isInterpolated());
        assertEquals(Arrays.asList(0.0, 0.5, 0.8, 1.0), de.getCumulativeProbabilities());
        assertEquals(Arrays.asList(-2.0, 2.0, 3.0, 4.0), de.getValues());
        assertEquals(new DistributionEntry(-2.0, 0.0), de.getFloorEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getFloorEntry(0.8));
        assertEquals(new DistributionEntry(2.0, 0.5), de.getCeilingEntry(0.2));
        assertEquals(new DistributionEntry(3.0, 0.8), de.getCeilingEntry(0.8));
        assertNull(de.getFloorEntry(-0.1));
        assertNull(de.getCeilingEntry(1.1));
        Try.testFail(() -> { EmpiricalDistribution.createFromCumulativeProbabilities(null, true); });
    }

    /* ******************************************************************************************************************** */
    /* ********************************* CONSTRUCT BASED ON PROBABILITY DENSITIES ***************************************** */
    /* ******************************************************************************************************************** */

}
