package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.event.InitialEventContext;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * A single replication that is executed outside of an Experiment.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class SingleReplication<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends AbstractReplication<A, R, T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20210404L;

    /**
     * construct a stand-alone replication.
     * @param id String; the id of the replication; should be unique within the experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the warmup
     *             time is longer than or equal to the runlength, or when a context for the replication cannot be created
     */
    public SingleReplication(final String id, final T startTime, final R warmupPeriod, final R runLength)
    {
        this(new RunControl<>(id, startTime, warmupPeriod, runLength));
    }

    /**
     * Construct a stand-alone replication using a RunControl to store the run information.
     * @param runControl RunControlInterface; the run control for the replication
     * @throws NullPointerException when runControl is null
     */
    public SingleReplication(final RunControlInterface<A, R, T> runControl)
    {
        super(runControl);
        setContext();
    }

    /**
     * Set the context for this replication.
     * @throws IllegalArgumentException in case a context for the replication cannot be created
     */
    protected void setContext()
    {
        try
        {
            ContextInterface rootContext = InitialEventContext.instantiate("root");
            this.context = ContextUtil.lookupOrCreateSubContext(rootContext, getId());
        }
        catch (RemoteException | NamingException exception)
        {
            throw new IllegalArgumentException(
                    "Cannot lookup or create context for experiment. Error is: " + exception.getMessage());
        }
    }

    /**
     * Remove the context for this replication.
     */
    public void removeFromContext()
    {
        try
        {
            if (this.context != null)
            {
                ContextInterface rootContext = InitialEventContext.instantiate("root");
                ContextUtil.destroySubContext(rootContext, getId());
            }
        }
        catch (RemoteException | NamingException exception)
        {
            throw new IllegalArgumentException("Cannot destroy context for replication. Error is: " + exception.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public RunControlInterface<A, R, T> getRunControl()
    {
        return this.runControl;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Replication.TimeDouble.
     */
    public static class TimeDouble extends SingleReplication<Double, Double, SimTimeDouble>
            implements ReplicationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the
         *             warmup time is longer than or equal to the runlength, or when a context for the replication cannot be
         *             created
         */
        public TimeDouble(final String id, final double startTime, final double warmupPeriod, final double runLength)
        {
            this(new RunControl.TimeDouble(id, startTime, warmupPeriod, runLength));
        }

        /**
         * Construct a stand-alone replication using a RunControl to store the run information.
         * @param runControl RunControlInterface; the run control for the replication
         * @throws NullPointerException when runControl is null
         */
        public TimeDouble(final RunControlInterface.TimeDouble runControl)
        {
            super(runControl);
        }

        /** {@inheritDoc} */
        @Override
        public RunControlInterface.TimeDouble getRunControl()
        {
            return (RunControlInterface.TimeDouble) super.getRunControl();
        }
    }

    /**
     * Easy access class Replication.TimeFloat.
     */
    public static class TimeFloat extends SingleReplication<Float, Float, SimTimeFloat>
            implements ReplicationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime float; the start time
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the
         *             warmup time is longer than or equal to the runlength, or when a context for the replication cannot be
         *             created
         */
        public TimeFloat(final String id, final float startTime, final float warmupPeriod, final float runLength)
        {
            this(new RunControl.TimeFloat(id, startTime, warmupPeriod, runLength));
        }

        /**
         * Construct a stand-alone replication using a RunControl to store the run information.
         * @param runControl RunControlInterface; the run control for the replication
         * @throws NullPointerException when runControl is null
         */
        public TimeFloat(final RunControlInterface.TimeFloat runControl)
        {
            super(runControl);
        }

        /** {@inheritDoc} */
        @Override
        public RunControlInterface.TimeFloat getRunControl()
        {
            return (RunControlInterface.TimeFloat) super.getRunControl();
        }
    }

    /**
     * Easy access class Replication.TimeLong.
     */
    public static class TimeLong extends SingleReplication<Long, Long, SimTimeLong> implements ReplicationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the
         *             warmup time is longer than or equal to the runlength, or when a context for the replication cannot be
         *             created
         */
        public TimeLong(final String id, final long startTime, final long warmupPeriod, final long runLength)
        {
            this(new RunControl.TimeLong(id, startTime, warmupPeriod, runLength));
        }

        /**
         * Construct a stand-alone replication using a RunControl to store the run information.
         * @param runControl RunControlInterface; the run control for the replication
         * @throws NullPointerException when runControl is null
         */
        public TimeLong(final RunControlInterface.TimeLong runControl)
        {
            super(runControl);
        }

        /** {@inheritDoc} */
        @Override
        public RunControlInterface.TimeLong getRunControl()
        {
            return (RunControlInterface.TimeLong) super.getRunControl();
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeDoubleUnit extends SingleReplication<Time, Duration, SimTimeDoubleUnit>
            implements ReplicationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the
         *             warmup time is longer than or equal to the runlength, or when a context for the replication cannot be
         *             created
         */
        public TimeDoubleUnit(final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength)
        {
            this(new RunControl.TimeDoubleUnit(id, startTime, warmupPeriod, runLength));
        }

        /**
         * Construct a stand-alone replication using a RunControl to store the run information.
         * @param runControl RunControlInterface; the run control for the replication
         * @throws NullPointerException when runControl is null
         */
        public TimeDoubleUnit(final RunControlInterface.TimeDoubleUnit runControl)
        {
            super(runControl);
        }

        /** {@inheritDoc} */
        @Override
        public RunControlInterface.TimeDoubleUnit getRunControl()
        {
            return (RunControlInterface.TimeDoubleUnit) super.getRunControl();
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeFloatUnit extends SingleReplication<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements ReplicationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the
         *             warmup time is longer than or equal to the runlength, or when a context for the replication cannot be
         *             created
         */
        public TimeFloatUnit(final String id, final FloatTime startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength)
        {
            this(new RunControl.TimeFloatUnit(id, startTime, warmupPeriod, runLength));
        }

        /**
         * Construct a stand-alone replication using a RunControl to store the run information.
         * @param runControl RunControlInterface; the run control for the replication
         * @throws NullPointerException when runControl is null
         */
        public TimeFloatUnit(final RunControlInterface.TimeFloatUnit runControl)
        {
            super(runControl);
        }

        /** {@inheritDoc} */
        @Override
        public RunControlInterface.TimeFloatUnit getRunControl()
        {
            return (RunControlInterface.TimeFloatUnit) super.getRunControl();
        }

    }

}
