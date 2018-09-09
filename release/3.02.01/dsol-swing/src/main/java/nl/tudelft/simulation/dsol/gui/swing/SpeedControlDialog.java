package nl.tudelft.simulation.dsol.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.tudelft.simulation.dsol.simulators.AnimatorInterface;
import nl.tudelft.simulation.dsol.simulators.Simulator;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

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
public class SpeedControlDialog extends JDialog implements ActionListener, ChangeListener, EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private Simulator<?, ?, ?> target;

    /** */
    private Logger logger = Logger.getLogger("nl.tudelft.simulation.dsol");

    /** */
    private long animationDelay;

    /** */
    private double simulatorSpeedRatio;

    /** */
    private JTextField animationValue;

    /** */
    private JTextField simulationValue;

    /** */
    private JSlider animationSlider;

    /** */
    private JSlider simulationSlider;

    /**
     * Constructor for SpeedControlDialog.
     * @param target the simulator
     * @throws HeadlessException when there is no graphics monitor attachted
     */
    public SpeedControlDialog(SimulatorInterface<?, ?, ?> target) throws HeadlessException
    {
        super((Frame) null, "Simulation & Animation Speed Control");
        try
        {
            this.target = (Simulator<?, ?, ?>) target;
            this.target.addListener(this, AnimatorInterface.ANIMATION_DELAY_CHANGED_EVENT);
            // XXX: this.target.addListener(this, SimpleAnimatorInterface.SIMULATION_SPEED_CHANGED_EVENT);

            JPanel contentPane = new JPanel(new BorderLayout());
            contentPane.setPreferredSize(new Dimension(150, 150));

            JPanel northPanel = new JPanel(new BorderLayout());
            northPanel.add(
                    new JLabel(new ImageIcon(this.getClass().getResource(
                            "/nl/tudelft/simulation/dsol/images/clock24.gif"))), BorderLayout.WEST);
            JTextField introduction = new JTextField("    change the simulation & animation speed");
            introduction.setFont(new Font("arial", Font.BOLD, 12));
            introduction.setBorder(BorderFactory.createEmptyBorder());
            introduction.setEditable(false);
            introduction.setAlignmentX(Component.CENTER_ALIGNMENT);
            northPanel.add(introduction, BorderLayout.CENTER);
            contentPane.add(northPanel, BorderLayout.NORTH);

            JPanel southPanel = new JPanel(new BorderLayout());
            JButton closeButton = new JButton("Close");
            closeButton.setName("closeButton");
            closeButton.setToolTipText("closes this dialog");
            closeButton.addActionListener(this);
            southPanel.add(closeButton, BorderLayout.EAST);
            contentPane.add(southPanel, BorderLayout.SOUTH);

            JPanel speedPanel = new JPanel();
            Border border =
                    BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20),
                            BorderFactory.createEtchedBorder());
            speedPanel.setBorder(BorderFactory.createTitledBorder(border, "speed control"));
            speedPanel.setLayout(new BoxLayout(speedPanel, BoxLayout.Y_AXIS));

            JPanel animationPanel = new JPanel(new BorderLayout());
            JTextField animationLabel = new JTextField("   animation refresh delay:  ");
            animationLabel.setFont(new Font("arial", Font.BOLD, 10));
            animationLabel.setEditable(false);
            animationLabel.setBorder(BorderFactory.createEmptyBorder());
            animationPanel.add(animationLabel, BorderLayout.WEST);

            this.animationSlider = new JSlider(50, 2000, 1000);
            this.animationSlider.setName("animation");
            this.animationSlider.addChangeListener(this);
            animationPanel.add(this.animationSlider, BorderLayout.EAST);

            this.animationValue = new JTextField("                                 ");
            this.animationValue.setFont(new Font("arial", Font.BOLD, 10));
            this.animationValue.setEditable(false);
            this.animationValue.setBorder(BorderFactory.createEmptyBorder());
            animationPanel.add(this.animationValue, BorderLayout.CENTER);
            speedPanel.add(animationPanel);

            JPanel simulationPanel = new JPanel(new BorderLayout());

            this.simulationSlider = new JSlider(-400, 400, 0);
            this.simulationSlider.setName("simulation");
            this.simulationSlider.addChangeListener(this);
            simulationPanel.add(this.simulationSlider, BorderLayout.EAST);

            this.simulationValue = new JTextField("                                 ");
            this.simulationValue.setFont(new Font("arial", Font.BOLD, 10));
            this.simulationValue.setEditable(false);
            this.simulationValue.setBorder(BorderFactory.createEmptyBorder());
            simulationPanel.add(this.simulationValue, BorderLayout.CENTER);
            speedPanel.add(simulationPanel);

            contentPane.add(speedPanel, BorderLayout.CENTER);

            // XXX: this.notify(new Event(AnimatorInterface.ANIMATION_DELAY_CHANGED_EVENT, this, new Long((long)
            // this.target.getTimeFactor().getValue())));
            // XXX: this.notify(new Event(SimpleAnimatorInterface.SIMULATION_SPEED_CHANGED_EVENT, this, new
            // Double(this.target.getSimulatorSpeedRatio())));

            this.getContentPane().add(contentPane);
            this.pack();
            this.setEnabled(true);
            this.setSize(new Dimension(600, 275));
            this.setVisible(true);
        }
        catch (Exception exception)
        {
            this.logger.logp(Level.SEVERE, "SpeedControlDialog", "SpeedControlDialog", "", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (((JButton) e.getSource()).getName().equals("closeButton"))
        {
            this.dispose();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stateChanged(ChangeEvent event)
    {
        try
        {
            if (((JSlider) event.getSource()).getName().equals("simulation"))
            {
                int value = ((JSlider) event.getSource()).getValue();
                boolean pause = this.target.isRunning();
                double ratio = Math.pow(10, value / 100.0);
                if (pause)
                    this.target.stop();
                // XXX: this.target.setSimulationSpeedRatio(ratio);
                if (pause)
                    this.target.start();
            }
            if (((JSlider) event.getSource()).getName().equals("animation"))
            {
                int value = ((JSlider) event.getSource()).getValue();
                boolean pause = this.target.isRunning();
                if (pause)
                    this.target.stop();
                // XXX: this.target.setAnimationDelay((long) value);
                if (pause)
                    this.target.start();
            }
        }
        catch (Exception exception)
        {
            this.logger.logp(Level.SEVERE, "SpeedControlDialog", "stateChanged", "", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(EventInterface event)
    {
        // XXX:
        /*-
        if (event.getType().equals(SimpleAnimatorInterface.SIMULATION_SPEED_CHANGED_EVENT))
        {
            this.simulatorSpeedRatio = new Double(event.getContent().toString()).doubleValue();
            double sliderValue = 10 * Math.log(this.simulatorSpeedRatio) / Math.log(10);
            this.simulationSlider.setValue(10 * (int) sliderValue);
            this.simulationValue.setText(NumberFormat.getInstance().format(this.simulatorSpeedRatio) + "      ");
        }
         */
        if (event.getType().equals(AnimatorInterface.ANIMATION_DELAY_CHANGED_EVENT))
        {
            this.animationDelay = new Long(event.getContent().toString()).longValue();
            this.animationSlider.setValue((int) this.animationDelay);
            this.animationValue.setText(this.animationDelay + " msec");
        }
    }
}
