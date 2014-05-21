/*
 * @(#)DESSSimulatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.event.EventType;

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification.
 * More information on Modeling & Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al.
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

public interface DESSSimulatorInterface extends SimulatorInterface
{
    /** TIME_STEP_CHANGED_EVENT is fired when the time step is set */
    EventType TIME_STEP_CHANGED_EVENT = new EventType("TIME_STEP_CHANGED_EVENT");

    /** DEFAULT_TIME_STEP represents the default timestep for the simulator */
    double DEFAULT_TIME_STEP = 0.1;

    /**
     * returns the time step of the DESS simulator
     * @return the timeStep
     * @throws RemoteException on network failure
     */
    double getTimeStep() throws RemoteException;

    /**
     * Method setTimeStep sets the time step of the simulator
     * @param timeStep the new timeStep. Its value should be >0.0
     * @throws RemoteException on network failure
     */
    void setTimeStep(double timeStep) throws RemoteException;
}