package nl.tudelft.simulation.dsol.logger;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * DefaultSimTimeFormatter.java. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DefaultSimTimeFormatter implements SimTimeFormatter
{
    /** The simulator of which to include the time in the log messages. */
    private SimulatorInterface<?, ?, ?> simulator;

    /**
     * Create a default simulation time formatter with a simulator.
     * @param simulator the simulator to use for the formatting
     */
    public DefaultSimTimeFormatter(final SimulatorInterface<?, ?, ?> simulator)
    {
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    public void setSimulator(final SimulatorInterface<?, ?, ?> simulator)
    {
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    public String format(final String message)
    {
        if (this.simulator == null)
        {
            return message;
        }
        return "[T=" + this.simulator.getSimulatorTime() + "] " + message;
    }

}
