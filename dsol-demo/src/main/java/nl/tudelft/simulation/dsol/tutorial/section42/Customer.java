package nl.tudelft.simulation.dsol.tutorial.section42;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistDiscrete;
import nl.tudelft.simulation.jstats.distributions.DistDiscreteEmpirical;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.distributions.empirical.Observations;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * A Customer <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 8, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Customer implements BuyerInterface
{
    /** the simulator to schedule on. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the retailer by whom we order our product. */
    private SellerInterface retailer = null;

    /** the intervalTime between consequtive orders. */
    private DistContinuous intervalTime = null;

    /** the orderBatchSize of an order. */
    private DistDiscrete orderBatchSize = null;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(Customer.class);

    /**
     * constructs a new Customer.
     * @param simulator the simulator to schedule on
     * @param retailer the retailer to buy at. In more advanced examples, we would look up this retailer at a yellow
     *            page.
     */
    public Customer(final DEVSSimulatorInterface.TimeDouble simulator, final SellerInterface retailer)
    {
        super();
        this.simulator = simulator;
        this.retailer = retailer;
        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.intervalTime = new DistExponential(stream, 0.1);
        Observations observations =
                new Observations(new Number[][]{{1, 1.0 / 6.0}, {2, 1.0 / 3.0}, {3, 1.0 / 3.0}, {4, 1.0 / 6.0}}, false);
        this.orderBatchSize = new DistDiscreteEmpirical(stream, observations);
        this.createOrder();
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveProduct(final long amount)
    {
        logger.trace("receiveProduct: received " + amount);
    }

    /**
     * creates an order.
     */
    private void createOrder()
    {
        this.retailer.order(this, this.orderBatchSize.draw());
        try
        {
            this.simulator.scheduleEvent(new SimEvent<SimTimeDouble>(
                    this.simulator.getSimTime().plus(this.intervalTime.draw()), this, this, "createOrder", null));
        }
        catch (Exception exception)
        {
            logger.warn("createOrder", exception);
        }
    }
}
