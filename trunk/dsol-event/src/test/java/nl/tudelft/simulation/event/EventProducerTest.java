package nl.tudelft.simulation.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

import org.junit.Test;

/**
 * The test script for the EventProducer class.
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventProducerTest
{
    /**
     * tests the EventProducer
     */
    @Test
    public void eventProducerTest()
    {
        this.basic();
        this.serialize();
    }

    /**
     * tests the EventProducer for Serializability
     */
    public void serialize()
    {
        try
        {
            File file = File.createTempFile("dsol", ".tmp", new File(System.getProperty("java.io.tmpdir")));
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));

            // Let's test for Serializability
            EventProducerInterface producer = new EventProducerParent();
            output.writeObject(producer);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            producer = (EventProducerInterface) input.readObject();
            input.close();
            assertNotNull(producer);

            // Now we start testing the persistency of listeners after Serialization
            SimpleListener listener = new SimpleListener("Listener");
            producer = new EventProducerParent();
            producer.addListener(listener, EventProducerParent.EVENT_A);
            output.writeObject(producer);

            input = new ObjectInputStream(new FileInputStream(file));
            producer = (EventProducerInterface) input.readObject();

            output.close();
            input.close();
        }
        catch (Exception exception)
        {
            fail();
        }
    }

    /**
     * tests the basic behavior of the eventProducer. All but Serializability, and concurrency is tested.
     */
    public void basic()
    {
        // We start with a basic eventProducer
        EventProducerInterface producer = new EventProducerChild();

        // Now we create some listeners
        EventListenerInterface listener1 = new RemoveListener("listener1");
        EventListenerInterface listener2 = new SimpleListener("listener2");
        EventListenerInterface listener3 = new SimpleListener("listener3");

        // Now we change the EVENT_D and see if the runTimeException occurs
        // This procedure checks for doublures in eventType values
        EventProducerChild.EVENT_D = EventProducerChild.EVENT_C;
        try
        {
            new EventProducerChild();
            // This must fail since EVENT_D and EVENT_C have the same value
            fail("double eventType values");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(RuntimeException.class));
        }

        // Now we test the eventProducer
        try
        {
            producer.addListener(listener2, EventProducerChild.EVENT_E);
            producer.addListener(listener1, EventProducerParent.EVENT_B);
            producer.addListener(listener1, EventProducerChild.EVENT_C);
            producer.addListener(listener1, EventProducerChild.EVENT_E);
            producer.addListener(listener3, EventProducerChild.EVENT_E);

            ((EventProducerChild) producer).fireEvent(new Event(EventProducerChild.EVENT_E, producer, "HI"));

            ((EventProducerChild) producer).fireEvent(new Event(EventProducerChild.EVENT_E, producer, "HI"));

            ((EventProducerChild) producer).fireEvent(new Event(EventProducerParent.EVENT_A, producer, "HI"));

            // we try to remove the listener from a wrong eventType.
            assertFalse(producer.removeListener(listener1, EventProducerParent.EVENT_A));

            // now we try to remove the same listener again.
            assertFalse(producer.removeListener(listener1, EventProducerChild.EVENT_E));

            // Now we subscribe twice on the same event. The first time should
            // succeed. The second fail.
            assertFalse(producer.addListener(listener1, EventProducerParent.EVENT_B));

            // Now we add a null listener
            assertFalse(producer.addListener(null, EventProducerParent.EVENT_A));

            // Now we add some random listeners
            assertTrue(producer.addListener(listener1, EventProducerParent.EVENT_A));

            // assertTrue(producer.removeAllListeners() == 4);
        }
        catch (Exception exception)
        {
            fail(exception.getMessage());
        }
    }

    /**
     * A basic listener class
     */
    private static class RemoveListener implements EventListenerInterface
    {
        /** name is the name of the listener. */
        private String name;

        /**
         * constructs a new Listener1.
         * @param name the name of the listener
         */
        public RemoveListener(final String name)
        {
            this.name = name;
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            assertTrue(event.getType().equals(EventProducerChild.EVENT_E));
            EventProducerInterface producer = (EventProducerInterface) event.getSource();
            assertTrue(producer.removeListener(this, EventProducerChild.EVENT_D));
            assertTrue(producer.removeListener(this, EventProducerChild.EVENT_E));
            assertFalse(producer.removeListener(this, EventProducerChild.EVENT_E));
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return this.name;
        }
    }

    /**
     * A basic listener class
     */
    private static class SimpleListener implements EventListenerInterface, Serializable
    {
        /** The default serial version UID for serializable classes. */
        private static final long serialVersionUID = 1L;

        /** name is the name of the listener. */
        private String name;

        /**
         * constructs a new Listener1.
         * @param name the name of the listener
         */
        public SimpleListener(final String name)
        {
            this.name = name;
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event)
        {
            assertTrue(event != null);
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return this.name;
        }
    }
}
