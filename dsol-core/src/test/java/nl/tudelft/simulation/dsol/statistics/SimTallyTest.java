package nl.tudelft.simulation.dsol.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.event.EventProducer;
import org.djutils.event.TimedEvent;
import org.djutils.event.TimedEventType;
import org.djutils.stats.ConfidenceInterval;
import org.junit.Test;

import nl.tudelft.simulation.dsol.experiment.SingleReplication;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The SimTallyTest tests the SimTally.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    private static final TimedEventType UPDATE_EVENT = new TimedEventType("UpdateEvent");

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "TallyTest";
    }

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
        SingleReplication.TimeDouble replication = new SingleReplication.TimeDouble("rep1", 0.0, 0.0, 10.0);
        simulator.initialize(model, replication);

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

        // remove the tally from he statistics
        replication.removeFromContext();
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
        SingleReplication.TimeDouble replication = new SingleReplication.TimeDouble("rep1", 0.0, 0.0, 10.0);
        simulator.initialize(model, replication);

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

        // remove the tally from he statistics
        replication.removeFromContext();
    }

    /**
     * Check the uninitialized tally.
     * @param tally the tally to test
     */
    private void checkBefore(final SimTally.TimeDouble tally)
    {
        // check the description
        assertEquals("THIS TALLY IS TESTED", tally.getDescription());

        // now we check the initial values
        assertTrue(Double.isNaN(tally.getMin()));
        assertTrue(Double.isNaN(tally.getMax()));
        assertTrue(Double.isNaN(tally.getSampleMean()));
        assertTrue(Double.isNaN(tally.getSampleVariance()));
        assertTrue(Double.isNaN(tally.getSampleStDev()));
        assertEquals(0.0, tally.getSum(), 1E-6);
        assertEquals(0L, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.LEFT_SIDE_CONFIDENCE));
    }

    /**
     * Check the initialized tally.
     * @param tally the tally to test
     */
    private void checkInitialized(final SimTally.TimeDouble tally)
    {
        assertTrue(Double.isNaN(tally.getMin()));
        assertTrue(Double.isNaN(tally.getMax()));
        assertTrue(Double.isNaN(tally.getSampleMean()));
        assertTrue(Double.isNaN(tally.getSampleVariance()));
        assertTrue(Double.isNaN(tally.getSampleStDev()));
        assertEquals(0.0, tally.getSum(), 1E-6);
        assertEquals(0L, tally.getN());
        assertNull(tally.getConfidenceInterval(0.95));
        assertNull(tally.getConfidenceInterval(0.95, ConfidenceInterval.LEFT_SIDE_CONFIDENCE));
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
            tally.notify(new TimedEvent<String>(UPDATE_EVENT, this, "ERROR", "ERROR"));
            fail("tally should react on timed event.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some events
        try
        {
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.0, 0.1));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.1, 0.2));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.2, 0.3));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.3, 0.4));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.4, 0.5));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.5, 0.6));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.6, 0.7));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.7, 0.8));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.8, 0.9));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 1.9, 1.0));
            tally.notify(new TimedEvent<Double>(UPDATE_EVENT, "TallyTest", 2.0, 1.1));
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
        assertEquals(2.0, tally.getMax(), 1E-6);
        assertEquals(1.0, tally.getMin(), 1E-6);
        assertEquals(11L, tally.getN());
        assertEquals(16.5, tally.getSum(), 1E-6);
        assertEquals(1.5, tally.getSampleMean(), 1E-6);
        assertEquals(0.11, tally.getSampleVariance(), 1E-6);
        assertEquals(Math.sqrt(0.11), tally.getSampleStDev(), 1E-6);
        assertEquals(1.304, tally.getConfidenceInterval(0.05)[0], 1E-3);

        // we check the input of the confidence interval
        try
        {
            assertNull(tally.getConfidenceInterval(-0.95));
            fail("should have reacted on wrong confidence levels");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }
        try
        {
            assertNull(tally.getConfidenceInterval(1.14));
            fail("should have reacted on wrong confidence levels");
        }
        catch (Exception exception)
        {
            assertTrue(exception.getClass().equals(IllegalArgumentException.class));
        }

        assertEquals(1.5, tally.getSampleMean(), 1E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, tally.getSampleVariance(), 1E-6);
        assertEquals(stDev, tally.getSampleStDev(), 1E-6);
    }

}
