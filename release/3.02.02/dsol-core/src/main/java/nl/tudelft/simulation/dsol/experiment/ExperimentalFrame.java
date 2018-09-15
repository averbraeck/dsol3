package nl.tudelft.simulation.dsol.experiment;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.pmw.tinylog.Logger;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The Experimental frame specifies the set of experiments to run. (c) 2002-2018
 * <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, such as
 *            Double or Long, the absolute and relative types are the same.
 * @param <T> the simulation time type based on the absolute and relative time.
 */
public class ExperimentalFrame<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements Iterator<Experiment<A, R, T>>, EventListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** END_OF_EXPERIMENTALFRAME_EVENT is fired when the experimental frame is ended. */
    public static final EventType END_OF_EXPERIMENTALFRAME_EVENT = new EventType("END_OF_EXPERIMENTALFRAME_EVENT");

    /** the list of experiments defined in this experimental frame. */
    private List<Experiment<A, R, T>> experiments = null;

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
     * @param url the url of the experimental frame
     */
    public ExperimentalFrame(final URL url)
    {
        super();
        this.url = url;
    }

    /**
     * Returns whether there is a next experiment to run. If one wants to link DSOL to optimization services, it is a
     * good idea to overwrite this method.
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext()
    {
        return this.currentExperiment < (this.experiments.size() - 1);
    }

    /**
     * Returns the next experiment to run. If one wants to link DSOL to optimization services, it is a good idea to
     * overwrite this method.
     * @see java.util.Iterator#next()
     */
    @Override
    public final Experiment<A, R, T> next()
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
    public final List<Experiment<A, R, T>> getExperiments()
    {
        return this.experiments;
    }

    /**
     * @param experiments The experiments to set.
     */
    public final void setExperiments(final List<Experiment<A, R, T>> experiments)
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
            Logger.warn(remoteException, "start");
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
     * represents an experimental frame.
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString()
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
    public final URL getUrl()
    {
        return this.url;
    }

    /**
     * sets the url of this experimentalframe.
     * @param url The url to set.
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
        for (Experiment<?, ?, ?> experiment : this.experiments)
        {
            experiment.reset();
        }
        this.currentExperiment = -1;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class ExperimentalFrame.TimeDouble. */
    public static class TimeDouble extends ExperimentalFrame<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new ExperimentalFrame.TimeDouble.
         */
        public TimeDouble()
        {
            super();
        }

        /**
         * constructs a new ExperimentalFrame.TimeDouble.
         * @param url the url of the experimental frame
         */
        public TimeDouble(final URL url)
        {
            super(url);
        }
    }

    /** Easy access class ExperimentalFrame.TimeFloat. */
    public static class TimeFloat extends ExperimentalFrame<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new ExperimentalFrame.TimeFloat.
         */
        public TimeFloat()
        {
            super();
        }

        /**
         * constructs a new ExperimentalFrame.TimeFloat.
         * @param url the url of the experimental frame
         */
        public TimeFloat(final URL url)
        {
            super(url);
        }
    }

    /** Easy access class ExperimentalFrame.TimeLong. */
    public static class TimeLong extends ExperimentalFrame<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        // TODO
    }

    /** Easy access class ExperimentalFrame.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends ExperimentalFrame<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

    }

    /** Easy access class ExperimentalFrame.TimeFloatUnit. */
    public static class TimeFloatUnit extends ExperimentalFrame<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

    }

    /** Easy access class ExperimentalFrame.CalendarDouble. */
    public static class CalendarDouble extends ExperimentalFrame<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

    }

    /** Easy access class ExperimentalFrame.CalendarFloat. */
    public static class CalendarFloat extends ExperimentalFrame<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

    }

    /** Easy access class ExperimentalFrame.CalendarLong. */
    public static class CalendarLong extends ExperimentalFrame<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

    }

}
