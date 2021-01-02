package nl.tudelft.simulation.dsol.hla;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.jstats.streams.Java2Random;

/**
 * A TestExperiment <br>
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>,
 *         <a href="http://www.tbm.tudelft.nl/webstaf/alexandv/index.htm">Alexander Verbraeck </a>
 */
public final class TestExperiment extends Experiment
{
    /**
     * STARTTIME defines the starting time for the experiment in millisec since 1970
     */
    public static final long STARTTIME = 0;

    /** TIMEUNIT refers to the time units of the experiment. */
    public static final TimeUnitInterface TIMEUNIT = TimeUnitInterface.UNIT;

    /** RUNLENGTH is the runLength for this experiment. */
    public static final double RUNLENGTH = 10;

    /** WARMUP period defines the warmup period for the experiment. */
    public static final double WARMUP = 10;

    /** SEED is the seed value for the DEFAULT stream. */
    public static final long SEED = 42;

    /** TIMESTEP is the timeStep to be used for the DESS formalism. */
    public static final double TIMESTEP = 0.01;

    /**
     * constructs a new TestExperiment.
     */
    private TestExperiment()
    {
        super();
        // unreachable code
    }

    /**
     * creates a new TestExperiment
     * @return Experiment
     */
    public static Experiment createExperiment()
    {
        Experiment experiment = new Experiment();
        experiment.setTreatments(TestExperiment.createTreatments(experiment));
        experiment.setProperty("TIMESTEP", new Double(TIMESTEP).toString());
        return experiment;
    }

    /**
     * creates the Treatments for this experiment
     * @param experiment the parent
     * @return Treatment[] the result
     */
    public static Treatment[] createTreatments(final Experiment experiment)
    {
        Treatment[] result = new Treatment[1];
        result[0] = new Treatment(experiment, 0);

        result[0].setStartTime(STARTTIME);
        result[0].setTimeUnit(TIMEUNIT);
        result[0].setRunControl(TestExperiment.createRunControl(result[0]));
        return result;
    }

    /**
     * creates a RunControl for the test Excperiment
     * @param treatment the treatment
     * @return RunControl the runControl of the TestExperiment
     */
    public static RunControl createRunControl(final Treatment treatment)
    {
        RunControl result = new RunControl(treatment);
        result.setRunLength(RUNLENGTH);
        result.setWarmupPeriod(WARMUP);
        result.setReplications(TestExperiment.createReplications(result));
        return result;
    }

    /**
     * creates the replications for the test experiment
     * @param runControl the parent
     * @return Replication[] result
     */
    public static Replication[] createReplications(final RunControl runControl)
    {
        Replication[] result = new Replication[1];
        result[0] = new Replication(runControl, 0);

        Map streams = new HashMap();
        streams.put("DEFAULT", new Java2Random(SEED));
        result[0].setStreams(streams);
        return result;
    }
}
