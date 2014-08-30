package nl.tudelft.simulation.dsol.statistics.charts;

import java.rmi.RemoteException;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The BoxAndWhiskerPlot extends the BoxAndWhiskerPlot and links this it to the dsol framework <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:21 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoxAndWhiskerChart extends nl.tudelft.simulation.jstats.charts.boxAndWhisker.BoxAndWhiskerChart
{
    /**
     * constructs a new BoxAndWhiskerChart.
     * @param simulator the simulator
     * @param title the title
     */
    public BoxAndWhiskerChart(final SimulatorInterface simulator, final String title)
    {
        super(title);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/charts");
            ContextUtil.bind(context, this);
        }
        catch (RemoteException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.getChart().getTitle().getText();
    }
}
