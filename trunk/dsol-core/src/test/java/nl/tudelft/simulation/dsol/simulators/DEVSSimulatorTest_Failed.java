package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DEVSSimulatorTest test the DEVS Simulator <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
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
        new DEVSSimulatorTest_Failed().test();
    }
}
