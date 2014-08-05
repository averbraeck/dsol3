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
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTime;

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
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class DESSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        Simulator<A, R, T> implements DESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator */
    protected R timeStep;

    /**
     * Construct a DESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep the initial time step to use in the integration.
     */
    public DESSSimulator(final R initialTimeStep)
    {
        super();
        setTimeStep(initialTimeStep);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.Simulator#initialize(nl.tudelft.simulation.dsol.experiment.Replication,
     *      nl.tudelft.simulation.dsol.experiment.ReplicationMode)
     */
    @Override
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws RemoteException, SimRuntimeException
    {
        super.initialize(initReplication, replicationMode);
        this.replication.getTreatment().getExperiment().getModel().constructModel(this);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #getTimeStep()
     */
    public R getTimeStep()
    {
        return this.timeStep;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #setTimeStep(double)
     */
    @Override
    public void setTimeStep(final R timeStep)
    {
        synchronized (super.semaphore)
        {
            // TODO: how to find out that a timestep < 0 and throw an exception if that is the case.
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.Simulator#run()
     */
    @Override
    public void run()
    {
        while (this.simulatorTime.lt(this.replication.getTreatment().getEndTime()) && isRunning())
        {
            synchronized (super.semaphore)
            {
                this.simulatorTime = this.simulatorTime.plus(this.timeStep);
                if (this.simulatorTime.gt(this.replication.getTreatment().getEndTime()))
                {
                    this.simulatorTime = this.replication.getTreatment().getEndTime().copy();
                    this.stop();
                }
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            }
        }
    }

}