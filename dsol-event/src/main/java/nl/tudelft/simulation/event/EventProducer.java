package nl.tudelft.simulation.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.djutils.logger.CategoryLogger;

import nl.tudelft.simulation.event.ref.Reference;
import nl.tudelft.simulation.event.ref.StrongReference;
import nl.tudelft.simulation.event.ref.WeakReference;

/**
 * The EventProducer forms the reference implementation of the EventProducerInterface. Objects extending this class are provided
 * all the functionalities for registration and event firing.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class EventProducer implements EventProducerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** listeners is the collection of interested listeners. */
    protected Map<EventType, List<Reference<EventListenerInterface>>> listeners =
            Collections.synchronizedMap(new EventListenerMap());

    /**
     * the semaphore used to lock on while performing thread sensitive operations.
     */
    private transient Object semaphore = new Object();

    /** the cache to prevent continuous reflection. */
    private transient EventType[] cache = null;

    /**
     * checks whether no duplicate short values are assigned to the producer. An event producer produces events of a certain
     * eventType. This eventType functions as a marker for registration. If the eventProducer defines two eventTypes with an
     * equal value, the marker function is lost. This method checks for this particular problem.
     * @return returns whether every eventType in this class is unique.
     */
    private boolean checkEventType()
    {
        EventType[] events = this.getEventTypes();
        for (int i = 0; i < events.length; i++)
        {
            for (int j = i + 1; j < events.length; j++) // assumes symmetric "equals"
            {
                if (events[i].equals(events[j]))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * constructs a new EventProducer and checks for double values in events.
     */
    public EventProducer()
    {
        if (!this.checkEventType())
        {
            throw new RuntimeException("EventProducer failed: " + "more events have the same class + name combination");
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final boolean weak)
    {
        return this.addListener(listener, eventType, EventProducerInterface.FIRST_POSITION, weak);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
            final short position)
    {
        if (listener == null || position < EventProducerInterface.LAST_POSITION)
        {
            return false;
        }
        return this.addListener(listener, eventType, position, false);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addListener(final EventListenerInterface listener, final EventType eventType,
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
                reference = new StrongReference<EventListenerInterface>(listener);
            }
            else
            {
                reference = new WeakReference<EventListenerInterface>(listener);
            }
            if (this.listeners.containsKey(eventType))
            {
                for (Reference<EventListenerInterface> entry : this.listeners.get(eventType))
                {
                    if (listener.equals(entry.get()))
                    {
                        return false;
                    }
                }
                List<Reference<EventListenerInterface>> entries = this.listeners.get(eventType);
                if (position == EventProducerInterface.LAST_POSITION)
                {
                    entries.add(reference);
                }
                else
                {
                    entries.add(position, reference);
                }
            }
            else
            {
                List<Reference<EventListenerInterface>> entries = new ArrayList<>();
                entries.add(reference);
                this.listeners.put(eventType, entries);
            }
        }
        return true;
    }

    /**
     * fires the event to the listener. This method is a hook method. The default implementation simply invokes the notify on
     * the listener. In specific cases (filtering, storing, queuing, this method can be overwritten.
     * @param listener EventListenerInterface; the listener for this event
     * @param event EventInterface; the event to fire
     * @return the event
     * @throws RemoteException on network failure.
     */
    protected synchronized EventInterface fireEvent(final EventListenerInterface listener, final EventInterface event)
            throws RemoteException
    {
        listener.notify(event);
        return event;
    }

    /**
     * fires an event to subscribed listeners.
     * @param event EventInterface; the event.
     * @return the event.
     */
    protected synchronized EventInterface fireEvent(final EventInterface event)
    {
        if (this.listeners.containsKey(event.getType()))
        {
            synchronized (this.semaphore)
            {
                // make a safe copy because of possible removeListener() in notify() method during fireEvent
                List<Reference<EventListenerInterface>> listenerList = new ArrayList<>(this.listeners.get(event.getType()));
                for (Reference<EventListenerInterface> reference : listenerList)
                {
                    EventListenerInterface listener = reference.get();
                    try
                    {
                        if (listener != null)
                        {
                            // The garbage collection has not cleaned the referent
                            this.fireEvent(listener, event);
                        }
                        else
                        {
                            // The garbage collection cleaned the referent;
                            // there is no need to keep the subscription
                            this.removeListener(reference, event.getType());
                        }
                    }
                    catch (RemoteException remoteException)
                    {
                        // A network failure prevented the delivery,
                        // subscription is removed.
                        this.removeListener(reference, event.getType());
                    }
                }
            }
        }
        return event;
    }

    /**
     * fires a value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value Object; the value of the event.
     * @return the Serializable value.
     */
    protected synchronized Object fireEvent(final EventType eventType, final Object value)
    {
        this.fireEvent(new Event(eventType, this, value));
        return value;
    }

    /**
     * notifies listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     */
    protected synchronized void fireEvent(final EventType eventType)
    {
        this.fireEvent(new Event(eventType, this, null));
    }

    /**
     * fires a Serializable value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value Object; the value of the event.
     * @param time C; a timestamp for the event.
     * @return the Serializable value.
     * @param <C> the comparable type to indicate the time when the event is fired.
     */
    protected synchronized <C extends Comparable<C>> Object fireTimedEvent(final EventType eventType, final Object value,
            final C time)
    {
        this.fireEvent(new TimedEvent<C>(eventType, this, value, time));
        return value;
    }

    /**
     * fires a byte value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value byte; the value of the event.
     * @return the byte value.
     */
    protected synchronized byte fireEvent(final EventType eventType, final byte value)
    {
        this.fireEvent(eventType, Byte.valueOf(value));
        return value;
    }

    /**
     * fires a byte value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value byte; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the byte value.
     */
    protected synchronized <C extends Comparable<C>> byte fireTimedEvent(final EventType eventType, final byte value,
            final C time)
    {
        this.fireTimedEvent(eventType, Byte.valueOf(value), time);
        return value;
    }

    /**
     * fires a boolean value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value boolean; the value of the event.
     * @return the boolean value.
     */
    protected synchronized boolean fireEvent(final EventType eventType, final boolean value)
    {
        this.fireEvent(eventType, Boolean.valueOf(value));
        return value;
    }

    /**
     * fires a boolean value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value boolean; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the boolean value.
     */
    protected synchronized <C extends Comparable<C>> boolean fireTimedEvent(final EventType eventType, final boolean value,
            final C time)
    {
        this.fireTimedEvent(eventType, Boolean.valueOf(value), time);
        return value;
    }

    /**
     * fires a double value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value double; the value of the event.
     * @return the double value.
     */
    protected synchronized double fireEvent(final EventType eventType, final double value)
    {
        this.fireEvent(eventType, Double.valueOf(value));
        return value;
    }

    /**
     * fires a double value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value double; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the double value.
     */
    protected synchronized <C extends Comparable<C>> double fireTimedEvent(final EventType eventType, final double value,
            final C time)
    {
        this.fireTimedEvent(eventType, Double.valueOf(value), time);
        return value;
    }

    /**
     * fires an integer value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value int; the value of the event.
     * @return the integer value.
     */
    protected synchronized int fireEvent(final EventType eventType, final int value)
    {
        this.fireEvent(eventType, Integer.valueOf(value));
        return value;
    }

    /**
     * fires an integer value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value int; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the integer value.
     */
    protected synchronized <C extends Comparable<C>> int fireTimedEvent(final EventType eventType, final int value,
            final C time)
    {
        this.fireTimedEvent(eventType, Integer.valueOf(value), time);
        return value;
    }

    /**
     * fires a long value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value long; the value of the event.
     * @return the long value.
     */
    protected synchronized long fireEvent(final EventType eventType, final long value)
    {
        this.fireEvent(eventType, Long.valueOf(value));
        return value;
    }

    /**
     * fires a long value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value long; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the long value.
     */
    protected synchronized <C extends Comparable<C>> long fireTimedEvent(final EventType eventType, final long value,
            final C time)
    {
        this.fireTimedEvent(eventType, Long.valueOf(value), time);
        return value;
    }

    /**
     * fires a short value to listeners subscribed to eventType.
     * @param eventType EventType; the eventType of the event.
     * @param value short; the value of the event.
     * @return the short value.
     */
    protected synchronized short fireEvent(final EventType eventType, final short value)
    {
        this.fireEvent(eventType, Short.valueOf(value));
        return value;
    }

    /**
     * fires a short value to listeners subscribed to eventType. A timed event is fired.
     * @param eventType EventType; the eventType of the event.
     * @param value short; the value of the event.
     * @param time C; a timestamp for the event.
     * @param <C> the comparable type to indicate the time when the event is fired.
     * @return the short value.
     */
    protected synchronized <C extends Comparable<C>> short fireTimedEvent(final EventType eventType, final short value,
            final C time)
    {
        this.fireTimedEvent(eventType, Short.valueOf(value), time);
        return value;
    }

    /**
     * @return the event types of this EventProducer
     */
    private synchronized EventType[] getEventTypes()
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
                }
                catch (Exception exception)
                {
                    CategoryLogger.always().error(exception);
                }
            }
        }
        this.cache = result.values().toArray(new EventType[result.size()]);
        return this.cache;
    }

    /**
     * removes all the listeners from the producer.
     * @return the number of removed listeners.
     */
    protected synchronized int removeAllListeners()
    {
        int result = this.listeners.size();
        this.listeners = null;
        this.listeners = Collections.synchronizedMap(new EventListenerMap());
        return result;
    }

    /**
     * removes all the listeners of a class.
     * @param ofClass Class&lt;?&gt;; the class or superclass.
     * @return the number of listeners which were removed.
     */
    protected synchronized int removeAllListeners(final Class<?> ofClass)
    {
        int result = 0;
        synchronized (this.semaphore)
        {
            for (EventType type : this.listeners.keySet())
            {
                for (Iterator<Reference<EventListenerInterface>> ii = this.listeners.get(type).iterator(); ii.hasNext();)
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

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeListener(final EventListenerInterface listener, final EventType eventType)
    {
        if (!this.listeners.containsKey(eventType))
        {
            return false;
        }
        boolean result = false;
        synchronized (this.semaphore)
        {
            for (Iterator<Reference<EventListenerInterface>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
            {
                Reference<EventListenerInterface> reference = i.next();
                EventListenerInterface entry = reference.get();
                if (entry == null)
                {
                    i.remove();
                }
                else
                {
                    if (listener.equals(entry))
                    {
                        i.remove();
                        result = true;
                    }
                }
            }
            if (this.listeners.get(eventType).size() == 0)
            {
                this.listeners.remove(eventType);
            }
        }
        return result;
    }

    /**
     * removes a reference from the subscription list.
     * @param reference Reference&lt;EventListenerInterface&gt;; the reference to remove
     * @param eventType EventType; the eventType for which reference must be removed
     * @return success whenever the reference is removes; otherwise returns false.
     */
    private synchronized boolean removeListener(final Reference<EventListenerInterface> reference, final EventType eventType)
    {
        boolean success = false;
        for (Iterator<Reference<EventListenerInterface>> i = this.listeners.get(eventType).iterator(); i.hasNext();)
        {
            if (i.next().equals(reference))
            {
                i.remove();
                success = true;
            }
        }
        if (this.listeners.get(eventType).size() == 0)
        {
            this.listeners.remove(eventType);
        }
        return success;
    }

    /**
     * writes a serializable method to stream.
     * @param out ObjectOutputStream; the output stream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    /**
     * reads a serializable method from stream.
     * @param in java.io.ObjectInputStream; the input stream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on class cast exeption when reading object
     */
    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.semaphore = new Object();
    }
}
