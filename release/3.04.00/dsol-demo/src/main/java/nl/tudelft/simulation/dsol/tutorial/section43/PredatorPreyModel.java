package nl.tudelft.simulation.dsol.tutorial.section43;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.swing.charts.xy.XYChart;

/**
 * A Predator-Prey model with a graph.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param simulator DESSSimulator.TimeDouble; the continuous simulator
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

            // This would work:
            // SimPersistent<Double, Double, SimTimeDouble> preyPopulation = new SimPersistent<>("prey population",
            // this.simulator, population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            // preyPopulation.initialize();
            // SimPersistent<Double, Double, SimTimeDouble> predatorPopulation = new SimPersistent<>("predator population",
            // this.simulator, population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);
            // predatorPopulation.initialize();
            // this.chart = new XYChart(this.simulator, "population");
            // this.chart.add(preyPopulation);
            // this.chart.add(predatorPopulation);

            // This would work as well:
            // this.chart = new XYChart(this.simulator, "population");
            // this.chart.add("prey population", population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            // this.chart.add("predator population", population, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);

            // this works 8 times faster because the graph gets fewer data points:
            this.chart = new XYChart(this.simulator, "population");
            SkipEventProducer preySEP = new SkipEventProducer(8);
            population.addListener(preySEP, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            this.chart.add("prey population", preySEP, DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
            SkipEventProducer predSEP = new SkipEventProducer(8);
            population.addListener(predSEP, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);
            this.chart.add("predator population", predSEP, DifferentialEquationInterface.VALUE_CHANGED_EVENT[1]);
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

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "PredatorPreyModel";
    }

    /** small event producer that fires events for the graph with a lower resolution. */
    static class SkipEventProducer extends EventProducer implements EventListenerInterface
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** */
        private int count = 0;

        /** */
        private final int skip;

        /**
         * @param skip the "mod" factor used. When skip = 10, one in 10 events will be re-fired.
         */
        SkipEventProducer(final int skip)
        {
            super();
            this.skip = skip;
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event) throws RemoteException
        {
            if (this.count % this.skip == 0)
            {
                fireEvent(event);
                this.count = 0;
            }
            this.count++;
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "SkipEventProducer";
        }
    }

}
