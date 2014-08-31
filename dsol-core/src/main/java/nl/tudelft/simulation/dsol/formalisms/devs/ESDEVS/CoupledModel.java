package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortNotFoundException;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.ref.Reference;

/**
 * CoupledModel class. This class implements the classic parallel DEVS coupled model with ports conform Zeigler et al.
 * (2000), section 4.3.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
public abstract class CoupledModel extends AbstractDEVSPortModel
{
    /** the default serialVersionUId. */
    private static final long serialVersionUID = 1L;

    /** the internal couplings (from internal models to internal models) */
    protected Set<IC<?>> internalCouplingSet = new HashSet<IC<?>>();

    /**
     * the couplings from the internal models to the output of this coupled model
     */
    protected Set<EOC<?>> externalOutputCouplingSet = new HashSet<EOC<?>>();

    /**
     * the couplings from the outside world to the internal models of this coupled model
     */
    protected Set<EIC<?>> externalInputCouplingSet = new HashSet<EIC<?>>();

    /** the models within this coupled model. */
    protected Set<AbstractDEVSModel> modelComponents = new HashSet<AbstractDEVSModel>();

    // ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS AND INITIALIZATION
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * The constructor of the top model when the simulator is still unknown (e.g. in the constructModel() method).
     * @param modelName the name of this component
     */
    public CoupledModel(final String modelName)
    {
        super(modelName, null, null);
    }

    /**
     * The constructor of a coupled model within another coupled model.
     * @param modelName the name of this component
     * @param parentModel the parent coupled model for this model.
     */
    public CoupledModel(final String modelName, final CoupledModel parentModel)
    {
        super(modelName, parentModel.getSimulator(), parentModel);
        if (this.parentModel != null)
        {
            this.parentModel.addModelComponent(this);
        }
    }

    /**
     * Constructor of a high-level coupled model without a parent model.
     * @param modelName the name of this component
     * @param simulator the simulator to schedule events on.
     */
    public CoupledModel(final String modelName, final DEVSSimulatorInterface.Double simulator)
    {
        super(modelName, simulator, null);

    }

    /**
     * Add a listener recursively to the model and all its submodels. Delegate it for this coupled model to the embedded
     * event producer.
     * @param eli the event listener.
     * @param et the event type.
     * @return success or failure of adding the listener to all submodels.
     */
    public boolean addHierarchicalListener(final EventListenerInterface eli, final EventType et)
    {
        boolean returnBoolean = true;
        returnBoolean &= super.addListener(eli, et);

        for (AbstractDEVSModel devsmodel : this.modelComponents)
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
     * @param <T> the type of message / event being transferred
     * @param x the output port through which the transfer takes place
     * @param y the value being transferred
     * @throws RemoteException remote exception
     * @throws SimRuntimeException simulation run time exception
     */
    @SuppressWarnings("unchecked")
    protected <T> void transfer(final OutputPortInterface<T> x, final T y) throws RemoteException, SimRuntimeException
    {
        for (IC<?> o : this.internalCouplingSet)
        {
            if (o.getFromPort() == x)
            {
                ((IC<T>) o).getToPort().receive(y, this.simulator.getSimulatorTime().get());
            }
        }
        for (EOC<?> o : this.externalOutputCouplingSet)
        {
            if (o.getFromPort() == x)
            {
                ((EOC<T>) o).getToPort().send(y);
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // COUPLING: MAKING AND REMOVING IC, EOC, EIC COUPLINGS
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * @param <T> the type of message / event for which the coupling is added.
     * @param fromPort the output port of an internal component that transfers the message / event to another internal
     *            component (start of the coupling)
     * @param toPort the input port of an internal component that receives a message / event from the other componet
     *            (end of the coupling)
     */
    public <T> void addInternalCoupling(final OutputPortInterface<T> fromPort, final InputPortInterface<T> toPort)
    {
        try
        {
            this.internalCouplingSet.add(new IC<T>(fromPort, toPort));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * @param <T> the type of message / event for which the coupling is removed.
     * @param fromPort the output port of an internal component that transfers the message / event to another internal
     *            component (start of the coupling)
     * @param toPort the input port of an internal component that receives a message / event from the other componet
     *            (end of the coupling)
     */
    public <T> void removeInternalCoupling(final OutputPortInterface<T> fromPort, final InputPortInterface<T> toPort)
    {
        for (IC<?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort().getModel() == fromPort && ic.getToPort().getModel() == toPort)
            {
                this.externalInputCouplingSet.remove(ic);
            }
        }

    }

    /**
     * Add an IOC within this coupled model.
     * @param <T> the type of message / event for which the coupling is added.
     * @param fromPort the input port of this coupled model that transfers the message / event to the internal component
     *            (start of the coupling)
     * @param toPort the input port of the internal component that receives a message / event from the overarching
     *            coupled model (end of the coupling)
     */
    public <T> void addExternalInputCoupling(final InputPortInterface<T> fromPort, final InputPortInterface<T> toPort)
    {
        try
        {
            this.externalInputCouplingSet.add(new EIC<T>(fromPort, toPort));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Remove an IOC within this coupled model.
     * @param <T> the type of message / event for which the coupling is removed.
     * @param fromPort the input port of this coupled model that transfers the message / event to the internal component
     *            (start of the coupling)
     * @param toPort the input port of the internal component that receives a message / event from the overarching
     *            coupled model (end of the coupling)
     */
    public <T> void removeExternalInputCoupling(final InputPortInterface<T> fromPort, final InputPortInterface<T> toPort)
    {
        for (EIC<?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort() == fromPort && eic.getToPort() == toPort)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }
    }

    /**
     * Add an EOC within this coupled model.
     * @param <T> the type of message / event for which the coupling is added.
     * @param fromPort the output port of the internal component that produces an event for the outside of the
     *            overarching coupled model (start of the coupling)
     * @param toPort the output port of this coupled model that transfers the message / event to the outside (end of the
     *            coupling)
     */
    public <T> void addExternalOutputCoupling(final OutputPortInterface<T> fromPort, final OutputPortInterface<T> toPort)
    {
        try
        {
            this.externalOutputCouplingSet.add(new EOC<T>(fromPort, toPort));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Remove an EOC within this coupled model.
     * @param <T> the type of message / event for which the coupling is removed.
     * @param fromPort the output port of the internal component that produces an event for the outside of the
     *            overarching coupled model (start of the coupling)
     * @param toPort the output port of this coupled model that transfers the message / event to the outside (end of the
     *            coupling)
     */
    public <T> void removeExternalOutputCoupling(final OutputPortInterface<T> fromPort,
            final OutputPortInterface<T> toPort)
    {
        for (EOC<?> eoc : this.externalOutputCouplingSet)
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
     * @param model the component to add.
     */
    public void addModelComponent(final AbstractDEVSModel model)
    {
        this.modelComponents.add(model);

        List<Reference<EventListenerInterface>> elis = this.listeners.get(AbstractDEVSModel.STATE_UPDATE);

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
     * Remove a model component from a coupled model, including all its couplings (internal, external in, and external
     * out).
     * @param model the component to remove.
     */
    public void removeModelComponent(final AbstractDEVSModel model)
    {
        for (EOC<?> eoc : this.externalOutputCouplingSet)
        {
            if (eoc.getFromPort().getModel() == model || eoc.getToPort().getModel() == model)
            {
                this.externalOutputCouplingSet.remove(eoc);
            }
        }

        for (EIC<?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort().getModel() == model || eic.getToPort().getModel() == model)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }

        for (IC<?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort().getModel() == model || ic.getToPort().getModel() == model)
            {
                this.externalInputCouplingSet.remove(ic);
            }
        }

        // this will also take care of the removal of the ports as they are not
        // connected to anything anymore.

        this.modelComponents.remove(model);
    }

    @Override
    protected void removeInputPort(final String name) throws PortNotFoundException
    {
        InputPortInterface<?> inputPort = this.inputPortMap.get(name);
        super.removeInputPort(name); // throws exception in case nonexistent

        for (EIC<?> eic : this.externalInputCouplingSet)
        {
            if (eic.getFromPort() == inputPort || eic.getToPort() == inputPort)
            {
                this.externalInputCouplingSet.remove(eic);
            }
        }

        for (IC<?> ic : this.internalCouplingSet)
        {
            if (ic.getToPort() == inputPort)
            {
                this.externalInputCouplingSet.remove(ic);
            }
        }
    }

    @Override
    protected void removeOutputPort(final String name) throws PortNotFoundException
    {
        OutputPortInterface<?> outputPort = this.outputPortMap.get(name);
        super.removeOutputPort(name); // throws exception in case nonexistent

        for (EOC<?> eoc : this.externalOutputCouplingSet)
        {
            if (eoc.getFromPort() == outputPort || eoc.getToPort() == outputPort)
            {
                this.externalOutputCouplingSet.remove(eoc);
            }
        }

        for (IC<?> ic : this.internalCouplingSet)
        {
            if (ic.getFromPort() == outputPort)
            {
                this.externalInputCouplingSet.remove(ic);
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // PRINTING THE MODEL
    // ///////////////////////////////////////////////////////////////////////////

    @Override
    public void printModel(final String space)
    {
        System.out.println(space + "================");
        System.out.println(space + "coupled model name: " + this.getClass().getName());
        System.out.println(space + "Externaloutputcouplings");
        for (EOC<?> eoc : this.externalOutputCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(eoc.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(eoc.getToPort().getModel().getClass().getName());
            System.out.println();
        }
        System.out.println(space + "Externalinputcouplings");
        for (EIC<?> eic : this.externalInputCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(eic.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(eic.getToPort().getModel().getClass().getName());
            System.out.println();
        }
        System.out.println(space + "Externaloutputcouplings");
        for (IC<?> ic : this.internalCouplingSet)
        {
            System.out.print(space);
            System.out.print("between ");
            System.out.print(ic.getFromPort().getModel().getClass().getName());
            System.out.print(" and ");
            System.out.print(ic.getToPort().getModel().getClass().getName());
            System.out.println();
        }

        for (AbstractDEVSModel dm : this.modelComponents)
        {
            dm.printModel(space + "    ");
        }
        System.out.println(space + "================");
    }

}
