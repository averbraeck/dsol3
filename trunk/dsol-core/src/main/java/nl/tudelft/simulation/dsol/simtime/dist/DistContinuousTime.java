package nl.tudelft.simulation.dsol.simtime.dist;

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
 * @param <R> the relative time type.
 */
public abstract class DistContinuousTime<R extends Number & Comparable<R>> extends Dist
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the wrapped distribution */
    protected final DistContinuous wrappedDistribution;
    
    /**
     * constructs a new continuous distribution
     * @param wrappedDistribution the wrapped continuous distribution
     */
    public DistContinuousTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract R draw();

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

    /** Easy access class DistContinuousTime.Double */
    public static class Double extends DistContinuousTime<java.lang.Double>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public java.lang.Double draw()
        {
            return super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Float */
    public static class Float extends DistContinuousTime<java.lang.Float>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public java.lang.Float draw()
        {
            return (float) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Long */
    public static class Long extends DistContinuousTime<java.lang.Long>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public java.lang.Long draw()
        {
            return (long) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.DoubleUnit */
    public static class DoubleUnit extends DistContinuousTime<UnitTimeDouble>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeDouble draw()
        {
            return new UnitTimeDouble(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.FloatUnit */
    public static class FloatUnit extends DistContinuousTime<UnitTimeFloat>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeFloat draw()
        {
            return new UnitTimeFloat((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.LongUnit */
    public static class LongUnit extends DistContinuousTime<UnitTimeLong> 
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeLong draw()
        {
            return new UnitTimeLong((long) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarDouble */
    public static class CalendarDouble extends DistContinuousTime<UnitTimeDouble>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeDouble draw()
        {
            return new UnitTimeDouble(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarFloat */
    public static class CalendarFloat extends DistContinuousTime<UnitTimeFloat>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeFloat draw()
        {
            return new UnitTimeFloat((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarLong */
    public static class CalendarLong extends DistContinuousTime<UnitTimeLong>
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
         * @see nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime#draw()
         */
        @Override
        public UnitTimeLong draw()
        {
            return new UnitTimeLong((long) super.wrappedDistribution.draw(), this.unit);
        }
    }

}
