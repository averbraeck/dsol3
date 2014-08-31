package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.logger.Logger;

/**
 * OutputPort class. The output port transfers the event (message) to the next receiver. In case there is no next
 * receiver (e.g. in case of the model being the highest coupled model in the simulation, the event is currently not
 * transferred.
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
 * @param <T> The type of messages the port produces.
 */
public class OutputPort<T> implements OutputPortInterface<T>
{
    /** The model to which the port links. */
    private AbstractDEVSModel model;

    /**
     * Constructor for the output port where the model is a coupled model,
     * @param coupledModel the coupled model.
     */
    public OutputPort(final CoupledModel coupledModel)
    {
        this.model = coupledModel;
    }

    /**
     * Constructor for the output port where the model is an atomic model,
     * @param atomicModel the atomic model.
     */
    public OutputPort(final AtomicModel atomicModel)
    {
        this.model = atomicModel;
    }

    /**
     * {@inheritDoc}
     */
    public void send(final T value)
    {
        if (this.model.parentModel != null)
        {
            try
            {
                Logger.fine(this, "send", "TIME IS " + this.model.getSimulator().getSimulatorTime());
                this.model.parentModel.transfer(this, value);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            catch (SimRuntimeException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    public AbstractDEVSModel getModel()
    {
        return this.model;
    }
}
