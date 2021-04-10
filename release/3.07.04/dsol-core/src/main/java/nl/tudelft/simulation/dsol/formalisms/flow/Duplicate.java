package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.rmi.MarshalledObject;

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
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A duplicate station duplicates incoming objects and sends them to their alternative destination.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Duplicate<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** DuplicateDestination which is the duplicate definition. */
    private StationInterface<A, R, T> duplicateDestination;

    /** numberCopies refers to the number of duplicates. */
    private int numberCopies;

    /**
     * Creates a new Duplicate that makes 1 copy.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param duplicateDestination StationInterface&lt;A,R,T&gt;; the duplicate destination
     */
    public Duplicate(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator,
            final StationInterface<A, R, T> duplicateDestination)
    {
        this(id, simulator, duplicateDestination, 1);
    }

    /**
     * Create a new Duplicate that makes numberCopies copies.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param duplicateDestination StationInterface&lt;A,R,T&gt;; which is the duplicate definition
     * @param numberCopies int; the number of copies
     */
    public Duplicate(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator,
            final StationInterface<A, R, T> duplicateDestination, final int numberCopies)
    {
        super(id, simulator);
        this.duplicateDestination = duplicateDestination;
        this.numberCopies = numberCopies;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.releaseObject(object);
            if (object instanceof Serializable)
            {
                for (int i = 0; i < this.numberCopies; i++)
                {
                    Object clone = new MarshalledObject<Object>(object).get();
                    this.fireTimedEvent(StationInterface.RELEASE_EVENT, 1, getSimulator().getSimulatorTime());
                    this.duplicateDestination.receiveObject(clone);
                }
            }
            else
            {
                throw new Exception(
                        "cannot duplicate object: " + object.getClass() + " does not implement java.io.Serializable");
            }
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "receiveMethod");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Duplicate.TimeDouble. */
    public static class TimeDouble extends Duplicate<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param duplicateDestination StationInterface.TimeDouble; the duplicate destination
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final StationInterface.TimeDouble duplicateDestination)
        {
            super(id, simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param duplicateDestination StationInterface.TimeDouble; which is the duplicate definition
         * @param numberCopies int; the number of copies
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final StationInterface.TimeDouble duplicateDestination, final int numberCopies)
        {
            super(id, simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeFloat. */
    public static class TimeFloat extends Duplicate<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param duplicateDestination StationInterface.TimeFloat; the duplicate destination
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final StationInterface.TimeFloat duplicateDestination)
        {
            super(id, simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param duplicateDestination StationInterface.TimeFloat; which is the duplicate definition
         * @param numberCopies int; the number of copies
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final StationInterface.TimeFloat duplicateDestination, final int numberCopies)
        {
            super(id, simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeLong. */
    public static class TimeLong extends Duplicate<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param duplicateDestination StationInterface.TimeLong; the duplicate destination
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final StationInterface.TimeLong duplicateDestination)
        {
            super(id, simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param duplicateDestination StationInterface.TimeLong; which is the duplicate definition
         * @param numberCopies int; the number of copies
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final StationInterface.TimeLong duplicateDestination, final int numberCopies)
        {
            super(id, simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Duplicate<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param duplicateDestination StationInterface.TimeDoubleUnit; the duplicate destination
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final StationInterface.TimeDoubleUnit duplicateDestination)
        {
            super(id, simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param duplicateDestination StationInterface.TimeDoubleUnit; which is the duplicate definition
         * @param numberCopies int; the number of copies
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final StationInterface.TimeDoubleUnit duplicateDestination, final int numberCopies)
        {
            super(id, simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeFloatUnit. */
    public static class TimeFloatUnit extends Duplicate<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param duplicateDestination StationInterface.TimeFloatUnit; the duplicate destination
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final StationInterface.TimeFloatUnit duplicateDestination)
        {
            super(id, simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param duplicateDestination StationInterface.TimeFloatUnit; which is the duplicate definition
         * @param numberCopies int; the number of copies
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final StationInterface.TimeFloatUnit duplicateDestination, final int numberCopies)
        {
            super(id, simulator, duplicateDestination, numberCopies);
        }
    }

}
