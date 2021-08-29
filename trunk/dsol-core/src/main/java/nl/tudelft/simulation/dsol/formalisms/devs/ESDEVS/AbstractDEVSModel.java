package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducer;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.djutils.reflection.ClassUtil;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AbstractDEVSModel class. The basic model or component from which the AtomicModel, the CoupledModel, and the AbstractEntity
 * are derived. The DEVSModel provides basic functionality for reporting its state changes through the publish/subscribe
 * mechanism.
 * <p>
 * Copyright (c) 2009-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class AbstractDEVSModel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends EventProducer
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /** the parent model we are part of. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected CoupledModel<A, R, T> parentModel;

    /** the simulator this model or component will schedule its events on. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DEVSSimulatorInterface<A, R, T> simulator;

    /** all DEVS models are named - this is the component name. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String modelName;

    /** all DEVS models are named - this is the full name with dot notation. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String fullName;

    /** event for listeners about state update. */
    public static final TimedEventType STATE_UPDATE = new TimedEventType(new MetaData("STATE_UPDATE", "State update",
            new ObjectDescriptor("stateUpdate", "State update", StateUpdate.class)));

    /** map of call classes and fields for which the state will be reported. */
    private static Map<Class<?>, Set<Field>> stateFieldMap = new LinkedHashMap<Class<?>, Set<Field>>();

    /** set of fields for this class which the state will be reported. */
    private Set<Field> stateFieldSet = null;

    /** the fields of the AtomicModel. */
    private static Set<Field> atomicFields = new LinkedHashSet<Field>();

    /** the fields of the CoupledModel. */
    private static Set<Field> coupledFields = new LinkedHashSet<Field>();

    /** the fields of the AbstractEntity. */
    private static Set<Field> entityFields = new LinkedHashSet<Field>();

    /** the fields of the AbstractDEVSMOdel. */
    private static Set<Field> abstractDEVSFields = new LinkedHashSet<Field>();

    /**
     * Static constructor. Takes care of filling the static constants the first time an extension of the AbstractDEVSModel is
     * created.
     */
    static
    {
        AbstractDEVSModel.atomicFields = ClassUtil.getAllFields(AtomicModel.class);
        AbstractDEVSModel.coupledFields = ClassUtil.getAllFields(CoupledModel.class);
        AbstractDEVSModel.entityFields = ClassUtil.getAllFields(AbstractEntity.class);
        AbstractDEVSModel.abstractDEVSFields = ClassUtil.getAllFields(AbstractDEVSModel.class);
    }

    /**
     * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent model
     * we are part of. A parent model of null means that we are the top model.
     * @param modelName String; the name of this component
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; the simulator to schedule the events on.
     * @param parentModel CoupledModel&lt;A,R,T&gt;; the parent model we are part of.
     */
    public AbstractDEVSModel(final String modelName, final DEVSSimulatorInterface<A, R, T> simulator,
            final CoupledModel<A, R, T> parentModel)
    {
        this.modelName = modelName;
        if (parentModel != null)
        {
            this.fullName = parentModel.getFullName() + ".";
        }
        this.fullName += this.modelName;
        this.simulator = simulator;
        this.parentModel = parentModel;
        if (!AbstractDEVSModel.stateFieldMap.containsKey(this.getClass()))
        {
            this.createStateFieldSet();
        }
        this.stateFieldSet = AbstractDEVSModel.stateFieldMap.get(this.getClass());
    }

    /**
     * @return the simulator this model schedules its events on.
     */
    public final DEVSSimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /**
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; the simulator to use from now on
     */
    public final void setSimulator(final DEVSSimulatorInterface<A, R, T> simulator)
    {
        this.simulator = simulator;
    }

    /**
     * @return the parent model we are part of.
     */
    public final CoupledModel<A, R, T> getParentModel()
    {
        return this.parentModel;
    }

    /**
     * @return the name of the model.
     */
    public final String getModelName()
    {
        return this.modelName;
    }

    /**
     * @return the full name of the model in dot notation.
     */
    public final String getFullName()
    {
        return this.fullName;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.fullName;
    }

    /**
     * Print the model, preceded by a user provided string.
     * @param header String; the user provided string to print in front of the model (e.g. newlines, header).
     */
    public abstract void printModel(String header);

    // ///////////////////////////////////////////////////////////////////////////
    // STATE CHANGE UPDATES, STATE SAVES
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Create state field set. For this first version, take all the fields into account as state variables. The method substract
     * the fields that are on the level of AbstractModel or Atomic Model or higher; only leave the non-static fields that are
     * part of the descendents of the abstract models.
     */
    private void createStateFieldSet()
    {
        Set<Field> fieldSet = ClassUtil.getAllFields(this.getClass());

        if (this instanceof AtomicModel)
        {
            fieldSet.removeAll(AbstractDEVSModel.atomicFields);
        }
        else if (this instanceof CoupledModel)
        {
            fieldSet.removeAll(AbstractDEVSModel.coupledFields);
        }
        else if (this instanceof AbstractEntity)
        {
            fieldSet.removeAll(AbstractDEVSModel.entityFields);
        }
        else
        {
            fieldSet.removeAll(AbstractDEVSModel.abstractDEVSFields);
        }

        // we can now do something with the annotations, but that comes later

        // put the results in the map
        AbstractDEVSModel.stateFieldMap.put(this.getClass(), fieldSet);
    }

    /**
     * Fire a state update. At this moment, all state variables are reported for an atomic model when it fires its
     * delta_internal or delta_external method. More intelligence can be added here later. For simple types, a comparison with
     * the old value (state map) is possible. For complex variables (objects) this is more difficult as a deep clone should be
     * saved as old state, followed by a full comparison. This does not seem practical, and more expensive than firing the state
     * change of all state variables. The intelligence to detect real state changes then has to be built in at the receiver's
     * side.
     */
    protected void fireUpdatedState()
    {
        for (Field field : this.stateFieldSet)
        {
            try
            {
                field.setAccessible(true);
                StateUpdate stateUpdate = new StateUpdate(this.getModelName(), field.getName(), field.get(this));
                this.fireTimedEvent(AbstractDEVSModel.STATE_UPDATE, stateUpdate, getSimulator().getSimulatorTime());
            }
            catch (IllegalAccessException exception)
            {
                this.simulator.getLogger().always().error("Tried to fire update for variable {} but got an exception.",
                        field.getName());
                System.err.println(this.getModelName() + " - fireUpdateState: Tried to fire update for variable "
                        + field.getName() + " but got an exception.");
            }
        }
    }

    /**
     * StateUpdate class. Reports a state update. Right now, it is a modelname - variable name - value tuple.
     * <p>
     * Copyright (c) 2009-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>.
     * The DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
     * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
     */
    public static class StateUpdate implements Serializable
    {
        /** the default serial version UId. */
        private static final long serialVersionUID = 1L;

        /** the name of the model. */
        private String model;

        /** the name of the variable. */
        private String variable;

        /** the value of the variable. */
        private Object value;

        /**
         * Construct a StateUPdate tuple to report a state update.
         * @param modelName String; the name of the model
         * @param variableName String; the name of the variable
         * @param value Object; the value
         */
        public StateUpdate(final String modelName, final String variableName, final Object value)
        {
            super();
            this.model = modelName;
            this.variable = variableName;
            this.value = value;
        }

        /**
         * @return the modelName
         */
        public final String getModel()
        {
            return this.model;
        }

        /**
         * @return the variableName
         */
        public final String getVariable()
        {
            return this.variable;
        }

        /**
         * @return the value
         */
        public final Object getValue()
        {
            return this.value;
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class AbstractDEVSModel.TimeDouble. */
    public abstract static class TimeDouble extends AbstractDEVSModel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent
         * model we are part of. A parent model of null means that we are the top model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator to schedule the events on.
         * @param parentModel CoupledModel.TimeDouble; the parent model we are part of.
         */
        public TimeDouble(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator,
                final CoupledModel.TimeDouble parentModel)
        {
            super(modelName, simulator, parentModel);
        }
    }

    /** Easy access class AbstractDEVSModel.TimeFloat. */
    public abstract static class TimeFloat extends AbstractDEVSModel<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent
         * model we are part of. A parent model of null means that we are the top model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeFloat; the simulator to schedule the events on.
         * @param parentModel CoupledModel.TimeFloat; the parent model we are part of.
         */
        public TimeFloat(final String modelName, final DEVSSimulatorInterface.TimeFloat simulator,
                final CoupledModel.TimeFloat parentModel)
        {
            super(modelName, simulator, parentModel);
        }
    }

    /** Easy access class AbstractDEVSModel.TimeLong. */
    public abstract static class TimeLong extends AbstractDEVSModel<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent
         * model we are part of. A parent model of null means that we are the top model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeLong; the simulator to schedule the events on.
         * @param parentModel CoupledModel.TimeLong; the parent model we are part of.
         */
        public TimeLong(final String modelName, final DEVSSimulatorInterface.TimeLong simulator,
                final CoupledModel.TimeLong parentModel)
        {
            super(modelName, simulator, parentModel);
        }
    }

    /** Easy access class AbstractDEVSModel.TimeDoubleUnit. */
    public abstract static class TimeDoubleUnit extends AbstractDEVSModel<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent
         * model we are part of. A parent model of null means that we are the top model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; the simulator to schedule the events on.
         * @param parentModel CoupledModel.TimeDoubleUnit; the parent model we are part of.
         */
        public TimeDoubleUnit(final String modelName, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final CoupledModel.TimeDoubleUnit parentModel)
        {
            super(modelName, simulator, parentModel);
        }
    }

    /** Easy access class AbstractDEVSModel.TimeFloatUnit. */
    public abstract static class TimeFloatUnit extends AbstractDEVSModel<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the parent
         * model we are part of. A parent model of null means that we are the top model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; the simulator to schedule the events on.
         * @param parentModel CoupledModel.TimeFloatUnit; the parent model we are part of.
         */
        public TimeFloatUnit(final String modelName, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final CoupledModel.TimeFloatUnit parentModel)
        {
            super(modelName, simulator, parentModel);
        }
    }

}
