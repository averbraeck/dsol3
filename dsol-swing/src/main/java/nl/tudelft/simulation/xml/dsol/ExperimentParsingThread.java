package nl.tudelft.simulation.xml.dsol;

import java.io.IOException;
import java.net.URL;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * A ExperimentParsingThread.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
            SimLogger.always().warn(exception, "run");
        }
    }
}
