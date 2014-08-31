package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.language.reflection.ClassUtil;

/**
 * The SimEvent forms the essential scheduling mechanism for D-SOL. Objects do not invoke methods directly on eachother;
 * they bundle the object on which the method is planned to be invoked together with the arguments and the name of the
 * method in a simEvent. The SimEvent is then stored in the eventList and executed.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:36:43 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class SimEvent<T extends SimTime<?, ?, T>> extends AbstractSimEvent<T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** source reflects the source that created the simevent. */
    protected Object source = null;

    /** target reflects the target on which a state change is scheduled. */
    protected Object target = null;

    /** method is the method which embodies the state change. */
    protected String methodName = null;

    /** args are the arguments which are used to invoke the method with. */
    protected Object[] args = null;

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke
     * @param executionTime reflects the time the event has to be executed.
     * @param source reflects the source that created the method
     * @param target reflects the object on which the method must be invoked.
     * @param method reflects the method to invoke
     * @param args reflects the argumenst the method to invoke with
     */
    public SimEvent(final T executionTime, final Object source, final Object target, final String method,
            final Object[] args)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke
     * @param executionTime reflects the time the event has to be executed.
     * @param priority reflects the priority of the event
     * @param source reflects the source that created the method
     * @param target reflects the object on which the method must be invoked.
     * @param method reflects the method to invoke
     * @param args reflects the argumenst the method to invoke with
     */
    public SimEvent(final T executionTime, final short priority, final Object source, final Object target,
            final String method, final Object[] args)
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
                    throw new SimRuntimeException(
                            "Invoking a constructor implies that target should be instance of Class");
                }
                Constructor<?> constructor = ClassUtil.resolveConstructor((Class<?>) this.target, this.args);
                if (!ClassUtil.isVisible(constructor, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.methodName + " is not accessible for " + this.source);
                }
                constructor.setAccessible(true);
                constructor.newInstance(this.args);
            }
            else
            {
                Method method = ClassUtil.resolveMethod(this.target, this.methodName, this.args);
                if (!ClassUtil.isVisible(method, this.source.getClass()))
                {
                    throw new SimRuntimeException(this.methodName + " is not accessible for " + this.source);
                }
                method.setAccessible(true);
                method.invoke(this.target, this.args);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SimEvent[time=" + this.absoluteExecutionTime + "; priority=" + this.priority + "; source="
                + this.source + "; target=" + this.target + "; method=" + this.methodName + "; args=" + this.args + "]";
    }
}
