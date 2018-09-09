package nl.tudelft.simulation.examples.dsol.animation3d;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * BallModel3D, the ball example in 3D <br>
 * Copyright (c) 2003-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.0 10.05.2004 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class BallModel3D implements DSOLModel<Double, Double, SimTimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * Constructs new BallModel3D.
     */
    public BallModel3D()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        System.out.println("*** Ball 3D Model ***");
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new Ball3D((DESSSimulatorInterface.TimeDouble) pSimulator);
            }
            catch (NamingException | RemoteException exception)
            {
                exception.printStackTrace();
            }
        }
        new World(new DirectedPoint(0, 0, -5.5), this.simulator);
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
