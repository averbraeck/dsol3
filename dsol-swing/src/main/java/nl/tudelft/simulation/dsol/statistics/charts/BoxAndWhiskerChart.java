package nl.tudelft.simulation.dsol.statistics.charts;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The BoxAndWhiskerPlot extends the BoxAndWhiskerPlot and links this it to the dsol framework.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoxAndWhiskerChart extends nl.tudelft.simulation.jstats.charts.boxAndWhisker.BoxAndWhiskerChart
{
    /**
     * constructs a new BoxAndWhiskerChart.
     * @param simulator SimulatorInterface; the simulator
     * @param title String; the title
     */
    public BoxAndWhiskerChart(final SimulatorInterface simulator, final String title)
    {
        super(title);
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
