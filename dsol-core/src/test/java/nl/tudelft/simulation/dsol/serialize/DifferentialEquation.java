package nl.tudelft.simulation.dsol.serialize;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version 1.0 Dec 7, 2004
 * @since 1.5
 */
public class DifferentialEquation extends nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation
{

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator) throws RemoteException
    {
        super(simulator);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep) throws RemoteException
    {
        super(simulator, timeStep);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @param numericalMethod the integration method
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep, short numericalMethod)
            throws RemoteException
    {
        super(simulator, timeStep, numericalMethod);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @param numericalIntegrator the integration method
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep,
            NumericalIntegrator numericalIntegrator) throws RemoteException
    {
        super(simulator, timeStep, numericalIntegrator);
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(double arg0, double[] arg1)
    {
        return new double[]{1.0};
    }
}
