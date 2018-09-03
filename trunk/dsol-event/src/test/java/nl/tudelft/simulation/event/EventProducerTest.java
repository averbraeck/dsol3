package nl.tudelft.simulation.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

import junit.framework.TestCase;

/**
 * The test script for the EventProducer class.
 * <p>
 * (c) 2002-2018-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public class EventProducerTest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public EventProducerTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param arg0 the name of the test method
     */
    public EventProducerTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the EventProducer
     */
    public void test()
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
            EventProducerInterface producer = new EventProducerChild();
            output.writeObject(producer);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            producer = (EventProducerInterface) input.readObject();
            input.close();
            assertNotNull(producer);

            // Now we start testing the persistency of listeners after Serialization
            SimpleListener listener = new SimpleListener("Listener");
            producer = new EventProducerChild();
            producer.addListener(listener, EventProducerChild.EVENT_A);
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
        EventProducerInterface producer = new EventProducerParent();

        // Now we create some listeners
        EventListenerInterface listener1 = new RemoveListener("listener1");
        EventListenerInterface listener2 = new SimpleListener("listener2");
        EventListenerInterface listener3 = new SimpleListener("listener3");

        // Now we change the EVENT_D and see if the runTimeException occurs
        // This procedure checks for doublures in eventType values
        EventProducerParent.eventD = EventProducerParent.EVENT_C;
        try
        {
            new EventProducerParent();
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
            producer.addListener(listener2, EventProducerParent.EVENT_E);
            producer.addListener(listener1, EventProducerChild.EVENT_B);
            producer.addListener(listener1, EventProducerParent.EVENT_C);
            producer.addListener(listener1, EventProducerParent.EVENT_E);
            producer.addListener(listener3, EventProducerParent.EVENT_E);

            ((EventProducerParent) producer).fireEvent(new Event(EventProducerParent.EVENT_E, producer, "HI"));

            ((EventProducerParent) producer).fireEvent(new Event(EventProducerParent.EVENT_E, producer, "HI"));

            ((EventProducerParent) producer).fireEvent(new Event(EventProducerChild.EVENT_A, producer, "HI"));

            // we try to remove the listener from a wrong eventType.
            assertFalse(producer.removeListener(listener1, EventProducerChild.EVENT_A));

            // now we try to remove the same listener again.
            assertFalse(producer.removeListener(listener1, EventProducerParent.EVENT_E));

            // Now we subscribe twice on the same event. The first time should
            // succeed. The second fail.
            assertFalse(producer.addListener(listener1, EventProducerChild.EVENT_B));

            // Now we add a null listener
            assertFalse(producer.addListener(null, EventProducerChild.EVENT_A));

            // Now we add some random listeners
            assertTrue(producer.addListener(listener1, EventProducerChild.EVENT_A));

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

        /** {@inheritDoc} */ @Override public  void notify(final EventInterface event) throws RemoteException
        {
            assertTrue(event.getType().equals(EventProducerParent.EVENT_E));
            EventProducerInterface producer = (EventProducerInterface) event.getSource();
            assertTrue(producer.removeListener(this, EventProducerParent.eventD));
            assertTrue(producer.removeListener(this, EventProducerParent.EVENT_E));
            assertFalse(producer.removeListener(this, EventProducerParent.EVENT_E));
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

        /** {@inheritDoc} */ @Override public  void notify(final EventInterface event)
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
