package nl.tudelft.simulation.dsol.tutorial.section43;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;

/**
 * A Life <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 9, 2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class PredatorPrey implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new Life.
     */
    public PredatorPrey()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator) throws RemoteException
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        DESSSimulatorInterface.TimeDouble dessSimulator = (DESSSimulatorInterface.TimeDouble) pSimulator;

        // Prey and Predator definitions
        Population population = new Population(dessSimulator, 0.1);

        Persistent<Double, Double, SimTimeDouble> preyPopulation =
                new Persistent<>("prey population", dessSimulator, population,
                        DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);

        Persistent<Double, Double, SimTimeDouble> predatorPopulation =
                new Persistent<>("predator population", dessSimulator, population,
                        DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);

        XYChart chart = new XYChart(dessSimulator, "population");
        chart.add(preyPopulation);
        chart.add(predatorPopulation);
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
