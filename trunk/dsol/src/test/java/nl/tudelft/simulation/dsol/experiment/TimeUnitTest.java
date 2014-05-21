/*
 * @(#) TimeUnitTest.java Aug 26, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.dsol.experiment;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class defines the JUnit test for the TimeUnit class <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a href="mailto:a.verbraeck@tudelft.nl">
 *         Alexander Verbraeck </a>
 */
public class TimeUnitTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * constructs a new TimeUnitTest
     */
    public TimeUnitTest()
    {
        this(TEST_METHOD_NAME);
    }

    /**
     * constructs a new TimeTest
     * @param arg0 the name of the test method
     */
    public TimeUnitTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the TimeClass
     */
    public void test()
    {
        Assert.assertEquals(new Long(TimeUnitInterface.WEEK.getValue()),
                new Long(7L * TimeUnitInterface.DAY.getValue()));
        Assert.assertEquals(new Long(TimeUnitInterface.DAY.getValue()),
                new Long(24L * TimeUnitInterface.HOUR.getValue()));
        Assert.assertEquals(new Long(TimeUnitInterface.HOUR.getValue()),
                new Long(60L * TimeUnitInterface.MINUTE.getValue()));
        Assert.assertEquals(new Long(TimeUnitInterface.MINUTE.getValue()),
                new Long(60L * TimeUnitInterface.SECOND.getValue()));
        Assert.assertEquals(new Long(TimeUnitInterface.SECOND.getValue()), new Long(
                1000L * TimeUnitInterface.MILLISECOND.getValue()));
        Assert.assertEquals(new Long(TimeUnitInterface.MILLISECOND.getValue()),
                new Long(TimeUnitInterface.UNIT.getValue()));
    }
}