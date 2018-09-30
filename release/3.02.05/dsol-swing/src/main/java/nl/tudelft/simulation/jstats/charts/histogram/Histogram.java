package nl.tudelft.simulation.jstats.charts.histogram;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.rmi.RemoteException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;

import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.Swingable;
import nl.tudelft.simulation.jstats.statistics.Counter;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @since 1.5
 */
public class Histogram implements Swingable
{
    /** LABEL_X_AXIS is the label on the X-axis. */
    public static final String LABEL_X_AXIS = "X";

    /** LABEL_Y_AXIS is the label on the Y-axis. */
    public static final String LABEL_Y_AXIS = "#";

    /** chart refers to the chart. */
    protected JFreeChart chart = null;

    /** dataset refers to the dataset. */
    protected HistogramDataset dataset = null;

    /**
     * constructs a new Histogram.
     * @param title the title
     * @param domain the domain
     * @param numberofBins the numberofbins
     */
    public Histogram(final String title, final double[] domain, final int numberofBins)
    {
        this(title, domain, null, numberofBins);
    }

    /**
     * constructs a new Histogram.
     * @param title the title. The title of the histogram
     * @param domain the domain of the x-axis.
     * @param range the y-axis range of the histogram.
     * @param numberofBins the numberofbins of this histogram.
     */
    public Histogram(final String title, final double[] domain, final double[] range, final int numberofBins)
    {
        super();
        this.dataset = new HistogramDataset(title, domain, range, numberofBins);

        this.chart = ChartFactory.createHistogram(title, LABEL_X_AXIS, LABEL_Y_AXIS, this.dataset,
                PlotOrientation.VERTICAL, true, true, true);
        this.chart.setBackgroundPaint(new GradientPaint(0.0F, 0.0F, Color.white, 1000F, 0.0F, Color.blue));

        this.chart.getXYPlot().setRangeAxis(new NumberAxis(Histogram.LABEL_Y_AXIS));
        this.chart.getXYPlot().getRangeAxis().setAutoRange(true);
        this.chart.getXYPlot().setDomainAxis(
                new HistogramDomainAxis(this.chart.getXYPlot(), Histogram.LABEL_X_AXIS, domain, numberofBins));
        this.dataset.addChangeListener(this.chart.getXYPlot());
    }

    /**
     * adds a counter to the histogramdataset. This histogram then subscribes its dataset to the
     * <code>Counter.COUNT_EVENT</code>.
     * @param counter the counter to add.
     */
    public synchronized void add(final Counter counter)
    {
        HistogramSeries set = this.getDataset().addSeries(counter.getDescription());
        counter.addListener(set, Counter.COUNT_EVENT, false);
    }

    /**
     * adds an eventProducer to the histogram dataset. The histogram subscribes its dataset subsequentially to the
     * specified event.
     * @param description the description of the eventProducer
     * @param source the eventproducer which functions as source for this histogram.
     * @param eventType the eventType.
     * @throws RemoteException on network error for the (possibly remote) event listener
     */
    public synchronized void add(final String description, final EventProducerInterface source,
            final EventType eventType) throws RemoteException
    {
        HistogramSeries set = this.getDataset().addSeries(description);
        source.addListener(set, eventType, false);
    }

    /**
     * returns the chart
     * @return JFreeChart
     */
    public JFreeChart getChart()
    {
        return this.chart;
    }

    /**
     * returns the chartPanel of this histogram.
     * @return ChartPanel
     */
    public Container getSwingPanel()
    {
        ChartPanel result = new ChartPanel(this.chart);
        result.setMouseZoomable(true, false);
        return result;
    }

    /**
     * returns the dataset of a histogram.
     * @return the HistogramDataset containing all series.
     */
    public HistogramDataset getDataset()
    {
        return this.dataset;
    }

}
