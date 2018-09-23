package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortNotFoundException;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * AbstractDEVSPortModel class. Adds named ports to the abstract DEVS model.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
public abstract class AbstractDEVSPortModel extends AbstractDEVSModel
{
    /** the default serial version UId. */
    private static final long serialVersionUID = 1L;

    /** the map of input port names to input ports. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Map<String, InputPortInterface<?>> inputPortMap = new HashMap<String, InputPortInterface<?>>();

    /** the map of output port names to output ports. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Map<String, OutputPortInterface<?>> outputPortMap = new HashMap<String, OutputPortInterface<?>>();

    /**
     * Constructor for an abstract DEVS model with ports: we have to indicate the simulator to schedule the events on,
     * and the parent model we are part of. A parent model of null means that we are the top model.
     * @param modelName the name of this component
     * @param simulator the simulator to schedule the events on.
     * @param parentModel the parent model we are part of.
     */
    public AbstractDEVSPortModel(final String modelName, final DEVSSimulatorInterface.TimeDouble simulator,
            final CoupledModel parentModel)
    {
        super(modelName, simulator, parentModel);
    }

    /**
     * Add an input port to the model. Use a name to be able to identify the port later.
     * @param <T> the type of variable of the input port
     * @param name the (unique) name of the input port
     * @param inputPort the input port to add
     * @throws PortAlreadyDefinedException in case the port name already exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected <T> void addInputPort(final String name, final InputPortInterface<T> inputPort)
            throws PortAlreadyDefinedException
    {
        if (this.inputPortMap.containsKey(name))
        {
            throw new PortAlreadyDefinedException(
                    "Adding port " + name + " for model " + this.toString() + ", but it already exists.");
        }
        this.inputPortMap.put(name, inputPort);
    }

    /**
     * Add an output port to the model. Use a name to be able to identify the port later.
     * @param <T> the type of variable of the output port
     * @param name the (unique) name of the output port
     * @param outputPort the output port to add
     * @throws PortAlreadyDefinedException in case the port name already exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected <T> void addOutputPort(final String name, final OutputPortInterface<T> outputPort)
            throws PortAlreadyDefinedException
    {
        if (this.outputPortMap.containsKey(name))
        {
            throw new PortAlreadyDefinedException(
                    "Adding port " + name + " for model " + this.toString() + ", but it already exists.");
        }
        this.outputPortMap.put(name, outputPort);
    }

    /**
     * Remove an input port from the model. Note: override this method in classes that extend the behavior, e.g. to
     * remove couplings from this port in case it is removed.
     * @param name the name of the input port to be removed
     * @throws PortNotFoundException in case the port name does not exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void removeInputPort(final String name) throws PortNotFoundException
    {
        if (!this.inputPortMap.containsKey(name))
        {
            throw new PortNotFoundException(
                    "Removing port " + name + " for model " + this.toString() + ", but it does not exist.");
        }
        this.inputPortMap.remove(name);
    }

    /**
     * Remove an output port from the model. Note: override this method in classes that extend the behavior, e.g. to
     * remove couplings from this port in case it is removed.
     * @param name the name of the output port to be removed
     * @throws PortNotFoundException in case the port name does not exist for the model
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void removeOutputPort(final String name) throws PortNotFoundException
    {
        if (!this.outputPortMap.containsKey(name))
        {
            throw new PortNotFoundException(
                    "Removing port " + name + " for model " + this.toString() + ", but it does not exist.");
        }
        this.outputPortMap.remove(name);
    }

    /**
     * @return inputPortMap; the map of input port names to input ports.
     */
    public final Map<String, InputPortInterface<?>> getInputPortMap()
    {
        return this.inputPortMap;
    }

    /**
     * @return outputPortMap; the map of output port names to output ports
     */
    public final Map<String, OutputPortInterface<?>> getOutputPortMap()
    {
        return this.outputPortMap;
    }
}
