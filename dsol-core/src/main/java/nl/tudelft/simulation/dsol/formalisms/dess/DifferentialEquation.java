package nl.tudelft.simulation.dsol.formalisms.dess;

import java.rmi.RemoteException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.TimedEventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegratorType;

/**
 * The Differential equation provides a reference implementation of the differential equation.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
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

    /** VALUE_CHANGED_EVENT is fired on value changes. The array is initialized in the ODE's constructor. */
    @SuppressWarnings({"checkstyle:visibilitymodifier", "checkstyle:membername"})
    public TimedEventType[] VALUE_CHANGED_EVENT;

    /** FUNCTION_CHANGED_EVENT is fired on function changes. */
    @SuppressWarnings({"checkstyle:visibilitymodifier", "checkstyle:membername"})
    public TimedEventType FUNCTION_CHANGED_EVENT = new TimedEventType("FUNCTION_CHANGED_EVENT", MetaData.NO_META_DATA);

    /** simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DESSSimulatorInterface<A, R, T> simulator = null;

    /** the number of variables in the equation. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public int numberOfVariables;

    /** the previousX. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double previousX;

    /** the previousY. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double[] previousY = null;

    /**
     * Construct a new DifferentialEquation with a step size equal to the simulator time step, and Runge-Kutta4 as the default
     * integrator. Indicate the number of variables that the differential qquation will use.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator
     * @param numberOfVariables int; the number of variables in the equation
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final int numberOfVariables)
            throws RemoteException
    {
        this(simulator, simulator.getTimeStep().doubleValue(), NumericalIntegratorType.RUNGEKUTTA4, numberOfVariables);
    }

    /**
     * constructs a new DifferentialEquation with a step size equal to the simulator timestep.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator
     * @param numericalIntegrator NumericalIntegrator; the actual integrator to be used.
     * @param numberOfVariables int; the number of variables in the equation
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator,
            final NumericalIntegratorType numericalIntegrator, final int numberOfVariables) throws RemoteException
    {
        this(simulator, simulator.getTimeStep().doubleValue(), numericalIntegrator, numberOfVariables);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator DESSSimulatorInterface&lt;A,R,T&gt;; the simulator.
     * @param timeStep double; the timeStep for ODE estimation.
     * @param numericalIntegrator NumericalIntegrator; the actual integrator to be used.
     * @param numberOfVariables int; the number of variables in the equation
     * @throws RemoteException on remote network exception for the listener
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final double timeStep,
            final NumericalIntegratorType numericalIntegrator, final int numberOfVariables) throws RemoteException
    {
        super(timeStep, numericalIntegrator);
        this.simulator = simulator;
        this.numberOfVariables = numberOfVariables;
        this.VALUE_CHANGED_EVENT = new TimedEventType[this.numberOfVariables];
        for (int i = 0; i < this.numberOfVariables; i++)
        {
            this.VALUE_CHANGED_EVENT[i] =
                    new TimedEventType(new MetaData("VALUE_CHANGED_EVENT[" + i + "]", "value changed for variable " + i,
                            new ObjectDescriptor("value_" + i, "value for variable " + i, Double.class)));
        }
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
                this.fireUnverifiedTimedEvent(this.VALUE_CHANGED_EVENT[i], this.previousY[i],
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
