package nl.tudelft.simulation.examples.dsol.dess;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.swing.charts.xy.XYChart;

/**
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/" target="_blank"> www.simulation.tudelft.nl</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DESSModel extends AbstractDSOLModel.TimeDouble<DESSSimulatorInterface.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the distance chart. */
    private XYChart distanceChart;

    /** the distance persistent. */
    private SimPersistent<Double, Double, SimTimeDouble> distancePersistent;

    /**
     * constructs a new DESSModel.
     * @param simulator DESSSimulatorInterface.TimeDouble; the continuous simulator
     */
    public DESSModel(final DESSSimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel()
    {
        try
        {
            Distance distance = new Distance(this.simulator);
            this.distancePersistent =
                    new SimPersistent<>("persistent on distance", this.simulator, distance, distance.VALUE_CHANGED_EVENT[0]);
        }
        catch (Exception exception)
        {
            getSimulator().getLogger().always().error(exception);
        }

        this.distancePersistent.initialize();
        this.distanceChart = new XYChart(this.simulator, "xyplot of distance");
        this.distanceChart.add(this.distancePersistent);

    }

    /**
     * @return chart
     */
    public XYChart getDistanceChart()
    {
        return this.distanceChart;
    }

    /**
     * @return distancePersistent
     */
    public SimPersistent<Double, Double, SimTimeDouble> getDistancePersistent()
    {
        return this.distancePersistent;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "DESSModel";
    }

}
