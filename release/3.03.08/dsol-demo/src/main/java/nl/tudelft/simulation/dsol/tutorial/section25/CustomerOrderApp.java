package nl.tudelft.simulation.dsol.tutorial.section25;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 * A Simple console app to run the Customer-Order model.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public final class CustomerOrderApp
{
    /** */
    private CustomerOrderApp()
    {
        // static class -- just meant to run the model as a console application
    }

    /**
     * executes our model.
     * @param args String[]; empty
     * @throws SimRuntimeException on simulation error
     * @throws NamingException on Context error
     */
    public static void main(final String[] args) throws SimRuntimeException, NamingException
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble();
        CustomerOrderModel model = new CustomerOrderModel(simulator);
        Replication.TimeDouble<DEVSSimulator.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        simulator.start();
    }
}
