/*
 * Created on Mar 9, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package nl.tudelft.simulation.dsol.hla.callBack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.util.EventProducingList;

/**
 * A callbackManager responsible for callBacks on the RTI.
 */
public class CallbackManager implements Runnable, EventListenerInterface
{
    /** the callBackQueue */
    private EventProducingList callBackQueue = null;

    /**
     * constructs a new CallbackManageger
     * 
     * @param callBackQueue the callBackQueue
     */
    public CallbackManager(final EventProducingList callBackQueue)
    {
        super();
        this.callBackQueue = callBackQueue;
        this.callBackQueue.addListener(this,
                EventProducingList.OBJECT_ADDED_EVENT);
    }

    /**
     * executed the list
     * 
     * @param list
     * @throws SimRuntimeException
     */
    protected void execute(List list) throws SimRuntimeException
    {
        for (Iterator i = list.iterator(); i.hasNext();)
        {
            CallbackTask callbackTask = (CallbackTask) i.next();
            i.remove();
            callbackTask.execute();
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        while (true)
        {
            try
            {
                while (!this.callBackQueue.isEmpty())
                {
                    List copy = new ArrayList(this.callBackQueue);
                    this.callBackQueue.clear();
                    this.execute(copy);
                }
                synchronized (this)
                {
                    this.wait();
                }
            } catch (Exception exception)
            {
                // Logger.warning(this,"run",exception);
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface#notify(nl.tudelft.simulation.event.EventInterface)
     */
    public synchronized void notify(final EventInterface arg0)
    {
        this.notifyAll();
    }
}