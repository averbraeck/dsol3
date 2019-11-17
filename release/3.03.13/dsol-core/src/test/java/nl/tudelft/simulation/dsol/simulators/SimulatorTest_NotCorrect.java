package nl.tudelft.simulation.dsol.simulators;

/**
 * The SimulatorTest test the basic behavior of the simulator.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class SimulatorTest_NotCorrect // extends TestCase
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
        // super(arg0);
        this.simulator = target;
    }

    /**
     * test the DEVS Simulator
     */
    public void test()
    {
        // TODO: not working...

        /*-
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
        */

    }
}
