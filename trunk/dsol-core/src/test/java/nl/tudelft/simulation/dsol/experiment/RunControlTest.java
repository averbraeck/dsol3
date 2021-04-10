package nl.tudelft.simulation.dsol.experiment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.djunits.unit.DurationUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.exceptions.Try;
import org.junit.Test;

import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

/**
 * RunControlTest tests the RunControl object.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RunControlTest
{
    /**
     * test the RunControl object.
     */
    @Test
    public void testRunControl()
    {
        RunControl<Double, Double, SimTimeDouble> rcdg = new RunControl<>("rcdg", new SimTimeDouble(10.0), 5.0, 20.0);
        assertEquals("rcdg", rcdg.getId());
        assertEquals("rcdg", rcdg.getDescription());
        rcdg.setDescription("description");
        assertEquals("description", rcdg.getDescription());
        assertEquals(new SimTimeDouble(10.0), rcdg.getStartSimTime());
        assertEquals(new SimTimeDouble(30.0), rcdg.getEndSimTime());
        assertEquals(new SimTimeDouble(15.0), rcdg.getWarmupSimTime());
        assertEquals(10.0, rcdg.getStartTime(), 1E-6);
        assertEquals(30.0, rcdg.getEndTime(), 1E-6);
        assertEquals(15.0, rcdg.getWarmupTime(), 1E-6);
        assertEquals(20.0, rcdg.getRunLength(), 1E-6);
        assertEquals(5.0, rcdg.getWarmupPeriod(), 1E-6);

        // types
        RunControl.TimeDouble rcd = new RunControl.TimeDouble("rc", 10.0, 5.0, 20.0);
        assertEquals(30.0, rcd.getEndTime(), 1E-6);
        RunControl.TimeFloat rcf = new RunControl.TimeFloat("rc", 10.0f, 5.0f, 20.0f);
        assertEquals(30.0f, rcf.getEndTime(), 1E-6);
        RunControl.TimeLong rcl = new RunControl.TimeLong("rc", 10L, 5L, 20L);
        assertEquals(30L, rcl.getEndTime().longValue());
        RunControl.TimeDoubleUnit rcdu = new RunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR),
                new Duration(5.0, DurationUnit.HOUR), new Duration(20.0, DurationUnit.HOUR));
        assertEquals(30.0, rcdu.getEndTime().getInUnit(), 1E-6);
        RunControl.TimeFloatUnit rcdf = new RunControl.TimeFloatUnit("rc", new FloatTime(10.0f, TimeUnit.BASE_HOUR),
                new FloatDuration(5.0f, DurationUnit.HOUR), new FloatDuration(20.0f, DurationUnit.HOUR));
        assertEquals(30.0f, rcdf.getEndTime().getInUnit(), 1E-6);

        // equals and hashCode
        assertTrue(rcd.equals(rcd));
        assertNotEquals(rcd, rcf);
        assertTrue(rcd.hashCode() == rcd.hashCode());
        assertFalse(rcd.hashCode() == rcf.hashCode());
        assertNotEquals(rcd, null);
        assertNotEquals(rcd, new RunControl.TimeDouble("rc2", 10.0, 5.0, 20.0));
        assertNotEquals(rcd, new RunControl.TimeDouble("rc", 11.0, 5.0, 20.0));
        assertNotEquals(rcd, new RunControl.TimeDouble("rc", 10.0, 15.0, 20.0));
        assertNotEquals(rcd, new RunControl.TimeDouble("rc", 10.0, 5.0, 21.0));
        assertNotEquals(rcd, new Object());
        assertEquals(rcd, new RunControl.TimeDouble("rc", 10.0, 5.0, 20.0));
        assertTrue(rcd.toString().contains("rc"));

        // errors
        Try.testFail(() -> {
            new RunControl.TimeDoubleUnit(null, new Time(10.0, TimeUnit.BASE_HOUR), new Duration(5.0, DurationUnit.DAY),
                    new Duration(20.0, DurationUnit.HOUR));
        });
        Try.testFail(() -> {
            new RunControl.TimeDoubleUnit("rc", null, new Duration(5.0, DurationUnit.DAY),
                    new Duration(20.0, DurationUnit.HOUR));
        });
        Try.testFail(() -> {
            new RunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR), null,
                    new Duration(20.0, DurationUnit.HOUR));
        });
        Try.testFail(() -> {
            new RunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR), new Duration(5.0, DurationUnit.DAY), null);
        });
        Try.testFail(() -> { new RunControl.TimeDouble("rc", 10.0, 5.0, 5.0); });
        Try.testFail(() -> { new RunControl.TimeDouble("rc", 10.0, -1.0, 15.0); });
        Try.testFail(() -> { new RunControl.TimeDouble("rc", 10.0, 5.0, -15.0); });
        Try.testFail(() -> { new RunControl.TimeDouble("rc", 10.0, 5.0, 0.0); });
    }

    /**
     * test the ExperimentRunControl object.
     */
    @Test
    public void testExperimentRunControl()
    {
        ExperimentRunControl<Double, Double, SimTimeDouble> rcdg =
                new ExperimentRunControl<>("rcdg", new SimTimeDouble(10.0), 5.0, 20.0, 10);
        assertEquals("rcdg", rcdg.getId());
        assertEquals("rcdg", rcdg.getDescription());
        rcdg.setDescription("description");
        assertEquals("description", rcdg.getDescription());
        assertEquals(new SimTimeDouble(10.0), rcdg.getStartSimTime());
        assertEquals(new SimTimeDouble(30.0), rcdg.getEndSimTime());
        assertEquals(new SimTimeDouble(15.0), rcdg.getWarmupSimTime());
        assertEquals(10.0, rcdg.getStartTime(), 1E-6);
        assertEquals(30.0, rcdg.getEndTime(), 1E-6);
        assertEquals(15.0, rcdg.getWarmupTime(), 1E-6);
        assertEquals(20.0, rcdg.getRunLength(), 1E-6);
        assertEquals(5.0, rcdg.getWarmupPeriod(), 1E-6);
        assertEquals(10, rcdg.getNumberOfReplications());

        // types
        ExperimentRunControl.TimeDouble rcd = new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 20.0, 10);
        assertEquals(30.0, rcd.getEndTime(), 1E-6);
        ExperimentRunControl.TimeFloat rcf = new ExperimentRunControl.TimeFloat("rc", 10.0f, 5.0f, 20.0f, 10);
        assertEquals(30.0f, rcf.getEndTime(), 1E-6);
        ExperimentRunControl.TimeLong rcl = new ExperimentRunControl.TimeLong("rc", 10L, 5L, 20L, 10);
        assertEquals(30L, rcl.getEndTime().longValue());
        ExperimentRunControl.TimeDoubleUnit rcdu =
                new ExperimentRunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR),
                        new Duration(5.0, DurationUnit.HOUR), new Duration(20.0, DurationUnit.HOUR), 10);
        assertEquals(30.0, rcdu.getEndTime().getInUnit(), 1E-6);
        ExperimentRunControl.TimeFloatUnit rcdf =
                new ExperimentRunControl.TimeFloatUnit("rc", new FloatTime(10.0f, TimeUnit.BASE_HOUR),
                        new FloatDuration(5.0f, DurationUnit.HOUR), new FloatDuration(20.0f, DurationUnit.HOUR), 10);
        assertEquals(30.0f, rcdf.getEndTime().getInUnit(), 1E-6);

        // equals and hashCode
        assertTrue(rcd.equals(rcd));
        assertNotEquals(rcd, rcf);
        assertTrue(rcd.hashCode() == rcd.hashCode());
        assertFalse(rcd.hashCode() == rcf.hashCode());
        assertNotEquals(rcd, null);
        assertNotEquals(rcd, new ExperimentRunControl.TimeDouble("rc2", 10.0, 5.0, 20.0, 10));
        assertNotEquals(rcd, new ExperimentRunControl.TimeDouble("rc", 11.0, 5.0, 20.0, 10));
        assertNotEquals(rcd, new ExperimentRunControl.TimeDouble("rc", 10.0, 15.0, 20.0, 10));
        assertNotEquals(rcd, new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 21.0, 10));
        assertNotEquals(rcd, new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 20.0, 11));
        assertNotEquals(rcd, new Object());
        assertEquals(rcd, new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 20.0, 10));
        assertTrue(rcd.toString().contains("rc"));

        // errors
        Try.testFail(() -> {
            new ExperimentRunControl.TimeDoubleUnit(null, new Time(10.0, TimeUnit.BASE_HOUR),
                    new Duration(5.0, DurationUnit.DAY), new Duration(20.0, DurationUnit.HOUR), 10);
        });
        Try.testFail(() -> {
            new ExperimentRunControl.TimeDoubleUnit("rc", null, new Duration(5.0, DurationUnit.DAY),
                    new Duration(20.0, DurationUnit.HOUR), 10);
        });
        Try.testFail(() -> {
            new ExperimentRunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR), null,
                    new Duration(20.0, DurationUnit.HOUR), 10);
        });
        Try.testFail(() -> {
            new ExperimentRunControl.TimeDoubleUnit("rc", new Time(10.0, TimeUnit.BASE_HOUR),
                    new Duration(5.0, DurationUnit.DAY), null, 10);
        });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 5.0, 10); });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, -1.0, 15.0, 10); });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, -15.0, 10); });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 0.0, 10); });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 15.0, 0); });
        Try.testFail(() -> { new ExperimentRunControl.TimeDouble("rc", 10.0, 5.0, 15.0, -1); });
    }

}
