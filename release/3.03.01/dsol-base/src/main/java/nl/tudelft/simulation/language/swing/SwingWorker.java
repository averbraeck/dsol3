package nl.tudelft.simulation.language.swing;

import javax.swing.SwingUtilities;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you subclass to perform
 * GUI-related work in a dedicated thread. For instructions on and examples of using this class, see:
 * <a href="https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html">
 * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html </a>. Note that the API changed slightly in
 * the 3rd version: You must now invoke start() on the SwingWorker after creating it.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
public abstract class SwingWorker
{
    /** the value of the worker. */
    private Object value; // see getValue(), setValue()

    /** the thread to use. */
    protected ThreadVar threadVar;

    /**
     * @return Get the value produced by the worker thread, or null if it hasn't been constructed yet.
     */
    protected synchronized Object getValue()
    {
        return this.value;
    }

    /**
     * Set the value produced by worker thread
     * @param x Object; the value
     */
    protected synchronized void setValue(final Object x)
    {
        this.value = x;
    }

    /**
     * @return Compute the value to be returned by the <code>get</code> method.
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread) after the <code>construct</code> method has
     * returned.
     */
    public void finished()
    {
        // Nothing to be done.
    }

    /**
     * A new method that interrupts the worker thread. Call this method to force the worker to stop what it's doing.
     */
    public void interrupt()
    {
        Thread t = this.threadVar.get();
        if (t != null)
        {
            t.interrupt();
        }
        this.threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method. Returns null if either the constructing thread or
     * the current thread was interrupted before a value was produced.
     * @return the value created by the <code>construct</code> method
     */
    public Object get()
    {
        while (true)
        {
            Thread t = this.threadVar.get();
            if (t == null)
            {
                return getValue();
            }
            try
            {
                t.join();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }

    /**
     * Start a thread that will call the <code>construct</code> method and then exit.
     */
    public SwingWorker()
    {
        final Runnable doFinished = new Runnable()
        {
            @Override
            public void run()
            {
                finished();
            }
        };

        Runnable doConstruct = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    SwingWorker.this.setValue(construct());
                }
                finally
                {
                    SwingWorker.this.threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        this.threadVar = new ThreadVar(t);
    }

    /**
     * Start the worker thread.
     */
    public void start()
    {
        Thread t = this.threadVar.get();
        if (t != null)
        {
            t.start();
        }
    }

    /**
     * Class to maintain reference to current worker thread under separate synchronization control.
     */
    private static class ThreadVar
    {
        /** the thread to use. */
        private Thread thread;

        /**
         * constructs a new ThreadVar.
         * @param t Thread; the thread
         */
        ThreadVar(final Thread t)
        {
            this.thread = t;
        }

        /**
         * returns the thread
         * @return Thread the thread
         */
        synchronized Thread get()
        {
            return this.thread;
        }

        /**
         * clears the thread
         */
        synchronized void clear()
        {
            this.thread = null;
        }
    }
}
