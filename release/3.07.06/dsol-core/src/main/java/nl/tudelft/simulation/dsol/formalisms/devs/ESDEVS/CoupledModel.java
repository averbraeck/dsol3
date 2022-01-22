package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.djutils.event.ref.Reference;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortNotFoundException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * CoupledModel class. This class implements the classic parallel DEVS coupled model with ports conform Zeigler et al. (2000),
 * section 4.3.
 * <p>
 * Copyright (c) 2009-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
public abstract class CoupledModel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends AbstractDEVSPortModel<A, R, T>
{
    /** the default serialVersionUId. */
    private static final long serialVersionUID = 1L;

    /** the internal couplings (from internal models to internal models). */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<IC<A, R, T, ?>> internalCouplingSet = new LinkedHashSet<IC<A, R, T, ?>>();

    /** the couplings from the internal models to the output of this coupled model. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<EOC<A, R, T, ?>> externalOutputCouplingSet = new LinkedHashSet<EOC<A, R, T, ?>>();

    /** the couplings from the outside world to the internal models of this coupled model. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<EIC<A, R, T, ?>> externalInputCouplingSet = new LinkedHashSet<EIC<A, R, T, ?>>();

    /** the models within this coupled model. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Set<AbstractDEVSModel<A, R, T>> modelComponents = new LinkedHashSet<>();

    // ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS AND INITIALIZATION
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
     * @param modelName String; the name of this component
     */
    public CoupledModel(final String modelName)
    {
        super(modelName, null, null);
    }

    /**
     * The constructor of a coupled model within another coupled model.
     * @param modelName String; the name of this component
     * @param parentModel CoupledModel&lt;A,R,T&gt;; the parent coupled model for this model.
     */
    public CoupledModel(final String modelName, final CoupledModel<A, R, T> parentModel)
    {
        super(modelName, parentModel.getSimulator(), parentModel);
        if (this.parentModel != null)
        {
            this.parentModel.addModelComponent(this);
        }
    }

    /**
     * Constructor of a high-level coupled model without a parent model.
     * @param modelName String; the name of this component
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; the simulator to schedule events on.
     */
    public CoupledModel(final String modelName, final DEVSSimulatorInterface<A, R, T> simulator)
    {
        super(modelName, simulator, null);

    }

    /**
     * Add a listener recursively to the model and all its submodels. Delegate it for this coupled model to the embedded event
     * producer.
     * @param eli EventListenerInterface; the event listener.
     * @param et EventType; the event type.
     * @return success or failure of adding the listener to all submodels.
     */
    public boolean addHierarchicalListener(final EventListenerInterface eli, final EventType et)
    {
        boolean returnBoolean = true;
        returnBoolean &= super.addListener(eli, et);

        for (AbstractDEVSModel<A, R, T> devsmodel : this.modelComponents)
        {
            returnBoolean &= devsmodel.addListener(eli, et);
        }

        return returnBoolean;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // TRANSFER FUNCTIONS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * The transfer function takes care of transferring a value from this coupled model to the outside world.
     * @param <TYPE> the type of message / event being transferred
     * @param x OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port through which the transfer takes place
     * @param y TYPE; the value being transferred
     * @throws RemoteException remote exception
     * @throws SimRuntimeException simulation run time exception
     */
    @SuppressWarnings("unchecked")
    public <TYPE> void transfer(final OutputPortInterface<A, R, T, TYPE> x, final TYPE y)
            throws RemoteException, SimRuntimeException
    {
        for (IC<A, R, T, ?> o : this.internalCouplingSet)
        {
            if (o.getFromPort() == x)
            {
                ((IC<A, R, T, TYPE>) o).getToPort().receive(y, this.simulator.getSimTime());
            }
        }
        for (EOC<A, R, T, ?> o : this.externalOutputCouplingSet)
        {
            if (o.getFromPort() == x)
            {
                ((EOC<A, R, T, TYPE>) o).getToPort().send(y);
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // COUPLING: MAKING AND REMOVING IC, EOC, EIC COUPLINGS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * @param <TYPE> the type of message / event for which the coupling is added.
     * @param fromPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of an internal component that transfers the
     *            message / event to another internal component (start of the coupling)
     * @param toPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of an internal component that receives a message /
     *            event from the other componet (end of the coupling)
     */
    public <TYPE> void addInternalCoupling(final OutputPortInterface<A, R, T, TYPE> fromPort,
            final InputPortInterface<A, R, T, TYPE> toPort)
    {
        try
        {
            this.internalCouplingSet.add(new IC<A, R, T, TYPE>(fromPort, toPort));
        }
        catch (Exception e)
        {
            this.simulator.getLogger().always().error(e);
        }

    }

    /**
     * @param <TYPE> the type of message / event for which the coupling is removed.
     * @param fromPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of an internal component that transfers the
     *            message / event to another internal component (start of the coupling)
     * @param toPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of an internal component that receives a message /
     *            event from the other componet (end of the coupling)
     */
    public <TYPE> void removeInternalCoupling(final OutputPortInterface<A, R, T, TYPE> fromPort,
            final InputPortInterface<A, R, T, TYPE> toPort)
    {
        for (IC<A, R, T, ?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort().getModel() == fromPort && ic.getToPort().getModel() == toPort)
            {
                this.internalCouplingSet.remove(ic);
            }
        }

    }

    /**
     * Add an IOC within this coupled model.
     * @param <TYPE> the type of message / event for which the coupling is added.
     * @param fromPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of this coupled model that transfers the message /
     *            event to the internal component (start of the coupling)
     * @param toPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of the internal component that receives a message /
     *            event from the overarching coupled model (end of the coupling)
     */
    public <TYPE> void addExternalInputCoupling(final InputPortInterface<A, R, T, TYPE> fromPort,
            final InputPortInterface<A, R, T, TYPE> toPort)
    {
        try
        {
            this.externalInputCouplingSet.add(new EIC<A, R, T, TYPE>(fromPort, toPort));
        }
        catch (Exception e)
        {
            this.simulator.getLogger().always().error(e);
        }
    }

    /**
     * Remove an IOC within this coupled model.
     * @param <TYPE> the type of message / event for which the coupling is removed.
     * @param fromPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of this coupled model that transfers the message /
     *            event to the internal component (start of the coupling)
     * @param toPort InputPortInterface&lt;A,R,T,TYPE&gt;; the input port of the internal component that receives a message /
     *            event from the overarching coupled model (end of the coupling)
     */
    public <TYPE> void removeExternalInputCoupling(final InputPortInterface<A, R, T, TYPE> fromPort,
            final InputPortInterface<A, R, T, TYPE> toPort)
    {
        for (EIC<A, R, T, ?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort() == fromPort && eic.getToPort() == toPort)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }
    }

    /**
     * Add an EOC within this coupled model.
     * @param <TYPE> the type of message / event for which the coupling is added.
     * @param fromPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of the internal component that produces an event
     *            for the outside of the overarching coupled model (start of the coupling)
     * @param toPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of this coupled model that transfers the message /
     *            event to the outside (end of the coupling)
     */
    public <TYPE> void addExternalOutputCoupling(final OutputPortInterface<A, R, T, TYPE> fromPort,
            final OutputPortInterface<A, R, T, TYPE> toPort)
    {
        try
        {
            this.externalOutputCouplingSet.add(new EOC<A, R, T, TYPE>(fromPort, toPort));
        }
        catch (Exception e)
        {
            this.simulator.getLogger().always().error(e);
        }
    }

    /**
     * Remove an EOC within this coupled model.
     * @param <TYPE> the type of message / event for which the coupling is removed.
     * @param fromPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of the internal component that produces an event
     *            for the outside of the overarching coupled model (start of the coupling)
     * @param toPort OutputPortInterface&lt;A,R,T,TYPE&gt;; the output port of this coupled model that transfers the message /
     *            event to the outside (end of the coupling)
     */
    public <TYPE> void removeExternalOutputCoupling(final OutputPortInterface<A, R, T, TYPE> fromPort,
            final OutputPortInterface<A, R, T, TYPE> toPort)
    {
        for (EOC<A, R, T, ?> eoc : this.externalOutputCouplingSet)
        {
            if (eoc.getFromPort() == fromPort && eoc.getToPort() == toPort)
            {
                this.externalOutputCouplingSet.remove(eoc);
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // STRUCTURE: ADDING AND REMOVING COMPONENTS AND PORTS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Add a model component to this coupled model.
     * @param model AbstractDEVSModel&lt;A,R,T&gt;; the component to add.
     */
    public void addModelComponent(final AbstractDEVSModel<A, R, T> model)
    {
        this.modelComponents.add(model);

        List<Reference<EventListenerInterface>> elis = getListenerReferences(AbstractDEVSModel.STATE_UPDATE);

        if (elis == null)
        {
            return;
        }

        for (Reference<EventListenerInterface> eli : elis)
        {
            model.addListener(eli.get(), AbstractDEVSModel.STATE_UPDATE);
        }
    }

    /**
     * Remove a model component from a coupled model, including all its couplings (internal, external in, and external out).
     * @param model AbstractDEVSModel&lt;A,R,T&gt;; the component to remove.
     */
    public void removeModelComponent(final AbstractDEVSModel<A, R, T> model)
    {
        for (EOC<A, R, T, ?> eoc : this.externalOutputCouplingSet)
        {
            if (eoc.getFromPort().getModel() == model || eoc.getToPort().getModel() == model)
            {
                this.externalOutputCouplingSet.remove(eoc);
            }
        }

        for (EIC<A, R, T, ?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort().getModel() == model || eic.getToPort().getModel() == model)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }

        for (IC<A, R, T, ?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort().getModel() == model || ic.getToPort().getModel() == model)
            {
                this.internalCouplingSet.remove(ic);
            }
        }

        // this will also take care of the removal of the ports as they are not
        // connected to anything anymore.

        this.modelComponents.remove(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeInputPort(final String name) throws PortNotFoundException
    {
        InputPortInterface<A, R, T, ?> inputPort = this.inputPortMap.get(name);
        super.removeInputPort(name); // throws exception in case nonexistent

        for (EIC<A, R, T, ?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort() == inputPort || eic.getToPort() == inputPort)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }

        for (IC<A, R, T, ?> ic : this.internalCouplingSet)
        {
            if (ic.getToPort() == inputPort)
            {
                this.internalCouplingSet.remove(ic);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOutputPort(final String name) throws PortNotFoundException
    {
        OutputPortInterface<A, R, T, ?> outputPort = this.outputPortMap.get(name);
        super.removeOutputPort(name); // throws exception in case nonexistent

        for (EOC<A, R, T, ?> eoc : this.externalOutputCouplingSet)
        {
            if (eoc.getFromPort() == outputPort || eoc.getToPort() == outputPort)
            {
                this.externalOutputCouplingSet.remove(eoc);
            }
        }

        for (IC<A, R, T, ?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort() == outputPort)
            {
                this.internalCouplingSet.remove(ic);
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // GETTERS FOR THE STRUCTURE
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * @return internalCouplingSet; the internal couplings (from internal models to internal models)
     */
    public Set<IC<A, R, T, ?>> getInternalCouplingSet()
    {
        return this.internalCouplingSet;
    }

    /**
     * @return externalOutputCouplingSet; the couplings from the internal models to the output of this coupled model
     */
    public Set<EOC<A, R, T, ?>> getExternalOutputCouplingSet()
    {
        return this.externalOutputCouplingSet;
    }

    /**
     * @return externalInputCouplingSet; the couplings from the outside world to the internal models of this coupled model
     */
    public Set<EIC<A, R, T, ?>> getExternalInputCouplingSet()
    {
        return this.externalInputCouplingSet;
    }

    /**
     * @return modelComponents; the models within the coupled model
     */
    public Set<AbstractDEVSModel<A, R, T>> getModelComponents()
    {
        return this.modelComponents;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // PRINTING THE MODEL
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void printModel(final String space)
    {
        System.out.println(space + "================");
        System.out.println(space + "coupled model name: " + this.getClass().getName());
        System.out.println(space + "Externaloutputcouplings");
        for (EOC<A, R, T, ?> eoc : this.externalOutputCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(eoc.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(eoc.getToPort().getModel().getClass().getName());
            System.out.println();
        }
        System.out.println(space + "Externalinputcouplings");
        for (EIC<A, R, T, ?> eic : this.externalInputCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(eic.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(eic.getToPort().getModel().getClass().getName());
            System.out.println();
        }
        System.out.println(space + "Externaloutputcouplings");
        for (IC<A, R, T, ?> ic : this.internalCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(ic.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(ic.getToPort().getModel().getClass().getName());
            System.out.println();
        }

        for (AbstractDEVSModel<A, R, T> dm : this.modelComponents)
        {
            dm.printModel(space + "    ");
        }
        System.out.println(space + "================");
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class AbstractDEVSModel.TimeDouble. */
    public abstract static class TimeDouble extends CoupledModel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
         * @param modelName String; the name of this component
         */
        public TimeDouble(final String modelName)
        {
            super(modelName);
        }

        /**
         * The constructor of a coupled model within another coupled model.
         * @param modelName String; the name of this component
         * @param parentModel CoupledModel.TimeDouble; the parent coupled model for this model.
         */
        public TimeDouble(final String modelName, final CoupledModel.TimeDouble parentModel)
        {
            super(modelName, parentModel);
        }

        /**
         * Constructor of a high-level coupled model without a parent model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator to schedule events on.
         */
        public TimeDouble(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(modelName, simulator);

        }
    }

    /** Easy access class CoupledModel.TimeFloat. */
    public abstract static class TimeFloat extends CoupledModel<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
         * @param modelName String; the name of this component
         */
        public TimeFloat(final String modelName)
        {
            super(modelName);
        }

        /**
         * The constructor of a coupled model within another coupled model.
         * @param modelName String; the name of this component
         * @param parentModel CoupledModel.TimeFloat; the parent coupled model for this model.
         */
        public TimeFloat(final String modelName, final CoupledModel.TimeFloat parentModel)
        {
            super(modelName, parentModel);
        }

        /**
         * Constructor of a high-level coupled model without a parent model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeFloat; the simulator to schedule events on.
         */
        public TimeFloat(final String modelName, final DEVSSimulatorInterface.TimeFloat simulator)
        {
            super(modelName, simulator);

        }
    }

    /** Easy access class CoupledModel.TimeLong. */
    public abstract static class TimeLong extends CoupledModel<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
         * @param modelName String; the name of this component
         */
        public TimeLong(final String modelName)
        {
            super(modelName);
        }

        /**
         * The constructor of a coupled model within another coupled model.
         * @param modelName String; the name of this component
         * @param parentModel CoupledModel.TimeLong; the parent coupled model for this model.
         */
        public TimeLong(final String modelName, final CoupledModel.TimeLong parentModel)
        {
            super(modelName, parentModel);
        }

        /**
         * Constructor of a high-level coupled model without a parent model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeLong; the simulator to schedule events on.
         */
        public TimeLong(final String modelName, final DEVSSimulatorInterface.TimeLong simulator)
        {
            super(modelName, simulator);

        }
    }

    /** Easy access class CoupledModel.TimeDoubleUnit. */
    public abstract static class TimeDoubleUnit extends CoupledModel<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
         * @param modelName String; the name of this component
         */
        public TimeDoubleUnit(final String modelName)
        {
            super(modelName);
        }

        /**
         * The constructor of a coupled model within another coupled model.
         * @param modelName String; the name of this component
         * @param parentModel CoupledModel.TimeDoubleUnit; the parent coupled model for this model.
         */
        public TimeDoubleUnit(final String modelName, final CoupledModel.TimeDoubleUnit parentModel)
        {
            super(modelName, parentModel);
        }

        /**
         * Constructor of a high-level coupled model without a parent model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; the simulator to schedule events on.
         */
        public TimeDoubleUnit(final String modelName, final DEVSSimulatorInterface.TimeDoubleUnit simulator)
        {
            super(modelName, simulator);

        }
    }

    /** Easy access class CoupledModel.TimeFloatUnit. */
    public abstract static class TimeFloatUnit extends CoupledModel<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20180929L;

        /**
         * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
         * @param modelName String; the name of this component
         */
        public TimeFloatUnit(final String modelName)
        {
            super(modelName);
        }

        /**
         * The constructor of a coupled model within another coupled model.
         * @param modelName String; the name of this component
         * @param parentModel CoupledModel.TimeFloatUnit; the parent coupled model for this model.
         */
        public TimeFloatUnit(final String modelName, final CoupledModel.TimeFloatUnit parentModel)
        {
            super(modelName, parentModel);
        }

        /**
         * Constructor of a high-level coupled model without a parent model.
         * @param modelName String; the name of this component
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; the simulator to schedule events on.
         */
        public TimeFloatUnit(final String modelName, final DEVSSimulatorInterface.TimeFloatUnit simulator)
        {
            super(modelName, simulator);

        }
    }

}
