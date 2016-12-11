package nl.tudelft.simulation.dsol.tutorial.section45;

import java.rmi.RemoteException;
import java.util.logging.Level;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A BoatModel <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoatModel implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new BoatModel.
     */
    public BoatModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface.TimeDouble simulator) throws SimRuntimeException, RemoteException
    {
        this.simulator = simulator;
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface) simulator;
        Port port = new Port(devsSimulator);

        // We schedule boat creation
        this.scheduleBoatArrival(0, devsSimulator, port);
        this.scheduleBoatArrival(1, devsSimulator, port);
        this.scheduleBoatArrival(15, devsSimulator, port);
    }

    /**
     * schedules the creation of a boat
     * @param time the time when the boat should arrive
     * @param simulator the simulator on which we schedule
     * @param port the port
     * @throws RemoteException on network failuer
     * @throws SimRuntimeException on simulation exception
     */
    private void scheduleBoatArrival(final double time, final DEVSSimulatorInterface.TimeDouble simulator, final Port port)
            throws RemoteException, SimRuntimeException
    {
        simulator.scheduleEvent(new SimEvent(time, this, Boat.class, "<init>", new Object[]{simulator, port}));
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /**
     * commandline executes the model
     * @param args the arguments to the commandline
     */
    public static void main(final String[] args)
    {
        try
        {
            Logger.setLogLevel(Level.WARNING);
            ExperimentalFrame experimentalFrame =
                    ExperimentParser.parseExperimentalFrame(URLResource.getResource("/section45.xml"));
            experimentalFrame.start();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
