package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="https://simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class DSOLPanel<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    protected SimulatorInterface<A, R, T> simulator;

    /** */
    protected Console console = new Console();

    /** the model. */
    private DSOLModel<A, R, T> model;

    /** */
    protected Logger logger = Logger.getLogger("nl.tudelft.simulation.dsol.gui.swing");

    /** */
    protected SimulatorControlPanel simulatorControlPanel;

    /** */
    protected TabbedContentPane tabbedPane = new TabbedContentPane(SwingConstants.BOTTOM);

    /** */
    protected StatusBar statusBar;

    /**
     * @param model the model to run in this panel
     * @param simulator the simulator to use for the model
     */
    public DSOLPanel(final DSOLModel<A, R, T> model, final SimulatorInterface<A, R, T> simulator)
    {
        this.model = model;
        this.simulator = simulator;

        // we add the loggers
        this.console.addLogger(this.logger);
        this.console.addLogger(Logger.getLogger("nl.tudelft.simulation.event"));
        this.console.addLogger(Logger.getLogger("nl.tudelft.simulation.context"));
        this.console.addLogger(Logger.getLogger("nl.tudelft.simulation.dsol"));
        try
        {
            this.initialize();
        }
        catch (Exception exception)
        {
            this.logger.logp(Level.SEVERE, "DSOLPanel", "DSOLPanel", "", exception);
        }
    }

    /**
     * initialize the panel and the simulator.
     * @throws RemoteException when simulator cannot be found
     * @throws SimRuntimeException when model cannot be constructed
     */
    public void initialize() throws RemoteException, SimRuntimeException
    {
        this.createContentPane();
        // this.model.constructModel(this.simulator);
    }

    /**
     * Method createContentPane.
     * @throws RemoteException on error
     */
    protected void createContentPane() throws RemoteException
    {
        this.setLayout(new BorderLayout());

        // Let's add our simulationControl
        this.simulatorControlPanel = new SimulatorControlPanel(this.simulator);
        this.add(this.simulatorControlPanel, BorderLayout.NORTH);

        // Let's add our console to our tabbed pane
        this.tabbedPane.addTab("console", new JScrollPane(this.console));

        // Let's display our tabbed contentPane
        this.add(this.tabbedPane, BorderLayout.CENTER);

        // put a status bar at the bottom
        this.statusBar = new StatusBar(this.simulator);
        this.add(this.statusBar, BorderLayout.SOUTH);
    }

    /**
     * @return tabbedPane
     */
    public TabbedContentPane getTabbedPane()
    {
        return this.tabbedPane;
    }

}
