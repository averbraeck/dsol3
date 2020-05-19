package nl.tudelft.simulation.dsol.formalisms.process;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.experiment.Experiment;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.TimeUnit;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.InitialEventContext;

/**
 * A TestExperimentalFrame <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class TestExperimentalFrame
{
    /**
     * STARTTIME defines the starting time for the experiment in millisec since 1970.
     */
    public static final long STARTTIME = 0;

    /** TIMEUNIT refers to the time units of the experiment. */
    public static final TimeUnit TIMEUNIT = TimeUnit.UNIT;

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
    private TestExperimentalFrame()
    {
        super();
        // unreachable code
    }

    /**
     * creates an experimental frame.
     * @param model the model
     * @param simulator the simulator
     * @return an experimental Frame
     */
    public static ExperimentalFrame createExperimentalFrame(final SimulatorInterface simulator,
            final DSOLModel model)
    {
        try
        {
            ExperimentalFrame experimentalFrame = new ExperimentalFrame();

            List<Experiment> experiments = new ArrayList<Experiment>();
            for (int i = 0; i < 3; i++)
            {
                Experiment experiment = TestExperimentalFrame.createExperiment();
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
     * creates a new TestExperimentalFrame.
     * @return ExperimentalFrame
     */
    public static Experiment createExperiment() throws NamingException
    {
        Experiment experiment = new Experiment();
        experiment.setTreatment(TestExperimentalFrame.createTreatment(experiment));
        experiment.setReplications(TestExperimentalFrame.createReplications(experiment));
        return experiment;
    }

    /**
     * creates the Treatment for this experiment.
     * @param experiment the parent
     * @return Treatment[] the result
     */
    public static Treatment createTreatment(final Experiment experiment)
    {
        Treatment treatment = new Treatment(experiment, "1", new SimTimeDouble(System.currentTimeMillis()), 0.0, 100.0);
        return treatment;
    }

    /**
     * creates the replications for the test experiment.
     * @param experiment the simulation experiment
     * @return a list of replications
     */
    public static List<Replication> createReplications(final Experiment experiment) throws NamingException
    {
        List<Replication> replications = new ArrayList<Replication>();

        for (int i = 0; i < 3; i++)
        {
            Replication replication = new Replication(experiment);
            Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();
            streams.put("DEFAULT", new Java2Random(SEED));
            replication.setStreams(streams);
            replications.add(replication);
        }
        return replications;
    }
}