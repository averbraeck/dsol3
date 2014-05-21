/*
 * @(#)EventProducer.java April 4, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tudelft.simulation.event.ref.Reference;
import nl.tudelft.simulation.event.ref.StrongReference;
import nl.tudelft.simulation.event.ref.WeakReference;

/**
 * The EventProducer forms the reference implementation of the
 * EventProducerInterface. Objects extending this class are provided all the
 * functionalities for registration and event firering.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/event"
 * >www.simulation.tudelft.nl/event </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public abstract class EventProducer implements EventProducerInterface,
        Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** listeners is the collection of interested listeners */
    protected Map<EventType, Reference<EventListenerInterface>[]> listeners = Collections
            .synchronizedMap(new EventListenerMap<EventType, Reference<EventListenerInterface>[]>());

    /**
     * the semaphore used to lock on while performing thread sensitive
     * operations.
     */
    private transient Object semaphore = new Object();

    /** the cache to prevent continuous reflection */
    private transient EventType[] cache = null;

    /**
     * checks whether no duplicate short values are assigned to the producer. An
     * eventproducer produces events of a certain eventType. This eventType
     * functions as a marker for registration. If the eventProducer defines two
     * eventTypes with an equal value, the marker function is lost. This method
     * checks for this particular problem.
     * 
     * @return returns whether every eventType in this class is unique.
     */
    private boolean checkEventType()
    {
        EventType[] events = this.getEventTypes();
        for (int i = 0; i < events.length; i++)
        {
            for (int j = 0; j < events.length; j++)
            {
                if (i != j && events[i].equals(events[j]))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * constructs a new EventProducer and checks for double values in events
     */
    public EventProducer()
    {
        super();
        if (!this.checkEventType())
        {
            throw new RuntimeException("EventProducer failed: "
                    + "more events have the same short value");
        }
    }

    /**
     * adds the listener as weak reference to the listener.
     * 
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(EventListenerInterface, EventType)
     */
    public synchronized boolean addListener(
            final EventListenerInterface listener, final EventType eventType)
    {
        return this.addListener(listener, eventType,
                EventProducerInterface.FIRST_POSITION);
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface,
     *      nl.tudelft.simulation.event.EventType, boolean)
     */
    public synchronized boolean addListener(
            final EventListenerInterface listener, final EventType eventType,
            final boolean weak)
    {
        return this.addListener(listener, eventType,
                EventProducerInterface.FIRST_POSITION, weak);
    }

    /**
     * adds the listener as weak reference to the listener.
     * 
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface,
     *      nl.tudelft.simulation.event.EventType, short)
     */
    public synchronized boolean addListener(
            final EventListenerInterface listener, final EventType eventType,
            final short position)
    {
        if (listener == null || position < EventProducerInterface.LAST_POSITION)
        {
            return false;
        }
        return this.addListener(listener, eventType, position, false);
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface,
     *      nl.tudelft.simulation.event.EventType, short, boolean)
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean addListener(
            final EventListenerInterface listener, final EventType eventType,
            final short position, final boolean weak)
    {
        if (listener == null || position < EventProducerInterface.LAST_POSITION)
        {
            return false;
        }
        synchronized (this.semaphore)
        {
            Reference<EventListenerInterface> reference = null;
            if (!weak)
            {
                reference = new StrongReference<EventListenerInterface>(
                        listener);
            } else
            {
                reference = new WeakReference<EventListenerInterface>(listener);
            }
            if (this.listeners.containsKey(eventType))
            {
                Reference<EventListenerInterface>[] entries = this.listeners
                        .get(eventType);
                for (int i = 0; i < entries.length; i++)
                {
                    if (listener.equals(entries[i].get()))
                    {
                        return false;
                    }
                }
                List<Reference<EventListenerInterface>> entriesArray = new ArrayList<Reference<EventListenerInterface>>(
                        Arrays.asList(entries));
                if (position == EventProducerInterface.LAST_POSITION)
                {
                    entriesArray.add(reference);
                } else
                {
                    entriesArray.add(position, reference);
                }
                this.listeners.put(eventType, entriesArray
                        .toArray(new Reference[entriesArray.size()]));
            } else
            {
                Reference[] entries = {reference};
                this.listeners.put(eventType, entries);
            }
        }
        return true;
    }

    /**
     * fires the event to the listener. This method is a hook method. The
     * default implementation simply invokes the notify on the listener. In
     * specific cases (filtering, storing, queuing, this method can be
     * overwritten.
     * 
     * @param listener the listener for this event
     * @param event the event to fire
     * @return the event
     * @throws RemoteException on network failure.
     */
    protected synchronized EventInterface fireEvent(
            final EventListenerInterface listener, final EventInterface event)
            throws RemoteException
    {
        listener.notify(event);
        return event;
    }

    /**
     * fires an event to subscribed listeners.
     * 
     * @param event the event.
     * @return the event.
     */
    protected synchronized EventInterface fireEvent(final EventInterface event)
    {
        if (this.listeners.containsKey(event.getType()))
        {
            synchronized (this.semaphore)
            {
                Reference<EventListenerInterface>[] entries = this.listeners
                        .get(event.getType());
                for (int i = 0; i < entries.length; i++)
                {
                    EventListenerInterface listener = entries[i].get();
                    try
                    {
                        if (listener != null)
                        {
                            // The garbage collection has not cleaned the
                            // referent
                            this.fireEvent(listener, event);
                        } else
                        {
                            // The garbage collection cleaned the referent;
                            // there is no need to keep the subscription
                            this.removeListener(entries[i], event.getType());
                        }
                    } catch (RemoteException remoteException)
                    {
                        // A network failure prevented the delivery,
                        // subscription is removed.
                        this.removeListener(entries[i], event.getType());
                    }
                }
            }
        }
        return event;
    }

    /**
     * fires a byte value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the byte value.
     */
    protected synchronized byte fireEvent(final EventType eventType,
            final byte value)
    {
        this.fireEvent(eventType, new Byte(value));
        return value;
    }

    /**
     * fires a boolean value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the byte value.
     */
    protected synchronized boolean fireEvent(final EventType eventType,
            final boolean value)
    {
        this.fireEvent(eventType, new Boolean(value));
        return value;
    }

    /**
     * fires a byte value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the byte value.
     */
    protected synchronized byte fireEvent(final EventType eventType,
            final byte value, final double time)
    {
        this.fireEvent(eventType, new Byte(value), time);
        return value;
    }

    /**
     * fires a boolean value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the byte value.
     */
    protected synchronized boolean fireEvent(final EventType eventType,
            final boolean value, final double time)
    {
        this.fireEvent(eventType, new Boolean(value), time);
        return value;
    }

    /**
     * fires a double value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the double value.
     */
    protected synchronized double fireEvent(final EventType eventType,
            final double value)
    {
        this.fireEvent(eventType, new Double(value));
        return value;
    }

    /**
     * fires a double value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the double value.
     */
    protected synchronized double fireEvent(final EventType eventType,
            final double value, final double time)
    {
        this.fireEvent(eventType, new Double(value), time);
        return value;
    }

    /**
     * fires an integer value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the integer value.
     */
    protected synchronized int fireEvent(final EventType eventType,
            final int value)
    {
        this.fireEvent(eventType, new Integer(value));
        return value;
    }

    /**
     * fires an integer value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the integer value.
     */
    protected synchronized int fireEvent(final EventType eventType,
            final int value, final double time)
    {
        this.fireEvent(eventType, new Integer(value), time);
        return value;
    }

    /**
     * fires a long value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the long value.
     */
    protected synchronized long fireEvent(final EventType eventType,
            final long value)
    {
        this.fireEvent(eventType, new Long(value));
        return value;
    }

    /**
     * fires a long value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the long value.
     */
    protected synchronized long fireEvent(final EventType eventType,
            final long value, final double time)
    {
        this.fireEvent(eventType, new Long(value), time);
        return value;
    }

    /**
     * fires a value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the Serializable value.
     */
    protected synchronized Object fireEvent(final EventType eventType,
            final Object value)
    {
        this.fireEvent(new Event(eventType, this, value));
        return value;
    }

    /**
     * fires a Serializable value to subscribed listeners subscribed to
     * eventType. A timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the Serializable value.
     */
    protected synchronized Object fireEvent(final EventType eventType,
            final Object value, final double time)
    {
        this.fireEvent(new TimedEvent(eventType, this, value, time));
        return value;
    }

    /**
     * fires a short value to subscribed listeners subscribed to eventType.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @return the short value.
     */
    protected synchronized short fireEvent(final EventType eventType,
            final short value)
    {
        this.fireEvent(eventType, new Short(value));
        return value;
    }

    /**
     * fires a short value to subscribed listeners subscribed to eventType. A
     * timed event is fired.
     * 
     * @param eventType the eventType of the event.
     * @param value the value of the event.
     * @param time a timestamp for the event.
     * @return the short value.
     */
    protected synchronized short fireEvent(final EventType eventType,
            final short value, final double time)
    {
        this.fireEvent(eventType, new Short(value), time);
        return value;
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface#getEventTypes()
     */
    public synchronized EventType[] getEventTypes()
    {
        if (this.cache != null)
        {
            return this.cache;
        }
        List<Field> fieldList = new ArrayList<Field>();
        Class<?> clazz = this.getClass();
        while (clazz != null)
        {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++)
            {
                fieldList.add(declaredFields[i]);
            }
            clazz = clazz.getSuperclass();
        }
        Field[] fields = fieldList.toArray(new Field[fieldList.size()]);
        Map<String, Object> result = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i].getType().equals(EventType.class))
            {
                fields[i].setAccessible(true);
                try
                {
                    if (!result.containsKey(fields[i].getName()))
                    {
                        result.put(fields[i].getName(), fields[i].get(this));
                    }
                } catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
        this.cache = result.values().toArray(new EventType[result.size()]);
        return this.cache;
    }

    /**
     * removes all the listeners from the producer.
     * 
     * @return int the amount of removed listeners.
     */
    protected synchronized int removeAllListeners()
    {
        int result = this.listeners.size();
        this.listeners = null;
        this.listeners = Collections
                .synchronizedMap(new EventListenerMap<EventType, Reference<EventListenerInterface>[]>());
        return result;
    }

    /**
     * removes all the listeners of a class.
     * 
     * @param ofClass the class or superclass.
     * @return the number of listeners which were removed.
     */
    protected synchronized int removeAllListeners(final Class<?> ofClass)
    {
        Map<EventType, Reference<EventListenerInterface>[]> temp = new HashMap<EventType, Reference<EventListenerInterface>[]>(
                this.listeners);
        int result = 0;
        synchronized (this.semaphore)
        {
            Set<EventType> keys = temp.keySet();
            for (Iterator<EventType> i = keys.iterator(); i.hasNext();)
            {
                EventType type = i.next();
                List<Reference<EventListenerInterface>> list = Arrays
                        .asList(this.listeners.get(type));
                for (Iterator<Reference<EventListenerInterface>> ii = list
                        .iterator(); ii.hasNext();)
                {
                    Reference<EventListenerInterface> listener = ii.next();
                    if (listener.getClass().isAssignableFrom(ofClass))
                    {
                        this.removeListener(listener.get(), type);
                        result++;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #removeListener(EventListenerInterface, EventType)
     */
    @SuppressWarnings("unchecked")
    public synchronized boolean removeListener(
            final EventListenerInterface listener, final EventType eventType)
    {
        if (!this.listeners.containsKey(eventType))
        {
            return false;
        }
        boolean result = false;
        synchronized (this.semaphore)
        {
            Reference<EventListenerInterface>[] entries = this.listeners
                    .get(eventType);
            List<Reference<EventListenerInterface>> list = new ArrayList<Reference<EventListenerInterface>>(
                    Arrays.asList(entries));
            for (Iterator<Reference<EventListenerInterface>> i = list
                    .iterator(); i.hasNext();)
            {
                Reference<EventListenerInterface> reference = i.next();
                EventListenerInterface entrie = reference.get();
                if (entrie == null)
                {
                    i.remove();
                } else
                {
                    if (listener.equals(entrie))
                    {
                        i.remove();
                        result = true;
                    }
                }
            }
            this.listeners.put(eventType, list.toArray(new Reference[list
                    .size()]));
            if (list.size() == 0)
            {
                this.listeners.remove(eventType);
            }
        }
        return result;
    }

    /**
     * removes a reference from the subscription list
     * 
     * @param reference the reference to remove
     * @param eventType the eventType for which reference must be removed
     * @return success whenever the reference is removes; otherwise returns
     *         false.
     */
    @SuppressWarnings("unchecked")
    private synchronized boolean removeListener(
            final Reference<EventListenerInterface> reference,
            final EventType eventType)
    {
        boolean success = false;
        Reference<EventListenerInterface>[] entries = this.listeners
                .get(eventType);
        List<Reference<EventListenerInterface>> list = new ArrayList<Reference<EventListenerInterface>>(
                Arrays.asList(entries));
        for (Iterator<Reference<EventListenerInterface>> i = list.iterator(); i
                .hasNext();)
        {
            if (i.next().equals(reference))
            {
                i.remove();
                success = true;
            }
        }
        Reference<EventListenerInterface>[] toArray = list
                .toArray(new Reference[list.size()]);
        this.listeners.put(eventType, toArray);
        if (list.size() == 0)
        {
            this.listeners.remove(eventType);
        }
        return success;
    }

    /**
     * writes a serializable method to stream
     * 
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out)
            throws IOException
    {
        out.defaultWriteObject();
    }

    /**
     * reads a serializable method from stream
     * 
     * @param in the inputstream
     * @throws IOException on IOException
     */
    private synchronized void readObject(final java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.semaphore = new Object();
    }
}