/*
 * @(#) EventLogHandler.java Nov 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.logger.handlers;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * A EventLogHandler <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a href="mailto:nlang@fbk.eur.nl">Niels Lang </a>
 */
public class EventLogHandler extends Handler implements EventProducerInterface
{
    /** LOG_RECORD_PRODUCED_EVENT is fired whenever an log record is received */
    public static final EventType LOG_RECORD_PRODUCED_EVENT = new EventType("LOG_RECORD_PRODUCED_EVENT");

    /** our private postman */
    private MyEventProducer postman = new MyEventProducer();

    /**
     * constructs a new EventLogHandler
     */
    public EventLogHandler()
    {
        super();
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface, nl.tudelft.simulation.event.EventType)
     */
    public boolean addListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.postman.addListener(listener, eventType);
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface, nl.tudelft.simulation.event.EventType,
     *      boolean)
     */
    public boolean addListener(final EventListenerInterface listener, final EventType eventType, final boolean weak)
    {
        return this.postman.addListener(listener, eventType, weak);
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface, nl.tudelft.simulation.event.EventType,
     *      short)
     */
    public boolean addListener(final EventListenerInterface listener, final EventType eventType, final short position)
    {
        return this.postman.addListener(listener, eventType, position);
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #addListener(nl.tudelft.simulation.event.EventListenerInterface, nl.tudelft.simulation.event.EventType,
     *      short,boolean)
     */
    public boolean addListener(final EventListenerInterface listener, final EventType eventType, final short position,
            final boolean weak)
    {
        return this.postman.addListener(listener, eventType, position, weak);
    }

    /**
     * @see java.util.logging.Handler#close()
     */
    @Override
    public void close()
    {
        this.flush();
    }

    /**
     * @see java.util.logging.Handler#flush()
     */
    @Override
    public void flush()
    {
        // Nothing to do
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface#getEventTypes()
     */
    public EventType[] getEventTypes()
    {
        return new EventType[]{EventLogHandler.LOG_RECORD_PRODUCED_EVENT};
    }

    /**
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     */
    @Override
    public void publish(final LogRecord arg0)
    {
        this.postman.fireEvent(new Event(EventLogHandler.LOG_RECORD_PRODUCED_EVENT, this, this.getFormatter().format(
                arg0)));
    }

    /**
     * @see nl.tudelft.simulation.event.EventProducerInterface
     *      #removeListener(nl.tudelft.simulation.event.EventListenerInterface, nl.tudelft.simulation.event.EventType)
     */
    public boolean removeListener(final EventListenerInterface listener, final EventType eventType)
    {
        return this.postman.removeListener(listener, eventType);
    }

    /**
     * A MyEventProducer is a more or less public Postman.
     */
    protected class MyEventProducer extends EventProducer
    {
        /** The default serial version UID for serializable classes */
        private static final long serialVersionUID = 1L;

        /**
         * @see nl.tudelft.simulation.event.EventProducer #fireEvent(nl.tudelft.simulation.event.EventInterface)
         */
        @Override
        public EventInterface fireEvent(final EventInterface event)
        {
            return super.fireEvent(event);
        }
    }
}