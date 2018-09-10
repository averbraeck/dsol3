package nl.tudelft.simulation.dsol.tutorial.appendix;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A EventPerformance <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class EventPerformance implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface<Double, Double, SimTimeDouble> simulator;

    /**
     * constructs a new EventPerformance.
     */
    public EventPerformance()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException
    {
        this.simulator = pSimulator;
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface.TimeDouble) pSimulator;
        devsSimulator.scheduleEventAbs(10000.0, this, this, "complete",
                new Object[]{new Long(System.currentTimeMillis())});
        for (int i = 0; i < 100000; i++)
        {
            devsSimulator.scheduleEventAbs(10000.0, this, this, "stepA", new Object[]{devsSimulator});
        }
    }

    /**
     * @return the simulator
     */
    @Override
    public SimulatorInterface<Double, Double, SimTimeDouble> getSimulator()
    {
        return this.simulator;
    }

    /**
     * stepA.
     * @param pSimulator the simulator
     * @throws SimRuntimeException on dsol error
     * @throws RemoteException on distributed error
     */
    protected void stepA(final DEVSSimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException, RemoteException
    {
        pSimulator.scheduleEventRel(10.0, this, this, "stepB", null);
    }

    /**
     * stepB.
     */
    protected void stepB()
    {
        // finished
    }

    /**
     * completes the run.
     * @param startTime the startTime
     */
    protected void complete(final long startTime)
    {
        System.out.println(System.currentTimeMillis() - startTime);
    }

    /**
     * commandline executes the model.
     * @param args the arguments to the commandline
     */
    public static void main(final String[] args)
    {
        try
        {
            ExperimentalFrame experimentalFrame =
                    ExperimentParser.parseExperimentalFrame(URLResource.getResource("/EventPerformance.xml"));
            experimentalFrame.start();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
