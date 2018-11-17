package nl.tudelft.simulation.examples.dsol.mm1queue;

import java.util.Properties;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.flow.Delay;
import nl.tudelft.simulation.dsol.formalisms.flow.Generator;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimulationTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.statistics.Counter;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.swing.charts.boxAndWhisker.BoxAndWhiskerChart;
import nl.tudelft.simulation.dsol.swing.charts.histogram.Histogram;
import nl.tudelft.simulation.dsol.swing.charts.xy.XYChart;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistDiscrete;
import nl.tudelft.simulation.jstats.distributions.DistDiscreteConstant;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.statistics.Tally;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The M/M/1 example as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4. .
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class MM1Queue extends AbstractDSOLModel.TimeDouble<DEVSSimulator.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructor for the MM1Queue.
     * @param simulator the simulator
     */
    public MM1Queue(final DEVSSimulator.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel() throws SimRuntimeException
    {
        StreamInterface defaultStream = this.simulator.getReplication().getStream("default");

        Properties properties = this.simulator.getReplication().getTreatment().getProperties();

        // The Generator
        Generator.TimeDouble generator = new Generator.TimeDouble(this.simulator, Customer.class, null);
        DistContinuousSimulationTime.TimeDouble intervalTime = new DistContinuousSimulationTime.TimeDouble(
                new DistExponential(defaultStream, new Double(properties.getProperty("generator.intervalTime")).doubleValue()));
        generator.setInterval(intervalTime);

        DistContinuousSimTime.TimeDouble startTime = new DistContinuousSimTime.TimeDouble(
                new DistConstant(defaultStream, new Double(properties.getProperty("generator.startTime")).doubleValue()));
        generator.setStartTime(startTime);

        DistDiscrete batchSize =
                new DistDiscreteConstant(defaultStream, new Integer(properties.getProperty("generator.batchSize")).intValue());
        generator.setBatchSize(batchSize);

        // The queue, the resource and the release
        double capacity = new Double(properties.getProperty("resource.capacity")).doubleValue();
        Resource<Double, Double, SimTimeDouble> resource = new Resource<>(this.simulator, capacity);

        // created a resource
        StationInterface.TimeDouble queue = new Seize(this.simulator, resource);
        StationInterface.TimeDouble release = new Release(this.simulator, resource, capacity);

        // The server
        DistContinuousSimulationTime.TimeDouble serviceTime = new DistContinuousSimulationTime.TimeDouble(
                new DistExponential(defaultStream, new Double(properties.getProperty("resource.serviceTime")).doubleValue()));
        StationInterface.TimeDouble server = new Delay.TimeDouble(this.simulator, serviceTime);

        // The flow
        generator.setDestination(queue);
        queue.setDestination(server);
        server.setDestination(release);

        // Statistics
        try
        {
            new Counter<Double, Double, SimTimeDouble>("counting the generator", this.simulator, generator,
                    Generator.CREATE_EVENT);
            Persistent<Double, Double, SimTimeDouble> persistent =
                    new Persistent<>("persistent on service time", this.simulator, release, Release.SERVICE_TIME_EVENT);

            Histogram histogram = new Histogram(this.simulator, "histogram on service time", new double[] {0, 10}, 30);
            histogram.add("histogram on service time", persistent, Tally.SAMPLE_MEAN_EVENT);

            XYChart xyChart = new XYChart(this.simulator, "XY chart of service time",
                    new double[] {0, this.simulator.getReplication().getTreatment().getRunLength()}, new double[] {-2, 30});
            xyChart.add(persistent);

            BoxAndWhiskerChart bwChart = new BoxAndWhiskerChart(this.simulator, "BoxAndWhisker on serviceTime");
            bwChart.add(persistent);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

}
