package nl.tudelft.simulation.dsol.tutorial.section41;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.pmw.tinylog.Level;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 * M/M/1 queuing model with animation and graphs.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Queue41SwingApplication extends DSOLApplication
{
    /** the model. */
    private MM1Queue41Model model;

    /**
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     * @param model MM1Queue41Model; the model
     * @param devsSimulator DEVSSimulator.TimeDouble; the simulator
     */
    public MM1Queue41SwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel,
            final MM1Queue41Model model, final DEVSSimulator.TimeDouble devsSimulator)
    {
        super(title, panel);
        this.model = model;
        panel.getConsole().setLogLevel(Level.TRACE);
        try
        {
            devsSimulator.scheduleEventAbs(1000.0, this, this, "terminate", null);
        }
        catch (SimRuntimeException exception)
        {
            SimLogger.always().error(exception, "<init>");
        }
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        SimLogger.setAllLogLevel(Level.TRACE);
        MM1Queue41Model model = new MM1Queue41Model();
        DEVSSimulator.TimeDouble devsSimulator = new DEVSSimulator.TimeDouble();
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 1000.0, model);
        devsSimulator.initialize(replication, ReplicationMode.TERMINATING);
        SimLogger.setSimulator(devsSimulator);
        MM1Queue41Panel panel = new MM1Queue41Panel(model, devsSimulator);
        new MM1Queue41SwingApplication("MM1 Queue model", panel, model, devsSimulator);
    }

    /** stop the simulation. */
    protected final void terminate()
    {
        SimLogger.always().info("average queue length = " + this.model.qN.getSampleMean());
        SimLogger.always().info("average queue wait   = " + this.model.dN.getSampleMean());
        SimLogger.always().info("average utilization  = " + this.model.uN.getSampleMean());
    }

}
