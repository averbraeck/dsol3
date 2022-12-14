package nl.tudelft.simulation.dsol.swing.gui.control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControl;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControlButton;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControlLabel;
import nl.tudelft.simulation.dsol.swing.gui.util.Icons;
import nl.tudelft.simulation.dsol.swing.gui.util.RegexFormatter;

/**
 * Panel that enables a panel that allows editing of the "run until" time.
 * <p>
 * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public abstract class RunUntilPanel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends JPanel implements AppearanceControl, ActionListener
{
    /** */
    private static final long serialVersionUID = 20141211L;

    /** the simulator. */
    private final SimulatorInterface<A, R, T> simulator;

    /** the input field. */
    private final JFormattedTextField textField;

    /** Font used to display the edit field. */
    private Font timeFont = new Font("SansSerif", Font.BOLD, 18);

    /** the initial / reset value of the timeUntil field. */
    private String initialValue;

    /** the apply or cancel button. */
    private JButton runUntilButton;

    /** the state: is there a valid value or not? */
    private boolean applyState = false;

    /** the "run until" time, or null when not set. */
    private T runUntilTime = null;

    /**
     * Construct a clock panel.
     * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
     * @param initialValue String; the initial value of the time to display
     * @param regex String; the regular expression to which the entered text needs to adhere
     */
    public RunUntilPanel(final SimulatorInterface<A, R, T> simulator, final String initialValue, final String regex)
    {
        this.simulator = simulator;
        this.initialValue = initialValue;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setMaximumSize(new Dimension(250, 35));

        this.textField = new JFormattedTextField(new RegexFormatter(regex));
        this.textField.setFont(getTimeFont());
        this.textField.setPreferredSize(new Dimension(120, 20));
        this.textField.setValue(this.initialValue);

        Icon runUntilIcon = Icons.loadIcon("/resources/Apply.png");
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
    public void actionPerformed(final ActionEvent actionEvent)
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
                if (this.runUntilTime == null || getSimulator().getSimTime().compareTo(this.runUntilTime) >= 0
                        || getSimulator().getReplication().getEndSimTime().compareTo(this.runUntilTime) < 0)
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
            this.runUntilButton.setIcon(Icons.loadIcon("/resources/Cancel.png"));
            this.textField.validate();
            // getSimulator().addListener(this, SimulatorInterface.TIME_CHANGED_EVENT);
            synchronized (this)
            {
                if (getSimulator().isStartingOrRunning())
                {
                    getSimulator().stop();
                }
                if (getSimulator().getSimTime().compareTo(this.runUntilTime) < 0)
                {
                    getSimulator().runUpTo(this.runUntilTime);
                }
            }
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
            this.runUntilButton.setIcon(Icons.loadIcon("/resources/Apply.png"));
            this.textField.validate();
            this.applyState = false;
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
     * @param simulationTimeString String; simulation time as a string
     * @return T; simulation time contained in the String or null when not valid
     */
    protected abstract T parseSimulationTime(String simulationTimeString);

    /**
     * @return simulator
     */
    public SimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return timeFont.
     */
    public Font getTimeFont()
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
    public String toString()
    {
        return "RunUntilPanel [time=" + this.textField.getText() + "]";
    }

    /**
     * RunUntilPanel for a double time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
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
            super(simulator, "0.0", "^([0-9]+([.][0-9]*)?|[.][0-9]+)$");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Double simulationTime)
        {
            return String.format("%s", simulationTime);
        }

        /** {@inheritDoc} */
        @Override
        protected SimTimeDouble parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                double t = Double.parseDouble(simulationTimeString);
                return t > 0.0 ? new SimTimeDouble(t) : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    /**
     * RunUntilPanel for a float time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloat extends RunUntilPanel<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a float time.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeFloat(final SimulatorInterface.TimeFloat simulator)
        {
            super(simulator, "0.0", "^([0-9]+([.][0-9]*)?|[.][0-9]+)$");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Float simulationTime)
        {
            return String.format("%s", simulationTime);
        }

        /** {@inheritDoc} */
        @Override
        protected SimTimeFloat parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                float t = Float.parseFloat(simulationTimeString);
                return t > 0.0f ? new SimTimeFloat(t) : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    /**
     * RunUntilPanel for a long time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeLong extends RunUntilPanel<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a long time.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeLong(final SimulatorInterface.TimeLong simulator)
        {
            super(simulator, "0", "[0-9]+");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Long simulationTime)
        {
            return String.format("%s", simulationTime);
        }

        /** {@inheritDoc} */
        @Override
        protected SimTimeLong parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                long t = Long.parseLong(simulationTimeString);
                return t > 0L ? new SimTimeLong(t) : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    /**
     * RunUntilPanel for a double djunits Time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDoubleUnit extends RunUntilPanel<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a double djunits Time.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeDoubleUnit(final SimulatorInterface.TimeDoubleUnit simulator)
        {
            super(simulator, "0.0 s",
                    "^[0-9]*[.]?[0-9]+([eE][-+]?[0-9]+)?\\s*(s|sec|Ys|Ysec|Zs|Zsec|Es|Esec|Ps|Psec|Ts|Tsec|Gs|Gsec"
                            + "|Ms|Msec|ks|ksec|hs|hsec|das|dasec|ds|dsec|cs|csec|ms|\\u03BCs|mus|\\u03BCsec|musec"
                            + "|ns|nsec|ps|psec|fs|fsec|as|asec|zs|zsec|ys|ysec|day|h|hr|hour|min|wk|week)");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Time simulationTime)
        {
            return String.format("%s", simulationTime.toString());
        }

        /** {@inheritDoc} */
        @Override
        protected SimTimeDoubleUnit parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                Time t = Time.valueOf(simulationTimeString);
                return t.gt(Time.ZERO) ? new SimTimeDoubleUnit(t) : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

    /**
     * RunUntilPanel for a float djunits Time. The time formatter and time display can be adjusted.
     * <p>
     * Copyright (c) 2020-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeFloatUnit extends RunUntilPanel<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a djunits FloatTime.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeFloatUnit(final SimulatorInterface.TimeFloatUnit simulator)
        {
            super(simulator, "0.0 s",
                    "^[0-9]*[.]?[0-9]+([eE][-+]?[0-9]+)?\\s*(s|sec|Ys|Ysec|Zs|Zsec|Es|Esec|Ps|Psec|Ts|Tsec|Gs|Gsec"
                            + "|Ms|Msec|ks|ksec|hs|hsec|das|dasec|ds|dsec|cs|csec|ms|\\u03BCs|mus|\\u03BCsec|musec"
                            + "|ns|nsec|ps|psec|fs|fsec|as|asec|zs|zsec|ys|ysec|day|h|hr|hour|min|wk|week)");
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final FloatTime simulationTime)
        {
            return String.format("%s", simulationTime.toString());
        }

        /** {@inheritDoc} */
        @Override
        protected SimTimeFloatUnit parseSimulationTime(final String simulationTimeString)
        {
            try
            {
                FloatTime t = FloatTime.valueOf(simulationTimeString);
                return t.gt(FloatTime.ZERO) ? new SimTimeFloatUnit(t) : null;
            }
            catch (Exception exception)
            {
                return null;
            }
        }
    }

}
