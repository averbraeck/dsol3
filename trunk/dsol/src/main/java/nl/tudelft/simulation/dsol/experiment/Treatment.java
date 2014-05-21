/*
 * @(#)Treatment.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Properties;

/**
 * The treatment is comprises the specification of input data, the runControl and the specification of output data.
 * (Sol:1982, Oeren&Zeigler:1979) <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class Treatment implements Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the TERMINATING replication mode */
    public static final short REPLICATION_MODE_TERMINATING = 0;

    /** the TERMINATING replication mode */
    public static final short REPLICATION_MODE_STEADY_STATE = 1;

    /** the replication mode */
    private short replicationMode = Treatment.REPLICATION_MODE_TERMINATING;

    /** the experiment to which this treatment belongs */
    private Experiment experiment = null;

    /** warmupPeriod is the warmup period */
    private double warmupPeriod = 0.0;

    /** runLength reflects the runLength of the treatment */
    private double runLength = Double.NaN;

    /** startTime of the treatment */
    private long startTime = 0L;

    /** timeUnit reflects the timeUnit */
    private TimeUnitInterface timeUnit = TimeUnitInterface.UNIT;

    /** the properties of this treatment */
    private Properties properties = new Properties();

    /**
     * constructs a Treatment
     * @param experiment reflects the experiment
     */
    public Treatment(final Experiment experiment)
    {
        this(experiment, Treatment.REPLICATION_MODE_TERMINATING);
    }

    /**
     * constructs a Treatment
     * @param experiment reflects the experiment
     * @param replicationMode the replication mode of this treatment
     */
    public Treatment(final Experiment experiment, final short replicationMode)
    {
        super();
        this.experiment = experiment;
        this.replicationMode = replicationMode;
    }

    /**
     * Returns the experiment
     * @return the experiment
     */
    public Experiment getExperiment()
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
     * returns the startTime
     * @return long the startTime
     */
    public long getStartTime()
    {
        return this.startTime;
    }

    /**
     * returns the timeUnit
     * @return timeUnit
     */
    public TimeUnitInterface getTimeUnit()
    {
        return this.timeUnit;
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
     * sets the startTime of the treatment
     * @param startTime reflects the startTime
     */
    public void setStartTime(final long startTime)
    {
        this.startTime = startTime;
    }

    /**
     * sets the timeUnit of the treatment
     * @param timeUnit is the timeunit
     */
    public void setTimeUnit(final TimeUnitInterface timeUnit)
    {
        this.timeUnit = timeUnit;
    }

    /**
     * sets the experiment of this treatment
     * @param experiment the experiment
     */
    public void setExperiment(final Experiment experiment)
    {
        this.experiment = experiment;
    }

    /**
     * Returns the replication mode of this treatment.
     * @return the replication mode of this treatment.
     */
    public short getReplicationMode()
    {
        return this.replicationMode;
    }

    /**
     * sets the replication mode of this treatment
     * @param replicationMode the replication mode
     */
    public void setReplicationMode(final short replicationMode)
    {
        this.replicationMode = replicationMode;
    }

    /**
     * @return Returns the runLength.
     */
    public double getRunLength()
    {
        return this.runLength;
    }

    /**
     * @param runLength The runLength to set.
     */
    public void setRunLength(final double runLength)
    {
        this.runLength = runLength;
    }

    /**
     * @return Returns the warmupPeriod.
     */
    public double getWarmupPeriod()
    {
        return this.warmupPeriod;
    }

    /**
     * @param warmupPeriod The warmupPeriod to set.
     */
    public void setWarmupPeriod(final double warmupPeriod)
    {
        this.warmupPeriod = warmupPeriod;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getStartTime());

        String result =
                "[" + super.toString() + " ; " + calendar.getTime().toString() + " ; " + this.getTimeUnit()
                        + " ; warmup=" + this.warmupPeriod + " ; runLength=" + this.runLength + "]";
        return result;
    }
}