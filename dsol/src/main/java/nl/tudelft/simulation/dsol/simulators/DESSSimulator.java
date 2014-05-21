/*
 * @(#)DESSSimulator.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.logger.Logger;

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification.
 * More information on Modeling & Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DESSSimulator extends Simulator implements DESSSimulatorInterface
{

    /** timeStep represents the timestep of the DESS simulator */
    protected double timeStep = DEFAULT_TIME_STEP;

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface
     *      #initialize(nl.tudelft.simulation.dsol.experiment.Replication,short)
     */
    @Override
    public void initialize(final Replication replication, final short replicationMode) throws RemoteException,
            SimRuntimeException
    {
        super.initialize(replication, replicationMode);
        this.replication.getTreatment().getExperiment().getModel().constructModel(this);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #getTimeStep()
     */
    public double getTimeStep()
    {
        return this.timeStep;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.Simulator#run()
     */
    @Override
    public void run()
    {
        while (this.simulatorTime <= this.replication.getTreatment().getRunLength() && isRunning())
        {
            synchronized (super.semaphore)
            {
                this.simulatorTime = this.simulatorTime + this.timeStep;
                if (this.simulatorTime > this.replication.getTreatment().getRunLength())
                {
                    this.simulatorTime = this.replication.getTreatment().getRunLength();
                    this.stop();
                }
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #setTimeStep(double)
     */
    public void setTimeStep(final double timeStep)
    {
        synchronized (super.semaphore)
        {
            if (timeStep < 0)
            {
                throw new IllegalArgumentException("timeStep <0 ?");
            }
            this.timeStep = timeStep;
            Logger.finer(this, "setTimeStep", "set the timeStep to " + timeStep);
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
    }

}