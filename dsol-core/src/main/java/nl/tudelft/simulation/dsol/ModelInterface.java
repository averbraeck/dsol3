/*
 * @(#)ModelInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology .
 */
package nl.tudelft.simulation.dsol;

import java.io.Serializable;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The model interface defines the model object. Since version 2.1.0 of DSOL, the ModelInterface now knows its simulator
 * and can return it to anyone interested. Through the Simulator, the Replication can be requested and through that the
 * Experiment and the Treatment under which the simulation is running.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:43 $
 * @since 1.5
 */
public interface ModelInterface extends Serializable
{
    /**
     * construct a model on a simulator
     * @param simulator is the simulator
     * @throws SimRuntimeException on model failure
     * @throws RemoteException on network failure
     */
    void constructModel(SimulatorInterface simulator) throws SimRuntimeException, RemoteException;

    /**
     * @return the simulator for the model
     * @throws RemoteException on network failure
     */
    SimulatorInterface getSimulator() throws RemoteException;;
}