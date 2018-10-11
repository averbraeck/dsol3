package nl.tudelft.simulation.dsol.tutorial.section25;

import java.net.URL;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A ConsoleRunner.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public final class ConsoleRunner25
{

    /**
     * constructs a new ConsoleRunner.
     */
    private ConsoleRunner25()
    {
        // unreachable code
        super();
    }

    /**
     * executes our model.
     * @param args String[]; the experiment xml-file url
     */
    public static void main(final String[] args)
    {
        try
        {
            // We are ready to start
            // First we resolve the experiment and parse it
            URL experimentalframeURL = URLResource.getResource("/section25.xml");
            ExperimentalFrame.TimeDouble experimentalFrame =
                    ExperimentParser.parseExperimentalFrameTimeDouble(experimentalframeURL);

            experimentalFrame.start();
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
        }
    }
}
