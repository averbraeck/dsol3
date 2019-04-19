package nl.tudelft.simulation.dsol.experiment;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The Experimental frame specifies the set of experiments to run.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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

    /** END_OF_EXPERIMENTALFRAME_EVENT is fired when the experimental frame is ended. */
    public static final EventType END_OF_EXPERIMENTALFRAME_EVENT = new EventType("END_OF_EXPERIMENTALFRAME_EVENT");

    /** the list of experiments defined in this experimental frame. */
    private List<? extends Experiment<?, ?, ?, ?>> experiments = null;

    /** the current experiment. */
    private int currentExperiment = -1;

    /** the URL where we can find this experimentalFrame. */
    private URL url = null;

    /**
     * constructs a new ExperimentalFrame.
     */
    public ExperimentalFrame()
    {
        this(null);
    }

    /**
     * constructs a new Experimental frame.
     * @param url URL; the url of the experimental frame
     */
    public ExperimentalFrame(final URL url)
    {
        super();
        this.url = url;
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
     * @return Returns the experiments.
     */
    public final List<? extends Experiment<?, ?, ?, ?>> getExperiments()
    {
        return this.experiments;
    }

    /**
     * @param experiments List&lt;Experiment&lt;A,R,T&gt;&gt;; The experiments to set.
     */
    public final void setExperiments(final List<? extends Experiment<?, ?, ?, ?>> experiments)
    {
        this.experiments = experiments;
    }

    /**
     * starts the experiment on a simulator.
     */
    public final synchronized void start()
    {
        try
        {
            this.notify(new Event(Experiment.END_OF_EXPERIMENT_EVENT, this, null));
        }
        catch (RemoteException remoteException)
        {
            SimLogger.always().warn(remoteException, "start");
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(Experiment.END_OF_EXPERIMENT_EVENT))
        {
            ((EventProducerInterface) event.getSource()).removeListener(this, Experiment.END_OF_EXPERIMENT_EVENT);
            if (this.hasNext())
            {
                // we can run the next experiment...s
                Experiment<?, ?, ?, ?> next = this.next();
                next.addListener(this, Experiment.END_OF_EXPERIMENT_EVENT, false);
                next.start();
            }
            else
            {
                // There is no experiment to run anymore
                this.fireEvent(ExperimentalFrame.END_OF_EXPERIMENTALFRAME_EVENT, true);
            }
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

    /**
     * @return Returns the url.
     */
    public final URL getUrl()
    {
        return this.url;
    }

    /**
     * sets the url of this experimentalframe.
     * @param url URL; The url to set.
     */
    public final void setUrl(final URL url)
    {
        this.url = url;
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

}
