package nl.tudelft.simulation.examples.dsol.mm1queue;

import java.rmi.RemoteException;
import java.util.Properties;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.flow.Delay;
import nl.tudelft.simulation.dsol.formalisms.flow.Generator;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Counter;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.charts.BoxAndWhiskerChart;
import nl.tudelft.simulation.dsol.statistics.charts.Histogram;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistDiscrete;
import nl.tudelft.simulation.jstats.distributions.DistDiscreteConstant;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.statistics.Tally;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The M/M/1 example as published in Simulation Modeling and Analysis by A.M. Law & W.D. Kelton section 1.4 and 2.4.
 * <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class MM1Queue implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructor for the MM1Queue.
     */
    public MM1Queue()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException, RemoteException
    {
        this.simulator = (DEVSSimulatorInterface.TimeDouble) pSimulator;
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface.TimeDouble) pSimulator;

        StreamInterface defaultStream = devsSimulator.getReplication().getStream("default");

        Properties properties = pSimulator.getReplication().getTreatment().getProperties();

        // The Generator
        Generator.TimeDouble generator = new Generator.TimeDouble(devsSimulator, Customer.class, null);
        DistContinuousTime.TimeDouble intervalTime =
                new DistContinuousTime.TimeDouble(new DistExponential(defaultStream,
                        new Double(properties.getProperty("generator.intervalTime")).doubleValue()));
        generator.setInterval(intervalTime);

        DistContinuousSimTime.TimeDouble startTime =
                new DistContinuousSimTime.TimeDouble(new DistConstant(defaultStream,
                        new Double(properties.getProperty("generator.startTime")).doubleValue()));
        generator.setStartTime(startTime);

        DistDiscrete batchSize = new DistDiscreteConstant(defaultStream,
                new Integer(properties.getProperty("generator.batchSize")).intValue());
        generator.setBatchSize(batchSize);

        // The queue, the resource and the release
        double capacity = new Double(properties.getProperty("resource.capacity")).doubleValue();
        Resource<SimTimeDouble> resource = new Resource<SimTimeDouble>(devsSimulator, capacity);

        // created a resource
        StationInterface queue = new Seize(devsSimulator, resource);
        StationInterface release = new Release(devsSimulator, resource, capacity);

        // The server
        DistContinuousTime.TimeDouble serviceTime = new DistContinuousTime.TimeDouble(new DistExponential(defaultStream,
                new Double(properties.getProperty("resource.serviceTime")).doubleValue()));
        StationInterface server = new Delay.TimeDouble(devsSimulator, serviceTime);

        // The flow
        generator.setDestination(queue);
        queue.setDestination(server);
        server.setDestination(release);

        // Statistics
        new Counter<Double, Double, SimTimeDouble>("counting the generator", pSimulator, generator,
                Generator.CREATE_EVENT);
        Persistent<Double, Double, SimTimeDouble> persistent =
                new Persistent<>("persistent on service time", pSimulator, release, Release.SERVICE_TIME_EVENT);

        Histogram histogram = new Histogram(pSimulator, "histogram on service time", new double[]{0, 10}, 30);
        histogram.add("histogram on service time", persistent, Tally.SAMPLE_MEAN_EVENT);

        XYChart xyChart = new XYChart(pSimulator, "XY chart of service time",
                new double[]{0, pSimulator.getReplication().getTreatment().getRunLength()}, new double[]{-2, 30});
        xyChart.add(persistent);

        BoxAndWhiskerChart bwChart = new BoxAndWhiskerChart(pSimulator, "BoxAndWhisker on serviceTime");
        bwChart.add(persistent);
    }

    /** {@inheritDoc} */
    @Override
    public final SimulatorInterface<Double, Double, SimTimeDouble> getSimulator() throws RemoteException
    {
        return this.simulator;
    }

}
