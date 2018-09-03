package nl.tudelft.simulation.dsol.tutorial.section42;

import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.tutorial.section42.policies.OrderingPolicy;
import nl.tudelft.simulation.dsol.tutorial.section42.policies.StationaryPolicy;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * A Retailer <br>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 8, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Retailer extends EventProducer implements BuyerInterface, SellerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** TOTAL_ORDERING_COST_EVENT is fired whenever ordering occurs. */
    public static final EventType TOTAL_ORDERING_COST_EVENT = new EventType("TOTAL_ORDERING_COST_EVENT");

    /** INVENTORY_LEVEL_EVENT is fired on changes in inventory. */
    public static final EventType INVENTORY_LEVEL_EVENT = new EventType("INVENTORY_LEVEL_EVENT");

    /** BACKLOG_LEVEL is fired on BACKLOG_LEVEL changes. */
    public static final EventType BACKLOG_LEVEL = new EventType("BACKLOG_LEVEL");

    /** the actual inventoryLevel. */
    private long inventory = 60L;

    /** the ordering backlog. */
    private long backLog = 0L;

    /** the simulator on which to schedule. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the warehouse we use. */
    private SellerInterface warehouse = null;

    /** the orderingPolicy. */
    private OrderingPolicy orderingPolicy = null;

    /** the costs. */
    private double backlogCosts;

    /** the costs. */
    private double holdingCosts;

    /** the costs. */
    private double marginalCosts;

    /** the costs. */
    private double setupCosts;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(Retailer.class);

    /**
     * constructs a new Retailer.
     * @param simulator the simulator on which we can schedule
     * @param warehouse the warehouse to buy at
     * @throws RemoteException on failure
     */
    public Retailer(final DEVSSimulatorInterface.TimeDouble simulator, final SellerInterface warehouse)
            throws RemoteException
    {
        super();
        this.simulator = simulator;
        this.warehouse = warehouse;
        this.orderingPolicy = new StationaryPolicy(simulator);
        Properties properties = this.simulator.getReplication().getTreatment().getProperties();
        this.backlogCosts = new Double(properties.getProperty("retailer.costs.setup")).doubleValue();
        this.holdingCosts = new Double(properties.getProperty("retailer.costs.holding")).doubleValue();
        this.marginalCosts = new Double(properties.getProperty("retailer.costs.marginal")).doubleValue();
        this.setupCosts = new Double(properties.getProperty("retailer.costs.setup")).doubleValue();
        this.reviewInventory();
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveProduct(final long amount)
    {
        long served = this.backLog - Math.max(0, this.backLog - amount);
        this.backLog = Math.max(0, this.backLog - amount);
        this.inventory = this.inventory + Math.max(0, amount - served);
        try
        {
            this.fireTimedEvent(INVENTORY_LEVEL_EVENT, this.inventory, this.simulator.getSimulatorTime().get());
            this.fireTimedEvent(BACKLOG_LEVEL, this.backLog, this.simulator.getSimulatorTime().get());
        }
        catch (RemoteException exception)
        {
            logger.warn("receiveProduct", exception);
        }
    }

    /**
     * reviews the inventoryLevel and possibly orders.
     */
    private void reviewInventory()
    {
        double costs = this.holdingCosts * this.inventory + this.backlogCosts * this.backLog;
        long amount = this.orderingPolicy.computeAmountToOrder(this.inventory);
        if (amount > 0)
        {
            costs = costs + this.setupCosts + amount * this.marginalCosts;
            this.fireEvent(TOTAL_ORDERING_COST_EVENT, costs);
            this.warehouse.order(this, amount);
        }
        try
        {
            this.simulator.scheduleEvent(new SimEvent<SimTimeDouble>(this.simulator.getSimulatorTime().plus(1.0), this,
                    this, "reviewInventory", null));
        }
        catch (Exception exception)
        {
            logger.warn("reviewInventory", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void order(final BuyerInterface buyer, final long amount)
    {
        long actualOrderSize = Math.min(amount, this.inventory);
        this.inventory = this.inventory - actualOrderSize;
        if (actualOrderSize < amount)
        {
            this.backLog = this.backLog + (amount - actualOrderSize);
        }
        try
        {
            this.fireTimedEvent(INVENTORY_LEVEL_EVENT, this.inventory, this.simulator.getSimulatorTime().get());
            this.fireTimedEvent(BACKLOG_LEVEL, this.backLog, this.simulator.getSimulatorTime().get());
        }
        catch (RemoteException exception)
        {
            logger.warn("receiveProduct", exception);
        }
        buyer.receiveProduct(actualOrderSize);
    }

}
