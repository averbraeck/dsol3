package nl.tudelft.simulation.dsol.tutorial.section43;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;

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
public class Life implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new Life.
     */
    public Life()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        this.simulator = simulator;
        DESSSimulatorInterface.TimeDouble dessSimulator = (DESSSimulatorInterface) simulator;

        // Prey and Predator definitions
        Population population = new Population(dessSimulator, 0.1);

        Persistent preyPopulation =
                new Persistent("prey population", dessSimulator, population,
                        DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);

        Persistent predatorPopulation =
                new Persistent("predator population", dessSimulator, population,
                        DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);

        XYChart chart = new XYChart(dessSimulator, "population");
        chart.add(preyPopulation);
        chart.add(predatorPopulation);
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
