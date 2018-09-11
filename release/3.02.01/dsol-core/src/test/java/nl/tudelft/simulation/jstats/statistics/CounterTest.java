package nl.tudelft.simulation.jstats.statistics;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The counterTest test the counter.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.0, 2004-03-18
 * @since 1.5
 */
public class CounterTest extends TestCase
{
    /** TEST_METHOD reflects the method which is invoked. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new CounterTest.
     */
    public CounterTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new CounterTest.
     * @param arg0 the name of the test method
     */
    public CounterTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the counter
     */
    public void test()
    {
        String description = "counter description";
        Counter counter = new Counter(description);
        Assert.assertEquals(counter.toString(), description);
        Assert.assertEquals(counter.getDescription(), description);

        Assert.assertTrue(counter.getN() == Long.MIN_VALUE);
        Assert.assertTrue(counter.getCount() == Long.MIN_VALUE);

        counter.initialize();

        counter.addListener(new EventListenerInterface()
        {
            public void notify(final EventInterface event)
            {
                Assert.assertTrue(event.getType().equals(Counter.COUNT_EVENT));
                Assert.assertTrue(event.getContent().getClass().equals(Long.class));
            }
        }, Counter.COUNT_EVENT);

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(Counter.COUNT_EVENT, this, new Long(2 * i)));
            value = value + 2 * i;
        }
        Assert.assertTrue(counter.getN() == 100);
        Assert.assertTrue(counter.getCount() == value);
    }
}
