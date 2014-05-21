/*
 * @(#)AnimatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * The reference implementation of the DEVDESS simulator.
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
public class DEVDESSSimulator extends DEVSSimulator implements DEVDESSSimulatorInterface
{

    /** timeStep represents the DESS timeStep */
    protected double timeStep = DEFAULT_TIME_STEP;

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #getTimeStep()
     */
    public double getTimeStep()
    {
        return this.timeStep;
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
                throw new IllegalArgumentException("timeStep < 0 ?");
            }
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
            Logger.finer(this, "setTimeStep", timeStep + "");
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulator#run()
     */
    @Override
    public void run()
    {
        while (this.isRunning() && !this.eventList.isEmpty()
                && this.simulatorTime <= this.replication.getTreatment().getRunLength())
        {
            synchronized (super.semaphore)
            {
                double runUntil = this.simulatorTime + this.timeStep;
                while (!this.eventList.isEmpty() && this.running
                        && runUntil >= (this.eventList.first()).getAbsoluteExecutionTime())
                {
                    SimEventInterface event = this.eventList.removeFirst();
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
                    try
                    {
                        event.execute();
                    }
                    catch (Exception exception)
                    {
                        Logger.severe(this, "run", exception);
                    }
                }
                if (this.running)
                {
                    this.simulatorTime = runUntil;
                }
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            }
        }
    }
}