package nl.tudelft.simulation.dsol.serialize;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import org.pmw.tinylog.Logger;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.process.TestExperimentalFrame;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * copyright (c) 2004-2018 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
 * <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version 1.0 Dec 7, 2004
 * @since 1.5
 */
public class Model implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;
    
    /**
     * the simulator to use.
     */
    private SimulatorInterface simulator = null;

    /**
     * constructs a new Model.
     */
    public Model()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface simulator) throws SimRuntimeException
    {
        this.simulator = simulator;
        DEVSSimulatorInterface devsSimulator = (DEVSSimulatorInterface.TimeDouble) simulator;
        devsSimulator.scheduleEventAbs(new SimTimeDouble(10.0), this, this, "pause", null);
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface getSimulator()
    {
        return this.simulator;
    }

    /**
     * pauses the model
     * @throws SimRuntimeException
     * @throws RemoteException
     */
    protected void pause() throws SimRuntimeException, RemoteException
    {
        this.simulator.stop();
        try
        {
            MarshalledObject serializedModel = new MarshalledObject(this);
            Model mySelf = (Model) serializedModel.get();
            mySelf.simulator.start();
        }
        catch (Exception exception)
        {
            Logger.warn(exception, "pause");
        }
    }

    /**
     * executes the Model
     * @param args the command line arguments
     */
    public static void main(final String[] args)
    {
        ExperimentalFrame experimentalFrame =
                TestExperimentalFrame.createExperimentalFrame(new DEVSSimulator(), new Model());
        experimentalFrame.start();
    }
}