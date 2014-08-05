package nl.tudelft.simulation.dsol.simtime.dist;

import java.util.Calendar;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.TimeUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 5, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute time type.
 * @param <R> the relative time type.
 * @param <T> the simulation time type.
 */
public abstract class DistContinuousSimTime<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Dist
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the wrapped distribution */
    protected final DistContinuous wrappedDistribution;

    /**
     * constructs a new continuous distribution
     * @param wrappedDistribution the wrapped continuous distribution
     */
    public DistContinuousSimTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract T draw();

    /**
     * returns the probability density value of an observation
     * @param observation the observation.
     * @return double the probability density.
     */
    public double probDensity(final double observation)
    {
        return this.wrappedDistribution.probDensity(observation);
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DistContinuousSimTime.Double */
    public static class Double extends DistContinuousSimTime<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public Double(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeDouble draw()
        {
            return new SimTimeDouble(super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Float */
    public static class Float extends DistContinuousSimTime<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public Float(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeFloat draw()
        {
            return new SimTimeFloat((float) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Long */
    public static class Long extends DistContinuousSimTime<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public Long(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeLong draw()
        {
            return new SimTimeLong((long) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.DoubleUnit */
    public static class DoubleUnit extends DistContinuousSimTime<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public DoubleUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeDoubleUnit draw()
        {
            return new SimTimeDoubleUnit(new UnitTimeDouble(super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.FloatUnit */
    public static class FloatUnit extends DistContinuousSimTime<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public FloatUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeFloatUnit draw()
        {
            return new SimTimeFloatUnit(new UnitTimeFloat((float) super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.LongUnit */
    public static class LongUnit extends DistContinuousSimTime<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public LongUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeLongUnit draw()
        {
            return new SimTimeLongUnit(new UnitTimeLong((long) super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarDouble */
    public static class CalendarDouble extends DistContinuousSimTime<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarDouble(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeCalendarDouble draw()
        {
            double ms = new UnitTimeDouble(super.wrappedDistribution.draw(), this.unit).getTimeMsec().doubleValue();
            return new SimTimeCalendarDouble(ms);
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarFloat */
    public static class CalendarFloat extends DistContinuousSimTime<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarFloat(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeCalendarFloat draw()
        {
            float ms = new UnitTimeFloat((float) super.wrappedDistribution.draw(), this.unit).getTimeMsec().floatValue();
            return new SimTimeCalendarFloat(ms);
        }
    }

    /** Easy access class DistContinuousSimTime.CalendarLong */
    public static class CalendarLong extends DistContinuousSimTime<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarLong(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /**
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime#draw()
         */
        @Override
        public SimTimeCalendarLong draw()
        {
            long ms = new UnitTimeLong((long) super.wrappedDistribution.draw(), this.unit).getTimeMsec().longValue();
            return new SimTimeCalendarLong(ms);
        }
    }

}
