package nl.tudelft.simulation.dsol.statistics.charts;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The XYPlot extends the XYPlot and links this it to the dsol framework.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class XYChart extends nl.tudelft.simulation.jstats.charts.xy.XYChart
{
    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     */
    public XYChart(final SimulatorInterface simulator, final String title)
    {
        this(simulator, title, new double[]{0, simulator.getReplication().getTreatment().getRunLength().doubleValue()});
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param axisType short; the axisType to use.
     */
    public XYChart(final SimulatorInterface simulator, final String title, final short axisType)
    {
        this(simulator, title, new double[]{0, simulator.getReplication().getTreatment().getRunLength().doubleValue()},
                axisType);
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param domain double[]; the domain
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param period double; the period
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param domain double[]; the domain
     * @param axisType short; the axisType to use.
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param period double; the period
     * @param axisType short; the axisType to use.
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param domain double[]; the domain
     * @param range double[]; the range
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param period double; the period
     * @param range double[]; the range
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param domain double[]; the domain
     * @param range double[]; the range
     * @param axisType short; the XYChart.axisType
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new XYChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     * @param period double; the period
     * @param range double[]; the range
     * @param axisType short; the XYChart.axisType
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
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.getChart().getTitle().getText();
    }
}
