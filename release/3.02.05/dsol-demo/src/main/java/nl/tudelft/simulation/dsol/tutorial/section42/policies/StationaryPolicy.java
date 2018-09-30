package nl.tudelft.simulation.dsol.tutorial.section42.policies;

import java.util.Properties;

import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * A StationaryPolicy.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
