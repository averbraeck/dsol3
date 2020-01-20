package nl.tudelft.simulation.dsol.formalisms.dess;

import java.rmi.RemoteException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegratorType;

/**
 * The Differential equation provides a reference implementation of the differential equation.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class DifferentialEquation<A extends Number & Comparable<A>, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends nl.tudelft.simulation.jstats.ode.DifferentialEquation
        implements DifferentialEquationInterface, EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 20140804L;

    // we initialize the array (30 size set seems enough..)
    static
    {
        for (int i = 0; i < VALUE_CHANGED_EVENT.length; i++)
        {
            VALUE_CHANGED_EVENT[i] = new EventType("VALUE_CHANGED_EVENT[" + i + "]");
        }
    }

    /** simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DESSSimulatorInterface<A, R, T> simulator = null;

    /** the previousX. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double previousX;

    /** the previousY. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double[] previousY = null;

    /**
     * constructs a new DifferentialEquation with a step size equal to the simulator time step, and Runge-Kutta4 as the default
     * integrator.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator) throws RemoteException
    {
        this(simulator, simulator.getTimeStep().doubleValue(), NumericalIntegratorType.RUNGEKUTTA4);
    }

    /**
     * constructs a new DifferentialEquation with a step size equal to the simulator timestep.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator
     * @param numericalIntegrator NumericalIntegrator; the actual integrator to be used.
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator,
            final NumericalIntegratorType numericalIntegrator) throws RemoteException
    {
        this(simulator, simulator.getTimeStep().doubleValue(), numericalIntegrator);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator.
     * @param timeStep double; the timeStep for ODE estimation.
     * @param numericalIntegrator NumericalIntegrator; the actual integrator to be used.
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final double timeStep,
            final NumericalIntegratorType numericalIntegrator) throws RemoteException
    {
        super(timeStep, numericalIntegrator);
        this.simulator = simulator;
        simulator.addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, ReferenceType.STRONG);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void notify(final EventInterface event) throws RemoteException
    {
        if (event.getSourceId().equals(this.simulator.getSourceId())
                && event.getType().equals(SimulatorInterface.TIME_CHANGED_EVENT))
        {
            if (this.simulator.getSimulatorTime().doubleValue() < super.lastX || Double.isNaN(super.lastX))
            {
                return;
            }
            // do not put super here!
            this.previousY = integrateY(this.simulator.getSimulatorTime().doubleValue(), this.previousX, this.previousY);
            for (int i = 0; i < super.lastY.length; i++)
            {
                this.fireTimedEvent(DifferentialEquationInterface.VALUE_CHANGED_EVENT[i], this.previousY[i],
                        this.simulator.getSimulatorTime());
            }
            this.previousX = this.simulator.getSimulatorTime().doubleValue();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final double x, final double[] y)
    {
        super.initialize(x, y);
        this.previousX = x;
        this.previousY = y;
    }
}
