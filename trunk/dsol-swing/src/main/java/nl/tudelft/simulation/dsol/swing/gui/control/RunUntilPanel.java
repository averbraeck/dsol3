package nl.tudelft.simulation.dsol.swing.gui.control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControl;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControlButton;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControlLabel;
import nl.tudelft.simulation.dsol.swing.gui.util.Icons;
import nl.tudelft.simulation.dsol.swing.gui.util.RegexFormatter;

/**
 * Panel that enables a panel that allows editing of the "run until" time.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public abstract class RunUntilPanel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends JPanel implements AppearanceControl, ActionListener, EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20141211L;

    /** the simulator. */
    final SimulatorInterface<A, R, T> simulator;

    /** the input field. */
    private final JFormattedTextField textField;

    /** Font used to display the edit field. */
    private Font timeFont = new Font("SansSerif", Font.BOLD, 18);

    /** the initial / reset value of the tumeUntil field. */
    private String initialValue;

    /** the apply or cancel button. */
    private JButton runUntilButton;

    /** the state: is there a valid value or not? */
    private boolean applyState = false;

    /** the "run until" time, or null when not set. */
    private A runUntilTime = null;

    /**
     * Construct a clock panel.
     * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
     * @param initialValue String; the initial value of the time to display
     * @param regex String; the regular expression to which the entered text needs to adhere
     * @param mask Sting; the mask to display the time
     */
    public RunUntilPanel(final SimulatorInterface<A, R, T> simulator, final String initialValue, final String regex,
            final String mask)
    {
        this.simulator = simulator;
        this.initialValue = initialValue;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMaximumSize(new Dimension(250, 35));

        this.textField = new JFormattedTextField(new RegexFormatter(regex));
        this.textField.setFont(getTimeFont());
        this.textField.setPreferredSize(new Dimension(120, 20));

        MaskFormatter mf = null;
        try
        {
            mf = new MaskFormatter(mask);
            mf.setPlaceholderCharacter('0');
            mf.setAllowsInvalid(false);
            mf.setCommitsOnValidEdit(true);
            mf.setOverwriteMode(true);
            mf.install(this.textField);
        }
        catch (ParseException exception)
        {
            exception.printStackTrace();
        }
        this.textField.setValue(this.initialValue);

        Icon runUntilIcon = Icons.loadIcon("/Apply.png");
        this.runUntilButton = new AppearanceControlButton(runUntilIcon);
        this.runUntilButton.setName("runUntil");
        this.runUntilButton.setEnabled(true);
        this.runUntilButton.setActionCommand("RunUntil");
        this.runUntilButton.setToolTipText("Run until the given time; ignored if earlier than current simulation time");
        this.runUntilButton.addActionListener(this);

        add(new AppearanceControlLabel("Run until: "));
        add(this.textField);
        add(this.runUntilButton);
    }

    /** {@inheritDoc} */
    @Override
    public final void actionPerformed(final ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();
        try
        {
            if (actionCommand.equals("RunUntil"))
            {
                if (this.applyState)
                {
                    cancel();
                    return;
                }
                this.textField.commitEdit();
                String stopTimeValue = (String) this.textField.getValue();
                this.runUntilTime = parseSimulationTime(stopTimeValue);
                if (this.runUntilTime == null || getSimulator().getSimulatorTime().compareTo(this.runUntilTime) >= 0
                        || getSimulator().getReplication().getTreatment().getEndTime().compareTo(this.runUntilTime) < 0)
                {
                    cancel();
                    return;
                }
                apply();
            }
        }
        catch (Exception exception)
        {
            getSimulator().getLogger().always().warn(exception);
            try
            {
                cancel();
            }
            catch (Exception e)
            {
                getSimulator().getLogger().always().warn(e);
            }
        }
    }

    /**
     * Text field ok -- make green and show cancel button.
     * @throws RemoteException on network error
     */
    private void apply() throws RemoteException
    {
        synchronized (this.textField)
        {
            this.textField.setBackground(Color.GREEN);
            this.runUntilButton.setIcon(Icons.loadIcon("/Cancel.png"));
            this.textField.validate();
            getSimulator().addListener(this, SimulatorInterface.TIME_CHANGED_EVENT);
            this.applyState = true;
        }
    }

    /**
     * Text field not ok, or runUntil time reached -- reset field, make field white, and show apply button.
     * @throws RemoteException on network error
     */
    protected void cancel() throws RemoteException
    {
        synchronized (this.textField)
        {
            this.runUntilTime = null;
            this.textField.setValue(this.initialValue);
            this.textField.setBackground(Color.WHITE);
            this.runUntilButton.setIcon(Icons.loadIcon("/Apply.png"));
            this.textField.validate();
            getSimulator().removeListener(this, SimulatorInterface.TIME_CHANGED_EVENT);
            this.applyState = false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(SimulatorInterface.TIME_CHANGED_EVENT))
        {
            synchronized (this.textField)
            {
                if (this.runUntilTime == null || getSimulator().getSimulatorTime().compareTo(this.runUntilTime) < 0)
                {
                    return;
                }
                this.simulator.stop();
                cancel();
            }
        }
    }

    /**
     * Returns the formatted simulation time.
     * @param simulationTime A; simulation time
     * @return formatted simulation time
     */
    protected abstract String formatSimulationTime(A simulationTime);

    /**
     * Returns the simulation time from the formatted string.
     * @param simulationTimeString A; simulation time as a string
     * @return simulation time contained in the String or null when not valid
     */
    protected abstract A parseSimulationTime(String simulationTimeString); 

    /**
     * @return simulator
     */
    public final SimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return timeFont.
     */
    protected final Font getTimeFont()
    {
        return this.timeFont;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isForeground()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString()
    {
        return "RunUntilPanel [time=" + this.textField.getText() + "]";
    }

    /**
     * RunUntilPanel for a double time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDouble extends RunUntilPanel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a double time.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeDouble(final SimulatorInterface.TimeDouble simulator)
        {
            super(simulator, "0.0", "^([0-9]+([.][0-9]*)?|[.][0-9]+)$", "#.##");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Double simulationTime)
        {
            return String.format("%s", simulationTime);
        }

        /** {@inheritDoc} */
        @Override
        protected Double parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                double t = Double.parseDouble(simulationTimeString);
                return t > 0.0 ? t : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    // double now = simulationTime == null ? 0.0 : Math.round(simulationTime * 1000) / 1000d;
    // int seconds = (int) Math.floor(now);
    // int fractionalSeconds = (int) Math.floor(1000 * (now - seconds));
    // return String.format(" %02d:%02d:%02d.%03d ", seconds / 3600, seconds / 60 % 60, seconds % 60, fractionalSeconds);

    // regex: "\\d\\d\\d\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\d"
    // mask: "####:##:##.###"

    // double v = newValue.getSI();
    // int integerPart = (int) Math.floor(v);
    // int fraction = (int) Math.floor((v - integerPart) * 1000);
    // String text =
    // String.format("%04d:%02d:%02d.%03d", integerPart / 3600, integerPart / 60 % 60, integerPart % 60, fraction);

    // String newValue = (String) evt.getNewValue();
    // String[] fields = newValue.split("[:\\.]");
    // int hours = Integer.parseInt(fields[0]);
    // int minutes = Integer.parseInt(fields[1]);
    // int seconds = Integer.parseInt(fields[2]);
    // int fraction = Integer.parseInt(fields[3]);
}
