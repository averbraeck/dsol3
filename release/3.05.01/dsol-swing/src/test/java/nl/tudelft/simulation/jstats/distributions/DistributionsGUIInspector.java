package nl.tudelft.simulation.jstats.distributions;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.djutils.event.Event;
import org.djutils.event.EventType;

import nl.tudelft.simulation.dsol.swing.charts.histogram.Histogram;
import nl.tudelft.simulation.dsol.swing.charts.histogram.HistogramSeries;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The DistributionsGUIInspector provides graphical insight in the randomness of different streams.
 * <p>
 * (c) 2002-2019-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class DistributionsGUIInspector extends JTabbedPane
{
    /**
     * constructs a new DistributionsGUIInspector
     */
    public DistributionsGUIInspector()
    {
        this.setPreferredSize(new Dimension(500, 500));
        StreamInterface stream = new MersenneTwister();

        DistContinuous distribution = new DistUniform(stream, 0, 1.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 1}).getSwingPanel());

        distribution = new DistConstant(stream, 10.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 10}).getSwingPanel());

        distribution = new DistExponential(stream, 1.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 6}).getSwingPanel());

        distribution = new DistGamma(stream, 2.0, 1.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 9}).getSwingPanel());

        distribution = new DistWeibull(stream, 2.0, 1.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 4}).getSwingPanel());

        distribution = new DistNormal(stream, 0.0, 1.0);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {-3, 3}).getSwingPanel());

        distribution = new DistLogNormal(stream, 0.0, 1);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 5}).getSwingPanel());

        distribution = new DistBeta(stream, 1.5, 5);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 1}).getSwingPanel());

        distribution = new DistBeta(stream, 5, 1.5);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 1}).getSwingPanel());

        distribution = new DistPearson5(stream, 1, 1);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 5}).getSwingPanel());

        distribution = new DistPearson6(stream, 1, 2, 4);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 5}).getSwingPanel());

        distribution = new DistTriangular(stream, 1, 1.5, 7);
        this.add(distribution.toString(), this.createHistogram(distribution, new double[] {0, 7}).getSwingPanel());
    }

    /**
     * creates a histogram of a discrete distribution
     * @param distribution the distribution to plot
     * @param domain the domain to show on the histogram
     * @return Histogram the histogram.
     */
    private Histogram createHistogram(final Dist distribution, final double[] domain)
    {
        Histogram histogram = new Histogram(distribution.toString(), domain, 20);
        HistogramSeries series = new HistogramSeries(distribution.toString(), domain, null, 20);
        EventType accept = new EventType("DISTRIBUTION_VALUE");
        for (int i = 0; i < 10000; i++)
        {
            if (distribution instanceof DistContinuous)
            {
                Double value = Double.valueOf(((DistContinuous) distribution).draw());
                series.notify(new Event(accept, null, value));
            }
            else
            {
                series.notify(new Event(accept, null, Long.valueOf(((DistDiscrete) distribution).draw())));
            }
        }
        histogram.getDataset().addSeries(series);
        return histogram;
    }

    /**
     * executes the main program
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        JFrame app = new JFrame("Distributions gui tester");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.getContentPane().add(new DistributionsGUIInspector());
        app.pack();
        app.setVisible(true);
    }
}
