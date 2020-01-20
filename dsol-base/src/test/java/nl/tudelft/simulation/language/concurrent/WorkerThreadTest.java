package nl.tudelft.simulation.language.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * WorkerThreadTest.java.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class WorkerThreadTest
{
    /** test value that will be incremented by a worker thread. */
    protected int value = 0;

    /**
     * Test the WorkerThread.
     */
    @Test
    public void testWorkerThread()
    {
        this.value = 0;
        WorkerThread wt = new WorkerThread("job", new Job());
        assertTrue(wt.isAlive());
        assertFalse(wt.isInterrupted());
        assertEquals("job", wt.getName());
        wt.interrupt();

        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException exception)
        {
            // ignore
        }
        
        wt.cleanUp();

        long startTime = System.currentTimeMillis();
        while (wt.isAlive() && System.currentTimeMillis() - startTime < 1000)
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException exception)
            {
                // ignore
            }
        }
        if (System.currentTimeMillis() - startTime > 1000)
        {
            fail("WorkerThread execution; System.currentTimeMillis() - startTime > 1000");
        }

        assertFalse(wt.isAlive());
        assertEquals(1, this.value);
    }

    /**
     * Test the WorkerThread.
     */
    @Test
    public void testWorkerThreadException()
    {
        WorkerThread wt = new WorkerThread("exception job", new ExceptionJob());
        assertEquals("exception job", wt.getName());
        wt.interrupt();
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException exception)
        {
            // ignore
        }
        wt.cleanUp();
        long startTime = System.currentTimeMillis();
        while (wt.isAlive() && System.currentTimeMillis() - startTime < 1000)
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException exception)
            {
                // ignore
            }
        }
        if (System.currentTimeMillis() - startTime > 1000)
        {
            fail("WorkerThread execution; System.currentTimeMillis() - startTime > 1000");
        }
        assertFalse(wt.isAlive());
    }
    
    /** The worker job. */
    class Job implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            WorkerThreadTest.this.value++;
        }
    }
    
    /** The worker job that throws an exception. */
    class ExceptionJob implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            throw new RuntimeException("this thread does not work; test successfully passed");
        }
    }
}
