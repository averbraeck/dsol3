package nl.tudelft.simulation.dsol.swing.animation.D2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.HomeAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.PanDownAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.PanLeftAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.PanRightAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.PanUpAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.ShowGridAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.ZoomInAction;
import nl.tudelft.simulation.dsol.swing.animation.D2.actions.ZoomOutAction;

/**
 * The AnimationFrame.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 */
public class AnimationFrame extends JFrame
{
    /** */
    private static final long serialVersionUID = 1L;

    // TODO different! Just a quick hack!
    static
    {
        double x = 4.0;
        double y = 51.0;
        double w = 7.0 - x;
        double h = 57.0 - y;
        System.getProperties().put("animation.panel.extent", x + ";" + y + ";" + w + ";" + h);
        System.getProperties().put("animation.panel.size", "1024;768");
    }

    /**
     * Constructor for AnimationFrame.
     * @param name String; the name of the frame
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator
     * @throws RemoteException on network error for one of the listeners
     */
    public AnimationFrame(final String name, final SimulatorInterface<?, ?, ?> simulator) throws RemoteException
    {
        super(name);
        this.getContentPane().setLayout(new BorderLayout());
        Rectangle2D extent = null;
        // TODO different!
        String extentString = System.getProperties().getProperty("animation.panel.extent");
        if (extentString != null)
        {
            double[] values = new double[4];
            for (int i = 0; i < 3; i++)
            {
                values[i] = new Double(extentString.substring(0, extentString.indexOf(";"))).doubleValue();
                extentString = extentString.substring(extentString.indexOf(";") + 1);
            }
            values[3] = new Double(extentString).doubleValue();
            extent = new Rectangle2D.Double(values[0], values[1], values[2], values[3]);
        }
        else
        {
            extent = new Rectangle2D.Double(-100, -100, 200, 200);
        }
        // TODO different!
        String sizeString = System.getProperties().getProperty("animation.panel.size");
        Dimension size = new Dimension(1024, 768);
        if (sizeString != null)
        {
            double width = new Double(sizeString.substring(0, sizeString.indexOf(";"))).doubleValue();
            double height = new Double(sizeString.substring(sizeString.indexOf(";") + 1)).doubleValue();
            size = new Dimension((int) width, (int) height);
        }
        AnimationPanel panel = new AnimationPanel(extent, size, simulator);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(new ButtonPanel(panel), BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        panel.requestFocus();
    }

    /**
     * The ButtonPanel class
     */
    public static class ButtonPanel extends JPanel
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * constructs a new ButtonPanel
         * @param target GridPanel; the target to control
         */
        public ButtonPanel(final GridPanel target)
        {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));

            JButton zoomIn = new JButton(new ZoomInAction(target));
            JButton zoomOut = new JButton(new ZoomOutAction(target));
            JButton left = new JButton(new PanLeftAction(target));
            JButton right = new JButton(new PanRightAction(target));
            JButton up = new JButton(new PanUpAction(target));
            JButton down = new JButton(new PanDownAction(target));
            JButton grid = new JButton(new ShowGridAction(target));
            JButton home = new JButton(new HomeAction(target));
            this.add(zoomIn);
            this.add(zoomOut);
            this.add(left);
            this.add(right);
            this.add(up);
            this.add(down);
            this.add(grid);
            this.add(home);
        }
    }
}
