/*
 * @(#) XYDataset.java Sep 26, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.charts.xy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.tudelft.simulation.language.filters.FilterInterface;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractSeriesDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

/**
 * The xyDataset specifies the xyDataset in DSOL
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2014/05/05 00:26:06 $
 * @since 1.5
 */
public class XYDataset extends AbstractSeriesDataset implements org.jfree.data.xy.XYDataset, DatasetChangeListener
{

    /** default UID */
    private static final long serialVersionUID = 1L;

    /** series contains the series of the set */
    private XYSeries[] series = new XYSeries[0];

    /**
     * constructs a new XYDataset
     */
    public XYDataset()
    {
        super();
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
     * adds a dataset to the series
     * @param dataset the set
     */
    public synchronized void addSeries(final XYSeries dataset)
    {
        dataset.addChangeListener(this);
        List<XYSeries> list = new ArrayList<XYSeries>(Arrays.asList(this.series));
        list.add(dataset);
        this.series = list.toArray(new XYSeries[list.size()]);
        this.fireDatasetChanged();
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getDomainOrder()
     */
    public DomainOrder getDomainOrder()
    {
        return DomainOrder.ASCENDING;
    }

    /**
     * @see org.jfree.data.XYDataset#getItemCount(int)
     */
    public int getItemCount(final int serie)
    {
        return this.series[serie].getItemCount();
    }

    /**
     * @see org.jfree.data.XYDataset#getXValue(int, int)
     */
    public double getXValue(final int serie, final int item)
    {
        return this.series[serie].getXValue(item);
    }

    /**
     * @see org.jfree.data.XYDataset#getYValue(int, int)
     */
    public double getYValue(final int serie, final int item)
    {
        return this.series[serie].getYValue(item);
    }

    /**
     * @see org.jfree.data.SeriesDataset#getSeriesCount()
     */
    public int getSeriesCount()
    {
        return this.series.length;
    }

    /**
     * @param serie
     * @return name
     */
    public String getSeriesName(final int serie)
    {
        return this.series[serie].getSeriesName();
    }

    /**
     * @see org.jfree.data.XYDataset#getX(int, int)
     */
    public Number getX(final int serie, final int item)
    {
        return new Double(this.series[serie].getXValue(item));
    }

    /**
     * @see org.jfree.data.XYDataset#getY(int, int)
     */
    public Number getY(final int serie, final int item)
    {
        return new Double(this.series[serie].getYValue(item));
    }

    /**
     * applies a filter on the chart
     * @param filter the filter to apply
     */
    public void setFilter(final FilterInterface filter)
    {
        for (int i = 0; i < this.series.length; i++)
        {
            this.series[i].setFilter(filter);
        }
    }

    /**
     * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesKey(int)
     */
    @Override
    public Comparable getSeriesKey(int arg0)
    {
        return "Series " + arg0;
    }

}