package nl.tudelft.simulation.dsol.swing.gui.control;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControl;
import nl.tudelft.simulation.dsol.swing.gui.appearance.AppearanceControlLabel;

/**
 * Panel that displays the simulation time and the simulation speed.
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
public abstract class ClockSpeedPanel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends JPanel implements AppearanceControl
{
    /** */
    private static final long serialVersionUID = 20141211L;

    /** The JLabel that displays the time. */
    protected final JLabel timeLabel;

    /** The JLabel that displays the simulation speed. */
    protected final JLabel speedLabel;

    /** the simulator. */
    final SimulatorInterface<A, R, T> simulator;

    /** Font used to display the clock. */
    private Font timeFont = new Font("SansSerif", Font.BOLD, 18);

    /** The timer (so we can cancel it). */
    private Timer timer;

    /** Timer update interval in msec. */
    private long updateInterval = 1000;

    /** Simulation time time. */
    private A prevSimTime;

    /**
     * Construct a clock panel.
     * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
     */
    public ClockSpeedPanel(final SimulatorInterface<A, R, T> simulator)
    {
        this.simulator = simulator;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setFont(getTimeFont());

        this.timeLabel = new AppearanceControlLabel();
        this.timeLabel.setFont(getTimeFont());
        this.timeLabel.setMaximumSize(new Dimension(150, 35));
        add(this.timeLabel);

        this.speedLabel = new AppearanceControlLabel();
        this.speedLabel.setFont(getTimeFont());
        this.speedLabel.setMaximumSize(new Dimension(100, 35));
        add(this.speedLabel);

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimeUpdateTask(), 0, this.updateInterval);
    }

    /**
     * Cancel the timer task.
     */
    public void cancelTimer()
    {
        if (this.timer != null)
        {
            this.timer.cancel();
        }
        this.timer = null;
    }

    /** Updater for the clock panel. */
    protected class TimeUpdateTask extends TimerTask implements Serializable
    {
        /** */
        private static final long serialVersionUID = 20140000L;

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            A simulationTime = ClockSpeedPanel.this.getSimulator().getSimulatorTime();
            getTimeLabel().setText(formatSimulationTime(simulationTime));
            getTimeLabel().repaint();
            getSpeedLabel().setText(formatSpeed(simulationTime));
            getSpeedLabel().repaint();
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "TimeUpdateTask of ClockPanel";
        }
    }

    /**
     * @return speedLabel
     */
    protected final JLabel getSpeedLabel()
    {
        return this.speedLabel;
    }

    /**
     * @return timeLabel
     */
    protected final JLabel getTimeLabel()
    {
        return this.timeLabel;
    }

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

    /**
     * @return updateInterval
     */
    protected final long getUpdateInterval()
    {
        return this.updateInterval;
    }

    /**
     * @return prevSimTime
     */
    protected final A getPrevSimTime()
    {
        return this.prevSimTime;
    }

    /**
     * Set the new simulation time to be used in the next calculation for the speed.
     * @param prevSimTime A; the new simulation time to be used in the next calculation for the speed
     */
    protected void setPrevSimTime(final A prevSimTime)
    {
        this.prevSimTime = prevSimTime;
    }

    /**
     * Returns the simulation speed as a String.
     * @param simulationTime A; simulation time
     * @return simulation speed
     */
    protected abstract String formatSpeed(A simulationTime);

    /**
     * Returns the formatted simulation time.
     * @param simulationTime A; simulation time
     * @return formatted simulation time
     */
    protected abstract String formatSimulationTime(A simulationTime);

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
        return "ClockSpeedLabel";
    }

    /**
     * ClockLabel for a double time. The formatter and speed calculation can be adjusted.
     * <p>
     * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The
     * DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
     * </p>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    public static class TimeDouble extends ClockSpeedPanel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20201227L;

        /**
         * Construct a clock panel with a double time.
         * @param simulator SimulatorInterface&lt;A, R, T&gt;; the simulator
         */
        public TimeDouble(final SimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
            setPrevSimTime(0.0);
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSpeed(final Double simulationTime)
        {
            if (simulationTime == null)
            {
                return "0.0";
            }
            double speed = (simulationTime - getPrevSimTime()) / (0.001 * getUpdateInterval());
            setPrevSimTime(simulationTime);
            return String.format(" | %6.2f x ", speed);
        }

        /** {@inheritDoc} */
        @Override
        protected String formatSimulationTime(final Double simulationTime)
        {
            return String.format(" t = %8.2f ", simulationTime);
        }
    }

    // double now = simulationTime == null ? 0.0 : Math.round(simulationTime * 1000) / 1000d;
    // int seconds = (int) Math.floor(now);
    // int fractionalSeconds = (int) Math.floor(1000 * (now - seconds));
    // return String.format(" %02d:%02d:%02d.%03d ", seconds / 3600, seconds / 60 % 60, seconds % 60, fractionalSeconds);

}
