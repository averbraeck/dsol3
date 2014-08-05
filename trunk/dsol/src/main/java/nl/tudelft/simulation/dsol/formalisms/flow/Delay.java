/*
 * @(#)Delay.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Delay object is a station which delays an entity by some time units. When an entity arrives at a delay object,
 * dsol delays the entity by the resulting time period. During the time delay, the entity is held in the delay object.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */
public class Delay extends Station
{
    /** */
    private static final long serialVersionUID = 20140805L;
    
    /**
     * delayDistribution which is the distribution defining the delay
     */
    protected DistContinuous delayDistribution;

    /**
     * Constructor for Delay.
     * @param simulator is the simulator
     * @param delayDistribution is the delayDistribution
     */
    public Delay(final DEVSSimulatorInterface<?, ?, ?> simulator, final DistContinuous delayDistribution)
    {
        super(simulator);
        this.delayDistribution = delayDistribution;
    }

    /**
     * @see StationInterface#receiveObject(Object)
     */
    @Override
    public synchronized void receiveObject(final Object object) throws RemoteException
    {
        super.receiveObject(object);
        try
        {
            this.simulator.scheduleEvent(this.delayDistribution.draw(), this, this, "releaseObject",
                    new Object[]{object});
        }
        catch (Exception exception)
        {
            Logger.warning(this, "receiveObject", exception);
        }
    }
}