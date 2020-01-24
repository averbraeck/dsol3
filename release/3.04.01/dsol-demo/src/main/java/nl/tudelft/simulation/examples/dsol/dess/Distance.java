package nl.tudelft.simulation.examples.dsol.dess;

import java.io.Serializable;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/" target="_blank"> www.simulation.tudelft.nl</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Distance extends DifferentialEquation<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the speed. */
    private Speed speed = null;

    /**
     * constructs a new Distance.
     * @param simulator DESSSimulatorInterface.TimeDouble; the simulator
     * @throws RemoteException on network error
     */
    public Distance(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
        this.speed = new Speed(simulator);
        this.speed.initialize(10, new double[] {0});
        this.initialize(10, new double[] {0});
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        return this.speed.y(x);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "Distance";
    }
}
