package nl.tudelft.simulation.dsol.web.test.gis;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeAnimator;
import nl.tudelft.simulation.dsol.web.DSOLWebServer;

/**
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    private static Rectangle2D.Double extent;

    static
    {
        double x = 115.637;
        double y = 39.247;
        double w = 117.099 - x;
        double h = 40.408 - y;
        extent = new Rectangle2D.Double(x, y, w, h);
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
        Replication.TimeDouble<DEVSRealTimeAnimator.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000000.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new GISWebApplication("GIS Animation model", simulator);
    }

}
