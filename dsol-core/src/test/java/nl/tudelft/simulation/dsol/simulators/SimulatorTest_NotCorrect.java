package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.SimRuntimeException;

/**
 * The SimulatorTest test the basic behavior of the simulator <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class SimulatorTest_NotCorrect extends TestCase
{
    /** TEST_METHOD refers to the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * simulator is the target to test
     */
    protected SimulatorInterface simulator;

    /**
     * constructs a new SimulatorTest.
     * @param target is the simulator which to test
     */
    public SimulatorTest_NotCorrect(final SimulatorInterface target)
    {
        this(TEST_METHOD, target);
    }

    /**
     * constructs a new SimulatorTest.
     * @param arg0 the name of the test method
     * @param target is the simulator which to test
     */
    public SimulatorTest_NotCorrect(final String arg0, final SimulatorInterface target)
    {
        super(arg0);
        this.simulator = target;
    }

    /**
     * test the DEVS Simulator
     */
    public void test()
    {
        try
        {
            this.simulator.start();
            Assert.fail("Simulator not initialized");
        }
        catch (Exception exception)
        {
            Assert.assertTrue(exception.getClass().equals(SimRuntimeException.class));
        }
        try
        {
            this.simulator.step();
            Assert.fail("Simulator not initialized");
        }
        catch (Exception exception)
        {
            Assert.assertTrue(exception.getClass().equals(SimRuntimeException.class));
        }
        try
        {
            Assert.assertFalse(this.simulator.isRunning());
        }
        catch (Exception exception)
        {
            Assert.assertTrue(exception.getClass().equals(RemoteException.class));
        }
        try
        {
            Assert.assertNull(this.simulator.getReplication());
            // TODO Assert.assertTrue(new Double(this.simulator.getSimulatorTime()).isNaN());
        }
        catch (Exception exception)
        {
            // initialize(null) should throw an exception
            Assert.assertTrue(exception instanceof IllegalArgumentException);
        }
    }
}
