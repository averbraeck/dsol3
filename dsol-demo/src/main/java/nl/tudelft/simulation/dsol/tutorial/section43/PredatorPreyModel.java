package nl.tudelft.simulation.dsol.tutorial.section43;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.swing.charts.xy.XYChart;

/**
 * A Life.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class PredatorPreyModel extends AbstractDSOLModel.TimeDouble<DESSSimulator.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the chart. */
    private XYChart chart;

    /**
     * constructs a new Life.
     * @param simulator the continuous simulator
     */
    public PredatorPreyModel(final DESSSimulator.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel() throws SimRuntimeException
    {
        try
        {
            // Prey and Predator definitions
            Population population = new Population(this.simulator);

            SimPersistent<Double, Double, SimTimeDouble> preyPopulation = new SimPersistent<>("prey population", this.simulator,
                    population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            preyPopulation.initialize();

            SimPersistent<Double, Double, SimTimeDouble> predatorPopulation = new SimPersistent<>("predator population",
                    this.simulator, population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);
            predatorPopulation.initialize();

            this.chart = new XYChart(this.simulator, "population");
            this.chart.add(preyPopulation);
            this.chart.add(predatorPopulation);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /**
     * @return chart
     */
    public final XYChart getChart()
    {
        return this.chart;
    }

}
