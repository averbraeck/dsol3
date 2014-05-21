/*
 * @(#)Station.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;

/**
 * A station is an object which can accept other objects.
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
public abstract class Station extends EventProducer implements StationInterface
{

    /**
     * simulator is the simulator on which behavior is scheduled
     */
    protected DEVSSimulatorInterface simulator;

    /**
     * destination refers to the next station in the process-model chain
     */
    protected StationInterface destination;

    /**
     * constructs a new Station.
     * @param simulator is the simulator on which behavior is scheduled
     */
    public Station(final DEVSSimulatorInterface simulator)
    {
        super();
        this.simulator = simulator;
    }

    /**
     * @see StationInterface#receiveObject(Object)
     */
    public void receiveObject(final Object object) throws RemoteException
    {
        this.fireEvent(StationInterface.RECEIVE_EVENT, 1.0, this.simulator.getSimulatorTime());
    }

    /**
     * @see StationInterface#setDestination(StationInterface)
     */
    public void setDestination(final StationInterface destination)
    {
        this.destination = destination;
    }

    /**
     * releases an object
     * @param object is the entity
     * @throws RemoteException on network failure
     */
    protected synchronized void releaseObject(final Object object) throws RemoteException
    {
        this.fireEvent(StationInterface.RELEASE_EVENT, 0.0, this.simulator.getSimulatorTime());
        if (this.destination != null)
        {
            this.destination.receiveObject(object);
        }
    }

    /**
     * Returns the destination.
     * @return the destination station
     */
    public StationInterface getDestination()
    {
        return this.destination;
    }

}