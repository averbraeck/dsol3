package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
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
public class SimulatorControlPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public static final int SMALL = 16;

    /** */
    public static final int LARGE = 24;

    /** */
    protected int size = SMALL;

    /** */
    protected SimulatorInterface<?, ?, ?> target;

    /** */
    protected Logger logger = Logger.getLogger("nl.tudelft.simulation.medlabs");

    /** */
    protected ActionListener simulatorControlListener;

    /**
     * Method SimulatorControlPanel.
     * @param target
     */
    public SimulatorControlPanel(SimulatorInterface<?, ?, ?> target)
    {
        this(target, SMALL);
    }

    /**
     * Method SimulatorControlPanel.
     * @param target
     * @param size
     */
    public SimulatorControlPanel(SimulatorInterface<?, ?, ?> target, int size)
    {
        super();
        this.target = target;
        this.createControlPanel();
        this.size = size;
        if (size != 16 && size != 24)
            size = 16;
    }

    /**
     * Method createControlPanel creates a new ControlJPanel for the simulator
     */
    private void createControlPanel()
    {
        this.setForeground(Color.darkGray);
        this.setLayout(new BorderLayout());
        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.X_AXIS));
        this.simulatorControlListener = new SimulatorControlListener(this.target);

        JButton stepButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/media/StepForward" + this.size + ".gif")));
        stepButton.setName("stepButton");
        stepButton.setToolTipText("steps the simulator");
        stepButton.addActionListener(this.simulatorControlListener);
        control.add(stepButton);

        JButton startButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/media/Play" + this.size + ".gif")));
        startButton.setName("startButton");
        startButton.setToolTipText("starts the simulator");
        startButton.addActionListener(this.simulatorControlListener);
        control.add(startButton);

        JButton fastForwardButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/media/FastForward" + this.size + ".gif")));
        fastForwardButton.setName("fastForwardButton");
        fastForwardButton.setToolTipText("fastForwards the simulator");
        fastForwardButton.addActionListener(this.simulatorControlListener);
        fastForwardButton.setEnabled(false);
        if (this.target instanceof AnimatorInterface)
            fastForwardButton.setEnabled(true);
        control.add(fastForwardButton);

        JButton pauseButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/media/Pause" + this.size + ".gif")));
        pauseButton.setName("pauseButton");
        pauseButton.setToolTipText("pauses the simulator");
        pauseButton.addActionListener(this.simulatorControlListener);
        control.add(pauseButton);

        JButton stopButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/media/Stop" + this.size + ".gif")));
        stopButton.setName("stopButton");
        stopButton.setToolTipText("stops the simulator");
        stopButton.addActionListener(this.simulatorControlListener);
        control.add(stopButton);

        JButton resetButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/toolbarButtonGraphics/general/Undo" + this.size + ".gif")));
        resetButton.setName("resetButton");
        resetButton.setToolTipText("resets the simulator");
        resetButton.addActionListener(this.simulatorControlListener);
        control.add(resetButton);
        this.add(control, BorderLayout.WEST);

        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.X_AXIS));

        JButton speedButton =
                new JButton(new ImageIcon(this.getClass().getResource(
                        "/nl/tudelft/simulation/dsol/images/clock" + this.size + ".gif")));
        speedButton.setName("speedButton");
        speedButton.setToolTipText("changes the speed of the animation & simulation");
        speedButton.addActionListener(this.simulatorControlListener);
        setupPanel.add(speedButton);

        this.add(speedButton, BorderLayout.EAST);
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
    private class SimulatorControlListener implements ActionListener
    {
        /** */
        private SimulatorInterface<?, ?, ?> target;

        /**
         * Method SimulatorControlListener.
         * @param target
         */
        public SimulatorControlListener(SimulatorInterface<?, ?, ?> target)
        {
            this.target = target;
        }

        /** {@inheritDoc} */ @Override public  void actionPerformed(ActionEvent e)
        {
            String actionName = "";
            if (e.getSource().getClass().equals(JButton.class))
            {
                actionName = ((JButton) e.getSource()).getName();
            }
            try
            {
                if (actionName.equals("stepButton"))
                {
                    this.target.step();
                }
                if (actionName.equals("startButton"))
                {
                    this.target.start();
                }
                if (actionName.equals("fastForwardButton"))
                {
                    // TODO: ((AnimatorInterface) this.target).setFastForward(true);
                }
                if (actionName.equals("pauseButton"))
                {
                    this.target.stop();
                }
                if (actionName.equals("speedButton"))
                {
                    new SpeedControlDialog(this.target);
                }
            }
            catch (Exception exception)
            {
                SimulatorControlPanel.this.logger.logp(Level.SEVERE, "SimulatorControlListener", "actionPerformed", "",
                        exception);
            }
        }
    }
}
