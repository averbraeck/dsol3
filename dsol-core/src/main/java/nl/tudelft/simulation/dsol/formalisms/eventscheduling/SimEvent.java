package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.reflection.ClassUtil;

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
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class SimEvent<T extends SimTime<?, ?, T>> extends AbstractSimEvent<T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** source the source that created the simevent. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object source = null;

    /** target the target on which a state change is scheduled. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object target = null;

    /** method is the method which embodies the state change. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String methodName = null;

    /** args are the arguments that are used to invoke the method with. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object[] args = null;

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime T; the absolute time the event has to be executed.
     * @param source Object; the source that created the method
     * @param target Object; the object on which the method must be invoked.
     * @param method String; the method to invoke
     * @param args Object[]; the arguments the method to invoke with
     */
    public SimEvent(final T executionTime, final Object source, final Object target, final String method, final Object[] args)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime T; the time the event has to be executed.
     * @param priority short; the priority of the event
     * @param source Object; the source that created the method
     * @param target Object; the object on which the method must be invoked.
     * @param method String; the method to invoke
     * @param args Object[]; the arguments the method to invoke with
     */
    public SimEvent(final T executionTime, final short priority, final Object source, final Object target, final String method,
            final Object[] args)
    {
        super(executionTime, priority);
        if (source == null || target == null || method == null)
        {
            throw new IllegalArgumentException("either source, target or method==null");
        }
        this.source = source;
        this.target = target;
        this.methodName = method;
        this.args = args;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void execute() throws SimRuntimeException
    {
        try
        {
            if (this.methodName.equals("<init>"))
            {
                if (!(this.target instanceof Class))
                {
                    throw new SimRuntimeException("Invoking a constructor implies that target should be instance of Class");
                }
                Constructor<?> constructor = ClassUtil.resolveConstructor((Class<?>) this.target, this.args);
                if (!ClassUtil.isVisible(constructor, this.source.getClass()))
                {
                    throw new SimRuntimeException(
                            printTarget() + "." + this.methodName + " is not accessible for " + this.source);
                }
                constructor.setAccessible(true);
                constructor.newInstance(this.args);
            }
            else
            {
                Method method = ClassUtil.resolveMethod(this.target, this.methodName, this.args);
                if (!ClassUtil.isVisible(method, this.source.getClass()))
                {
                    throw new SimRuntimeException(
                            printTarget() + "." + this.methodName + " is not accessible for " + this.source);
                }
                method.setAccessible(true);
                method.invoke(this.target, this.args);
            }
        }
        catch (Exception exception)
        {
            System.err.println(exception.toString() + " calling " + printTarget() + "." + this.methodName + " with arguments "
                    + printArgs());
            throw new SimRuntimeException(exception);
        }
    }

    /**
     * @return Returns the args.
     */
    public Object[] getArgs()
    {
        return this.args;
    }

    /**
     * @return Returns the method.
     */
    public String getMethod()
    {
        return this.methodName;
    }

    /**
     * @return Returns the source.
     */
    public Object getSource()
    {
        return this.source;
    }

    /**
     * @return Returns the target.
     */
    public Object getTarget()
    {
        return this.target;
    }

    /**
     * Retrieve the target in a human readable way.
     * @return String; the target in a human readable way
     */
    public String printTarget()
    {
        String s = "{" + this.target.getClass().getSimpleName() + ":";
        try
        {
            s += this.target.toString() + "}";
        }
        catch (Exception e)
        {
            s += "Error in toString()}";
        }
        return s;
    }

    /**
     * Retrieve the arguments in a human readable way.
     * @return String; the arguments in a human readable way
     */
    public String printArgs()
    {
        if (getArgs() == null)
        {
            return "null";
        }
        List<String> argsList = new ArrayList<>();
        for (int i = 0; i < this.getArgs().length; i++)
        {
            String s = "{" + this.getArgs()[i].getClass().getSimpleName() + ":";
            try
            {
                s += this.getArgs()[i].toString() + "}";
            }
            catch (Exception e)
            {
                s += "Error in toString()}";
            }
            argsList.add(s);
        }
        return argsList.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SimEvent[time=" + this.absoluteExecutionTime + "; priority=" + this.priority + "; source=" + this.source
                + "; target=" + printTarget() + "; method=" + this.methodName + "; args=" + printArgs() + "]";
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class SimEvent.TimeDouble. */
    public static class TimeDouble extends SimEvent<SimTimeDouble> implements SimEventInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Double; the absolute time the event has to be executed.
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeDouble(final Double executionTime, final Object source, final Object target, final String method,
                final Object[] args)
        {
            super(new SimTimeDouble(executionTime), source, target, method, args);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Double; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeDouble(final Double executionTime, final short priority, final Object source, final Object target,
                final String method, final Object[] args)
        {
            super(new SimTimeDouble(executionTime), priority, source, target, method, args);
        }
    }

    /** Easy access class SimEvent.TimeFloat. */
    public static class TimeFloat extends SimEvent<SimTimeFloat> implements SimEventInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Float; the absolute time the event has to be executed.
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeFloat(final Float executionTime, final Object source, final Object target, final String method,
                final Object[] args)
        {
            super(new SimTimeFloat(executionTime), source, target, method, args);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Float; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeFloat(final Float executionTime, final short priority, final Object source, final Object target,
                final String method, final Object[] args)
        {
            super(new SimTimeFloat(executionTime), priority, source, target, method, args);
        }
    }

    /** Easy access class SimEvent.TimeLong. */
    public static class TimeLong extends SimEvent<SimTimeLong> implements SimEventInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Long; the absolute time the event has to be executed.
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeLong(final Long executionTime, final Object source, final Object target, final String method,
                final Object[] args)
        {
            super(new SimTimeLong(executionTime), source, target, method, args);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Long; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeLong(final Long executionTime, final short priority, final Object source, final Object target,
                final String method, final Object[] args)
        {
            super(new SimTimeLong(executionTime), priority, source, target, method, args);
        }
    }

    /** Easy access class SimEvent.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends SimEvent<SimTimeDoubleUnit> implements SimEventInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Time; the absolute time the event has to be executed.
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeDoubleUnit(final Time executionTime, final Object source, final Object target, final String method,
                final Object[] args)
        {
            super(new SimTimeDoubleUnit(executionTime), source, target, method, args);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime Time; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeDoubleUnit(final Time executionTime, final short priority, final Object source, final Object target,
                final String method, final Object[] args)
        {
            super(new SimTimeDoubleUnit(executionTime), priority, source, target, method, args);
        }
    }

    /** Easy access class SimEvent.TimeFloatUnit. */
    public static class TimeFloatUnit extends SimEvent<SimTimeFloatUnit> implements SimEventInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime FloatTime; the absolute time the event has to be executed.
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeFloatUnit(final FloatTime executionTime, final Object source, final Object target, final String method,
                final Object[] args)
        {
            super(new SimTimeFloatUnit(executionTime), source, target, method, args);
        }

        /**
         * The constructor of the event stores the time the event must be executed and the object and method to invoke.
         * @param executionTime FloatTime; the time the event has to be executed.
         * @param priority short; the priority of the event
         * @param source Object; the source that created the method
         * @param target Object; the object on which the method must be invoked.
         * @param method String; the method to invoke
         * @param args Object[]; the arguments the method to invoke with
         */
        public TimeFloatUnit(final FloatTime executionTime, final short priority, final Object source, final Object target,
                final String method, final Object[] args)
        {
            super(new SimTimeFloatUnit(executionTime), priority, source, target, method, args);
        }
    }

}
