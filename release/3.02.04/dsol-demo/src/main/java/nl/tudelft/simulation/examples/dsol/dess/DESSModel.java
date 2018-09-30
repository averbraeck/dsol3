package nl.tudelft.simulation.examples.dsol.dess;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * @version Aug 15, 2014 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DESSModel implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /** the distance chart. */
    private XYChart distanceChart;

    /** the distance persistent. */
    private Persistent<Double, Double, SimTimeDouble> distancePersistent;

    /**
     * constructs a new DESSModel.
     */
    public DESSModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        try
        {
            Distance distance = new Distance((DESSSimulatorInterface.TimeDouble) pSimulator);

            this.distancePersistent = new Persistent<>("persistent on distance", pSimulator, distance,
                    DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
        }

        this.distancePersistent.initialize();
        this.distanceChart = new XYChart(pSimulator, "xyplot of distance");
        this.distanceChart.add(this.distancePersistent);

    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /**
     * @return chart
     */
    public final XYChart getDistanceChart()
    {
        return this.distanceChart;
    }

    /**
     * @return distancePersistent
     */
    public final Persistent<Double, Double, SimTimeDouble> getDistancePersistent()
    {
        return this.distancePersistent;
    }

}
