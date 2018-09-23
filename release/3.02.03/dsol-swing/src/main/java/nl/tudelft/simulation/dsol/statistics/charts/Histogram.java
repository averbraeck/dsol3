package nl.tudelft.simulation.dsol.statistics.charts;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The Histogram extends the Histogram and links this it to the dsol framework <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:21 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Histogram extends nl.tudelft.simulation.jstats.charts.histogram.Histogram
{
    /**
     * constructs a new Histogram.
     * @param simulator the simulator
     * @param title the title of the plot
     * @param domain the domain of the plot
     * @param range the range of the plot
     * @param numberofBins the number of bins in this plot
     */
    public Histogram(final SimulatorInterface simulator, final String title, final double[] domain,
            final double[] range, final int numberofBins)
    {
        super(title, domain, range, numberofBins);
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
     * constructs a new Histogram.
     * @param simulator the simulator
     * @param title the title
     * @param domain the domain
     * @param numberofBins the number of bins
     */
    public Histogram(final SimulatorInterface simulator, final String title, final double[] domain,
            final int numberofBins)
    {
        super(title, domain, numberofBins);
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
