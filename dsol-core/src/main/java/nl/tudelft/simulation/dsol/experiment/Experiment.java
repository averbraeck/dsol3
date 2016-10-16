package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The Experiment specifies the parameters for a simulation experiment <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Experiment<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements EventListenerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** END_OF_EXPERIMENT_EVENT is fired when the experiment is ended. */
    public static final EventType END_OF_EXPERIMENT_EVENT = new EventType("END_OF_EXPERIMENT_EVENT");

    /** MODEL_CHANGED_EVENT is fired whenever the model is changed. */
    public static final EventType MODEL_CHANGED_EVENT = new EventType("MODEL_CHANGED_EVENT");

    /** SIMULATOR_CHANGED_EVENT is fired whenever the simulator is changed. */
    public static final EventType SIMULATOR_CHANGED_EVENT = new EventType("SIMULATOR_CHANGED_EVENT");

    /** replications are the replications of this experiment. */
    private List<Replication<A, R, T>> replications;

    /** treatment represent the treatment of this experiment. */
    private Treatment<A, R, T> treatment = null;

    /** simulator reflects the simulator. */
    private SimulatorInterface<A, R, T> simulator;

    /** model reflects the model. */
    private DSOLModel<A, R, T> model;

    /** the description of this experiment. */
    private String description = null;

    /** the analyst for this experiment. */
    private String analyst = null;

    /** the current replication. */
    private int currentReplication = -1;

    /** are we already subscribed to the END_OF_REPLICATION_EVENT. */
    private boolean subscribed = false;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(Experiment.class);

    /**
     * constructs a new Experiment.
     */
    public Experiment()
    {
        super();
    }

    /**
     * constructs a new Experiment.
     * @param treatment the treatment for this experiment
     * @param simulator the simulator
     * @param model the model to experiment with
     */
    public Experiment(final Treatment<A, R, T> treatment, final SimulatorInterface<A, R, T> simulator,
            final DSOLModel<A, R, T> model)
    {
        this.setSimulator(simulator);
        this.setTreatment(treatment);
        this.setModel(model);
    }

    /**
     * sets the simulator.
     * @param simulator the simulator
     */
    public final synchronized void setSimulator(final SimulatorInterface<A, R, T> simulator)
    {
        this.simulator = simulator;
        this.fireEvent(SIMULATOR_CHANGED_EVENT, simulator);
    }

    /**
     * returns the simulator.
     * @return SimulatorInterface
     */
    public final SimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /**
     * returns the model.
     * @return ModelInterface the model
     */
    public final DSOLModel<A, R, T> getModel()
    {
        return this.model;
    }

    /**
     * @return Returns the replications.
     */
    public final List<Replication<A, R, T>> getReplications()
    {
        return this.replications;
    }

    /**
     * @param replications The replications to set.
     */
    public final void setReplications(final List<Replication<A, R, T>> replications)
    {
        this.replications = replications;
    }

    /**
     * starts the experiment on a simulator.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public synchronized void start()
    {
        try
        {
            this.notify(new Event(SimulatorInterface.END_OF_REPLICATION_EVENT, this.simulator, Boolean.TRUE));
        }
        catch (RemoteException remoteException)
        {
            logger.warn("notify", remoteException);
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event) throws RemoteException
    {
        if (!this.subscribed)
        {
            this.simulator.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT, false);
            this.subscribed = true;
        }
        if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
        {
            if (this.currentReplication < (this.getReplications().size() - 1))
            {
                // we can run the next replication
                try
                {
                    this.currentReplication++;
                    Replication<A, R, T> next = this.getReplications().get(this.currentReplication);
                    this.simulator.initialize(next, this.treatment.getReplicationMode());
                    this.simulator.start();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
            else
            {
                // There is no experiment to run anymore
                // XXX Concurrent Modification Exception
                // this.fireEvent(Experiment.END_OF_EXPERIMENT_EVENT, true);
                // XXX this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
            }
        }
    }

    /**
     * sets the model on the experiment.
     * @param model the simulator model
     */
    public final synchronized void setModel(final DSOLModel<A, R, T> model)
    {
        this.model = model;
        this.fireEvent(MODEL_CHANGED_EVENT, model);
    }

    /**
     * @return the treatment of this experiment
     */
    public final Treatment<A, R, T> getTreatment()
    {
        return this.treatment;
    }

    /**
     * sets the treatment of an experiment.
     * @param treatment the treatment
     */
    public final void setTreatment(final Treatment<A, R, T> treatment)
    {
        this.treatment = treatment;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        String result = "[" + super.toString() + " ; " + " \n treatment=" + this.treatment.toString() + "\n"
                + "simulator=" + this.simulator;
        return result;
    }

    /**
     * resets the experiment.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void reset()
    {
        this.currentReplication = -1;
    }

    /**
     * @return Returns the experiment description.
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * @param description The description to set.
     */
    public final void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the analyst.
     */
    public final String getAnalyst()
    {
        return this.analyst;
    }

    /**
     * @param analyst the analyst to set.
     */
    public final void setAnalyst(final String analyst)
    {
        this.analyst = analyst;
    }

    /**
     * @return the context of the experiment, based on the hashCode.
     * @throws NamingException if context could not be found or created.
     */
    public final Context getContext() throws NamingException
    {
        return ContextUtil.lookup(String.valueOf(hashCode()));
    }

    /**
     * remove the entire experiment tree from the context.
     * @throws NamingException if context could not be found or removed.
     */
    public final void removeFromContext() throws NamingException
    {
        ContextUtil.destroySubContext(String.valueOf(hashCode()));
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Experiment.TimeDouble. */
    public static class TimeDouble extends Experiment<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TomeDouble.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeDouble(final Treatment.TimeDouble treatment, final SimulatorInterface.TimeDouble simulator,
                final DSOLModel.TimeDouble model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.TimeFloat. */
    public static class TimeFloat extends Experiment<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TimeFloat.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeFloat(final Treatment.TimeFloat treatment, final SimulatorInterface.TimeFloat simulator,
                final DSOLModel.TimeFloat model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.TimeLong. */
    public static class TimeLong extends Experiment<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TimeLong.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeLong(final Treatment.TimeLong treatment, final SimulatorInterface.TimeLong simulator,
                final DSOLModel.TimeLong model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Experiment<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TimeDoubleUnit.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeDoubleUnit(final Treatment.TimeDoubleUnit treatment,
                final SimulatorInterface.TimeDoubleUnit simulator, final DSOLModel.TimeDoubleUnit model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.TimeFloatUnit. */
    public static class TimeFloatUnit extends Experiment<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TimeFloatUnit.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeFloatUnit(final Treatment.TimeFloatUnit treatment, final SimulatorInterface.TimeFloatUnit simulator,
                final DSOLModel.TimeFloatUnit model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.TimeLongUnit. */
    public static class TimeLongUnit extends Experiment<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.TimeLongUnit.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public TimeLongUnit(final Treatment.TimeLongUnit treatment, final SimulatorInterface.TimeLongUnit simulator,
                final DSOLModel.TimeLongUnit model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.CalendarDouble. */
    public static class CalendarDouble extends Experiment<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.CalendarDouble.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public CalendarDouble(final Treatment.CalendarDouble treatment,
                final SimulatorInterface.CalendarDouble simulator, final DSOLModel.CalendarDouble model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.CalendarFloat. */
    public static class CalendarFloat extends Experiment<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.CalendarFloat.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public CalendarFloat(final Treatment.CalendarFloat treatment, final SimulatorInterface.CalendarFloat simulator,
                final DSOLModel.CalendarFloat model)
        {
            super(treatment, simulator, model);
        }
    }

    /** Easy access class Experiment.CalendarLong. */
    public static class CalendarLong extends Experiment<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.CalendarLong.
         * @param treatment the treatment for this experiment
         * @param simulator the simulator
         * @param model the model to experiment with
         */
        public CalendarLong(final Treatment.CalendarLong treatment, final SimulatorInterface.CalendarLong simulator,
                final DSOLModel.CalendarLong model)
        {
            super(treatment, simulator, model);
        }
    }

}
