package nl.tudelft.simulation.dsol.serialize;

import java.io.IOException;
import java.rmi.MarshalledObject;

import org.junit.Test;

import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.process.ExperimentalFrameUtil;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVDESSAnimator;
import nl.tudelft.simulation.dsol.simulators.DEVDESSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeClock;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegratorType;

/**
 * This class defines the JUnit test for the SerializeTest.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class SerializeTest
{

    /**
     * tests the serializability of several simulator objects
     * @throws IOException on marshalling error
     */
    @Test
    public void testSerializability() throws IOException
    {
        // We start with the simulators.
        new MarshalledObject(new DEVSSimulator("SerializeTest"));
        new MarshalledObject(new DESSSimulator("SerializeTest", 0.1));
        new MarshalledObject(new DEVDESSSimulator("SerializeTest", 0.1));
        new MarshalledObject(new DEVDESSAnimator("SerializeTest", 0.1));
        new MarshalledObject(new DEVSRealTimeClock.TimeDoubleUnit("SerializeTest"));

        // Now we look at the experiment
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("SerializeTest");
        ExperimentalFrame experimentalFrame = ExperimentalFrameUtil.createExperimentalFrame(simulator, new Model(simulator));
        new MarshalledObject(experimentalFrame);

        // ---------- Let's test the formalisms ----------------

        // The DEVS formalism
        new MarshalledObject(new RedBlackTree());
        new MarshalledObject(new SimEvent(new SimTimeDouble(1.1), "Peter", "Peter", "toString", null));

        // The DESS formalism
        new MarshalledObject(
                new SimpleDifferentialEquation(new DESSSimulator("SerializeTest", 0.1), 0.1, NumericalIntegratorType.ADAMS, 1));

        // The process interaction formalism
        // XXX: gives error; first check interpreter package: new Process(new DEVSSimulator());

    }
}
