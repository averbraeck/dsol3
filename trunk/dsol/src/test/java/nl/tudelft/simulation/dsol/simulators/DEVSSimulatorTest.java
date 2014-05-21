/*
 * @(#) DEVSSimulatorTest.java Sep 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DEVSSimulatorTest test the DEVS Simulator <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DEVSSimulatorTest extends SimulatorTest
{
    /**
     * constructs a new DEVSSimulatorTest
     */
    public DEVSSimulatorTest()
    {
        super(new DEVSSimulator());
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorTest#test()
     */
    @Override
    public void test()
    {
        super.test();
        ExperimentalFrame experimentalFrame =
                TestExperiment.createExperimentalFrame(new DEVSSimulator(), new DEVSTestModel());
        experimentalFrame.start();
    }

    /**
     * The main method
     * @param args command-line input
     */
    public static void main(final String[] args)
    {
        new DEVSSimulatorTest().test();
    }
}