/*
 * @(#) DEVSTestModel.java Sep 5, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.logger.Logger;

/**
 * The DEVSSimulatorTestmodel specifies the model <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DEVSTestModel extends TestModel
{
    /**
     * simulator refers to the target
     */
    protected DEVSSimulatorInterface simulator;

    /**
     * constructs a new DEVSTestModel
     */
    public DEVSTestModel()
    {
        super();
    }

    /**
     * @see nl.tudelft.simulation.dsol.ModelInterface #constructModel(SimulatorInterface)
     */
    @Override
    public void constructModel(final SimulatorInterface simulator) throws RemoteException
    {
        super.constructModel(simulator);
        this.simulator = (DEVSSimulator) simulator;
        for (int i = 0; i < 100; i++)
        {
            try
            {
                this.simulator.scheduleEvent(new SimEvent(Math.random() * i, this, this, "run", null));
            }
            catch (Exception exception)
            {
                Logger.warning(this, "constructModel", exception);
            }
        }
    }

    /**
     * the method which is scheduled
     */
    public void run()
    {
        // Testing method
    }
}