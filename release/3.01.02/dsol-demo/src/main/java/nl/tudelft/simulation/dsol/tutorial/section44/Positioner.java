package nl.tudelft.simulation.dsol.tutorial.section44;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * A Positioner <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Mar 3, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Positioner extends DifferentialEquation
{
    /**
     * constructs a new Positioner.
     * @param simulator the simulator
     * @throws RemoteException Exception
     */
    public Positioner(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
        this.initialize(0.0, new double[]{0.0, 0.0});
    }

    /**
     * sets the value
     * @param value the new value
     */
    public void setValue(final double value)
    {
        try
        {
            super.initialize(this.simulator.getSimulatorTime(), new double[]{value, 0.0});
        }
        catch (RemoteException exception)
        {
            logger.warn("setValue", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        double[] dy = new double[2];
        dy[0] = y[1]; // v(t) = a(t)
        dy[1] = 0.5; // a(t) = constant
        return dy;
    }
}
