package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DESSSSimulatorTest test the DEVS Simulator <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
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
