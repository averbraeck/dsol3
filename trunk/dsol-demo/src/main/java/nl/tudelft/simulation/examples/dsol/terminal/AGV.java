package nl.tudelft.simulation.examples.dsol.terminal;

import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * The AGVs modeled as resources.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Jul 25, 2018
 */
public class AGV extends IntResource<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** AGV time delay. */
    private final DistContinuous agvTime;

    /**
     * @param simulator the simulator
     * @param description the description
     * @param capacity the capacity
     * @param agvTime AGV time delay
     */
    public AGV(final DEVSSimulatorInterface.TimeDouble simulator, final String description, final long capacity,
            final DistContinuous agvTime)
    {
        super(simulator, description, capacity);
        this.agvTime = agvTime;
    }

    /**
     * @return the AGV handling time
     */
    public double drawDelay()
    {
        return this.agvTime.draw();
    }
}
