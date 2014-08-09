/*
 * @(#) SwingWorker.java Apr 29, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.language.swing;

import javax.swing.SwingUtilities;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you subclass to perform
 * GUI-related work in a dedicated thread. For instructions on and examples of using this class, see:
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html Note that the API changed slightly in the 3rd
 * version: You must now invoke start() on the SwingWorker after creating it.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
 */
public abstract class SwingWorker
{
    /** the value of the worker */
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
     * @param x the value
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
            public void run()
            {
                finished();
            }
        };

        Runnable doConstruct = new Runnable()
        {
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
        /** the thread to use */
        private Thread thread;

        /**
         * constructs a new ThreadVar
         * @param t the thread
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