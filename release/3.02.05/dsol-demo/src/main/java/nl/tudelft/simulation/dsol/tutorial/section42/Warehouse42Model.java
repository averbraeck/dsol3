package nl.tudelft.simulation.dsol.tutorial.section42;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.Tally;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * A BoatModel.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Warehouse42Model implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator;

    /** ordering costs statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Tally<Double, Double, SimTimeDouble> orderingCosts;

    /** inventory statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Persistent<Double, Double, SimTimeDouble> inventory;

    /** backlog statistic. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Persistent<Double, Double, SimTimeDouble> backlog;

    /**
     * constructs a new BoatModel.
     */
    public Warehouse42Model()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> devsSimulator)
            throws SimRuntimeException
    {
        this.simulator = (DEVSSimulatorInterface.TimeDouble) devsSimulator;

        Properties properties = this.simulator.getReplication().getTreatment().getProperties();
        properties.put("retailer.costs.backlog", "1");
        properties.put("retailer.costs.holding", "1");
        properties.put("retailer.costs.marginal", "3");
        properties.put("retailer.costs.setup", "30");
        properties.put("policy.lowerBound", "8");
        properties.put("policy.upperBound", "80");

        Map<String, StreamInterface> streams = new HashMap<>();
        streams.put("default", new MersenneTwister());
        this.simulator.getReplication().setStreams(streams);

        SellerInterface warehouse = new Warehouse(this.simulator);
        Retailer retailer = new Retailer(this.simulator, warehouse);
        new Customer(this.simulator, retailer);

        try
        {
            this.orderingCosts =
                    new Tally<>("orderingCosts", devsSimulator, retailer, Retailer.TOTAL_ORDERING_COST_EVENT);
            this.inventory =
                    new Persistent<>("inventory level", devsSimulator, retailer, Retailer.INVENTORY_LEVEL_EVENT);
            this.backlog = new Persistent<>("backlog level", devsSimulator, retailer, Retailer.BACKLOG_LEVEL);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
