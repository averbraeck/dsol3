/*
 * @(#) EventIteratorTest.java Sep 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The test script for the Event class.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public class EventTest extends TestCase
{
    /** TEST_METHOD is the name of the test method */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public EventTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest
     * 
     * @param method the name of the test method
     */
    public EventTest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        Object source = this;
        EventType eventType = new EventType("TEST_TYPE");
        Object content = new Object();

        // Some basic tests
        EventInterface event = new Event(eventType, source, content);
        Assert.assertEquals(event.getContent(), content);
        Assert.assertEquals(event.getSource(), source);
        Assert.assertEquals(event.getType(), eventType);

    }
}