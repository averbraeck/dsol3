package nl.tudelft.simulation.dsol.tutorial.section41;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.logger.ConsoleLogger;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Queue41SwingApplication extends DSOLApplication
{
    /** the model. */
    private MM1Queue41Model model;

    /**
     * @param title the title
     * @param panel the panel
     * @param model the model
     * @param devsSimulator the simulator
     */
    public MM1Queue41SwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel,
            final MM1Queue41Model model, final DEVSSimulator.TimeDouble devsSimulator)
    {
        super(title, panel);
        this.model = model;
        panel.getConsole().setLogLevel(Level.TRACE);
        panel.getConsole().setLogMessageFormat("{message|indent=4}");
        try
        {
            devsSimulator.scheduleEventAbs(1000.0, this, this, "terminate", null);
        }
        catch (SimRuntimeException exception)
        {
            Logger.error(exception, "<init>");
        }
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        ConsoleLogger.create();
        ConsoleLogger.setLevel(Level.TRACE);
        ConsoleLogger.setMessageFormat("{class_name}.{method}:{line} {message|indent=4}");
        MM1Queue41Model model = new MM1Queue41Model();
        DEVSSimulator.TimeDouble devsSimulator = new DEVSSimulator.TimeDouble();
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 1000.0, model);
        devsSimulator.initialize(replication, ReplicationMode.TERMINATING);
        new MM1Queue41SwingApplication("MM1 Queue model", new MM1Queue41Panel(model, devsSimulator), model,
                devsSimulator);
    }

    /** stop the simulation. */
    protected final void terminate()
    {
        Logger.info("average queue length = " + this.model.qN.getSampleMean());
        Logger.info("average queue wait   = " + this.model.dN.getSampleMean());
        Logger.info("average utilization  = " + this.model.uN.getSampleMean());
    }

}