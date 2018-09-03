package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.support.ClassUtilV2;

/**
 * AbstractDEVSModel class. The basic model or component from which the AtomicModel, the CoupledModel, and the
 * AbstractEntity are derived. The DEVSModel provides basic functionality for reporting its state changes through the
 * publish/subscribe mechanism.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Oct 17, 2009 <br>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 */
// TODO public abstract class AbstractDEVSModel<A, R, T> extends EventProducer
public abstract class AbstractDEVSModel extends EventProducer
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /** the parent model we are part of. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected CoupledModel parentModel;

    /** the simulator this model or component will schedule its events on. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DEVSSimulatorInterface.TimeDouble simulator;

    /** all DEVS models are named - this is the component name. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String modelName;

    /** all DEVS models are named - this is the full name with dot notation. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String fullName;

    /** event for listeners about state update. */
    public static final EventType STATE_UPDATE = new EventType("STATE_UPDATE");

    /** map of call classes and fields for which the state will be reported. */
    private static Map<Class<?>, Set<Field>> stateFieldMap = new HashMap<Class<?>, Set<Field>>();

    /** set of fields for this class which the state will be reported. */
    private Set<Field> stateFieldSet = null;

    /** the fields of the AtomicModel. */
    private static Set<Field> atomicFields = new HashSet<Field>();

    /** the fields of the CoupledModel. */
    private static Set<Field> coupledFields = new HashSet<Field>();

    /** the fields of the AbstractEntity. */
    private static Set<Field> entityFields = new HashSet<Field>();

    /** the fields of the AbstractDEVSMOdel. */
    private static Set<Field> abstractDEVSFields = new HashSet<Field>();

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(AbstractDEVSModel.class);

    /**
     * Static constructor. Takes care of filling the static constants the first time an extension of the
     * AbstractDEVSModel is created.
     */
    static
    {
        AbstractDEVSModel.atomicFields = ClassUtilV2.getAllFields(AtomicModel.class);
        AbstractDEVSModel.coupledFields = ClassUtilV2.getAllFields(CoupledModel.class);
        AbstractDEVSModel.entityFields = ClassUtilV2.getAllFields(AbstractEntity.class);
        AbstractDEVSModel.abstractDEVSFields = ClassUtilV2.getAllFields(AbstractDEVSModel.class);
    }

    /**
     * Constructor for an abstract DEVS model: we have to indicate the simulator to schedule the events on, and the
     * parent model we are part of. A parent model of null means that we are the top model.
     * @param modelName the name of this component
     * @param simulator the simulator to schedule the events on.
     * @param parentModel the parent model we are part of.
     */
    public AbstractDEVSModel(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator,
            final CoupledModel parentModel)
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
    public final DEVSSimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /**
     * @param simulator the simulator to use from now on
     */
    public final void setSimulator(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        this.simulator = simulator;
    }

    /**
     * @return the parent model we are part of.
     */
    public final CoupledModel getParentModel()
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

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.fullName;
    }

    /**
     * Print the model, preceded by a user provided string.
     * @param header the user provided string to print in front of the model (e.g. newlines, header).
     */
    public abstract void printModel(String header);

    // ///////////////////////////////////////////////////////////////////////////
    // STATE CHANGE UPDATES, STATE SAVES
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Create state field set. For this first version, take all the fields into account as state variables. The method
     * substract the fields that are on the level of AbstractModel or Atomic Model or higher; only leave the non-static
     * fields that are part of the descendents of the abstract models.
     */
    private void createStateFieldSet()
    {
        Set<Field> fieldSet = ClassUtilV2.getAllFields(this.getClass());

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
     * delta_internal or delta_external method. More intelligence can be added here later. For simple types, a
     * comparison with the old value (state map) is possible. For complex variables (objects) this is more difficult as
     * a deep clone should be saved as old state, followed by a full comparison. This does not seem practical, and more
     * expensive than firing the state change of all state variables. The intelligence to detect real state changes then
     * has to be built in at the receiver's side.
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void fireUpdatedState()
    {
        for (Field field : this.stateFieldSet)
        {
            try
            {
                field.setAccessible(true);
                StateUpdate stateUpdate = new StateUpdate(this.getModelName(), field.getName(), field.get(this));
                this.fireEvent(AbstractDEVSModel.STATE_UPDATE, stateUpdate);
            }
            catch (IllegalAccessException exception)
            {
                logger.error("Tried to fire update for variable " + field.getName() + " but got an exception.");
                System.err.println(this.getModelName() + " - fireUpdateState: Tried to fire update for variable "
                        + field.getName() + " but got an exception.");
            }
        }
    }

    /**
     * StateUpdate class. Reports a state update. Right now, it is a modelname - variable name - value tuple.
     * <p>
     * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
     * reserved.
     * <p>
     * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
     * <p>
     * The DSOL project is distributed under the following BSD-style license:<br>
     * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
     * following conditions are met:
     * <ul>
     * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the
     * following disclaimer.</li>
     * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
     * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
     * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse
     * or promote products derived from this software without specific prior written permission.</li>
     * </ul>
     * This software is provided by the copyright holders and contributors "as is" and any express or implied
     * warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular
     * purpose are disclaimed. In no event shall the copyright holder or contributors be liable for any direct,
     * indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of
     * substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any
     * theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising
     * in any way out of the use of this software, even if advised of the possibility of such damage.
     * @version Oct 17, 2009 <br>
     * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
     * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
     */
    public class StateUpdate implements Serializable
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
         * @param modelName the name of the model
         * @param variableName the name of the variable
         * @param value the value
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

    // TODO public static class TimeDouble
}
