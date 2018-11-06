package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DEVSSimulatorTest test the DEVS Simulator.
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
public class DEVSSimulatorTest_Failed extends SimulatorTest_NotCorrect
{
    /**
     * constructs a new DEVSSimulatorTest.
     */
    public DEVSSimulatorTest_Failed()
    {
        super(new DEVSSimulator());
    }

    /** {@inheritDoc} */
    @Override
    public void test()
    {
        super.test();
        DEVSSimulatorInterface.TimeDouble devsSimulator = new DEVSSimulator.TimeDouble();
        ExperimentalFrame experimentalFrame =
                TestExperiment.createExperimentalFrame(devsSimulator, new DEVSTestModel(devsSimulator));
        experimentalFrame.start();
    }

    /**
     * The main method.
     * @param args command-line input
     */
    public static void main(final String[] args)
    {
        new DEVSSimulatorTest_Failed().test();
    }
}
