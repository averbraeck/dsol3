package nl.tudelft.simulation.dsol.web.test.ball;

import org.djutils.draw.bounds.Bounds2d;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeAnimator;
import nl.tudelft.simulation.dsol.web.DSOLWebServer;

/**
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class BallWebApplication extends DSOLWebServer
{
    /**
     * @param title String; the tile for the model
     * @param simulator DEVSRealTimeClock.TimeDouble; the simulator
     * @throws Exception on jetty error
     */
    public BallWebApplication(final String title, final DEVSRealTimeAnimator.TimeDouble simulator) throws Exception
    {
        super(title, simulator, new Bounds2d(-100, 100, -100, 100));
    }

    
    /**
     * @param args String[]; arguments, expected to be empty
     * @throws Exception on error
     */
    public static void main(final String[] args) throws Exception
    {
        DEVSRealTimeAnimator.TimeDouble simulator = new DEVSRealTimeAnimator.TimeDouble("BallWebApplication", 0.01);
        BallModel model = new BallModel(simulator);
        Replication.TimeDouble<DEVSRealTimeAnimator.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000000.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new BallWebApplication("Ball Animation model", simulator);
    }

}
