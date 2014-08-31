package nl.tudelft.simulation.dsol.experiment;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Experimental frame specifies the set of experiments to run. (c) copyright 2002-2005 <a
 * href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, such as
 *            Double or UnitTimeLong, the absolute and relative types are the same.
 * @param <T> the simulation time type based on the absolute and relative time.
 */
public class ExperimentalFrame<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements Iterator<Experiment<A, R, T>>, EventListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * END_OF_EXPERIMENTALFRAME_EVENT is fired when the experimental frame is ended
     */
    public static final EventType END_OF_EXPERIMENTALFRAME_EVENT = new EventType("END_OF_EXPERIMENTALFRAME_EVENT");

    /** the list of experiments defined in this experimental frame. */
    protected List<Experiment<A, R, T>> experiments = null;

    /** the current experiment. */
    protected int currentExperiment = -1;

    /**
     * the context of this experimentalFrame. This context is used as the root in the context
     */
    private Context context = null;

    /** the URL where we can find this experimentalFrame. */
    private URL url = null;

    /**
     * constructs a new ExperimentalFrame.
     * @param context the contet to use
     */
    public ExperimentalFrame(final Context context)
    {
        this(context, null);
    }

    /**
     * constructs a new Experimental frame.
     * @param context the name in the nameSpace
     * @param url the url of the experimental frame
     */
    public ExperimentalFrame(final Context context, final URL url)
    {
        super();
        this.context = context;
        this.url = url;
    }

    /**
     * Returns whether there is a next experiment to run. If one wants to link DSOL to optimization services, it is a
     * good idea to overwrite this method.
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        return this.currentExperiment < (this.experiments.size() - 1);
    }

    /**
     * Returns the next experiment to run. If one wants to link DSOL to optimization services, it is a good idea to
     * overwrite this method.
     * @see java.util.Iterator#next()
     */
    public Experiment<A, R, T> next()
    {
        this.currentExperiment++;
        return this.experiments.get(this.currentExperiment);
    }

    /** {@inheritDoc} */
    public void remove()
    {
        this.experiments.remove(this.currentExperiment);
    }

    /**
     * @return Returns the experiments.
     */
    public List<Experiment<A, R, T>> getExperiments()
    {
        return this.experiments;
    }

    /**
     * @param experiments The experiments to set.
     */
    public void setExperiments(final List<Experiment<A, R, T>> experiments)
    {
        this.experiments = experiments;
    }

    /**
     * starts the experiment on a simulator
     */
    public synchronized void start()
    {
        try
        {
            this.notify(new Event(Experiment.END_OF_EXPERIMENT_EVENT, this, null));
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "start", remoteException);
        }
    }

    /** {@inheritDoc} */
    public void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(Experiment.END_OF_EXPERIMENT_EVENT))
        {
            ((EventProducerInterface) event.getSource()).removeListener(this, Experiment.END_OF_EXPERIMENT_EVENT);
            if (this.hasNext())
            {
                // we can run the next experiment...s
                Experiment<?, ?, ?> next = this.next();
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
     * @return Returns the context.
     */
    public Context getContext()
    {
        return this.context;
    }

    /**
     * represents an experimental frame
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String result = "ExperimentalFrame ";
        for (Experiment<?, ?, ?> experiment : this.experiments)
        {
            result = result + "\n [Experiment=" + experiment.toString();
        }
        return result;
    }

    /**
     * @return Returns the url.
     */
    public URL getUrl()
    {
        return this.url;
    }

    /**
     * sets the url of this experimentalframe
     * @param url The url to set.
     */
    public void setUrl(final URL url)
    {
        this.url = url;
    }

    /**
     * resets the experimentalFrame
     */
    public void reset()
    {
        for (Experiment<?, ?, ?> experiment : this.experiments)
        {
            experiment.reset();
        }
        this.currentExperiment = -1;
    }
}
