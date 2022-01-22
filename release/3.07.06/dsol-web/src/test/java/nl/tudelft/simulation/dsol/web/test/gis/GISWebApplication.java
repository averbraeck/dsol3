package nl.tudelft.simulation.dsol.web.test.gis;

import java.awt.Dimension;

import org.djutils.draw.bounds.Bounds2d;

import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.experiment.SingleReplication;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeAnimator;
import nl.tudelft.simulation.dsol.web.DSOLWebServer;

/**
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class GISWebApplication extends DSOLWebServer
{
    /** the default extent. */
    private static Bounds2d extent;

    static
    {
        double x1 = 115.637;
        double y1 = 39.247;
        double x2 = 117.099;
        double y2 = 40.408;
        extent = new Bounds2d(x1, x2, y1, y2);
    }

    /**
     * @param title String; the tile for the model
     * @param simulator DEVSRealTimeClock.TimeDouble; the simulator
     * @throws Exception on jetty error
     */
    public GISWebApplication(final String title, final DEVSRealTimeAnimator.TimeDouble simulator) throws Exception
    {
        super(title, simulator, extent);
        getAnimationPanel().setSize(new Dimension(800, 600));
    }

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws Exception on error
     */
    public static void main(final String[] args) throws Exception
    {
        DEVSRealTimeAnimator.TimeDouble simulator = new DEVSRealTimeAnimator.TimeDouble("GISWebApplication", 0.01);
        GISModel model = new GISModel(simulator);
        ReplicationInterface.TimeDouble replication = new SingleReplication.TimeDouble("rep1", 0.0, 0.0, 1000000.0);
        simulator.initialize(model, replication);
        new GISWebApplication("GIS Animation model", simulator);
    }

}
