package nl.tudelft.simulation.examples.dsol.timesharedcomputer;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Counter;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.charts.BoxAndWhiskerChart;
import nl.tudelft.simulation.dsol.statistics.charts.Histogram;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Computer example as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and
 * 2.4. <br>
 * Copyright (c) 2003-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.1 02.04.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Computer implements DSOLModel<Double, Double, SimTimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /** the number of jobs. */
    public static final long NUMBER_OF_JOBS = 1000;

    /** the number of terminals. */
    public static final long NUMBER_OF_TERMINALS = 80;

    /**
     * constructs a new Computer.
     */
    public Computer()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> modelSimulator)
            throws SimRuntimeException
    {
        this.simulator = (SimulatorInterface.TimeDouble) modelSimulator;
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface.TimeDouble) modelSimulator;
        StreamInterface stream = modelSimulator.getReplication().getStream("default");

        CPU cpu = new CPU(devsSimulator);
        DistContinuous thinkDelay = new DistExponential(stream, 25);
        DistContinuous processDelay = new DistExponential(stream, 0.8);

        try
        {
            // First the statistics
            Persistent<Double, Double, SimTimeDouble> persistent = new Persistent<>("service time", devsSimulator);
            ExitCounter exitCounter = new ExitCounter("counter", devsSimulator);

            // Now the charts
            Histogram histogram = new Histogram(modelSimulator, "service time", new double[]{0, 200}, 200);
            histogram.add("serviceTime", persistent, nl.tudelft.simulation.jstats.statistics.Persistent.VALUE_EVENT);

            BoxAndWhiskerChart boxAndWhisker = new BoxAndWhiskerChart(modelSimulator, "serviceTime");
            boxAndWhisker.add(persistent);

            // Now we start the action
            for (int i = 0; i < NUMBER_OF_TERMINALS; i++)
            {
                Terminal terminal = new Terminal(devsSimulator, cpu, thinkDelay, processDelay);
                terminal.addListener(exitCounter, StationInterface.RELEASE_EVENT);
                terminal.addListener(persistent, Terminal.SERVICE_TIME);
            }
        }
        catch (RemoteException exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /**
     * A counter which stops after a predifined number of jobs.
     */
    public static class ExitCounter extends Counter<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /** simulator refers to the simulator. */
        private SimulatorInterface.TimeDouble simulator = null;

        /**
         * constructs a new ExitCounter.
         * @param description the description of the counter
         * @param simulator the simulator
         * @throws RemoteException on network failure
         */
        public ExitCounter(final String description, final SimulatorInterface.TimeDouble simulator)
                throws RemoteException
        {
            super(description, simulator);
            this.simulator = simulator;
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final EventInterface event)
        {
            super.notify(event);
            if (this.count >= NUMBER_OF_JOBS)
            {
                try
                {
                    if (this.simulator.isRunning())
                    {
                        this.simulator.stop();
                    }
                }
                catch (SimRuntimeException exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
