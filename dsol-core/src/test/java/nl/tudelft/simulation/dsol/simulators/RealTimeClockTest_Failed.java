package nl.tudelft.simulation.dsol.simulators;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DESSSSimulatorTest test the DEVS Simulator <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class RealTimeClockTest_Failed extends TestCase
{
    /**
     * constructs a new DEVSSimulatorTest.
     */
    public RealTimeClockTest_Failed()
    {
        super();
    }

    /** */
    public void test()
    {
        try
        {
            RealTimeClock clock = new RealTimeClock(0.1);
            clock.setTimeStep(10);

            ExperimentalFrame experiment = TestExperiment.createExperimentalFrame(clock, new TestModel());
            // TODO experiment.getExperiments().get(0).getTreatment().setRunLength(100000);
            experiment.start();
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Executes a DESSSimulatorTest
     * @param args the arguments given on the command line
     */
    public static void main(final String[] args)
    {
        new RealTimeClockTest_Failed().test();
    }
}
