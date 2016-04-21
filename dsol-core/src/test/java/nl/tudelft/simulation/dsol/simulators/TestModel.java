package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * The TestModel <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class TestModel implements DSOLModel, EventListenerInterface
{

    /** the startTime. */
    private long startTime = 0L;

    /** the simulator. */
    private SimulatorInterface simulator;

    /**
     * constructs a new TestModel.
     */
    public TestModel()
    {
        super();
    }

    /** {@inheritDoc} */ @Override public  void constructModel(final SimulatorInterface simulator) throws RemoteException
    {
        this.simulator = simulator;
        simulator.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT, false);
        simulator.addListener(this, SimulatorInterface.START_REPLICATION_EVENT, false);
        simulator.addListener(this, SimulatorInterface.START_EVENT, false);
        simulator.addListener(this, SimulatorInterface.STOP_EVENT, false);
        simulator.addListener(this, SimulatorInterface.STEP_EVENT, false);
        simulator.addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, false);
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface getSimulator()
    {
        return this.simulator;
    }

    /** {@inheritDoc} */ @Override public  void notify(final EventInterface event)
    {
        if (event.getType().equals(SimulatorInterface.START_EVENT))
        {
            this.startTime = System.currentTimeMillis();
            System.out.println("started @ " + this.startTime);
        }
        if (event.getType().equals(SimulatorInterface.STOP_EVENT))
        {
            long runLength = System.currentTimeMillis() - this.startTime;
            try
            {
                System.out.println("runlength=" + runLength + " time="
                        + ((SimulatorInterface) event.getSource()).getSimulatorTime());
            }
            catch (RemoteException exception)
            {
                Logger.warning(this, "notify", exception);
            }
        }
    }
}
