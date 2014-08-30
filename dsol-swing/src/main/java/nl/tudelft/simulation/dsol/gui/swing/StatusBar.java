package nl.tudelft.simulation.dsol.gui.swing;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <br>
 * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at providing policy
 * analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL simulation engine and
 * the agent-based modeling formalism. See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>. The project is a co-operation between TU Delft, Systems Engineering and Simulation
 * Department (Netherlands) and NUDT, Simulation Engineering Department (China). This software is licensed under the BSD
 * license. See license.txt in the main project.
 * @version May 4, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
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
    protected JTextField timeField = new JTextField(DateFormat.getDateTimeInstance().format(this.time.getTime()));

    /** */
    protected JTextField simulatorTimeField = new JTextField("simulator.time:Double.NaN");

    /** */
    protected SimulatorInterface<?, ?, ?> simulator;

    /** */
    protected Logger logger = Logger.getLogger("nl.tudelft.simulation.medlabs.simulation.gui");

    /** timer update in msec. */
    protected final long PERIOD = 1000;

    /** */
    protected static String[] WEEKDAY = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};

    /**
     * @param simulator
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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimeUpdateTask(this.PERIOD), 0, this.PERIOD);
        this.add(this.timeField, "0,0,L,B");

        this.simulatorTimeField.setEditable(false);
        this.simulatorTimeField.setToolTipText("displays the simulator time");
        this.simulatorTimeField.setBorder(BorderFactory.createEmptyBorder());
        this.add(this.simulatorTimeField, "1,0,L,B");
    }

    /**
     * <br>
     * Copyright (c) 2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved. The MEDLABS project (Modeling Epidemic Disease with Large-scale Agent-Based Simulation) is aimed at
     * providing policy analysis tools to predict and help contain the spread of epidemics. It makes use of the DSOL
     * simulation engine and the agent-based modeling formalism. See for project information <a
     * href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>. The project is a co-operation between TU
     * Delft, Systems Engineering and Simulation Department (Netherlands) and NUDT, Simulation Engineering Department
     * (China). This software is licensed under the BSD license. See license.txt in the main project.
     * @version May 4, 2014 <br>
     * @author <a href="http://www.tbm.tudelft.nl/mzhang">Mingxin Zhang </a>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck </a>
     */
    private class TimeUpdateTask extends TimerTask
    {
        /** */
        private long period;

        /**
         * @param period
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
            try
            {
                StatusBar.this.simulatorTimeField.setText(StatusBar.this.simulator.getSimulatorTime().toString()
                        + "     ");
            }
            catch (RemoteException exception)
            {
                // XXX: throw exception? separate run method. how?
                exception.printStackTrace();
            }
            StatusBar.this.repaint();
        }
    }
}
