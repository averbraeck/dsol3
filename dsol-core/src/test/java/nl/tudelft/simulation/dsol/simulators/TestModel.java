package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The TestModel.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class TestModel extends AbstractDSOLModel.TimeDouble<SimulatorInterface.TimeDouble> implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the startTime. */
    private long startTime = 0L;

    /**
     * constructs a new TestModel.
     * @param simulator the simulator
     */
    public TestModel(final SimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel()
    {
        try
        {
            getSimulator().addListener(this, SimulatorInterface.END_REPLICATION_EVENT, false);
            getSimulator().addListener(this, SimulatorInterface.START_REPLICATION_EVENT, false);
            getSimulator().addListener(this, SimulatorInterface.START_EVENT, false);
            getSimulator().addListener(this, SimulatorInterface.STOP_EVENT, false);
            getSimulator().addListener(this, SimulatorInterface.STEP_EVENT, false);
            getSimulator().addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, false);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event.getType().equals(SimulatorInterface.START_EVENT))
        {
            this.startTime = System.currentTimeMillis();
            System.out.println("started @ " + this.startTime);
        }
        if (event.getType().equals(SimulatorInterface.STOP_EVENT))
        {
            long runLength = System.currentTimeMillis() - this.startTime;
            System.out
                    .println("runlength=" + runLength + " time=" + ((SimulatorInterface) event.getSource()).getSimulatorTime());
        }
    }
}
