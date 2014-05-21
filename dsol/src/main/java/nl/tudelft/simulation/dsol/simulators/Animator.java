/*
 * @(#) Animator.java Sep 6, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * The reference implementation of the animator.
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
public class Animator extends DEVDESSSimulator implements AnimatorInterface
{

    /** AnimationDelay refers to the delay in miliseconds between timeSteps */
    protected long animationDelay = 100L;

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #getAnimationDelay()
     */
    public long getAnimationDelay()
    {
        return this.animationDelay;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #setAnimationDelay(long)
     */
    public void setAnimationDelay(final long animationDelay)
    {
        this.animationDelay = animationDelay;
        Logger.finer(this, "setAnimationDelay", "set the animationDelay to " + animationDelay);
        this.fireEvent(ANIMATION_DELAY_CHANGED_EVENT, animationDelay);
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
            try
            {
                if (this.animationDelay > 0)
                {
                    Thread.sleep(this.animationDelay);
                }
            }
            catch (Exception exception)
            {
                exception = null;
                // Let's neglect this sleep...
            }
            double runUntil = this.simulatorTime + this.timeStep;
            while (!this.eventList.isEmpty() && this.running
                    && runUntil >= this.eventList.first().getAbsoluteExecutionTime())
            {
                synchronized (super.semaphore)
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
            }
            if (this.running)
            {
                this.simulatorTime = runUntil;
            }
            this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            this.fireEvent(AnimatorInterface.UPDATE_ANIMATION_EVENT, this.simulatorTime);
        }
    }
}