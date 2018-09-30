package nl.tudelft.simulation.dsol.animation.D2;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="https://simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version Apr 30, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class Animation
{    
    /**
     * @param frameName the name to use for the frame
     * @param simulator the simulator.
     */
    public Animation(final String frameName, final SimulatorInterface<?, ?, ?> simulator)
    {
        try
        {
            boolean running = false;
            try
            {
                if (simulator.isRunning())
                {
                    running = true;
                    simulator.stop();
                }
            }
            catch (NullPointerException nullPointerException)
            {
                // This was meant to happen
                nullPointerException = null;
            }
            new AnimationFrame(frameName, simulator);
            if (running)
            {
                simulator.start();
            }
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "actionPerformed");
        }
    }

}
