package nl.tudelft.simulation.dsol.eventList;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

/**
 * This class defines the JUnit test for the TreeMapEventListOld <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class EventListTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * eventList is the eventList on which the test is fired
     */
    private EventListInterface<SimTimeDouble> eventList = null;

    /**
     * constructs a new RedblackTree.
     */
    public EventListTest()
    {
        this(new RedBlackTree());
    }

    /**
     * constructs a new BasicReflectionTest.
     * @param eventList is the eventList on which the test is fired
     */
    public EventListTest(final EventListInterface<SimTimeDouble> eventList)
    {
        this(EventListTest.TEST_METHOD_NAME, eventList);
    }

    /**
     * constructs a new BasicReflectionTest.
     * @param arg0 the name of the test method
     * @param eventList is the eventList on which the test is fired
     */
    public EventListTest(final String arg0, final EventListInterface<SimTimeDouble> eventList)
    {
        super(arg0);
        this.eventList = eventList;
    }

    /**
     * tests the TreeMapEventListOld
     */
    public void test()
    {
        Assert.assertNotNull(this.eventList);
        try
        {
            // We fill the eventList with 500 events with random times
            // between [0..200]
            for (int i = 0; i < 500; i++)
            {
                this.eventList.add(new SimEvent<SimTimeDouble>(new SimTimeDouble(200 * Math.random()), this,
                        new String(), "trim", null));
            }

            // Now we assert some getters on the eventList
            Assert.assertTrue(!this.eventList.isEmpty());
            Assert.assertTrue(this.eventList.size() == 500);

            // Let's see if the eventList was properly ordered
            double time = 0;
            for (int i = 0; i < 500; i++)
            {
                SimEventInterface<SimTimeDouble> simEvent = this.eventList.first();
                this.eventList.remove(this.eventList.first());
                double executionTime = simEvent.getAbsoluteExecutionTime().get().doubleValue();
                Assert.assertTrue(executionTime >= 0.0);
                Assert.assertTrue(executionTime <= 200.0);
                Assert.assertTrue(executionTime >= time);
                time = executionTime;
            }

            // Now we fill the eventList with a number of events with
            // different priorities on time=0.0
            for (int i = 1; i < 10; i++)
            {
                this.eventList.add(new SimEvent<SimTimeDouble>(new SimTimeDouble(0.0), (short) i, this, new String(),
                        "trim", null));
            }
            short priority = SimEventInterface.MAX_PRIORITY;

            // Let's empty the eventList and check the priorities
            while (!this.eventList.isEmpty())
            {
                SimEventInterface<SimTimeDouble> simEvent = this.eventList.first();
                this.eventList.remove(this.eventList.first());
                double executionTime = simEvent.getAbsoluteExecutionTime().get().doubleValue();
                short eventPriority = simEvent.getPriority();

                Assert.assertTrue(executionTime == 0.0);
                Assert.assertTrue(eventPriority <= SimEventInterface.MAX_PRIORITY);
                Assert.assertTrue(eventPriority >= SimEventInterface.MIN_PRIORITY);
                Assert.assertTrue(eventPriority <= priority);
                priority = eventPriority;
            }

            // Let's check the empty eventList
            Assert.assertTrue(this.eventList.isEmpty());
            Assert.assertNull(this.eventList.first());
            // TODO this gives an error
            // Assert.assertFalse(this.eventList.remove(null));
            Assert.assertFalse(this.eventList
                    .remove(new SimEvent(new SimTimeDouble(200 * Math.random()), this, new String(), "trim", null)));
            this.eventList.clear();

            // Let's cancel an event
            this.eventList.add(new SimEvent(new SimTimeDouble(100), this, this, "toString", null));
            SimEventInterface simEvent = new SimEvent(new SimTimeDouble(100), this, this, "toString", null);
            this.eventList.add(simEvent);
            assertTrue(this.eventList.remove(simEvent));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            Assert.fail(exception.getMessage());
        }
    }
}
