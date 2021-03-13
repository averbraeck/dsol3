package nl.tudelft.simulation.dsol.simulators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * A TestExperimentalFrame.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class ExperimentUtilDouble
{
    /**
     * STARTTIME defines the starting time for the experiment in millisec since 1970.
     */
    public static final long STARTTIME = 0;

    /** RUNLENGTH is the runLength for this experiment. */
    public static final double RUNLENGTH = 100;

    /** WARMUP period defines the warmup period for the experiment. */
    public static final double WARMUP = 10;

    /** SEED is the seed value for the DEFAULT stream. */
    public static final long SEED = 42;

    /** TIMESTEP is the timeStep to be used for the DESS formalism. */
    public static final double TIMESTEP = 0.01;

    /**
     * constructs a new TestExperimentalFrame.
     */
    private ExperimentUtilDouble()
    {
        // unreachable code
    }

    /**
     * creates an experimental frame.
     * @param model the model
     * @param simulator the simulator
     * @return an experimental Frame
     */
    public static ExperimentalFrame createExperimentalFrame(final SimulatorInterface.TimeDouble simulator,
            final DSOLModel.TimeDouble<SimulatorInterface.TimeDouble> model)
    {
        try
        {
            ExperimentalFrame experimentalFrame = new ExperimentalFrame("test");
            List<Experiment<?, ?, ?, ?>> experiments = new ArrayList<Experiment<?, ?, ?, ?>>();
            for (int i = 0; i < 3; i++)
            {
                Experiment.TimeDouble<SimulatorInterface.TimeDouble> experiment = ExperimentUtilDouble.createExperiment();
                experiment.setSimulator(simulator);
                experiment.setModel(model);
                experiment.setDescription("Exp #" + i);
                experiment.setAnalyst("Analyst: none");
                experiments.add(experiment);
            }
            experimentalFrame.setExperiments(experiments);
            return experimentalFrame;
        }
        catch (NamingException e)
        {
            simulator.getLogger().always().warn(e, "createExperimentalFrame");
        }
        return null;
    }

    /**
     * creates a new TestExperimentalFrame.
     * @return ExperimentalFrame
     * @throws NamingException on error
     */
    public static Experiment.TimeDouble<SimulatorInterface.TimeDouble> createExperiment() throws NamingException
    {
        Experiment.TimeDouble<SimulatorInterface.TimeDouble> experiment = new Experiment.TimeDouble<>();
        experiment.setTreatment(ExperimentUtilDouble.createTreatment(experiment));
        experiment.setReplications(ExperimentUtilDouble.createReplications(experiment));
        return experiment;
    }

    /**
     * creates the Treatment for this experiment.
     * @param experiment the parent
     * @return Treatment[] the result
     */
    public static Treatment.TimeDouble createTreatment(final Experiment.TimeDouble<SimulatorInterface.TimeDouble> experiment)
    {
        Treatment.TimeDouble treatment = new Treatment.TimeDouble(experiment, "tr1", 0.0, 0.0, 100.0);
        return treatment;
    }

    /**
     * creates the replications for the test experiment.
     * @param experiment the simulation experiment
     * @return a list of replications
     * @throws NamingException on error
     */
    public static List<Replication.TimeDouble<SimulatorInterface.TimeDouble>> createReplications(
            final Experiment.TimeDouble<SimulatorInterface.TimeDouble> experiment) throws NamingException
    {
        List<Replication.TimeDouble<SimulatorInterface.TimeDouble>> replications =
                new ArrayList<Replication.TimeDouble<SimulatorInterface.TimeDouble>>();

        for (int i = 0; i < 3; i++)
        {
            Replication.TimeDouble<SimulatorInterface.TimeDouble> replication = new Replication.TimeDouble<>(i, experiment);
            Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();
            streams.put("DEFAULT", new Java2Random(SEED));
            replication.setStreams(streams);
            replication.setDescription("rep #" + i);
            replications.add(replication);
        }
        return replications;
    }
}
