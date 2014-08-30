package nl.tudelft.simulation.event.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The test script for the EventIterator class.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 */
public class EventIteratorTest extends TestCase implements EventListenerInterface
{
    /** a check on the removed state. */
    private boolean removed = false;

    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public EventIteratorTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param method the name of the test method
     */
    public EventIteratorTest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new Object());
        EventIterator<Object> iterator = new EventIterator<Object>(list.iterator());
        iterator.next();
        iterator.addListener(this, EventIterator.OBJECT_REMOVED_EVENT);
        iterator.remove();
        Assert.assertTrue(this.removed);
    }

    /** {@inheritDoc} */
    public void notify(final EventInterface event)
    {
        if (event.getType().equals(EventIterator.OBJECT_REMOVED_EVENT))
        {
            this.removed = true;
        }
    }
}
