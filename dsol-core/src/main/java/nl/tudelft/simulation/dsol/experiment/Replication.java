/*
 * @(#)Replication.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The replication of a runcontrol.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Replication<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> implements Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** streams used in the replication */
    private Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();

    /** description the description of the replication */
    private String description = "rep_no_description";

    /** the experiment to which this replication belongs */
    private Experiment<A, R, T> experiment = null;

    /** the contextRoot of this replication */
    private Context context = null;

    /**
     * constructs a new Replication
     * @param context the name under which this replication can be found in the nameSpace
     * @param experiment the experiment to which this replication belongs
     */
    public Replication(final Context context, final Experiment<A, R, T> experiment)
    {
        super();
        this.experiment = experiment;
        this.context = context;
    }

    /**
     * gets the description of this replication
     * @return String the description of this replication
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * returns the streams
     * @return Map the streams of this replication
     */
    public Map<String, StreamInterface> getStreams()
    {
        return this.streams;
    }

    /**
     * returns a specific stream
     * @param name the name of the stream
     * @return StreamInterface the stream
     */
    public StreamInterface getStream(final String name)
    {
        return this.streams.get(name);
    }

    /**
     * resets the RunControl
     */
    public synchronized void reset()
    {
        for (StreamInterface stream : this.streams.values())
        {
            stream.reset();
        }
    }

    /**
     * Sets the description of this replication
     * @param description the description of this replication
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * sets the stream for this replication
     * @param streams the map of stream,name tuples
     */
    public void setStreams(final Map<String, StreamInterface> streams)
    {
        this.streams = streams;
    }

    /**
     * @return Returns the experiment.
     */
    public Experiment<A, R, T> getExperiment()
    {
        return this.experiment;
    }

    /**
     * @return Returns the treatment. This is a convenience method to avoid the getExperiment().getTreatment() many
     *         times.
     */
    public Treatment<A, R, T> getTreatment()
    {
        return this.experiment.getTreatment();
    }

    /**
     * @return Returns the context.
     */
    public Context getContext()
    {
        return this.context;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String result = super.toString() + " ; " + this.getDescription() + " ; streams=[";
        for (StreamInterface stream : this.streams.values())
        {
            result = result + stream.toString() + " ; ";
        }
        result = result.substring(0, result.length() - 2) + "]";
        return result;
    }
}