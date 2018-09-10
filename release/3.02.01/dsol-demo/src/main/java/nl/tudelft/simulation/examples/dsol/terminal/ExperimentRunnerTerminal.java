package nl.tudelft.simulation.examples.dsol.terminal;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;

/**
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Jul 26, 2018
 */
public final class ExperimentRunnerTerminal implements EventListenerInterface
{
    /** number of running simulations. */
    private int numruns = 0;

    /** number of completed simulations. */
    private int completed = 0;

    /** number of runs. */
    private static final int REPS = 100;

    /** number of runs. */
    private static final int RUNS = 3 * 4 * REPS;

    /**
     * Construct the terminal experiment.
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    private ExperimentRunnerTerminal() throws SimRuntimeException, RemoteException, NamingException
    {
        long seed = 1;
        int maxConcurrent = 8;
        for (int numQC = 4; numQC <= 6; numQC++)
        {
            for (int numAGV = 25; numAGV <= 28; numAGV++)
            {
                for (int rep = 1; rep <= REPS; rep++)
                {
                    while (this.numruns > maxConcurrent)
                    {
                        try
                        {
                            Thread.sleep(1);
                        }
                        catch (InterruptedException exception)
                        {
                            //
                        }
                    }

                    Terminal model = new Terminal(rep);
                    double runtime = 40 * 60;
                    DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble();
                    Replication<Double, Double, SimTimeDouble> replication =
                            new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, runtime, model);
                    replication.getStreams().put("default", new MersenneTwister(seed++));
                    replication.getTreatment().getProperties().setProperty("numQC", "" + numQC);
                    replication.getTreatment().getProperties().setProperty("numAGV", "" + numAGV);
                    simulator.initialize(replication, ReplicationMode.TERMINATING);
                    model.addListener(this, Terminal.READY_EVENT);
                    this.numruns++;
                    simulator.start();
                    simulator.scheduleEventAbs(runtime - 0.00001, this, this, "terminate",
                            new Object[]{simulator, numQC, numAGV, rep, model});
                }
            }
        }
    }

    /**
     * @param simulator the simulator
     * @param numQC num QC
     * @param numAGV num AGV
     * @param rep replication number
     * @param model the model
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     */
    public synchronized void terminate(final DEVSSimulator.TimeDouble simulator, final int numQC, final int numAGV,
            final int rep, final Terminal model) throws SimRuntimeException, RemoteException
    {
        simulator.stop();
        System.out.println(numQC + "\t" + numAGV + "\t" + rep + "\tNaN\tNaN\t40\t" + model.getShip().getContainers());
        this.numruns--;
        this.completed++;
        if (this.completed == RUNS)
        {
            System.exit(0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(Terminal.READY_EVENT))
        {
            Terminal.Output output = (Terminal.Output) event.getContent();
            System.out.println(output.getNumQC() + "\t" + output.getNumAGV() + "\t" + output.getRep() + "\t"
                    + output.getDelayHours() + "\t" + output.getCosts() + "\t" + output.getReady() + "\t3000");
            this.numruns--;
            this.completed++;
            if (this.completed == RUNS)
            {
                System.exit(0);
            }
        }
    }

    /**
     * @param args args
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new ExperimentRunnerTerminal();
    }

}
