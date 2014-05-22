/*
 * @(#) ExperimentParsingThread.java Mar 2, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.xml.dsol;

import java.io.IOException;
import java.net.URL;

import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * A ExperimentParsingThread <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Mar 2, 2004 <br>
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ExperimentParsingThread extends Thread
{
    /** EXPERIMENT_PARSED_EVENT */
    public static final EventType EXPERIMENT_PARSED_EVENT = new EventType("EXPERIMENT_PARSED_EVENT");

    /** the owning listener */
    protected EventListenerInterface source = null;

    /** the experiment */
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

    /**
     * @see java.lang.Runnable#run()
     */
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
            Logger.warning(this, "run", exception);
        }
    }
}