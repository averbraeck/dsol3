package nl.tudelft.simulation.dsol.swing.gui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControl;
import nl.tudelft.simulation.dsol.swing.gui.control.AbstractControlPanel;

/**
 * Tabbed content panel for the simulation with a control bar on top.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public class DSOLPanel extends JPanel 
{
    /** */
    private static final long serialVersionUID = 20150617L;

    /** The control panel to control start/stop, speed of the simulation. */
    private AbstractControlPanel<?, ?, ?, ?> controlPanel;

    /** The tabbed pane that contains the different (default) screens. */
    protected final TabbedContentPane tabbedPane;

    static
    {
        // use narrow border for TabbedPane, which cannot be changed afterwards
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 1, 1));
    }

    /**
     * Construct a panel for an interactive simulation model.
     * @param simulator S; the simulator or animator of the model.
     * @param model DSOLModel&lt;A, R, T, S&gt;; the model with its properties.
     * @param controlPanel DSOLControlPanel&lt;A, R, T, S&gt;; the control panel to use (especially with relation to time
     *            control)
     * @throws RemoteException when communications to a remote machine fails
     */
    public DSOLPanel(final AbstractControlPanel<?, ?, ?, ?> controlPanel) throws RemoteException
    {
        this.tabbedPane = new AppearanceControlTabbedContentPane(SwingConstants.BOTTOM);
        setLayout(new BorderLayout());

        // add the simulationControl at the top
        this.controlPanel = controlPanel;
        add(this.controlPanel, BorderLayout.NORTH);

        // add the tabbed contentPane in the center
        add(this.tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Adds a console tab for the Logger.
     */
    public final void addConsoleLogger()
    {
        this.tabbedPane.addTab("logger", new ConsoleLogger());
    }

    /**
     * Adds a console tab for steout and stderr.
     */
    public final void addConsolOutput()
    {
        this.tabbedPane.addTab("console", new ConsoleOutput());
    }

    /**
     * Adds a properties tab.
     * @throws InputParameterException on exception with properties
     */
    public final void addPropertiesTab() throws InputParameterException
    {
        // TODO: make a tab with the InputParameters
    }

    /**
     * @return tabbedPane
     */
    public final TabbedContentPane getTabbedPane()
    {
        return this.tabbedPane;
    }

    /**
     * @return simulator.
     */
    public final SimulatorInterface<?, ?, ?> getSimulator()
    {
        return this.controlPanel.getSimulator();
    }

    /**
     * Return the control panel of this SimulationPanel.
     * @return ControlPanel; the control panel
     */
    public final AbstractControlPanel<?, ?, ?, ?> getControlPanel()
    {
        return this.controlPanel;
    }

    /**
     * @return the Model
     */
    public DSOLModel<?, ?, ?, ?> getModel()
    {
        return this.controlPanel.getModel();
    }

    /**
     * Enable the simulation or animation buttons in the GUI. This method HAS TO BE CALLED in order for the buttons to be
     * enabled, because the initial state is DISABLED. Typically, this is done after all tabs, statistics, and other user
     * interface and model components have been constructed and initialized.
     */
    public void enableSimulationControlButtons()
    {
        this.controlPanel.setControlButtonsState(true);
    }

    /**
     * Disable the simulation or animation buttons in the GUI.
     */
    public void disableSimulationControlButtons()
    {
        this.controlPanel.setControlButtonsState(false);
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "SimulationPanel";
    }

    /**
     * TabbedContentPane which ignores appearance (it has too much colors looking ugly / becoming unreadable).
     * <p>
     * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
     * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
     */
    static class AppearanceControlTabbedContentPane extends TabbedContentPane implements AppearanceControl
    {
        /** */
        private static final long serialVersionUID = 20180206L;

        /**
         * @param tabPlacement int; tabPlacement
         */
        AppearanceControlTabbedContentPane(final int tabPlacement)
        {
            super(tabPlacement);
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "AppearanceControlTabbedContentPane []";
        }

    }
}
