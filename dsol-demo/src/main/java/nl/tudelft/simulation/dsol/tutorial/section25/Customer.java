package nl.tudelft.simulation.dsol.tutorial.section25;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Customer class as presented in section 2.5 in the DSOL tutorial..
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Customer
{
    /** the simulator we can schedule on. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /**
     * constructs a new Customer.
     * @param simulator DEVSSimulatorInterface.TimeDouble; The simulator to use.
     */
    public Customer(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super();
        this.simulator = simulator;
        this.generateOrder();
    }

    /** Generates a new Order. */
    private void generateOrder()
    {
        try
        {
            Order order = new Order("Television", 2.0);
            System.out.println("ordered " + order + " @ time=" + this.simulator.getSimulatorTime());

            // Now we schedule the next action at time = time + 2.0
            SimEventInterface<SimTimeDouble> simEvent =
                    new SimEvent<SimTimeDouble>(this.simulator.getSimTime().plus(2.0), this, this, "generateOrder", null);
            this.simulator.scheduleEvent(simEvent);
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().error(exception);
        }
    }
}
