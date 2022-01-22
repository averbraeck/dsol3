package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The SimEvent forms the essential scheduling mechanism for D-SOL. Objects do not invoke methods directly on eachother; they
 * bundle the object on which the method is planned to be invoked together with the arguments and the name of the method in a
 * simEvent. The SimEvent is then stored in the eventList and executed.
 * <p>
 * Copyright (c) 2016-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class LambdaSimEvent<T extends SimTime<?, ?, T>> extends AbstractSimEvent<T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** executable is the lambda expression tghat takes care of the state change. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Executable executable = null;

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime T; the absolute time the event has to be executed.
     * @param executable Executable; the lambda method to invoke
     */
    public LambdaSimEvent(final T executionTime, final Executable executable)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime T; the time the event has to be executed.
     * @param priority short; the priority of the event
     * @param executable Executable; the lambda method to invoke
     */
    public LambdaSimEvent(final T executionTime, final short priority, final Executable executable)
    {
        super(executionTime, priority);
        if (executable == null)
        {
            throw new IllegalArgumentException("executable==null");
        }
        this.executable = executable;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void execute() throws SimRuntimeException
    {
        try
        {
            this.executable.execute();
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SimEvent[time=" + this.absoluteExecutionTime + "; priority=" + this.priority + "; executable=" + this.executable
                + "]";
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class SimEvent.TimeDouble. */
    public static class TimeDouble extends LambdaSimEvent<SimTimeDouble> implements SimEventInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Double; the time the event has to be executed.
         * @param executable Executable; the lambda method to invoke
         */
        public TimeDouble(final Double executionTime, final Executable executable)
        {
            super(new SimTimeDouble(executionTime), executable);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Double; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param executable Executable; the lambda method to invoke
         */
        public TimeDouble(final Double executionTime, final short priority, final Executable executable)
        {
            super(new SimTimeDouble(executionTime), priority, executable);
        }
    }

    /** Easy access class SimEvent.TimeFloat. */
    public static class TimeFloat extends LambdaSimEvent<SimTimeFloat> implements SimEventInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Float; the time the event has to be executed.
         * @param executable Executable; the lambda method to invoke
         */
        public TimeFloat(final Float executionTime, final Executable executable)
        {
            super(new SimTimeFloat(executionTime), executable);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Float; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param executable Executable; the lambda method to invoke
         */
        public TimeFloat(final Float executionTime, final short priority, final Executable executable)
        {
            super(new SimTimeFloat(executionTime), priority, executable);
        }
    }

    /** Easy access class SimEvent.TimeLong. */
    public static class TimeLong extends LambdaSimEvent<SimTimeLong> implements SimEventInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Long; the time the event has to be executed.
         * @param executable Executable; the lambda method to invoke
         */
        public TimeLong(final Long executionTime, final Executable executable)
        {
            super(new SimTimeLong(executionTime), executable);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Long; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param executable Executable; the lambda method to invoke
         */
        public TimeLong(final Long executionTime, final short priority, final Executable executable)
        {
            super(new SimTimeLong(executionTime), priority, executable);
        }
    }

    /** Easy access class SimEvent.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends LambdaSimEvent<SimTimeDoubleUnit> implements SimEventInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Time; the time the event has to be executed.
         * @param executable Executable; the lambda method to invoke
         */
        public TimeDoubleUnit(final Time executionTime, final Executable executable)
        {
            super(new SimTimeDoubleUnit(executionTime), executable);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Time; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param executable Executable; the lambda method to invoke
         */
        public TimeDoubleUnit(final Time executionTime, final short priority, final Executable executable)
        {
            super(new SimTimeDoubleUnit(executionTime), priority, executable);
        }
    }

    /** Easy access class SimEvent.TimeFloatUnit. */
    public static class TimeFloatUnit extends LambdaSimEvent<SimTimeFloatUnit> implements SimEventInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime FloatTime; the time the event has to be executed.
         * @param executable Executable; the lambda method to invoke
         */
        public TimeFloatUnit(final FloatTime executionTime, final Executable executable)
        {
            super(new SimTimeFloatUnit(executionTime), executable);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime FloatTime; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param executable Executable; the lambda method to invoke
         */
        public TimeFloatUnit(final FloatTime executionTime, final short priority, final Executable executable)
        {
            super(new SimTimeFloatUnit(executionTime), priority, executable);
        }
    }

}
