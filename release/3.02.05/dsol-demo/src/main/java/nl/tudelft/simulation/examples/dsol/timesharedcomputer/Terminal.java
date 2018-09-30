package nl.tudelft.simulation.examples.dsol.timesharedcomputer;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.flow.Station;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * The Terminal as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4..
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
            this.simulator.scheduleEventAbs(this.simulator.getSimulatorTime() + this.thinkDelay.draw(), this, this,
                    "releaseObject", args);
        }
        catch (SimRuntimeException exception)
        {
            SimLogger.always().error(exception);
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
