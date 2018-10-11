package nl.tudelft.simulation.dsol.tutorial.section43;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;

/**
 * A Life.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class PredatorPrey implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /** the chart. */
    private XYChart chart;

    /**
     * constructs a new Life.
     */
    public PredatorPrey()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        DESSSimulatorInterface.TimeDouble dessSimulator = (DESSSimulatorInterface.TimeDouble) pSimulator;

        try
        {
            // Prey and Predator definitions
            Population population = new Population(dessSimulator);

            Persistent<Double, Double, SimTimeDouble> preyPopulation = new Persistent<>("prey population",
                    dessSimulator, population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            preyPopulation.initialize();

            Persistent<Double, Double, SimTimeDouble> predatorPopulation = new Persistent<>("predator population",
                    dessSimulator, population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);
            predatorPopulation.initialize();

            this.chart = new XYChart(dessSimulator, "population");
            this.chart.add(preyPopulation);
            this.chart.add(predatorPopulation);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return chart
     */
    public final XYChart getChart()
    {
        return this.chart;
    }

}
