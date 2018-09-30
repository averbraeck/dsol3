package nl.tudelft.simulation.dsol.tutorial.section42.policies;

import java.util.Properties;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * A StationaryPolicy <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class StationaryPolicy implements OrderingPolicy
{
    /** the lower bound of the policy. */
    private long lowerBound;

    /** the upper bound of the policy. */
    private long upperBound;

    /**
     * constructs a new StationaryPolicy.
     * @param simulator the simulator which is executing the experiment
     */
    public StationaryPolicy(final SimulatorInterface.TimeDouble simulator)
    {
        super();
        Properties properties = simulator.getReplication().getTreatment().getProperties();

        this.lowerBound = new Long(properties.getProperty("policy.lowerBound")).longValue();
        this.upperBound = new Long(properties.getProperty("policy.upperBound")).longValue();
    }

    /** {@inheritDoc} */
    @Override
    public final long computeAmountToOrder(final long inventory)
    {
        if (inventory <= this.lowerBound)
        {
            return this.upperBound - inventory;
        }
        return 0;
    }
}
