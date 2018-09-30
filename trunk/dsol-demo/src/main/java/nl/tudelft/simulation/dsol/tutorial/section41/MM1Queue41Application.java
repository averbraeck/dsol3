package nl.tudelft.simulation.dsol.tutorial.section41;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.pmw.tinylog.Level;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Queue41Application
{
    /** */
    private DEVSSimulator.TimeDouble simulator;

    /** */
    private MM1Queue41Model model;

    /**
     * Construct a console application.
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    protected MM1Queue41Application() throws SimRuntimeException, RemoteException, NamingException
    {
        this.model = new MM1Queue41Model();
        this.simulator = new DEVSSimulator.TimeDouble();
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 1000.0, this.model);
        this.simulator.initialize(replication, ReplicationMode.TERMINATING);
        this.simulator.scheduleEventAbs(1000.0, this, this, "terminate", null);
        this.simulator.start();
    }

    /** stop the simulation. */
    protected final void terminate()
    {
        System.out.println("average queue length = " + this.model.qN.getSampleMean());
        System.out.println("average queue wait   = " + this.model.dN.getSampleMean());
        System.out.println("average utilization  = " + this.model.uN.getSampleMean());

        System.exit(0);
    }

    /**
     * @param args can be left empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        SimLogger.setAllLogLevel(Level.TRACE);
        new MM1Queue41Application();
    }

}
