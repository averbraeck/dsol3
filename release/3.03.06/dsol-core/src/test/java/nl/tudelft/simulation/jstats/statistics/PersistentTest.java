package nl.tudelft.simulation.jstats.statistics;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.TimedEvent;

/**
 * The PersistentTest test the persistent
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class PersistentTest extends TestCase
{
    /** TEST_METHOD reflects the method which is invoked. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new PersistentTest.
     */
    public PersistentTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new PersistentTest.
     * @param arg0 the name of the method to be tested
     */
    public PersistentTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the persistent
     */
    public void test()
    {
        String description = "THIS PERSISTENT IS TESTED";
        Persistent persistent = new Persistent(description);

        // check the description
        Assert.assertTrue(persistent.toString().equals(description));

        // now we check the initial values
        Assert.assertTrue(new Double(persistent.getMin()).isNaN());
        Assert.assertTrue(new Double(persistent.getMax()).isNaN());
        Assert.assertTrue(new Double(persistent.getSampleMean()).isNaN());
        Assert.assertTrue(new Double(persistent.getSampleVariance()).isNaN());
        Assert.assertTrue(new Double(persistent.getStdDev()).isNaN());
        Assert.assertTrue(new Double(persistent.getSum()).isNaN());
        Assert.assertTrue(persistent.getN() == Long.MIN_VALUE);
        Assert.assertTrue(persistent.getConfidenceInterval(0.95) == null);
        Assert.assertTrue(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // now we initialize the persistent

        persistent.initialize();

        // now we check wether all the properties are correct
        Assert.assertTrue(persistent.getMin() == Double.MAX_VALUE);
        Assert.assertTrue(persistent.getMax() == -Double.MAX_VALUE);
        Assert.assertTrue(new Double(persistent.getSampleMean()).isNaN());
        Assert.assertTrue(new Double(persistent.getSampleVariance()).isNaN());
        Assert.assertTrue(new Double(persistent.getStdDev()).isNaN());
        Assert.assertTrue(persistent.getSum() == 0);
        Assert.assertTrue(persistent.getN() == 0);
        Assert.assertTrue(persistent.getConfidenceInterval(0.95) == null);
        Assert.assertTrue(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // We first fire a wrong event
        try
        {
            persistent.notify(new Event(null, "ERROR", "ERROR"));
            Assert.fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            Assert.assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            persistent.notify(new TimedEvent(null, this, new Double(1.0), 0.0));
            persistent.notify(new TimedEvent(null, this, new Double(1.1), 0.1));
            persistent.notify(new TimedEvent(null, this, new Double(1.2), 0.2));
            persistent.notify(new TimedEvent(null, this, new Double(1.3), 0.3));
            persistent.notify(new TimedEvent(null, this, new Double(1.4), 0.4));
            persistent.notify(new TimedEvent(null, this, new Double(1.5), 0.5));
            persistent.notify(new TimedEvent(null, this, new Double(1.6), 0.6));
            persistent.notify(new TimedEvent(null, this, new Double(1.7), 0.7));
            persistent.notify(new TimedEvent(null, this, new Double(1.8), 0.8));
            persistent.notify(new TimedEvent(null, this, new Double(1.9), 0.9));
            persistent.notify(new TimedEvent(null, this, new Double(2.0), 1.0));
            persistent.notify(new TimedEvent(null, this, new Double(2.1), 1.1));
        }
        catch (Exception exception)
        {
            Assert.fail(exception.getMessage());
        }

        // Now we check the persistent
        Assert.assertTrue(persistent.getMax() == 2.1);
        Assert.assertTrue(persistent.getMin() == 1.0);
        Assert.assertTrue(persistent.getN() == 12);
        Assert.assertTrue(persistent.getSum() == 18.6);
        Assert.assertTrue(Math.abs(persistent.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        Assert.assertTrue(Math.abs(persistent.getSampleVariance() - variance) < 10E-6);
        Assert.assertTrue(Math.abs(persistent.getStdDev() - stDev) < 10E-6);
    }
}
