package nl.tudelft.simulation.dsol.tutorial.section42;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistUniform;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * A Warehouse <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 8, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Warehouse implements SellerInterface
{
    /** simulator. the simulator to schedule on. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the delivery or leadTime. */
    private DistContinuous leadTime = null;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(Warehouse.class);

    /**
     * constructs a new Warehouse.
     * @param simulator the simulator to schedule on
     */
    public Warehouse(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super();
        this.simulator = simulator;

        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.leadTime = new DistUniform(stream, 0.5, 1.0);
    }

    /** {@inheritDoc} */
    @Override
    public final void order(final BuyerInterface buyer, final long amount)
    {
        try
        {
            this.simulator
                    .scheduleEvent(new SimEvent.TimeDouble(this.simulator.getSimulatorTime() + this.leadTime.draw(),
                            this, buyer, "receiveProduct", new Long[]{new Long(amount)}));
        }
        catch (Exception exception)
        {
            logger.warn("order", exception);
        }
    }
}
