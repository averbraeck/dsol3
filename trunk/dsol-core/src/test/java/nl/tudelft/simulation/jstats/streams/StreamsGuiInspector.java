/*
 * @(#)DistributionsGUIInspector.java Mar 21, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.jstats.streams;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * The DistributionsGUIInspector provides graphical insight in the randomness of different streams.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version 1.0, 2004-03-18
 * @since 1.5
 */
public class StreamsGuiInspector extends JPanel
{

    /**
     * the stream of the frame
     */
    private StreamInterface stream = null;

    /**
     * constructs a new DistributionsGUIInspector
     * @param stream the stream to display
     */
    public StreamsGuiInspector(final StreamInterface stream)
    {
        this.setPreferredSize(new Dimension(500, 500));
        this.stream = stream;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 10000; i++)
        {
            g2.fillOval(this.stream.nextInt(0, 500), this.stream.nextInt(0, 500), 1, 1);
        }
    }

    /**
     * executes the main program
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        JFrame app = new JFrame("Stream gui tester");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane contentPane = new JTabbedPane();
        contentPane.add("Java2Random", new StreamsGuiInspector(new Java2Random()));
        contentPane.add("MersenneTwister", new StreamsGuiInspector(new MersenneTwister()));
        contentPane.add("DX120Generator", new StreamsGuiInspector(new DX120Generator()));

        app.getContentPane().add(contentPane);
        app.pack();
        app.setVisible(true);
    }
}