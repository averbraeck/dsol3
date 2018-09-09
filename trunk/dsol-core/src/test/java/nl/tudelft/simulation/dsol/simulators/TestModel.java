package nl.tudelft.simulation.dsol.simulators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The TestModel <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class TestModel implements DSOLModel, EventListenerInterface
{

    /** the startTime. */
    private long startTime = 0L;

    /** the simulator. */
    private SimulatorInterface simulator;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(TestModel.class);

    /**
     * constructs a new TestModel.
     */
    public TestModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface simulator)
    {
        this.simulator = simulator;
        try
        {
            simulator.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT, false);
            simulator.addListener(this, SimulatorInterface.START_REPLICATION_EVENT, false);
            simulator.addListener(this, SimulatorInterface.START_EVENT, false);
            simulator.addListener(this, SimulatorInterface.STOP_EVENT, false);
            simulator.addListener(this, SimulatorInterface.STEP_EVENT, false);
            simulator.addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, false);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface getSimulator()
    {
        return this.simulator;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event.getType().equals(SimulatorInterface.START_EVENT))
        {
            this.startTime = System.currentTimeMillis();
            System.out.println("started @ " + this.startTime);
        }
        if (event.getType().equals(SimulatorInterface.STOP_EVENT))
        {
            long runLength = System.currentTimeMillis() - this.startTime;
            System.out.println(
                    "runlength=" + runLength + " time=" + ((SimulatorInterface) event.getSource()).getSimulatorTime());
        }
    }
}
