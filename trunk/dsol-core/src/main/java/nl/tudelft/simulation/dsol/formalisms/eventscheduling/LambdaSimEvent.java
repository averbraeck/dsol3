package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;

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
public class LambdaSimEvent<T extends SimTime<?, ?, T>> extends AbstractSimEvent<T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** executable is the lambda expression tghat takes care of the state change. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Executable executable = null;

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime the absolute time the event has to be executed.
     * @param executable the lambda method to invoke
     */
    public LambdaSimEvent(final T executionTime, final Executable executable)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime the time the event has to be executed.
     * @param priority the priority of the event
     * @param executable the lambda method to invoke
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
    @SuppressWarnings("checkstyle:designforextension")
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
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "SimEvent[time=" + this.absoluteExecutionTime + "; priority=" + this.priority + "; executable="
                + this.executable + "]";
    }
}
