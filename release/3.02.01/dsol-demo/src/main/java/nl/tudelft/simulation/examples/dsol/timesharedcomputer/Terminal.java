package nl.tudelft.simulation.examples.dsol.timesharedcomputer;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.flow.Station;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * The Terminal as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4. <br>
 * Copyright (c) 2003-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.1 02.04.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Terminal extends Station<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** SERVICE_TIME is fired on job completion. */
    public static final EventType SERVICE_TIME = new EventType("SERVICE_TIME");

    /** the thinkDelay. */
    private DistContinuous thinkDelay = null;

    /** the jobSize. */
    private DistContinuous jobSize = null;

    /**
     * constructs a new Terminal.
     * @param simulator the simulator
     * @param cpu the destination
     * @param thinkDelay the delay
     * @param jobSize in time
     */
    public Terminal(final DEVSSimulatorInterface.TimeDouble simulator, final StationInterface cpu,
            final DistContinuous thinkDelay, final DistContinuous jobSize)
    {
        super(simulator);
        this.thinkDelay = thinkDelay;
        this.jobSize = jobSize;
        this.setDestination(cpu);
        this.releaseObject(null);
    }

    /** {@inheritDoc} */
    @Override
    public void receiveObject(final Object object)
    {
        this.fireTimedEvent(SERVICE_TIME, this.simulator.getSimulatorTime() - ((Job) object).getCreationTime(),
                this.simulator.getSimulatorTime());
        try
        {
            Object[] args = {object};
            this.simulator.scheduleEventAbs(this.simulator.getSimulatorTime() + this.thinkDelay.draw(), this,
                    this, "releaseObject", args);
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void releaseObject(final Object object)
    {
        Job job = new Job(this.jobSize, this, this.simulator.getSimulatorTime());
        this.fireEvent(StationInterface.RELEASE_EVENT, 1);
        super.destination.receiveObject(job);
    }
}
