/*
 * @(#)Experiment.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.Context;

import nl.tudelft.simulation.dsol.ModelInterface;
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
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class Experiment extends EventProducer implements EventListenerInterface, Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** END_OF_EXPERIMENT_EVENT is fired when the experiment is ended */
    public static final EventType END_OF_EXPERIMENT_EVENT = new EventType("END_OF_EXPERIMENT_EVENT");

    /** MODEL_CHANGED_EVENT is fired whenever the model is changed */
    public static final EventType MODEL_CHANGED_EVENT = new EventType("MODEL_CHANGED_EVENT");

    /** SIMULATOR_CHANGED_EVENT is fired whenever the simulator is changed */
    public static final EventType SIMULATOR_CHANGED_EVENT = new EventType("SIMULATOR_CHANGED_EVENT");

    /** replications are the replications of this experiment */
    private List<Replication> replications;

    /** treatment represent the treatment of this experiment */
    private Treatment treatment = null;

    /** simulator reflects the simulator */
    private SimulatorInterface simulator;

    /** model reflects the model */
    private ModelInterface model;

    /** the context */
    private Context context = null;

    /** the description of this experiment */
    private String description = null;

    /** the analyst for this experiment */
    private String analyst = null;

    /** the current replication */
    private int currentReplication = -1;

    /** are we already subscribed to the END_OF_REPLICATION_EVENT */
    private boolean subscribed = false;

    /**
     * constructs a new Experiment
     * @param context the context for this experiment. This is probably experimentalFrame.contextRoot/experimentNumber
     */
    public Experiment(final Context context)
    {
        super();
        this.context = context;
    }

    /**
     * constructs a new Experiment
     * @param context the context for this experiment. This is probably experimentalFrame.contextRoot/experimentNumber
     * @param treatment the treatment for this experiment
     * @param simulator the simulator
     * @param model the model to experiment with
     */
    public Experiment(final Context context, final Treatment treatment, final SimulatorInterface simulator,
            final ModelInterface model)
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
    public synchronized void setSimulator(final SimulatorInterface simulator)
    {
        this.simulator = simulator;
        this.fireEvent(SIMULATOR_CHANGED_EVENT, simulator);
    }

    /**
     * returns the simulator
     * @return SimulatorInterface
     */
    public SimulatorInterface getSimulator()
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
    public List<Replication> getReplications()
    {
        return this.replications;
    }

    /**
     * @param replications The replications to set.
     */
    public void setReplications(final List<Replication> replications)
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

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface#notify(nl.tudelft.simulation.event.EventInterface)
     */
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
                    Replication next = this.getReplications().get(this.currentReplication);
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
    public Treatment getTreatment()
    {
        return this.treatment;
    }

    /**
     * sets the treatment of an experiment
     * @param treatment the treatment
     */
    public void setTreatment(final Treatment treatment)
    {
        this.treatment = treatment;
    }

    /**
     * @see java.lang.Object#toString()
     */
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