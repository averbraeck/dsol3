package nl.tudelft.simulation.jstats.statistics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.TimedEvent;

/**
 * The PersistentTest test the persistent
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class PersistentTest
{
    /**
     * tests the persistent.
     */
    @Test
    public void testPersistent()
    {
        String description = "THIS PERSISTENT IS TESTED";
        Persistent persistent = new Persistent(description);

        // check the description
        assertTrue(persistent.toString().equals(description));

        // now we check the initial values
        assertTrue(Double.valueOf(persistent.getMin()).isNaN());
        assertTrue(Double.valueOf(persistent.getMax()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(persistent.getStdDev()).isNaN());
        assertTrue(Double.valueOf(persistent.getSum()).isNaN());
        assertTrue(persistent.getN() == Long.MIN_VALUE);
        assertTrue(persistent.getConfidenceInterval(0.95) == null);
        assertTrue(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // now we initialize the persistent

        persistent.initialize();

        // now we check wether all the properties are correct
        assertTrue(persistent.getMin() == Double.MAX_VALUE);
        assertTrue(persistent.getMax() == -Double.MAX_VALUE);
        assertTrue(Double.valueOf(persistent.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(persistent.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(persistent.getStdDev()).isNaN());
        assertTrue(persistent.getSum() == 0);
        assertTrue(persistent.getN() == 0);
        assertTrue(persistent.getConfidenceInterval(0.95) == null);
        assertTrue(persistent.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // We first fire a wrong event
        try
        {
            persistent.notify(new Event(null, "ERROR", "ERROR"));
            fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.0), 0.0));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.1), 0.1));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.2), 0.2));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.3), 0.3));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.4), 0.4));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.5), 0.5));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.6), 0.6));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.7), 0.7));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.8), 0.8));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(1.9), 0.9));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(2.0), 1.0));
            persistent.notify(new TimedEvent<Double>(null, this, Double.valueOf(2.1), 1.1));
        }
        catch (Exception exception)
        {
            fail(exception.getMessage());
        }

        // Now we check the persistent
        assertTrue(persistent.getMax() == 2.1);
        assertTrue(persistent.getMin() == 1.0);
        assertTrue(persistent.getN() == 12);
        assertTrue(persistent.getSum() == 18.6);
        assertTrue(Math.abs(persistent.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertTrue(Math.abs(persistent.getSampleVariance() - variance) < 10E-6);
        assertTrue(Math.abs(persistent.getStdDev() - stDev) < 10E-6);
    }
}
