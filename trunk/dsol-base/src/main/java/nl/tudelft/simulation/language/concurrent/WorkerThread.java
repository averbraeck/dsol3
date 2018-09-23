package nl.tudelft.simulation.language.concurrent;

import java.util.logging.Logger;

import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * The WorkerThread is a working thread. The thread sleeps while not interrupted. If interrupted the job.run operation
 * is invoked.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Oct 17, 2009
 * @author <a href="mailto:phmjacobs@hotmail.com">Peter H.M. Jacobs</a>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */

public class WorkerThread extends Thread
{
    /** the job to execute. */
    private Runnable job = null;

    /** finalized. */
    private boolean finalized = false;

    /**
     * constructs a new SimulatorRunThread.
     * @param name the name of the thread
     * @param job the job to run
     */
    public WorkerThread(final String name, final Runnable job)
    {
        super(name);
        this.job = job;
        this.setDaemon(false);
        this.setPriority(Thread.NORM_PRIORITY);
        this.start();
    }

    /** 
     * Clean up the worker thread.
     * synchronized method, otherwise it does not own the Monitor on the wait. 
     */
    public final synchronized void cleanUp()
    {
        this.finalized = true;
        if (!this.isInterrupted())
        {
            this.notify(); // in case it is in the 'wait' state
        }
        this.job = null;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void run()
    {
        while (!this.finalized) // always until finalized
        {
            try
            {
                this.wait(); // as long as possible
            }
            catch (InterruptedException interruptedException)
            {
                if (!this.finalized)
                {
                    this.interrupt(); // set the status to interrupted
                    try
                    {
                        this.job.run();
                    }
                    catch (Exception exception)
                    {
                        CategoryLogger.always().error(exception);
                    }
                    Thread.interrupted();
                }
            }
        }
    }
}
