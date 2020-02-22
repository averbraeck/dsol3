package nl.tudelft.simulation.dsol.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.junit.Test;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.statistics.Counter;

/**
 * The counterTest test the counter.
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
public class SimCounterTest extends EventProducer
{
    /**
     * Test the counter.
     * @throws RemoteException on remote error (should not happen)
     * @throws NamingException on statistics registration error
     */
    @Test
    public void test() throws RemoteException, NamingException
    {
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("sim");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new DummyModel(simulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 10.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        
        String description = "counter description";
        SimCounter.TimeDouble counter = new SimCounter.TimeDouble(description, simulator);
        assertEquals(counter.toString(), description);
        assertEquals(counter.getDescription(), description);

        assertTrue(counter.getN() == Long.MIN_VALUE);
        assertTrue(counter.getCount() == Long.MIN_VALUE);

        counter.initialize();

        counter.addListener(new EventListenerInterface()
        {
            /** */
            private static final long serialVersionUID = 1L;

            @Override
            public void notify(final EventInterface event)
            {
                assertTrue(event.getType().equals(Counter.COUNT_EVENT));
                assertTrue(event.getContent().getClass().equals(Long.class));
            }
        }, Counter.COUNT_EVENT);

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(Counter.COUNT_EVENT, "CounterTest", Long.valueOf(2 * i)));
            value = value + 2 * i;
        }
        assertTrue(counter.getN() == 100);
        assertTrue(counter.getCount() == value);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "CounterTest";
    }
}
