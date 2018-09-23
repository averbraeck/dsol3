package nl.tudelft.simulation.dsol.logger;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * SimTimeFormatter formats the message to include the simulation time. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public interface SimTimeFormatter
{
    /**
     * Format the message to include the simulation time.
     * @param message the message to format
     * @return the formatted message.
     */
    String format(String message);

    /**
     * Set the simulator used (can be null).
     * @param simulator set the simulator
     */
    void setSimulator(SimulatorInterface<?, ?, ?> simulator);
}
