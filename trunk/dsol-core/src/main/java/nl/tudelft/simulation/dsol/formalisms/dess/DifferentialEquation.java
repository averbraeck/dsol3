package nl.tudelft.simulation.dsol.formalisms.dess;

import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The Differential equation provides a reference implementation of the differential equation.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:46 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class DifferentialEquation<A extends Number & Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends nl.tudelft.simulation.jstats.ode.DifferentialEquation
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
    protected DESSSimulatorInterface<A, R, T> simulator = null;

    /** the previousX */
    protected double previousX;

    /** the previousY */
    protected double[] previousY = null;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(DifferentialEquation.class);

    /**
     * constructs a new stateful DifferentialEquation with Euleras numerical integration method.
     * @param simulator the simulator
     * @throws RemoteException on network exception
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator) throws RemoteException
    {
        this(simulator, simulator.getTimeStep(), NumericalIntegrator.DEFAULT_INTEGRATOR);
    }

    /**
     * constructs a new stateful DifferentialEquation with Euleras numerical integration method.
     * @param simulator the simulator
     * @param timeStep the timeStep for ODE estimation
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final R timeStep)
    {
        this(simulator, timeStep, NumericalIntegrator.DEFAULT_INTEGRATOR);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator.
     * @param timeStep the timeStep for ODE estimation.
     * @param numericalMethod the numerical method to be used.
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final R timeStep,
            final short numericalMethod)
    {
        super(timeStep.doubleValue(), numericalMethod);
        this.simulator = simulator;
        try
        {
            simulator.addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, false);
        }
        catch (RemoteException exception)
        {
            logger.warn("DifferentialEquation", exception);
        }
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator.
     * @param timeStep the timeStep for ODE estimation.
     * @param numericalIntegrator the actual integrator to be used.
     */
    public DifferentialEquation(final DESSSimulatorInterface<A, R, T> simulator, final double timeStep,
            final NumericalIntegrator numericalIntegrator)
    {
        super(timeStep, numericalIntegrator);
        this.simulator = simulator;
        try
        {
            simulator.addListener(this, SimulatorInterface.TIME_CHANGED_EVENT, false);
        }
        catch (RemoteException exception)
        {
            logger.warn("DifferentialEquation", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void notify(final EventInterface event) throws RemoteException
    {
        if (event.getSource() instanceof DESSSimulatorInterface
                && event.getType().equals(SimulatorInterface.TIME_CHANGED_EVENT))
        {
            if (this.simulator.getSimulatorTime().get().doubleValue() < super.x0 || Double.isNaN(super.x0))
            {
                return;
            }
            // do not put super here!
            this.previousY =
                    integrateY(this.simulator.getSimulatorTime().get().doubleValue(), this.previousX, this.previousY);
            for (int i = 0; i < super.y0.length; i++)
            {
                this.fireTimedEvent(DifferentialEquationInterface.VALUE_CHANGED_EVENT[i], this.previousY[i],
                        this.simulator.getSimulatorTime().get());
            }
            this.previousX = this.simulator.getSimulatorTime().get().doubleValue();
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
