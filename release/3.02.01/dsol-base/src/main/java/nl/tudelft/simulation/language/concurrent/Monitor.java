package nl.tudelft.simulation.language.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitor class. In the Java programming language there is a lock associated with every object. The language does not
 * provide a way to perform separate lock and unlock operations; instead, they are implicitly performed by high-level
 * constructs that always arrange to pair such operations correctly. This Monitor class, however, provides separate
 * monitorenter and monitorexit instructions that implement the lock and unlock operations.
 * <p>
 * The class is final for now, as it is not the idea that the class should be extended. It has only static methods.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
 * @version Oct 17, 2009
 * @author <a href="mailto:phmjacobs@hotmail.com">Peter H.M. Jacobs</a>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class Monitor
{
    /** the locks held. */
    private static List<Monitor.Entry> locks = new ArrayList<Monitor.Entry>();

    /**
     * constructs a new Monitor.
     */
    private Monitor()
    {
        super();
        // unreachable code
    }

    /**
     * locks an object for the current thread.
     * @param object the object to lock
     */
    public static void lock(final Object object)
    {
        Monitor.lock(object, Thread.currentThread());
    }

    /**
     * locks an object for the given requestor.
     * @param object the object to lock.
     * @param requestor the requesting thread.
     */
    public static void lock(final Object object, final Thread requestor)
    {
        synchronized (Monitor.locks)
        {
            if (Monitor.get(object) == null)
            {
                Monitor.locks.add(new Entry(object, new MonitorThread(requestor, object)));
            }
            else
            {
                MonitorThread thread = Monitor.get(object);
                if (thread.getOwner().equals(requestor))
                {
                    thread.increaseCounter();
                }
                else
                {
                    synchronized (object)
                    {
                        // We wait until we gained access to the monitor
                        Monitor.locks.add(new Entry(object, new MonitorThread(requestor, object)));
                    }
                }
            }
        }
    }

    /**
     * unlocks an object locked by the current Thread.
     * @param object the object to unlock
     */
    public static void unlock(final Object object)
    {
        Monitor.unlock(object, Thread.currentThread());
    }

    /**
     * unlocks an object locked by owner.
     * @param object the object to unlock.
     * @param owner the owning thread.
     */
    public static void unlock(final Object object, final Thread owner)
    {
        synchronized (Monitor.locks)
        {
            MonitorThread thread = Monitor.get(object);
            if (thread == null)
            {
                throw new IllegalMonitorStateException("object(" + object + ") is not locked");
            }
            if (!thread.getOwner().equals(owner))
            {
                throw new IllegalMonitorStateException(owner + " cannot" + " unlock object owned by "
                        + thread.getOwner());
            }
            thread.decreaseCounter();
            if (thread.getCounter() == 0)
            {
                thread.interrupt();
                Monitor.locks.remove(object);
            }
        }
    }

    /**
     * returns the MonitorThread for a specific key.
     * @param key the key to resolve
     * @return the MonitorThread
     */
    private static MonitorThread get(final Object key)
    {
        for (Entry next : Monitor.locks)
        {
            if (next.getKey().equals(key))
            {
                return next.getThread();
            }
        }
        return null;
    }

    /**
     * The Entry specifies entries in the set.
     */
    private static final class Entry
    {
        /** the key to use. */
        private Object key = null;

        /** the monitorThread. */
        private MonitorThread thread = null;

        /**
         * constructs a new Entry.
         * @param key the key that locked the thread
         * @param thread the thread to be locked
         */
        public Entry(final Object key, final MonitorThread thread)
        {
            super();
            this.key = key;
            this.thread = thread;
        }

        /**
         * @return the key that is locked by a thread
         */
        public Object getKey()
        {
            return this.key;
        }

        /**
         * @return the thread that locked the key
         */
        public MonitorThread getThread()
        {
            return this.thread;
        }
    }

    /**
     * A MonitorThread is used to lock an object.
     */
    private static class MonitorThread extends Thread
    {
        /** the monitor to use. */
        private Object object = null;

        /** the owning thread. */
        private Thread owner = null;

        /** the counter. */
        private int counter = 0;

        /**
         * constructs a new MonitorThread.
         * @param owner the owning thread
         * @param object the object
         */
        public MonitorThread(final Thread owner, final Object object)
        {
            super("MonitorThread on " + object.getClass());
            this.setDaemon(true);
            this.owner = owner;
            synchronized (object)
            {
                this.object = object;
                this.counter++;
                this.start();
            }
            synchronized (owner)
            {
                try
                {
                    this.owner.wait();
                }
                catch (InterruptedException exception)
                {
                    exception = null;
                    /*
                     * This interrupted exception is thrown because this monitor thread has started and interrupted its
                     * constructor. We now know object is locked and may therefore return.
                     */
                }
            }
        }

        /**
         * @return Returns the counter.
         */
        public synchronized int getCounter()
        {
            return this.counter;
        }

        /**
         * decreases the counter with one.
         */
        public synchronized void decreaseCounter()
        {
            this.counter = Math.max(0, this.counter - 1);
        }

        /**
         * increases the counter of this thread with one.
         */
        public synchronized void increaseCounter()
        {
            this.counter++;
        }

        /**
         * @return Returns the owning thread.
         */
        public Thread getOwner()
        {
            return this.owner;
        }

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            try
            {
                // We lock the object
                synchronized (this.object)
                {
                    // Since we have locked the object, we can now return
                    // the constructor
                    this.owner.interrupt();

                    // We join
                    this.join();
                }
            }
            catch (Exception exception)
            {
                // This is OK.. We use this construction in the
                // MonitorTest.unlock to release a lock
                exception = null;
            }
        }
    }
}
