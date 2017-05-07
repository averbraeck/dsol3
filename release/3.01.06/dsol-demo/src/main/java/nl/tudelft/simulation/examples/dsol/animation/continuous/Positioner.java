package nl.tudelft.simulation.examples.dsol.animation.continuous;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * A Positioner <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.0 Mar 3, 2004 <br>
 * @author <a href="http://www.simulation.tudelft.nl/people/jacobs.html">Peter Jacobs </a>
 */
public class Positioner extends DifferentialEquation<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Positioner.
     * @param simulator the simulator
     * @throws RemoteException on remote error
     */
    public Positioner(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
        this.initialize(0.0, new double[]{0.0, 0.0});
    }

    /**
     * sets the value.
     * @param value the new value
     */
    public void setValue(final double value)
    {
        try
        {
            super.initialize(this.simulator.getSimulatorTime().get(), new double[]{0.0, value});
        }
        catch (RemoteException exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        double[] dy = new double[2];
        dy[0] = 0.5; // a(t) = constant
        dy[1] = y[0]; // v(t) = a(t)
        return dy;
    }
}
