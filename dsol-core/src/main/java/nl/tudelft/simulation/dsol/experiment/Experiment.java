package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Experiment specifies the parameters for a simulation experiment <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Experiment<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        EventProducer implements EventListenerInterface, Serializable
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
    private ModelInterface model;

    /** the context. */
    private Context context = null;

    /** the description of this experiment. */
    private String description = null;

    /** the analyst for this experiment. */
    private String analyst = null;

    /** the current replication. */
    private int currentReplication = -1;

    /** are we already subscribed to the END_OF_REPLICATION_EVENT */
    private boolean subscribed = false;

    /**
     * constructs a new Experiment.
     * @param context the context for this experiment. This is probably experimentalFrame.contextRoot/experimentNumber
     */
    public Experiment(final Context context)
    {
        super();
        this.context = context;
    }

    /**
     * constructs a new Experiment.
     * @param context the context for this experiment. This is probably experimentalFrame.contextRoot/experimentNumber
     * @param treatment the treatment for this experiment
     * @param simulator the simulator
     * @param model the model to experiment with
     */
    public Experiment(final Context context, final Treatment<A, R, T> treatment,
            final SimulatorInterface<A, R, T> simulator, final ModelInterface model)
    {
        this(context);
        this.setSimulator(simulator);
        this.setTreatment(treatment);
        this.setModel(model);
    }

    /**
     * @return Returns the context.
     */
    public Context getContext()
    {
        return this.context;
    }

    /**
     * sets the simulator
     * @param simulator the simulator
     */
    public synchronized void setSimulator(final SimulatorInterface<A, R, T> simulator)
    {
        this.simulator = simulator;
        this.fireEvent(SIMULATOR_CHANGED_EVENT, simulator);
    }

    /**
     * returns the simulator
     * @return SimulatorInterface
     */
    public SimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /**
     * returns the model
     * @return ModelInterface the model
     */
    public ModelInterface getModel()
    {
        return this.model;
    }

    /**
     * @return Returns the replications.
     */
    public List<Replication<A, R, T>> getReplications()
    {
        return this.replications;
    }

    /**
     * @param replications The replications to set.
     */
    public void setReplications(final List<Replication<A, R, T>> replications)
    {
        this.replications = replications;
    }

    /**
     * starts the experiment on a simulator
     */
    public synchronized void start()
    {
        try
        {
            this.notify(new Event(SimulatorInterface.END_OF_REPLICATION_EVENT, this.simulator, Boolean.TRUE));
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "notify", remoteException);
        }
    }

    /** {@inheritDoc} */
    @Override
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
                this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                // There is no experiment to run anymore
                this.fireEvent(Experiment.END_OF_EXPERIMENT_EVENT, true);
            }
        }
    }

    /**
     * sets the model on the experiment
     * @param model the simulatormodel
     */
    public synchronized void setModel(final ModelInterface model)
    {
        this.model = model;
        this.fireEvent(MODEL_CHANGED_EVENT, model);
    }

    /**
     * Returns the treatment of this experiment
     * @return the treatment of this experiment
     */
    public Treatment<A, R, T> getTreatment()
    {
        return this.treatment;
    }

    /**
     * sets the treatment of an experiment
     * @param treatment the treatment
     */
    public void setTreatment(final Treatment<A, R, T> treatment)
    {
        this.treatment = treatment;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result =
                "[" + super.toString() + " ; " + " \n treatment=" + this.treatment.toString() + "\n" + "simulator="
                        + this.simulator;
        return result;
    }

    /**
     * resets the experiment
     */
    public void reset()
    {
        this.currentReplication = -1;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the analyst
     */
    public String getAnalyst()
    {
        return this.analyst;
    }

    /**
     * @param analyst the analyst to set
     */
    public void setAnalyst(String analyst)
    {
        this.analyst = analyst;
    }
}
