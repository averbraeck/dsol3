package nl.tudelft.simulation.dsol.statistics.charts;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The XYPlot extends the XYPlot and links this it to the dsol framework <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:21 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class XYChart extends nl.tudelft.simulation.jstats.charts.xy.XYChart
{
    /** the logger. */
    private static Logger logger = LogManager.getLogger(XYChart.class);

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     */
    public XYChart(final SimulatorInterface simulator, final String title)
    {
        this(simulator, title, new double[]{0, simulator.getReplication().getTreatment().getRunLength().doubleValue()});
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param axisType the axisType to use.
     */
    public XYChart(final SimulatorInterface simulator, final String title, final short axisType)
    {
        this(simulator, title, new double[]{0, simulator.getReplication().getTreatment().getRunLength().doubleValue()},
                axisType);
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param domain the domain
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double[] domain)
    {
        super(title, domain);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param period the period
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double period)
    {
        super(title, period);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param domain the domain
     * @param axisType the axisType to use.
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double[] domain, final short axisType)
    {
        super(title, domain, axisType);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param period the period
     * @param axisType the axisType to use.
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double period, final short axisType)
    {
        super(title, period, axisType);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param domain the domain
     * @param range the range
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double[] domain, final double[] range)
    {
        super(title, domain, range);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param period the period
     * @param range the range
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double period, final double[] range)
    {
        super(title, period, range);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param domain the domain
     * @param range the range
     * @param axisType the XYChart.axisType
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double[] domain, final double[] range,
            final short axisType)
    {
        super(title, domain, range, axisType);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator the simulator
     * @param title the title
     * @param period the period
     * @param range the range
     * @param axisType the XYChart.axisType
     */
    public XYChart(final SimulatorInterface simulator, final String title, final double period, final double[] range,
            final short axisType)
    {
        super(title, period, range, axisType);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.getChart().getTitle().getText();
    }
}
