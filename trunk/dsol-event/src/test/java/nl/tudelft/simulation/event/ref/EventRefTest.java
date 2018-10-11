package nl.tudelft.simulation.event.ref;

import java.rmi.MarshalledObject;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The test script for the reference package. All classes in this package are tested with this test
 * <p>
 * Copyright (c) 2004-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventRefTest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public EventRefTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param method the name of the test method
     */
    public EventRefTest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        try
        {
            // Test 1: We since we have a pointer to referent, gc should not
            // clean the weakReference

            Object referent = new String("EventIteratorTest");
            /*
             * It is absolutely amazing what you see if you replace the above with the following: Object referent =
             * "EventIteratorTest";
             */

            Reference<Object> reference = new WeakReference<Object>(referent);
            Assert.assertEquals(reference.get(), referent);
            Runtime.getRuntime().gc();
            Assert.assertNotNull(reference.get());

            // Test 2: We since we have a pointer to referent, gc should
            // clean the weakReference
            reference = new WeakReference<Object>(new String("EventIteratorTest"));
            Runtime.getRuntime().gc();
            Assert.assertNull(reference.get());

            // Test 3: The strong reference..
            reference = new StrongReference<Object>(new String("EventIteratorTest"));
            Assert.assertNotNull(reference.get());
            Runtime.getRuntime().gc();
            Assert.assertNotNull(reference.get());

            // A Strong one
            new MarshalledObject<Object>(new StrongReference<Object>(new Double(12))).get();

            // A Weak one
            new MarshalledObject<Object>(new WeakReference<Object>(new Double(12))).get();
        }
        catch (Throwable throwable)
        {
            // runtime exceptions are not appreciated
            throwable.printStackTrace();
            Assert.fail();
        }
    }
}
