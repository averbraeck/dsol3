package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.logger.Logger;

/**
 * The schedule is an extension to the generate which accepts a schedule of interarrival times. Instead of generating
 * with a continuous interarrival distribution we submit a map consiting of keys (execution times). Each key indicates
 * the <i>starting time </i> of a new interval, while the value in the map is the continuous distribution function to
 * use to draw the interarrival times. If no values have to be generated in a certain interval, use a large interarrival
 * time value in the distribution function, or use DistConstant(stream, 1E20) to indicate that the next drawing will
 * take place <i>after </i> the end of the interval. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class Schedule extends Generator
{
    /**
     * schedule is a time sorted map of distributions
     */
    private SortedMap<Double, DistContinuous> schedule = Collections
            .synchronizedSortedMap(new TreeMap<Double, DistContinuous>());

    /**
     * constructs a new Schedule
     * @param simulator is the on which the construction of the objects must be scheduled.
     * @param myClass is the class of which entities are created
     * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
     *            <code>constructorArgument[n]=new Integer(12)</code> may have constructorArgumentClasses[n]=int.class;
     * @throws SimRuntimeException on constructor invokation.
     */
    public Schedule(final DEVSSimulatorInterface simulator, final Class myClass, final Object[] constructorArguments)
            throws SimRuntimeException
    {
        super(simulator, myClass, constructorArguments);
    }

    /**
     * returns the schedule
     * @return SortedMap the schedule
     */
    public SortedMap<Double, DistContinuous> getSchedule()
    {
        return this.schedule;
    }

    /**
     * sets the schedule
     * @param map is the new map
     */
    public synchronized void setSchedule(final SortedMap<Double, DistContinuous> map)
    {
        this.schedule = map;
        this.changeIntervalTime();
    }

    /**
     * changes the intervalTime of the schedule
     */
    public synchronized void changeIntervalTime()
    {
        try
        {
            if (!this.schedule.isEmpty())
            {
                this.simulator.cancelEvent(super.nextEvent);
                this.interval = this.schedule.values().iterator().next();
                this.schedule.remove(this.schedule.firstKey());
                this.simulator.scheduleEvent(new SimEvent(this.schedule.firstKey().doubleValue(), this, this,
                        "changeIntervalTime", null));
                this.generate(this.constructorArguments);
                Logger.finest(this, "changeIntervalTime", "set the intervalTime to " + this.interval);
            }
        }
        catch (Exception exception)
        {
            Logger.warning(this, "changeIntervalTime", exception);
        }
    }
}