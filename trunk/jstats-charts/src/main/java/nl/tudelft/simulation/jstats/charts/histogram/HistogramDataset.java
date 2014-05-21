/*
 * @(#)HistogramDataset.java Sep 22, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.simulation.jstats.charts.histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

/**
 * The dataset defines a histogram data set. A dataset contains multiple series each containing the entries to display.
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:39:02 $
 * @since 1.5
 */
public class HistogramDataset extends SimpleHistogramDataset implements IntervalXYDataset, DatasetChangeListener
{
    /** domain is the minimal value to be displayed in this set */
    protected double[] domain = null;

    /** range is the maximum value to be displayed in the set */
    protected double[] range = null;

    /** numberOfBins is the number of bins (or categories between min-max) */
    protected int numberOfBins = 0;

    /** series the series in this set */
    protected HistogramSeries[] series = new HistogramSeries[0];

    /**
     * constructs a new HistogramDataset
     * @param domain the domain of the set.
     * @param range the range of the set.
     * @param numberOfBins the number of bins
     */
    public HistogramDataset(final Comparable<?> key, final double[] domain, final double[] range, final int numberOfBins)
    {
        super(key);
        this.domain = domain;
        this.range = range;
        this.numberOfBins = numberOfBins;
    }

    /**
     * adds a series to the dataset.
     * @param name the name of the series.
     * @return HistogramSeries.
     */
    public synchronized HistogramSeries addSeries(final String name)
    {
        HistogramSeries histogramSeries = new HistogramSeries(name, this.domain, this.range, this.numberOfBins);
        this.addSeries(histogramSeries);
        return histogramSeries;
    }

    /**
     * adds a series to the dataset
     * @param newSeries the set to add.
     */
    public synchronized void addSeries(final HistogramSeries newSeries)
    {
        newSeries.addChangeListener(this);
        List<HistogramSeries> list = new ArrayList<HistogramSeries>(Arrays.asList(this.series));
        list.add(newSeries);
        this.series = list.toArray(new HistogramSeries[list.size()]);
        this.fireDatasetChanged();
    }

    /**
     * @see org.jfree.data.DatasetChangeListener #datasetChanged(org.jfree.data.DatasetChangeEvent)
     */
    public void datasetChanged(final DatasetChangeEvent arg0)
    {
        if (arg0 != null)
        {
            this.fireDatasetChanged();
        }
    }

    /**
     * @see org.jfree.data.statistics.SimpleHistogramDataset#getEndXValue(int, int)
     */
    public double getEndXValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getEndXValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getEndYValue(int, int)
     */
    public double getEndYValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getEndYValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getStartXValue(int, int)
     */
    public double getStartXValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getStartXValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getStartYValue(int, int)
     */
    public double getStartYValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getStartYValue(bin);
    }

    /**
     * @see org.jfree.data.XYDataset#getItemCount(int)
     */
    public int getItemCount(final int serieNr)
    {
        return this.series[serieNr].getBinCount();
    }

    /**
     * @see org.jfree.data.XYDataset#getXValue(int, int)
     */
    public double getXValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getXValue(bin);
    }

    /**
     * @see org.jfree.data.XYDataset#getYValue(int, int)
     */
    public double getYValue(final int serieNr, final int bin)
    {
        return this.series[serieNr].getYValue(bin);
    }

    /**
     * @see org.jfree.data.SeriesDataset#getSeriesCount()
     */
    public int getSeriesCount()
    {
        return this.series.length;
    }

    /**
     * @param serieNr
     * @return
     */
    public String getSeriesName(final int serieNr)
    {
        return this.series[serieNr].getName();
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getEndX(int, int)
     */
    public Number getEndX(final int serieNr, final int bin)
    {
        return this.series[serieNr].getEndXValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getEndY(int, int)
     */
    public Number getEndY(final int serieNr, final int bin)
    {
        return this.series[serieNr].getEndYValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getStartX(int, int)
     */
    public Number getStartX(final int serieNr, final int bin)
    {
        return this.series[serieNr].getStartXValue(bin);
    }

    /**
     * @see org.jfree.data.IntervalXYDataset#getStartY(int, int)
     */
    public Number getStartY(final int serieNr, final int bin)
    {
        return this.series[serieNr].getStartYValue(bin);
    }

    /**
     * @see org.jfree.data.XYDataset#getX(int, int)
     */
    public Number getX(final int serieNr, final int bin)
    {
        return this.series[serieNr].getXValue(bin);
    }

    /**
     * @see org.jfree.data.XYDataset#getY(int, int)
     */
    public Number getY(final int serieNr, final int bin)
    {
        return this.series[serieNr].getYValue(bin);
    }
}