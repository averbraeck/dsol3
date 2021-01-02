package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.djunits.Throw;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The Experimental frame specifies a set of experiments to run. The ExperimentalFrame has a start() method that executes all
 * experiments in the frame. The reset() method allows for a full reset, after which the experiments can be started again.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Peter Jacobs, Alexander Verbraeck
 */
public class ExperimentalFrame extends EventProducer implements Iterator<Experiment<?, ?, ?, ?>>, EventListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** START_EXPERIMENTALFRAME_EVENT is fired when the experimental frame is started. */
    public static final EventType START_EXPERIMENTALFRAME_EVENT =
            new EventType(new MetaData("START_EXPERIMENTALFRAME_EVENT", "Start of experimental frame"));

    /** END_EXPERIMENTALFRAME_EVENT is fired when the experimental frame is ended. */
    public static final EventType END_EXPERIMENTALFRAME_EVENT =
            new EventType(new MetaData("END_EXPERIMENTALFRAME_EVENT", "End of experimental frame"));

    /** the list of experiments defined in this experimental frame. */
    private List<? extends Experiment<?, ?, ?, ?>> experiments = null;

    /** the current experiment. */
    private int currentExperiment = -1;

    /** the id of the set of experiments. */
    private final Serializable id;

    /**
     * Construct a new ExperimentalFrame.
     * @param id Serializable; the id of the set of experiments
     * @throws NullPointerException when id is null
     */
    public ExperimentalFrame(final Serializable id)
    {
        Throw.whenNull(id, "ExperimentalFrame needs a valid id");
        this.id = id;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.id;
    }

    /**
     * Returns whether there is a next experiment to run. If one wants to link DSOL to optimization services, it is a good idea
     * to overwrite this method.
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext()
    {
        return this.currentExperiment < (this.experiments.size() - 1);
    }

    /**
     * Returns the next experiment to run. If one wants to link DSOL to optimization services, it is a good idea to overwrite
     * this method.
     * @see java.util.Iterator#next()
     */
    @Override
    public final Experiment<?, ?, ?, ?> next()
    {
        this.currentExperiment++;
        return this.experiments.get(this.currentExperiment);
    }

    /** {@inheritDoc} */
    @Override
    public final void remove()
    {
        this.experiments.remove(this.currentExperiment);
    }

    /**
     * Returns the experiments.
     * @return List&lt;? extends Experiment&lt;?, ?, ?, ?&gt;&gt;; the experiments.
     */
    public final List<? extends Experiment<?, ?, ?, ?>> getExperiments()
    {
        return this.experiments;
    }

    /**
     * @param experiments List&lt;? extends Experiment&lt;?, ?, ?, ?&gt;&gt;; The experiments to set.
     * @throws NullPointerException on a null list of experiments, or a null experiment in the list
     * @throws IllegalArgumentException on an empty list of experiments
     */
    public final void setExperiments(final List<? extends Experiment<?, ?, ?, ?>> experiments)
    {
        Throw.whenNull(experiments, "ExperimentalFrame does not accept a null list of experiments");
        Throw.when(experiments.isEmpty(), IllegalArgumentException.class,
                "ExperimentalFrame does not accept an empty list of experiments");
        this.experiments = experiments;
        int i = 0;
        for (Experiment<?, ?, ?, ?> experiment : experiments)
        {
            Throw.whenNull(experiment, "ExperimentalFrame: null experiment in experiment list");
            if (experiment.getDescription() == null)
            {
                experiment.setDescription("Exp " + i++);
            }
            if (experiment.getAnalyst() == null)
            {
                experiment.setAnalyst("Analyst: none");
            }
        }
    }

    /**
     * Start the next experiment from the list of experiments, or fire END_EXPERIMENTALFRAME_EVENT when there are no more
     * non-executed experiments.
     * @throws RemoteException on network error when using RMI
     */
    private void startNextExperiment() throws RemoteException
    {
        if (this.hasNext())
        {
            // we can run the next experiment
            Experiment<?, ?, ?, ?> next = this.next();
            next.addListener(this, Experiment.END_EXPERIMENT_EVENT, ReferenceType.STRONG);
            this.fireEvent(ExperimentalFrame.START_EXPERIMENTALFRAME_EVENT, null);
            next.start();
        }
        else
        {
            // There is no experiment to run anymore
            this.fireEvent(ExperimentalFrame.END_EXPERIMENTALFRAME_EVENT, null);
        }
    }

    /**
     * starts the experiment on a simulator.
     * @throws RemoteException on network error when using RMI
     * @throws SimRuntimeException when there are no more experiments to run, or when the simulator is already running
     */
    public final synchronized void start() throws RemoteException
    {
        Throw.when(this.currentExperiment >= this.experiments.size(), SimRuntimeException.class,
                "ExperimentalFrame: No more experiments");
        if (this.currentExperiment >= 0)
        {
            Experiment<?, ?, ?, ?> experiment = this.experiments.get(this.currentExperiment);
            SimulatorInterface<?, ?, ?> simulator = experiment.getSimulator();
            Throw.when(simulator.isStartingOrRunning(), SimRuntimeException.class,
                    "Simulator for experiment running -- ExperimentalFrame cannot be started");
        }
        startNextExperiment();
    }

    /**
     * resets the experimentalFrame.
     */
    public final void reset()
    {
        for (Experiment<?, ?, ?, ?> experiment : this.experiments)
        {
            experiment.reset();
        }
        this.currentExperiment = -1;
    }

    /** {@inheritDoc} */
    @Override
    public final void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(Experiment.END_EXPERIMENT_EVENT))
        {
            this.experiments.get(this.currentExperiment).removeListener(this, Experiment.END_EXPERIMENT_EVENT);
            startNextExperiment();
        }
    }

    /**
     * represents an experimental frame.
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString()
    {
        String result = "ExperimentalFrame ";
        for (Experiment<?, ?, ?, ?> experiment : this.experiments)
        {
            result = result + "\n [Experiment=" + experiment.toString();
        }
        return result;
    }

}
