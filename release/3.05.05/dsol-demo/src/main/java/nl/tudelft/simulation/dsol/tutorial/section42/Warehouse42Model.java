package nl.tudelft.simulation.dsol.tutorial.section42;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterDouble;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterLong;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.statistics.SimTally;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * A BoatModel.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Warehouse42Model extends AbstractDSOLModel.TimeDouble<DEVSSimulator.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** ordering costs statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    SimTally.TimeDouble orderingCosts;

    /** inventory statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    SimPersistent.TimeDouble inventory;

    /** backlog statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    SimPersistent.TimeDouble backlog;

    /**
     * constructs a new BoatModel.
     * @param simulator DEVSSimulator.TimeDouble; the simulator
     * @throws InputParameterException on parameter error
     */
    public Warehouse42Model(final DEVSSimulator.TimeDouble simulator) throws InputParameterException
    {
        super(simulator);
        InputParameterMap retailerMap = new InputParameterMap("retailer", "Retailer", "Retailer", 1.0);
        retailerMap.add(new InputParameterDouble("backlogCosts", "Backlog costs", "Backlog costs", 1.0, 1.0));
        retailerMap.add(new InputParameterDouble("holdingCosts", "Holding costs", "Holding costs", 1.0, 2.0));
        retailerMap.add(new InputParameterDouble("marginalCosts", "Marginal costs", "Marginal costs", 3.0, 3.0));
        retailerMap.add(new InputParameterDouble("setupCosts", "Setup costs", "Setup costs", 30.0, 4.0));
        this.inputParameterMap.add(retailerMap);
        InputParameterMap policyMap = new InputParameterMap("policy", "Policy", "Policy", 2.0);
        policyMap.add(new InputParameterLong("lowerBound", "Lower bound", "Lower bound", 8L, 1.0));
        policyMap.add(new InputParameterLong("upperBound", "Upper bound", "Upper bound", 80L, 2.0));
        this.inputParameterMap.add(policyMap);
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel() throws SimRuntimeException
    {
        Map<String, StreamInterface> streams = new HashMap<>();
        streams.put("default", new MersenneTwister());
        this.simulator.getReplication().setStreams(streams);

        SellerInterface warehouse = new Warehouse(this.simulator);
        Retailer retailer = new Retailer(this.simulator, warehouse);
        new Customer(this.simulator, retailer);

        try
        {
            this.orderingCosts =
                    new SimTally.TimeDouble("orderingCosts", this.simulator, retailer, Retailer.TOTAL_ORDERING_COST_EVENT);
            this.inventory =
                    new SimPersistent.TimeDouble("inventory level", this.simulator, retailer, Retailer.INVENTORY_LEVEL_EVENT);
            this.backlog = new SimPersistent.TimeDouble("backlog level", this.simulator, retailer, Retailer.BACKLOG_LEVEL);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "Warehouse42Model";
    }
}
