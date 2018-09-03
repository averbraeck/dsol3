package nl.tudelft.simulation.dsol.serialize;

import java.rmi.MarshalledObject;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.process.TestExperimentalFrame;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVDESSAnimator;
import nl.tudelft.simulation.dsol.simulators.DEVDESSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeClock;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * This class defines the JUnit test for the SerializeTest
 * <p>
 * copyright (c) 2004-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class SerializeTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * constructs a new BasicReflectionTest.
     * @param testMethod the name of the test method
     */
    public SerializeTest(final String testMethod)
    {
        super(testMethod);
    }

    /**
     * tests the serializability of the experiment
     */
    public void test()
    {
        try
        {
            // We start with the simulators.
            new MarshalledObject(new DEVSSimulator());
            new MarshalledObject(new DESSSimulator(0.1));
            new MarshalledObject(new DEVDESSSimulator(0.1));
            new MarshalledObject(new DEVDESSAnimator(0.1));
            new MarshalledObject(new DEVSRealTimeClock.TimeDoubleUnit());

            // Now we look at the experiment
            ExperimentalFrame experimentalFrame =
                    TestExperimentalFrame.createExperimentalFrame(new DEVSSimulator(), new Model());
            new MarshalledObject(experimentalFrame);

            // ---------- Let's test the formalisms ----------------

            // The DEVS formalism
            new MarshalledObject(new RedBlackTree());
            new MarshalledObject(new SimEvent(new SimTimeDouble(1.1), "Peter", "Peter", "toString", null));

            // The DESS formalism
            new MarshalledObject(new DifferentialEquation(new DESSSimulator(0.1), 0.1, NumericalIntegrator.ADAMS));

            // The process interaction formalism
            new Process(new DEVSSimulator());

        }
        catch (Exception exception)
        {
            Assert.fail(exception.getMessage());
        }
    }
}
