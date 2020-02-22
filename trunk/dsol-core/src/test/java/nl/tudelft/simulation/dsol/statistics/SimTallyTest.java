package nl.tudelft.simulation.dsol.statistics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.event.Event;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.junit.Test;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.statistics.Tally;

/**
 * The TallyTest test the tally.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class SimTallyTest extends EventProducer
{
    /** */
    private static final long serialVersionUID = 1L;

    /** update event. */
    private static final EventType UPDATE_EVENT = new EventType("UpdateEvent");

    /**
     * Test the tally.
     * @throws RemoteException on remote error (should not happen)
     * @throws NamingException on failure registering the replication or statistic in the Context
     */
    @Test
    public void testTallyTimeDouble() throws RemoteException, NamingException
    {
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("sim");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new DummyModel(simulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 10.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);

        String description = "THIS TALLY IS TESTED";
        SimTally.TimeDouble tally = new SimTally.TimeDouble(description, simulator);

        // check uninitialized tally
        checkBefore(tally);

        // now we initialize the tally
        tally.initialize();

        // check the initialized tally
        checkInitialized(tally);

        // fire evets to fill the tally
        fireEvents(tally);

        // Now we check the tally
        check(tally);
    }

    /**
     * Test the tally.
     * @throws RemoteException on remote error (should not happen)
     * @throws NamingException on failure registering the replication or statistic in the Context
     */
    @Test
    public void testTallyEventProducerTimeDouble() throws RemoteException, NamingException
    {
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("sim");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new DummyModel(simulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 10.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);

        String description = "THIS TALLY IS TESTED";
        SimTally.TimeDouble tally = new SimTally.TimeDouble(description, simulator, this, UPDATE_EVENT);

        // check uninitialized tally
        checkBefore(tally);

        // now we initialize the tally
        tally.initialize();

        // check the initialized tally
        checkInitialized(tally);

        // fire evets to fill the tally
        fireEvents(tally);

        // Now we check the tally
        check(tally);
    }

    /**
     * Check the uninitialized tally.
     * @param tally the tally to test
     */
    private void checkBefore(final SimTally.TimeDouble tally)
    {
        // check the description
        assertTrue(tally.toString().equals("THIS TALLY IS TESTED"));

        // now we check the initial values
        assertTrue(Double.valueOf(tally.getMin()).isNaN());
        assertTrue(Double.valueOf(tally.getMax()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getStdDev()).isNaN());
        assertTrue(Double.valueOf(tally.getSum()).isNaN());
        assertTrue(tally.getN() == Long.MIN_VALUE);
        assertTrue(tally.getConfidenceInterval(0.95) == null);
        assertTrue(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);
    }

    /**
     * Check the initialized tally.
     * @param tally the tally to test
     */
    private void checkInitialized(final SimTally.TimeDouble tally)
    {
        assertTrue(tally.getMin() == Double.MAX_VALUE);
        assertTrue(tally.getMax() == -Double.MAX_VALUE);
        assertTrue(Double.valueOf(tally.getSampleMean()).isNaN());
        assertTrue(Double.valueOf(tally.getSampleVariance()).isNaN());
        assertTrue(Double.valueOf(tally.getStdDev()).isNaN());
        assertTrue(tally.getSum() == 0);
        assertTrue(tally.getN() == 0);
        assertTrue(tally.getConfidenceInterval(0.95) == null);
        assertTrue(tally.getConfidenceInterval(0.95, Tally.LEFT_SIDE_CONFIDENCE) == null);
    }

    /**
     * Fire events to the initialized tally.
     * @param tally the tally to test
     */
    private void fireEvents(final SimTally.TimeDouble tally)
    {
        // We first fire a wrong event
        try
        {
            tally.notify(new Event(UPDATE_EVENT, "ERROR", "ERROR"));
            fail("tally should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.0)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.1)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.2)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.3)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.4)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.5)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.6)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.7)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.8)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(1.9)));
            tally.notify(new Event(UPDATE_EVENT, "TallyTest", Double.valueOf(2.0)));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            fail(exception.getMessage());
        }
    }

    /**
     * Check the filled tally.
     * @param tally the tally to test
     */
    private void check(final SimTally.TimeDouble tally)
    {
        assertTrue(tally.getMax() == 2.0);
        assertTrue(tally.getMin() == 1.0);
        assertTrue(tally.getN() == 11);
        assertTrue(tally.getSum() == 16.5);
        double mean = Math.round(1000 * tally.getSampleMean()) / 1000.0;
        assertTrue(mean == 1.5);
        double variance = Math.round(1000 * tally.getSampleVariance()) / 1000.0;
        assertTrue(variance == 0.11);
        double stdv = Math.round(1000 * tally.getStdDev()) / 1000.0;
        assertTrue(stdv == 0.332);
        double confidence = Math.round(1000 * tally.getConfidenceInterval(0.05)[0]) / 1000.0;
        assertTrue(confidence == 1.304);

        // we check the input of the confidence interval
        try
        {
            tally.getConfidenceInterval(0.95, (short) 14);
            fail("14 is not defined as side of confidence level");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }
        try
        {
            assertTrue(tally.getConfidenceInterval(-0.95) == null);
            assertTrue(tally.getConfidenceInterval(1.14) == null);
            fail("should have reacted on wrong confidence levels");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }

        assertTrue(Math.abs(tally.getSampleMean() - 1.5) < 10E-6);

        // Let's compute the standard deviation
        variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertTrue(Math.abs(tally.getSampleVariance() - variance) < 10E-6);
        assertTrue(Math.abs(tally.getStdDev() - stDev) < 10E-6);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "TallyTest";
    }

}
