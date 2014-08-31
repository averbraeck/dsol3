package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Properties;

import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * The treatment is comprises the specification of input data, the runControl and the specification of output data.
 * (Sol:1982, Oeren&Zeigler:1979) <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Treatment<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> implements
        Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the replication mode. */
    private final ReplicationMode replicationMode;

    /** the experiment to which this treatment belongs. */
    private final Experiment<A, R, T> experiment;

    /** warmupPeriod is the warmup period. */
    private final R warmupPeriod;

    /** runLength reflects the runLength of the treatment. */
    private final R runLength;

    /** the start time of the simulation. */
    private final T startTime;

    /** the end time of the simulation. */
    private final T endTime;

    /** the warmup time of the simulation (included in the total run length)*/
    private final T warmupTime;

    /** the properties of this treatment. */
    private Properties properties = new Properties();

    /** the id. */
    private final String id;

    /**
     * constructs a Treatment.
     * @param experiment reflects the experiment
     * @param id an id to recognize the treatment
     * @param startTime the absolute start time of a run (can be zero)
     * @param warmupPeriod the relative warmup time of a run (can be zero), <u>included</u> in the runLength
     * @param runLength the run length of a run (relative to the start time)
     * @param replicationMode the replication mode of this treatment
     */
    public Treatment(final Experiment<A, R, T> experiment, final String id, final T startTime, final R warmupPeriod,
            final R runLength, final ReplicationMode replicationMode)
    {
        super();
        this.experiment = experiment;
        this.id = id;
        this.startTime = startTime.copy();
        this.warmupPeriod = warmupPeriod;
        this.runLength = runLength;
        this.endTime = startTime.copy();
        this.endTime.add(runLength);
        this.warmupTime = startTime.copy();
        this.warmupTime.add(warmupPeriod);
        this.replicationMode = replicationMode;
    }

    /**
     * constructs a Treatment.
     * @param experiment reflects the experiment
     * @param id an id to recognize the treatment
     * @param startTime the absolute start time of a run (can be zero)
     * @param warmupPeriod the relative warmup time of a run (can be zero), <u>included</u> in the runLength
     * @param runLength the run length of a run (relative to the start time)
     */
    public Treatment(final Experiment<A, R, T> experiment, final String id, final T startTime, final R warmupPeriod,
            final R runLength)
    {
        this(experiment, id, startTime, warmupPeriod, runLength, ReplicationMode.TERMINATING);
    }

    /**
     * Returns the experiment
     * @return the experiment
     */
    public Experiment<A, R, T> getExperiment()
    {
        return this.experiment;
    }

    /**
     * returns the properties for this treatment
     * @return Properties
     */
    public Properties getProperties()
    {
        return this.properties;
    }

    /**
     * sets the properties
     * @param properties the properties
     */
    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    /**
     * @return the replication mode of this treatment.
     */
    public ReplicationMode getReplicationMode()
    {
        return this.replicationMode;
    }

    /**
     * @return Returns the runLength.
     */
    public R getRunLength()
    {
        return this.runLength;
    }

    /**
     * @return Returns the warmupPeriod.
     */
    public R getWarmupPeriod()
    {
        return this.warmupPeriod;
    }

    /**
     * @return the absolute end time of the simulation that can be compared with the simulator time.
     */
    public T getEndTime()
    {
        return this.endTime;
    }

    /**
     * @return startTime
     */
    public T getStartTime()
    {
        return this.startTime;
    }

    /**
     * @return warmupTime
     */
    public T getWarmupTime()
    {
        return this.warmupTime;
    }

    /**
     * @return id
     */
    public String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result = "[Treatment; warmup=" + this.warmupPeriod + " ; runLength=" + this.runLength + "]";
        return result;
    }
}
