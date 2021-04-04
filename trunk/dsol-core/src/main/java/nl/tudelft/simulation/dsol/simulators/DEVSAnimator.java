package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The reference implementation of the animator.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class DEVSAnimator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVSSimulator<A, R, T> implements AnimatorInterface
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /**
     * Create a new DEVSAnimator.
     * @param id the id of the simulator, used in logging and firing of events.
     */
    public DEVSAnimator(final Serializable id)
    {
        super(id);
    }

    /** AnimationDelay refers to the delay in milliseconds between timeSteps. */
    private long animationDelay = AnimatorInterface.DEFAULT_ANIMATION_DELAY;

    /** {@inheritDoc} */
    @Override
    public final long getAnimationDelay()
    {
        return this.animationDelay;
    }

    /** {@inheritDoc} */
    @Override
    public final void setAnimationDelay(final long animationDelay)
    {
        this.animationDelay = animationDelay;
        this.fireEvent(ANIMATION_DELAY_CHANGED_EVENT, animationDelay);
    }

    /** {@inheritDoc} */
    @Override
    public final void updateAnimation()
    {
        this.fireTimedEvent(AnimatorInterface.UPDATE_ANIMATION_EVENT, null, this.simulatorTime);
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        AnimationThread animationThread = new AnimationThread(this);
        animationThread.start();
        while (!isStoppingOrStopped() && !this.eventList.isEmpty() && this.simulatorTime.le(this.replication.getEndSimTime()))
        {
            while (!this.eventList.isEmpty() && !isStoppingOrStopped())
            {
                synchronized (super.semaphore)
                {
                    int cmp = this.eventList.first().getAbsoluteExecutionTime().compareTo(this.runUntilTime);
                    if ((cmp == 0 && !this.runUntilIncluding) || cmp > 0)
                    {
                        this.simulatorTime.set(this.runUntilTime.get());
                        this.runState = RunState.STOPPING;
                        break;
                    }

                    SimEventInterface<T> event = this.eventList.removeFirst();
                    if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                    {
                        super.fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null,
                                event.getAbsoluteExecutionTime().get());
                    }
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    try
                    {
                        event.execute();
                        if (this.eventList.isEmpty())
                        {
                            this.simulatorTime.set(this.runUntilTime.get());
                            this.runState = RunState.STOPPING;
                            break;
                        }
                    }
                    catch (Exception exception)
                    {
                        getLogger().always().error(exception);
                        if (this.isPauseOnError())
                        {
                            try
                            {
                                this.runState = RunState.STOPPING;
                                this.stop();
                            }
                            catch (SimRuntimeException stopException)
                            {
                                getLogger().always().error(stopException);
                            }
                        }
                    }
                }
            }
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, this.simulatorTime.get());
        }
        updateAnimation();
        animationThread.stopAnimation();
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Animator.TimeDouble. */
    public static class TimeDouble extends DEVSAnimator<Double, Double, SimTimeDouble>
            implements DEVSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSAnimator.TimeDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeDouble getReplication()
        {
            return (ReplicationInterface.TimeDouble) super.getReplication();
        }
    }

    /** Easy access class Animator.TimeFloat. */
    public static class TimeFloat extends DEVSAnimator<Float, Float, SimTimeFloat> implements DEVSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSAnimator.TimeFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeFloat getReplication()
        {
            return (ReplicationInterface.TimeFloat) super.getReplication();
        }
    }

    /** Easy access class Animator.TimeLong. */
    public static class TimeLong extends DEVSAnimator<Long, Long, SimTimeLong> implements DEVSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSAnimator.TimeLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeLong getReplication()
        {
            return (ReplicationInterface.TimeLong) super.getReplication();
        }
    }

    /** Easy access class Animator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVSAnimator<Time, Duration, SimTimeDoubleUnit>
            implements DEVSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSAnimator.TimeDoubleUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDoubleUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeDoubleUnit getReplication()
        {
            return (ReplicationInterface.TimeDoubleUnit) super.getReplication();
        }
    }

    /** Easy access class Animator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVSAnimator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSAnimator.TimeFloatUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloatUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeFloatUnit getReplication()
        {
            return (ReplicationInterface.TimeFloatUnit) super.getReplication();
        }
    }

}
