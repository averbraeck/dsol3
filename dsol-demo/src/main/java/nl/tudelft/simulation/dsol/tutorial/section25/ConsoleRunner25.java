package nl.tudelft.simulation.dsol.tutorial.section25;

import java.net.URL;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A ConsoleRunner <br>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Dec 1, 2003 <br>
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
     * @param args the experiment xml-file url
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
            exception.printStackTrace();
        }
    }
}
