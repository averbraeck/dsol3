package nl.tudelft.simulation.examples.dsol.mm1queue;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Seize is an extended Seize block whic sets delay times on arriving customers..
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Seize extends nl.tudelft.simulation.dsol.formalisms.flow.Seize.TimeDouble
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Seize.
     * @param simulator the devs simulator on which to schedule
     * @param resource the resource to claim
     */
    public Seize(final DEVSSimulatorInterface.TimeDouble simulator,
            final Resource<Double, Double, SimTimeDouble> resource)
    {
        super(simulator, resource);
    }

    /**
     * constructs a new Seize.
     * @param simulator the devs simulator on which to schedule
     * @param resource the resource to claim
     * @param requestedCapacity the amount to claim
     */
    public Seize(final DEVSSimulatorInterface.TimeDouble simulator,
            final Resource<Double, Double, SimTimeDouble> resource, final double requestedCapacity)
    {
        super(simulator, resource, requestedCapacity);
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveObject(final Object object)
    {
        Customer customer = (Customer) object;
        customer.setEntranceTime(this.simulator.getSimulatorTime());
        super.receiveObject(object);
    }
}
