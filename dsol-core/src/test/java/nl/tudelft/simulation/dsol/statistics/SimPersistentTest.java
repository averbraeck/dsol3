package nl.tudelft.simulation.dsol.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.event.EventProducer;
import org.djutils.event.TimedEvent;
import org.junit.Test;

import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.experiment.SingleReplication;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The SimPersistentTest test the SimPersistent.
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
public class SimPersistentTest extends EventProducer
{

    /** */
    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "SimPersistentTest";
    }

    /**
     * Test the SimPersistent.
     * @throws NamingException for failure to register the statistics in the context
     * @throws RemoteException on remote communication error with the statistic
     */
    @Test
    public void testSimPersistent() throws NamingException, RemoteException
    {
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("sim");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new DummyModel(simulator);
        ReplicationInterface.TimeDouble replication = new SingleReplication.TimeDouble("rep1", 0.0, 0.0, 10.0);
        simulator.initialize(model, replication);

        String description = "THIS PERSISTENT IS TESTED";
        SimPersistent.TimeDouble persistent = new SimPersistent.TimeDouble(description, simulator);

        // check the description
        assertEquals(description, persistent.getDescription());

        // now we check the initial values
        assertTrue(Double.isNaN(persistent.getMin()));
        assertTrue(Double.isNaN(persistent.getMax()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleMean()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleStDev()));
        assertEquals(0.0, persistent.getWeightedSum(), 1E-6);
        assertEquals(0L, persistent.getN());

        // now we initialize the persistent
        persistent.initialize();

        // now we check whether all the properties are correct
        assertTrue(Double.isNaN(persistent.getMin()));
        assertTrue(Double.isNaN(persistent.getMax()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleMean()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleVariance()));
        assertTrue(Double.isNaN(persistent.getWeightedSampleStDev()));
        assertEquals(0.0, persistent.getWeightedSum(), 1E-6);
        assertEquals(0L, persistent.getN());

        // We fire a first event
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.0, 0.0));

        // We fire a wrong event
        try
        {
            persistent.notify(new TimedEvent<String>(null, this, "ERROR", "ERROR"));
            fail("persistent should react on events.value !instanceOf Double");
        }
        catch (Exception exception)
        {
            assertNotNull(exception);
        }

        // Now we fire some more events
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.1, 0.1));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.2, 0.2));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.3, 0.3));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.4, 0.4));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.5, 0.5));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.6, 0.6));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.7, 0.7));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.8, 0.8));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 1.9, 0.9));
        persistent.notify(new TimedEvent<Double>(null, "SimPersistentTest", 2.0, 1.0));
        persistent.endObservations(1.1);

        // Now we check the persistent
        assertEquals(2.0, persistent.getMax(), 1E-6);
        assertEquals(1.0, persistent.getMin(), 1E-6);
        assertEquals(11, persistent.getN());
        assertEquals(18.6, persistent.getWeightedSum(), 1E06);
        assertEquals(1.5, persistent.getWeightedSampleMean(), 1E-6);

        // Let's compute the standard deviation
        double variance = 0;
        for (int i = 0; i < 11; i++)
        {
            variance = Math.pow(1.5 - (1.0 + i / 10.0), 2) + variance;
        }
        variance = variance / 10.0;
        double stDev = Math.sqrt(variance);

        assertEquals(variance, persistent.getWeightedSampleVariance(), 1E-6);
        assertEquals(stDev, persistent.getWeightedSampleStDev(), 1E-6);
    }

}
