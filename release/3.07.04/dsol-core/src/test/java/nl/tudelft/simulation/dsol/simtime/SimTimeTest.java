package nl.tudelft.simulation.dsol.simtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.djunits.unit.DurationUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.junit.Test;

/**
 * SimTimeTest tests the SimTime classes.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeTest
{

    /**
     * test SimTimeDouble.
     */
    @Test
    public void testSimTimeDouble()
    {
        SimTimeDouble st2 = new SimTimeDouble(2.0);
        assertEquals(2.0, st2.get(), 1E-6);
        SimTimeDouble st1 = new SimTimeDouble(1.0);
        SimTimeDouble st2b = new SimTimeDouble(2.0);
        SimTimeDouble st0 = new SimTimeDouble(0.0);
        SimTimeDouble stm1 = new SimTimeDouble(-1.0);
        SimTimeDouble stc = new SimTimeDouble(10.0);

        assertTrue(st2.equals(st2b));
        assertTrue(st2.equals(st2));
        assertFalse(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
        assertFalse(st1.equals(null));
        assertTrue(st2b.hashCode() == st2.hashCode());

        assertTrue(st2.eq(st2b));
        assertTrue(st2.eq(st2));
        assertFalse(st1.eq(st2));
        assertTrue(st1.lt(st2));
        assertFalse(st2.lt(st1));
        assertTrue(st1.le(st2));
        assertFalse(st2.le(st1));
        assertTrue(st2.le(st2b));
        assertTrue(st2.gt(st1));
        assertFalse(st1.gt(st2));
        assertTrue(st2.ge(st2b));
        assertFalse(st1.ge(st2));
        assertTrue(st2.ge(st1));
        assertFalse(st2.ne(st2b));
        assertTrue(st2.ne(st1));

        assertTrue(st0.eq0());
        assertTrue(st0.le0());
        assertTrue(st0.ge0());
        assertTrue(stm1.lt0());
        assertTrue(st1.ne0());
        assertTrue(st1.gt0());
        assertFalse(st1.eq0());
        assertFalse(st1.le0());
        assertFalse(stm1.ge0());
        assertFalse(st1.lt0());
        assertFalse(st0.ne0());
        assertFalse(stm1.gt0());

        assertEquals("1.0", st1.toString());

        assertEquals(0.0, st1.getAbsoluteZero(), 1E-6);
        assertEquals(0.0, st1.getRelativeZero(), 1E-6);

        assertEquals(new SimTimeDouble(10.0), st1.sum(3.0, 3.0, 3.0));
        assertEquals(new SimTimeDouble(4.0), st1.plus(3.0));
        assertEquals(new SimTimeDouble(1.0), st2.minus(1.0));
        assertEquals(-3.0, stm1.diff(st2), 1E-6);

        SimTimeDouble stcopy = stc.copy();
        assertEquals(10.0, stc.get(), 1E-6);
        stc.add(1.0);
        assertEquals(11.0, stc.get(), 1E-6);
        assertEquals(10.0, stcopy.get(), 1E-6);
        stc.subtract(1.0);
        assertEquals(10.0, stc.get(), 1E-6);
    }

    /**
     * test SimTimeFloat.
     */
    @Test
    public void testSimTimeFloat()
    {
        SimTimeFloat st2 = new SimTimeFloat(2.0f);
        assertEquals(2.0f, st2.get(), 1E-6);
        SimTimeFloat st1 = new SimTimeFloat(1.0f);
        SimTimeFloat st2b = new SimTimeFloat(2.0f);
        SimTimeFloat st0 = new SimTimeFloat(0.0f);
        SimTimeFloat stm1 = new SimTimeFloat(-1.0f);
        SimTimeFloat stc = new SimTimeFloat(10.0f);

        assertTrue(st2.equals(st2b));
        assertTrue(st2.equals(st2));
        assertFalse(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
        assertFalse(st1.equals(null));
        assertTrue(st2b.hashCode() == st2.hashCode());

        assertTrue(st2.eq(st2b));
        assertTrue(st2.eq(st2));
        assertFalse(st1.eq(st2));
        assertTrue(st1.lt(st2));
        assertFalse(st2.lt(st1));
        assertTrue(st1.le(st2));
        assertFalse(st2.le(st1));
        assertTrue(st2.le(st2b));
        assertTrue(st2.gt(st1));
        assertFalse(st1.gt(st2));
        assertTrue(st2.ge(st2b));
        assertFalse(st1.ge(st2));
        assertTrue(st2.ge(st1));
        assertFalse(st2.ne(st2b));
        assertTrue(st2.ne(st1));

        assertTrue(st0.eq0());
        assertTrue(st0.le0());
        assertTrue(st0.ge0());
        assertTrue(stm1.lt0());
        assertTrue(st1.ne0());
        assertTrue(st1.gt0());
        assertFalse(st1.eq0());
        assertFalse(st1.le0());
        assertFalse(stm1.ge0());
        assertFalse(st1.lt0());
        assertFalse(st0.ne0());
        assertFalse(stm1.gt0());

        assertEquals("1.0", st1.toString());

        assertEquals(0.0f, st1.getAbsoluteZero(), 1E-6);
        assertEquals(0.0f, st1.getRelativeZero(), 1E-6);

        assertEquals(new SimTimeFloat(10.0f), st1.sum(3.0f, 3.0f, 3.0f));
        assertEquals(new SimTimeFloat(4.0f), st1.plus(3.0f));
        assertEquals(new SimTimeFloat(1.0f), st2.minus(1.0f));
        assertEquals(-3.0f, stm1.diff(st2), 1E-6);

        SimTimeFloat stcopy = stc.copy();
        assertEquals(10.0f, stc.get(), 1E-6);
        stc.add(1.0f);
        assertEquals(11.0f, stc.get(), 1E-6);
        assertEquals(10.0f, stcopy.get(), 1E-6);
        stc.subtract(1.0f);
        assertEquals(10.0f, stc.get(), 1E-6);
    }

    /**
     * test SimTimeLong.
     */
    @Test
    public void testSimTimeLong()
    {
        SimTimeLong st2 = new SimTimeLong(2L);
        assertEquals(2L, st2.get(), 1E-6);
        SimTimeLong st1 = new SimTimeLong(1L);
        SimTimeLong st2b = new SimTimeLong(2L);
        SimTimeLong st0 = new SimTimeLong(0L);
        SimTimeLong stm1 = new SimTimeLong(-1L);
        SimTimeLong stc = new SimTimeLong(10L);

        assertTrue(st2.equals(st2b));
        assertTrue(st2.equals(st2));
        assertFalse(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
        assertFalse(st1.equals(null));
        assertTrue(st2b.hashCode() == st2.hashCode());

        assertTrue(st2.eq(st2b));
        assertTrue(st2.eq(st2));
        assertFalse(st1.eq(st2));
        assertTrue(st1.lt(st2));
        assertFalse(st2.lt(st1));
        assertTrue(st1.le(st2));
        assertFalse(st2.le(st1));
        assertTrue(st2.le(st2b));
        assertTrue(st2.gt(st1));
        assertFalse(st1.gt(st2));
        assertTrue(st2.ge(st2b));
        assertFalse(st1.ge(st2));
        assertTrue(st2.ge(st1));
        assertFalse(st2.ne(st2b));
        assertTrue(st2.ne(st1));

        assertTrue(st0.eq0());
        assertTrue(st0.le0());
        assertTrue(st0.ge0());
        assertTrue(stm1.lt0());
        assertTrue(st1.ne0());
        assertTrue(st1.gt0());
        assertFalse(st1.eq0());
        assertFalse(st1.le0());
        assertFalse(stm1.ge0());
        assertFalse(st1.lt0());
        assertFalse(st0.ne0());
        assertFalse(stm1.gt0());

        assertEquals("1", st1.toString());

        assertEquals(0L, st1.getAbsoluteZero(), 1E-6);
        assertEquals(0L, st1.getRelativeZero(), 1E-6);

        assertEquals(new SimTimeLong(10L), st1.sum(3L, 3L, 3L));
        assertEquals(new SimTimeLong(4L), st1.plus(3L));
        assertEquals(new SimTimeLong(1L), st2.minus(1L));
        assertEquals(-3L, stm1.diff(st2), 1E-6);

        SimTimeLong stcopy = stc.copy();
        assertEquals(10L, stc.get(), 1E-6);
        stc.add(1L);
        assertEquals(11L, stc.get(), 1E-6);
        assertEquals(10L, stcopy.get(), 1E-6);
        stc.subtract(1L);
        assertEquals(10L, stc.get(), 1E-6);
    }

    /**
     * test SimTimeDoubleUnit.
     */
    @Test
    public void testSimTimeDoubleUnit()
    {
        SimTimeDoubleUnit st2 = new SimTimeDoubleUnit(new Time(2.0, TimeUnit.BASE_SECOND));
        assertEquals(new Time(2.0, TimeUnit.BASE_SECOND), st2.get());
        SimTimeDoubleUnit st1 = new SimTimeDoubleUnit(new Time(1.0, TimeUnit.BASE_SECOND));
        SimTimeDoubleUnit st2b = new SimTimeDoubleUnit(new Time(2.0, TimeUnit.BASE_SECOND));
        SimTimeDoubleUnit st0 = new SimTimeDoubleUnit(new Time(0.0, TimeUnit.BASE_SECOND));
        SimTimeDoubleUnit stm1 = new SimTimeDoubleUnit(new Time(-1.0, TimeUnit.BASE_SECOND));
        SimTimeDoubleUnit stc = new SimTimeDoubleUnit(new Time(10.0, TimeUnit.BASE_SECOND));

        assertTrue(st2.equals(st2b));
        assertTrue(st2.equals(st2));
        assertFalse(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
        assertFalse(st1.equals(null));
        assertTrue(st2b.hashCode() == st2.hashCode());

        assertTrue(st2.eq(st2b));
        assertTrue(st2.eq(st2));
        assertFalse(st1.eq(st2));
        assertTrue(st1.lt(st2));
        assertFalse(st2.lt(st1));
        assertTrue(st1.le(st2));
        assertFalse(st2.le(st1));
        assertTrue(st2.le(st2b));
        assertTrue(st2.gt(st1));
        assertFalse(st1.gt(st2));
        assertTrue(st2.ge(st2b));
        assertFalse(st1.ge(st2));
        assertTrue(st2.ge(st1));
        assertFalse(st2.ne(st2b));
        assertTrue(st2.ne(st1));

        assertTrue(st0.eq0());
        assertTrue(st0.le0());
        assertTrue(st0.ge0());
        assertTrue(stm1.lt0());
        assertTrue(st1.ne0());
        assertTrue(st1.gt0());
        assertFalse(st1.eq0());
        assertFalse(st1.le0());
        assertFalse(stm1.ge0());
        assertFalse(st1.lt0());
        assertFalse(st0.ne0());
        assertFalse(stm1.gt0());

        assertTrue(st1.toString().contains("1.0"));
        assertTrue(st1.toString().contains("s"));

        assertEquals(Time.ZERO, st1.getAbsoluteZero());
        assertEquals(Duration.ZERO, st1.getRelativeZero());

        Duration rt3 = new Duration(3.0, DurationUnit.SECOND);
        assertEquals(new SimTimeDoubleUnit(new Time(10.0, TimeUnit.BASE_SECOND)), st1.sum(rt3, rt3, rt3));
        assertEquals(new SimTimeDoubleUnit(new Time(4.0, TimeUnit.BASE_SECOND)), st1.plus(rt3));
        assertEquals(new SimTimeDoubleUnit(new Time(1.0, TimeUnit.BASE_SECOND)),
                st2.minus(new Duration(1.0, DurationUnit.SECOND)));
        assertEquals(rt3, st2.diff(stm1));

        SimTimeDoubleUnit stcopy = stc.copy();
        assertEquals(new Time(10.0, TimeUnit.BASE_SECOND), stc.get());
        stc.add(new Duration(1.0, DurationUnit.SECOND));
        assertEquals(new Time(11.0, TimeUnit.BASE_SECOND), stc.get());
        assertEquals(new Time(10.0, TimeUnit.BASE_SECOND), stcopy.get());
        stc.subtract(new Duration(1.0, DurationUnit.SECOND));
        assertEquals(new Time(10.0, TimeUnit.BASE_SECOND), stc.get());
    }

    /**
     * test SimTimeFloatUnit.
     */
    @Test
    public void testSimTimeFloatUnit()
    {
        SimTimeFloatUnit st2 = new SimTimeFloatUnit(new FloatTime(2.0, TimeUnit.BASE_SECOND));
        assertEquals(new FloatTime(2.0, TimeUnit.BASE_SECOND), st2.get());
        SimTimeFloatUnit st1 = new SimTimeFloatUnit(new FloatTime(1.0, TimeUnit.BASE_SECOND));
        SimTimeFloatUnit st2b = new SimTimeFloatUnit(new FloatTime(2.0, TimeUnit.BASE_SECOND));
        SimTimeFloatUnit st0 = new SimTimeFloatUnit(new FloatTime(0.0, TimeUnit.BASE_SECOND));
        SimTimeFloatUnit stm1 = new SimTimeFloatUnit(new FloatTime(-1.0, TimeUnit.BASE_SECOND));
        SimTimeFloatUnit stc = new SimTimeFloatUnit(new FloatTime(10.0, TimeUnit.BASE_SECOND));

        assertTrue(st2.equals(st2b));
        assertTrue(st2.equals(st2));
        assertFalse(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
        assertFalse(st1.equals(null));
        assertTrue(st2b.hashCode() == st2.hashCode());

        assertTrue(st2.eq(st2b));
        assertTrue(st2.eq(st2));
        assertFalse(st1.eq(st2));
        assertTrue(st1.lt(st2));
        assertFalse(st2.lt(st1));
        assertTrue(st1.le(st2));
        assertFalse(st2.le(st1));
        assertTrue(st2.le(st2b));
        assertTrue(st2.gt(st1));
        assertFalse(st1.gt(st2));
        assertTrue(st2.ge(st2b));
        assertFalse(st1.ge(st2));
        assertTrue(st2.ge(st1));
        assertFalse(st2.ne(st2b));
        assertTrue(st2.ne(st1));

        assertTrue(st0.eq0());
        assertTrue(st0.le0());
        assertTrue(st0.ge0());
        assertTrue(stm1.lt0());
        assertTrue(st1.ne0());
        assertTrue(st1.gt0());
        assertFalse(st1.eq0());
        assertFalse(st1.le0());
        assertFalse(stm1.ge0());
        assertFalse(st1.lt0());
        assertFalse(st0.ne0());
        assertFalse(stm1.gt0());

        assertTrue(st1.toString().contains("1.0"));
        assertTrue(st1.toString().contains("s"));

        assertEquals(FloatTime.ZERO, st1.getAbsoluteZero());
        assertEquals(FloatDuration.ZERO, st1.getRelativeZero());

        FloatDuration rt3 = new FloatDuration(3.0, DurationUnit.SECOND);
        assertEquals(new SimTimeFloatUnit(new FloatTime(10.0, TimeUnit.BASE_SECOND)), st1.sum(rt3, rt3, rt3));
        assertEquals(new SimTimeFloatUnit(new FloatTime(4.0, TimeUnit.BASE_SECOND)), st1.plus(rt3));
        assertEquals(new SimTimeFloatUnit(new FloatTime(1.0, TimeUnit.BASE_SECOND)),
                st2.minus(new FloatDuration(1.0, DurationUnit.SECOND)));
        assertEquals(rt3, st2.diff(stm1));

        SimTimeFloatUnit stcopy = stc.copy();
        assertEquals(new FloatTime(10.0, TimeUnit.BASE_SECOND), stc.get());
        stc.add(new FloatDuration(1.0, DurationUnit.SECOND));
        assertEquals(new FloatTime(11.0, TimeUnit.BASE_SECOND), stc.get());
        assertEquals(new FloatTime(10.0, TimeUnit.BASE_SECOND), stcopy.get());
        stc.subtract(new FloatDuration(1.0, DurationUnit.SECOND));
        assertEquals(new FloatTime(10.0, TimeUnit.BASE_SECOND), stc.get());
    }

}
