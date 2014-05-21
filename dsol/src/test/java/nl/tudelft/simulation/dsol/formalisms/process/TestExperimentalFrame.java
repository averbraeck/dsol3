/*
 * @(#) TestExperimentalFrame.java Sep 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.formalisms.process;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.InitialEventContext;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * A TestExperimentalFrame <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class TestExperimentalFrame
{
    /**
     * STARTTIME defines the starting time for the experiment in millisec since 1970
     */
    public static final long STARTTIME = 0;

    /** TIMEUNIT refers to the time units of the experiment */
    public static final TimeUnitInterface TIMEUNIT = TimeUnitInterface.UNIT;

    /** RUNLENGTH is the runLength for this experiment */
    public static final double RUNLENGTH = 100;

    /** WARMUP period defines the warmup period for the experiment */
    public static final double WARMUP = 10;

    /** SEED is the seed value for the DEFAULT stream */
    public static final long SEED = 42;

    /** TIMESTEP is the timeStep to be used for the DESS formalism */
    public static final double TIMESTEP = 0.01;

    /**
     * constructs a new TestExperimentalFrame
     */
    private TestExperimentalFrame()
    {
        super();
        // unreachable code
    }

    /**
     * creates an experimental frame
     * @param model the model
     * @param simulator the simulator
     * @return an experimental Frame
     */
    public static ExperimentalFrame createExperimentalFrame(SimulatorInterface simulator, ModelInterface model)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String name = DateFormat.getDateTimeInstance().format(calendar.getTime());
            Context root = new InitialEventContext().createSubcontext(name);
            ExperimentalFrame experimentalFrame = new ExperimentalFrame(root);

            List<Experiment> experiments = new ArrayList<Experiment>();
            for (int i = 0; i < 3; i++)
            {
                Context context = ContextUtil.lookup(root, "/experiment[" + i + "]");
                Experiment experiment = TestExperimentalFrame.createExperiment(context);
                experiment.setSimulator(simulator);
                experiment.setModel(model);
                experiments.add(experiment);
            }
            return experimentalFrame;
        }
        catch (NamingException e)
        {
            Logger.warning(TestExperimentalFrame.class, "createExperimentalFrame", e);
        }
        return null;
    }

    /**
     * creates a new TestExperimentalFrame
     * @param context the context
     * @return ExperimentalFrame
     */
    public static Experiment createExperiment(final Context context)
    {
        Experiment experiment = new Experiment(context);
        experiment.setTreatment(TestExperimentalFrame.createTreatment(context, experiment));
        experiment.setReplications(TestExperimentalFrame.createReplications(context, experiment));
        return experiment;
    }

    /**
     * creates the Treatment for this experiment
     * @param context the context to use
     * @param experiment the parent
     * @return Treatment[] the result
     */
    public static Treatment createTreatment(final Context context, final Experiment experiment)
    {
        Treatment treatment = new Treatment(experiment);
        treatment.setWarmupPeriod(0.0);
        treatment.setRunLength(100.0);
        treatment.setStartTime(System.currentTimeMillis());
        return treatment;
    }

    /**
     * creates the replications for the test experiment
     * @param experiment the simulation experiment
     * @param context the context of the experiment
     * @return a list of replications
     */
    public static List<Replication> createReplications(Context context, final Experiment experiment)
    {
        List<Replication> replications = new ArrayList<Replication>();

        for (int i = 0; i < 3; i++)
        {
            Context subContext = ContextUtil.lookup(context, "/" + i);
            Replication replication = new Replication(subContext, experiment);
            Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();
            streams.put("DEFAULT", new Java2Random(SEED));
            replication.setStreams(streams);
            replications.add(replication);
        }
        return replications;
    }
}