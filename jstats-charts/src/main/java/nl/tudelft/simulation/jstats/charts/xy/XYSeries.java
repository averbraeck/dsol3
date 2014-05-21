/*
 * @(#) XYSeries.java Sep 26, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */

package nl.tudelft.simulation.jstats.charts.xy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.TimedEvent;
import nl.tudelft.simulation.language.filters.FilterInterface;
import nl.tudelft.simulation.language.filters.ZeroFilter;
import nl.tudelft.simulation.logger.Logger;

import org.jfree.data.general.AbstractDataset;

/**
 * The xySerie specifies an xySerie for XY Plots in DSOL.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:39:04 $
 * @since 1.5
 */
public class XYSeries extends AbstractDataset implements EventListenerInterface
{
    /** serial version UID */
    private static final long serialVersionUID = 1L;

    /** LOWER_RANGE_EVENT is fired on a range change */
    public static final EventType LOWER_RANGE_EVENT = new EventType("LOWER_RANGE_EVENT");

    /** UPPER_RANGE_EVENT is fired on a range change */
    public static final EventType UPPER_RANGE_EVENT = new EventType("UPPER_RANGE_EVENT");

    /** name refers to the name of the serie */
    private String name = null;

    /** the entries of the serie */
    protected List<double[]> entries = new ArrayList<double[]>();

    /** the axisType (default, logarithmic) */
    private short axisType = XYChart.XLINEAR_YLINEAR;

    /** the filters of this dataset */
    private FilterInterface filter = new ZeroFilter();

    /** the period of this set */
    private final double PERIOD;

    /**
     * constructs a new XYSeries.
     * @param name the name of the series.
     * @param axisType whether this serie is logarithmic (x=0 & y=0 are neglected)
     * @param period the period of this series.
     */
    public XYSeries(final String name, final short axisType, final double period)
    {
        super();
        this.axisType = axisType;
        this.name = name;
        this.PERIOD = period;
        this.fireDatasetChanged();
    }

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface #notify(nl.tudelft.simulation.event.EventInterface)
     */
    public synchronized void notify(final EventInterface event)
    {
        TimedEvent timedEvent = (TimedEvent) event;

        // We have chosen to simply neglect <=0.0 values on logarithmic axis
        if (this.axisType == XYChart.XLOGARITHMIC_YLINEAR || this.axisType == XYChart.XLOGARITHMIC_YLOGARITHMIC)
        {
            if (timedEvent.getTimeStamp() <= 0.0)
            {
                Logger.warning(this, "notify", "refusing xvalue of " + event + " on logrithmic chart");
                return;
            }
        }
        if (this.axisType == XYChart.XLINEAR_YLOGARITHMIC || this.axisType == XYChart.XLOGARITHMIC_YLOGARITHMIC)
        {
            if (((Number) timedEvent.getContent()).doubleValue() <= 0.0)
            {
                Logger.warning(this, "notify", "refusing yValue of " + event + " on logrithmic chart");
                return;
            }
        }
        double[] point = {timedEvent.getTimeStamp(), ((Number) timedEvent.getContent()).doubleValue()};
        if (!this.filter.accept(point))
        {
            return;
        }
        this.entries.add(point);
        if (!(Double.isInfinite(this.PERIOD)))
        {
            double lowerBounds = point[0] - this.PERIOD;
            for (Iterator<double[]> i = this.entries.iterator(); i.hasNext();)
            {
                double[] entry = i.next();
                if (entry[0] < lowerBounds)
                {
                    i.remove();
                }
                else
                {
                    break;
                }
            }
        }
        this.fireDatasetChanged();
    }

    /**
     * returns the number of items in this seris
     * @return int the number
     */
    public int getItemCount()
    {
        return this.entries.size();
    }

    /**
     * returns the X value
     * @param item the item
     * @return Number the xValue
     */
    public synchronized double getXValue(int item)
    {
        item = Math.min(item, this.entries.size() - 1);
        return this.entries.get(item)[0];
    }

    /**
     * returns the yValue
     * @param item the item
     * @return Number
     */
    public double getYValue(int item)
    {
        item = Math.min(item, this.entries.size() - 1);
        return this.entries.get(item)[1];
    }

    /**
     * returns the name of this serie
     * @return String name
     */
    public String getSeriesName()
    {
        return this.name;
    }

    /**
     * applies a filter on the chart
     * @param filter the filter to apply
     */
    public void setFilter(final FilterInterface filter)
    {
        this.filter = filter;
    }
}