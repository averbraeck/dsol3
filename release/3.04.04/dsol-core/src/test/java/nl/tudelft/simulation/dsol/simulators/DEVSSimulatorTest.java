package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.junit.Test;

import net.jodah.concurrentunit.Waiter;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;

/**
 * The DEVSSimulatorTest test the DEVS Simulator.
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
public class DEVSSimulatorTest implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;
    
    /** the Waiter from ConcurrentUnit that catches AssertionErrors in other threads. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Waiter waiter;

    /**
     * DEVSSimulatorTest.
     * @throws InterruptedException on error
     * @throws TimeoutException on error
     * @throws RemoteException on error
     */
    @Test
    public void testDEVSSimulator() throws TimeoutException, InterruptedException, RemoteException
    {
        this.waiter = new Waiter();
        DEVSSimulatorInterface.TimeDouble devsSimulator = new DEVSSimulator.TimeDouble("DEVSSimulatorTest");
        devsSimulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT);
        ExperimentalFrame experimentalFrame =
                ExperimentUtilDouble.createExperimentalFrame(devsSimulator, new DEVSTestModel(devsSimulator));
        experimentalFrame.start();
        this.waiter.await();
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        this.waiter.resume();
    }

}
