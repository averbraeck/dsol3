package nl.tudelft.simulation.jstats.statistics;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.event.Event;

/**
 * The TallyTest test the tally.
 * <p>
 * (c) 2002-2018-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.6, 2004-03-18
 * @since 1.5
 */
public class TallyTest extends TestCase
{
    /** TEST_METHOD reflects the method which is invoked. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new TallyTest.
     */
    public TallyTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new TallyTest.
     * @param arg0 the name of the method to be tested
     */
    public TallyTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the tally
     */
    public void test()
    {
        String description = "THIS TALLY IS TESTED";
        Tally tally = new Tally(description);

        // check the description
        Assert.assertTrue(tally.toString().equals(description));

        // now we check the initial values
        Assert.assertTrue(new Double(tally.getMin()).isNaN());
        Assert.assertTrue(new Double(tally.getMax()).isNaN());
        Assert.assertTrue(new Double(tally.getSampleMean()).isNaN());
        Assert.assertTrue(new Double(tally.getSampleVariance()).isNaN());
        Assert.assertTrue(new Double(tally.getStdDev()).isNaN());
        Assert.assertTrue(new Double(tally.getSum()).isNaN());
        Assert.assertTrue(tally.getN() == Long.MIN_VALUE);
        Assert.assertTrue(tally.getConfidenceInterval(0.95) == null);
        Assert.assertTrue(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // now we initialize the tally
        tally.initialize();

        // now we check wether all the properties are correct
        Assert.assertTrue(tally.getMin() == Double.MAX_VALUE);
        Assert.assertTrue(tally.getMax() == -Double.MAX_VALUE);
        Assert.assertTrue(new Double(tally.getSampleMean()).isNaN());
        Assert.assertTrue(new Double(tally.getSampleVariance()).isNaN());
        Assert.assertTrue(new Double(tally.getStdDev()).isNaN());
        Assert.assertTrue(tally.getSum() == 0);
        Assert.assertTrue(tally.getN() == 0);
        Assert.assertTrue(tally.getConfidenceInterval(0.95) == null);
        Assert.assertTrue(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);

        // We first fire a wrong event
        try
        {
            tally.notify(new Event(null, "ERROR", "ERROR"));
            Assert.fail("tally should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            Assert.assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            tally.notify(new Event(null, this, new Double(1.0)));
            tally.notify(new Event(null, this, new Double(1.1)));
            tally.notify(new Event(null, this, new Double(1.2)));
            tally.notify(new Event(null, this, new Double(1.3)));
            tally.notify(new Event(null, this, new Double(1.4)));
            tally.notify(new Event(null, this, new Double(1.5)));
            tally.notify(new Event(null, this, new Double(1.6)));
            tally.notify(new Event(null, this, new Double(1.7)));
            tally.notify(new Event(null, this, new Double(1.8)));
            tally.notify(new Event(null, this, new Double(1.9)));
            tally.notify(new Event(null, this, new Double(2.0)));
        }
        catch (Exception exception)
        {
            Assert.fail(exception.getMessage());
        }

        // Now we check the tally
        Assert.assertTrue(tally.getMax() == 2.0);
        Assert.assertTrue(tally.getMin() == 1.0);
        Assert.assertTrue(tally.getN() == 11);
        Assert.assertTrue(tally.getSum() == 16.5);
        double mean = Math.round(1000 * tally.getSampleMean()) / 1000.0;
        Assert.assertTrue(mean == 1.5);
        double variance = Math.round(1000 * tally.getSampleVariance()) / 1000.0;
        Assert.assertTrue(variance == 0.11);
        double stdv = Math.round(1000 * tally.getStdDev()) / 1000.0;
        Assert.assertTrue(stdv == 0.332);
        double confidence = Math.round(1000 * tally.getConfidenceInterval(0.05)[0]) / 1000.0;
        Assert.assertTrue(confidence == 1.304);

        // we check the input of the confidence interval
        try
        {
            tally.getConfidenceInterval(0.95, (short) 14);
            Assert.fail("14 is not defined as side of confidence level");
        }
        catch (Exception exception)
        {
            Assert.assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }
        try
        {
            Assert.assertTrue(tally.getConfidenceInterval(-0.95) == null);
            Assert.assertTrue(tally.getConfidenceInterval(1.14) == null);
            Assert.fail("should have reacted on wrong confidence levels");
        }
        catch (Exception exception)
        {
            Assert.assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }

        Assert.assertTrue(Math.abs(tally.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        Assert.assertTrue(Math.abs(tally.getSampleVariance() - variance) < 10E-6);
        Assert.assertTrue(Math.abs(tally.getStdDev() - stDev) < 10E-6);
    }
}
