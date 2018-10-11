package nl.tudelft.simulation.dsol.naming;

import javax.naming.Context;
import javax.naming.NameParser;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.naming.JVMContext;

/**
 * Tests the context.
 * <p>
 * Copyright (c) 2004-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Niels Lang
 * @since 1.5
 */
public class ContextTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /** eventList is the eventList on which the test is fired. */
    private Context context = null;

    /**
     * constructs a new EventListTest.
     * @param context is the context to test
     */
    public ContextTest(final Context context)
    {
        this(ContextTest.TEST_METHOD_NAME, context);
    }

    /**
     * constructs a new EventListTest.
     */
    public ContextTest()
    {
        this(ContextTest.TEST_METHOD_NAME, new JVMContext());
    }

    /**
     * constructs a new EventListTest.
     * @param arg0 the name of the test method
     * @param context is the context on which the test is fired
     */
    public ContextTest(final String arg0, final Context context)
    {
        super(arg0);
        this.context = context;
    }

    /**
     * tests the TreeMapEventListOld
     */
    public void test()
    {
        try
        {
            this.context.createSubcontext("level_1");
            this.context.createSubcontext("level_1/level_2");
            this.context.bind("/level_1/level_2/test", "HelloWorld");

            Context lev2 = ((Context) ((Context) this.context.lookup("level_1")).lookup("level_2"));
            lev2.createSubcontext("./level_3");
            Assert.assertTrue(lev2.lookup("test").equals("HelloWorld"));
            Assert.assertTrue(lev2.lookup("./test").equals("HelloWorld"));

            // Checking empty name
            NameParser parser = lev2.getNameParser("");
            Assert.assertEquals(parser.parse("").size(), 0);

            // Binding @ absolute adress
            lev2.bind("/level_1/test_2", "HelloWorld_2");
            lev2.createSubcontext("level_21");
            lev2.bind("level_21/test_3", "HelloWorld_3");
            Assert.assertEquals(((Context) lev2.lookup("level_3")).lookup("/level_1/level_2/level_21/test_3"),
                    "HelloWorld_3");
        }
        catch (Exception exception)
        {
            Assert.fail(exception.getMessage());
        }
    }
}
