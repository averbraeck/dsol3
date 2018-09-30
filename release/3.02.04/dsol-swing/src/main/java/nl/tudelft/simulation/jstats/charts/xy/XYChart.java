package nl.tudelft.simulation.jstats.charts.xy;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.rmi.RemoteException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.PlotOrientation;

import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.Swingable;
import nl.tudelft.simulation.jstats.statistics.Persistent;
import nl.tudelft.simulation.language.filters.FilterInterface;

/**
 * The xyChart specifies the xyChart in DSOL.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:39:04 $
 * @since 1.5
 */
public class XYChart implements Swingable
{
    /** x-axis is linear y-axis is linear. */
    public static final short XLINEAR_YLINEAR = 0;

    /** x-axis is linear y-axis is logarithmic. */
    public static final short XLINEAR_YLOGARITHMIC = 1;

    /** x-axis is logarithmic y-axis is linear. */
    public static final short XLOGARITHMIC_YLINEAR = 2;

    /** x-axis is logarithmic y-axis is logarithmic. */
    public static final short XLOGARITHMIC_YLOGARITHMIC = 3;

    /** LABEL_X_AXIS is the label on the X-axis. */
    public static final String LABEL_X_AXIS = "X";

    /** LABEL_Y_AXIS is the label on the Y-axis. */
    public static final String LABEL_Y_AXIS = "value";

    /** chart refers to the chart. */
    protected JFreeChart chart = null;

    /** dataset refers to the dataset. */
    protected XYDataset dataset = new XYDataset();

    /** the axis type of the chart. */
    protected short axisType = XLINEAR_YLINEAR;

    /** the period to show on the domain axis. */
    private double period = Double.POSITIVE_INFINITY;

    /**
     * constructs a new XYChart.
     * @param title the title
     */
    public XYChart(final String title)
    {
        this(title, null, null);
    }

    /**
     * constructs a new XYChart.
     * @param title the title
     * @param axisType the axisType
     */
    public XYChart(final String title, final short axisType)
    {
        this(title, null, null, axisType);
    }

    /**
     * constructs a new XYChart.
     * @param title tht title
     * @param domain the domain
     */
    public XYChart(final String title, final double[] domain)
    {
        this(title, domain, null);
    }

    /**
     * constructs a new XYChart.
     * @param title tht title
     * @param period the period to show in the domain
     */
    public XYChart(final String title, final double period)
    {
        this(title, period, null);
    }

    /**
     * constructs a new XYChart.
     * @param title tht title
     * @param period the period to show in the domain
     * @param axisType the axisType to use.
     */
    public XYChart(final String title, final double period, final short axisType)
    {
        this(title, period, null, axisType);
    }

    /**
     * constructs a new XYChart.
     * @param title tht title
     * @param domain the domain
     * @param axisType the axisType to use.
     */
    public XYChart(final String title, final double[] domain, final short axisType)
    {
        this(title, domain, null, axisType);
    }

    /**
     * constructs a new XYChart.
     * @param title the title
     * @param domain the domain
     * @param range the range
     */
    public XYChart(final String title, final double[] domain, final double[] range)
    {
        this(title, domain, range, XYChart.XLINEAR_YLINEAR);
    }

    /**
     * constructs a new XYChart.
     * @param title the title
     * @param period the period to show in the domain
     * @param range the range
     */
    public XYChart(final String title, final double period, final double[] range)
    {
        this(title, period, range, XYChart.XLINEAR_YLINEAR);
    }

    /**
     * constructs a new XYChart.
     * @param title the title
     * @param period the period to show in the domain
     * @param range the range
     * @param axisType the type of the axsis
     */
    public XYChart(final String title, final double period, final double[] range, final short axisType)
    {
        this(title, new double[]{Double.NaN, period}, range, axisType);
    }

    /**
     * constructs a new XYChart.
     * @param title the title
     * @param domain the domain
     * @param range the range
     * @param axisType the type of the axsis
     */
    public XYChart(final String title, final double[] domain, final double[] range, final short axisType)
    {
        super();
        this.chart =
                ChartFactory.createXYLineChart(title, LABEL_X_AXIS, LABEL_Y_AXIS, this.dataset,
                        PlotOrientation.VERTICAL, true, true, true);
        this.chart.setBackgroundPaint(new GradientPaint(0.0F, 0.0F, Color.white, 1000F, 0.0F, Color.blue));
        this.axisType = axisType;
        switch (this.axisType)
        {
            case XYChart.XLINEAR_YLOGARITHMIC:
                this.chart.getXYPlot().setRangeAxis(new LogarithmicAxis(XYChart.LABEL_Y_AXIS));
                break;
            case XYChart.XLOGARITHMIC_YLINEAR:
                this.chart.getXYPlot().setDomainAxis(new LogarithmicAxis(XYChart.LABEL_X_AXIS));
                break;
            case XYChart.XLOGARITHMIC_YLOGARITHMIC:
                this.chart.getXYPlot().setDomainAxis(new LogarithmicAxis(XYChart.LABEL_X_AXIS));
                this.chart.getXYPlot().setRangeAxis(new LogarithmicAxis(XYChart.LABEL_Y_AXIS));
                break;
            default:
                break;
        }

        if (domain != null)
        {
            if (Double.isNaN(domain[0]))
            {
                this.chart.getXYPlot().getDomainAxis().setAutoRange(true);
                this.period = domain[1];
            }
            else
            {
                this.chart.getXYPlot().getDomainAxis().setAutoRange(false);
                this.chart.getXYPlot().getDomainAxis().setLowerBound(domain[0]);
                this.chart.getXYPlot().getDomainAxis().setUpperBound(domain[1]);
            }
        }
        else
        {
            this.chart.getXYPlot().getDomainAxis().setAutoRange(true);
        }
        if (range != null)
        {
            this.chart.getXYPlot().getRangeAxis().setAutoRange(false);
            this.chart.getXYPlot().getRangeAxis().setLowerBound(range[0]);
            this.chart.getXYPlot().getRangeAxis().setUpperBound(range[1]);
        }
        else
        {
            this.chart.getXYPlot().getRangeAxis().setAutoRange(true);
        }
        this.dataset.addChangeListener(this.chart.getXYPlot());
        this.getChart().fireChartChanged();
    }

    /**
     * adds a tally to the xyChart
     * @param persistent the persistent
     */
    public void add(final Persistent persistent)
    {
        XYSeries set = new XYSeries(persistent.getDescription(), this.axisType, this.period);
        persistent.addListener(set, Persistent.VALUE_EVENT, false);
        this.getDataset().addSeries(set);
    }

    /**
     * adds an eventProducer to the xyChart
     * @param description the description of the eventProducer
     * @param source the souce
     * @param eventType the event
     * @throws RemoteException on network failure
     */
    public void add(final String description, final EventProducerInterface source, final EventType eventType)
            throws RemoteException
    {
        XYSeries set = new XYSeries(description, this.axisType, this.period);
        source.addListener(set, eventType, EventProducerInterface.FIRST_POSITION, false);
        this.getDataset().addSeries(set);
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
     * returns the chartPanel of this xyChart
     * @return ChartPanel
     */
    public Container getSwingPanel()
    {
        ChartPanel result = new ChartPanel(this.chart);
        result.setMouseZoomable(true, false);
        return result;
    }

    /**
     * returns the dataset of a xyChart
     * @return HistogramSeries
     */
    public XYDataset getDataset()
    {
        return this.dataset;
    }

    /**
     * applies a filter on the chart
     * @param filter the filter to apply
     */
    public void setFilter(final FilterInterface filter)
    {
        this.dataset.setFilter(filter);
    }
}
