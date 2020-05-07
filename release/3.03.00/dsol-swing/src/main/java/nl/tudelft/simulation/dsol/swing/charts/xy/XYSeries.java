package nl.tudelft.simulation.dsol.swing.charts.xy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.data.general.AbstractDataset;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.TimedEvent;
import nl.tudelft.simulation.language.filters.FilterInterface;
import nl.tudelft.simulation.language.filters.ZeroFilter;

/**
 * The xySerie specifies an xySerie for XY Plots in DSOL..
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class XYSeries extends AbstractDataset implements EventListenerInterface
{
    /** serial version UId. */
    private static final long serialVersionUID = 1L;

    /** LOWER_RANGE_EVENT is fired on a range change. */
    public static final EventType LOWER_RANGE_EVENT = new EventType("LOWER_RANGE_EVENT");

    /** UPPER_RANGE_EVENT is fired on a range change. */
    public static final EventType UPPER_RANGE_EVENT = new EventType("UPPER_RANGE_EVENT");

    /** name refers to the name of the serie. */
    private String name = null;

    /** the entries of the serie. */
    protected List<double[]> entries = new ArrayList<double[]>();

    /** the axisType (default, logarithmic). */
    private short axisType = XYChart.XLINEAR_YLINEAR;

    /** the filters of this dataset. */
    private FilterInterface filter = new ZeroFilter();

    /** the period of this set. */
    private final double PERIOD;

    /**
     * constructs a new XYSeries.
     * @param name String; the name of the series.
     * @param axisType short; whether this serie is logarithmic (x=0 and y=0 are neglected)
     * @param period double; the period of this series.
     */
    public XYSeries(final String name, final short axisType, final double period)
    {
        super();
        this.axisType = axisType;
        this.name = name;
        this.PERIOD = period;
        this.fireDatasetChanged();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void notify(final EventInterface event)
    {
        TimedEvent<?> timedEvent = (TimedEvent<?>) event;
        Number timeStamp = (Number) timedEvent.getTimeStamp();

        // We have chosen to simply neglect <=0.0 values on logarithmic axis
        if (this.axisType == XYChart.XLOGARITHMIC_YLINEAR || this.axisType == XYChart.XLOGARITHMIC_YLOGARITHMIC)
        {
            if (timeStamp.doubleValue() <= 0.0)
            {
                SimLogger.always().warn("notify: refusing xvalue of {} on logrithmic chart", event);
                return;
            }
        }
        if (this.axisType == XYChart.XLINEAR_YLOGARITHMIC || this.axisType == XYChart.XLOGARITHMIC_YLOGARITHMIC)
        {
            if (((Number) timedEvent.getContent()).doubleValue() <= 0.0)
            {
                SimLogger.always().warn("notify: refusing yValue of {} on logrithmic chart", event);
                return;
            }
        }
        double[] point = {timeStamp.doubleValue(), ((Number) timedEvent.getContent()).doubleValue()};
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
     * @param item int; the item
     * @return Number the xValue
     */
    public synchronized double getXValue(int item)
    {
        item = Math.min(item, this.entries.size() - 1);
        return this.entries.get(item)[0];
    }

    /**
     * returns the yValue
     * @param item int; the item
     * @return Number
     */
    public double getYValue(int item)
    {
        item = Math.min(item, this.entries.size() - 1);
        return this.entries.get(item)[1];
    }

    /**
     * returns the name of this series
     * @return String name
     */
    public String getSeriesName()
    {
        return this.name;
    }

    /**
     * applies a filter on the chart
     * @param filter FilterInterface; the filter to apply
     */
    public void setFilter(final FilterInterface filter)
    {
        this.filter = filter;
    }
}