package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DESSSSimulatorTest test the DEVS Simulator.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DESSSimulatorTest_Failed extends SimulatorTest_NotCorrect
{
    /**
     * constructs a new DEVSSimulatorTest.
     * @throws SimRuntimeException on error
     */
    public DESSSimulatorTest_Failed() throws SimRuntimeException
    {
        super(new DESSSimulator(0.1));
    }

    /** {@inheritDoc} */
    @Override
    public void test()
    {
        super.test();

        DESSSimulatorInterface dessSimulator = (DESSSimulatorInterface) super.simulator;
        ExperimentalFrame experimentalFrame = TestExperiment.createExperimentalFrame(dessSimulator, new TestModel());
        experimentalFrame.start();

    }

    /**
     * Executes a DESSSimulatorTest
     * @param args the arguments given on the command line
     * @throws SimRuntimeException on error
     */
    public static void main(final String[] args) throws SimRuntimeException
    {
        new DESSSimulatorTest_Failed().test();
    }
}
