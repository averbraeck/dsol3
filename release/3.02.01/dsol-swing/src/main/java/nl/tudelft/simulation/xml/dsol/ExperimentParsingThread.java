package nl.tudelft.simulation.xml.dsol;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * A ExperimentParsingThread <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Mar 2, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class ExperimentParsingThread extends Thread
{
    /** EXPERIMENT_PARSED_EVENT */
    public static final EventType EXPERIMENT_PARSED_EVENT = new EventType("EXPERIMENT_PARSED_EVENT");

    /** the owning listener. */
    protected EventListenerInterface source = null;

    /** the experiment. */
    protected URL experiment = null;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(ExperimentParsingThread.class);

    /**
     * constructs a new ExperimentParsingThread
     * @param source the source of this thread
     * @param experiment the experiment to parse
     */
    public ExperimentParsingThread(final EventListenerInterface source, final URL experiment)
    {
        super("ExperimentParsingThread");
        this.source = source;
        this.experiment = experiment;
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        try
        {
            ExperimentalFrame experimentalFrame = ExperimentParser.parseExperimentalFrame(this.experiment);
            this.source.notify(new Event(EXPERIMENT_PARSED_EVENT, this, experimentalFrame));
        }
        catch (IOException exception)
        {
            logger.warn("run", exception);
        }
    }
}
