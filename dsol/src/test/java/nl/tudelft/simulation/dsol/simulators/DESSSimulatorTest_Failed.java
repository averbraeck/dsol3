/*
 * @(#) DEVSSimulatorTest.java Sep 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DESSSSimulatorTest test the DEVS Simulator <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DESSSimulatorTest_Failed extends SimulatorTest_NotCorrect
{
    /**
     * constructs a new DEVSSimulatorTest
     */
    public DESSSimulatorTest_Failed()
    {
        super(new DESSSimulator());
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorTest_NotCorrect#test()
     */
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
     */
    public static void main(final String[] args)
    {
        new DESSSimulatorTest_Failed().test();
    }
}