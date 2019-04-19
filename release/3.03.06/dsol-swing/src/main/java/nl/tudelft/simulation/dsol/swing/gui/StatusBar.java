package nl.tudelft.simulation.dsol.swing.gui;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
 */
public class StatusBar extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    protected Calendar time = Calendar.getInstance();

    /** */
    protected Calendar simulationCalendar;

    /** */
    private Timer timer;

    /** */
    protected JTextField timeField = new JTextField(DateFormat.getDateTimeInstance().format(this.time.getTime()));

    /** */
    protected JTextField simulatorTimeField;

    /** */
    protected SimulatorInterface<?, ?, ?> simulator;

    /** timer update in msec. */
    protected final long PERIOD = 1000;

    /** */
    protected static String[] WEEKDAY =
            new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    /**
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     */
    public StatusBar(SimulatorInterface<?, ?, ?> simulator)
    {
        super();
        this.simulator = simulator;
        double[][] size = {{TableLayoutConstants.FILL, TableLayoutConstants.FILL}, {this.getFont().getSize() + 5}};

        this.setLayout(new TableLayout(size));

        this.timeField.setEditable(false);
        this.timeField.setBorder(BorderFactory.createEmptyBorder());
        this.timeField.setToolTipText("displays the current time");
        this.simulatorTimeField = new JTextField("simulator.time:Double.NaN");
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimeUpdateTask(this.PERIOD), 0, this.PERIOD);
        this.add(this.timeField, "0,0,L,B");

        this.simulatorTimeField.setEditable(false);
        this.simulatorTimeField.setToolTipText("displays the simulator time");
        this.simulatorTimeField.setBorder(BorderFactory.createEmptyBorder());
        this.add(this.simulatorTimeField, "1,0,L,B");
    }

    /**
     * cancel the timer.
     */
    public final void cancelTimer()
    {
        if (this.timer != null)
        {
            this.timer.cancel();
            this.timer = null;
        }
    }

    /**
     * The TimerTask to update the time and simulation time in the status bar.
     * <p>
     * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
     * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which
     * can be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
     * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck </a>
     */
    private class TimeUpdateTask extends TimerTask
    {
        /** */
        private long period;

        /**
         * @param period long;
         */
        public TimeUpdateTask(long period)
        {
            this.period = period;
        }

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            StatusBar.this.time.setTimeInMillis(StatusBar.this.time.getTimeInMillis() + this.period);
            StatusBar.this.timeField.setText(DateFormat.getDateTimeInstance().format(StatusBar.this.time.getTime()));
            StatusBar.this.simulatorTimeField.setText("" + StatusBar.this.simulator.getSimulatorTime() + "     ");
            StatusBar.this.repaint();
        }
    }
}
