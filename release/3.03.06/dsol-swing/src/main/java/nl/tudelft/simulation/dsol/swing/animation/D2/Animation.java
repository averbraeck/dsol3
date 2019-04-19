package nl.tudelft.simulation.dsol.swing.animation.D2;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class Animation
{
    /**
     * @param frameName String; the name to use for the frame
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator.
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
