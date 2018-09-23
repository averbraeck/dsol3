package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.language.support.DoubleCompare;
import nl.tudelft.simulation.logger.Cat;

/**
 * InputPort class. The input port can function as an input port for a Parallel DEVS Atomic Model as well as for a
 * Parallel Hierarchical DEVS Coupled Model. A boolean in the class indicates whether it behaves as a port for an atomic
 * model or a coupled model. For a coupled model, the input message is passed on to the external input couplings (EIC),
 * for an atomic model, the external event handler is called (or the confluent event handler in case of a conflict).
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
 * @param <T> The type of messages the input port accepts.
 */
// TODO public class InputPort<A, R, T, TYPE> implements InputPortInterface<T>
public class InputPort<T> implements InputPortInterface<T>
{
    /** The model to which the port links. */
    private AbstractDEVSModel model;

    /** Is the model atomic or not? */
    private boolean atomic;

    /**
     * Constructor for the input port where the model is a coupled model.
     * @param coupledModel the coupled model to which the port is added.
     */
    public InputPort(final CoupledModel coupledModel)
    {
        this.model = coupledModel;
        this.atomic = false;
    }

    /**
     * Constructor for the input port where the model is an atomic model.
     * @param atomicModel the atomic model to which the port is added.
     */
    public InputPort(final AtomicModel atomicModel)
    {
        this.model = atomicModel;
        this.atomic = true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final synchronized void receive(final T value, final double time) throws RemoteException, SimRuntimeException
    {
        if (this.atomic)
        {
            // ATOMIC MODEL
            AtomicModel atomicModel = (AtomicModel) this.model;
            while (atomicModel.activePort != null)
            {
                SimLogger.filter(Cat.DSOL)
                        .trace("receive: Waiting for event treatement // Another input is being processed");
                try
                {
                    Thread.sleep(1); // added because of infinite loop
                }
                catch (InterruptedException exception)
                {
                    // do nothing -- just wait till activePort != null
                }
            }

            if (atomicModel.activePort == null)
            {
                atomicModel.activePort = this;
                boolean passivity = true;
                SimEvent<SimTimeDouble> nextEventCopy = null;
                SimLogger.filter(Cat.DSOL).debug("receive: TIME IS {}",
                        this.model.getSimulator().getSimulatorTime());

                // Original: if (elapsedTime(time) - 0.000001 > timeAdvance())
                int etminta = DoubleCompare.compare(atomicModel.elapsedTime(time), atomicModel.timeAdvance());
                if (etminta == 1)
                {
                    SimLogger.always().error("receive: {} - {}", atomicModel.elapsedTime(time),
                            atomicModel.timeAdvance());
                    SimLogger.always().error("receive - IMPOSSIBLE !!! TIME SYNCHRONIZATION PROBLEM {}",
                            atomicModel.toString());
                    System.err.println("IMPOSSIBLE !!! TIME SYNCHRONIZATION PROBLEM " + atomicModel.toString());
                }
                else
                {
                    if (etminta == 0 && atomicModel.elapsedTime(time) > 0) // 22-10-2009
                    {
                        atomicModel.setConflict(true);
                        passivity = false;
                        nextEventCopy = atomicModel.getNextEvent();
                    }
                    else
                    {
                        atomicModel.setConflict(false);
                        if (atomicModel.timeAdvance() != Double.POSITIVE_INFINITY)
                        {
                            passivity = false;
                            nextEventCopy = atomicModel.getNextEvent();
                        }
                        else
                        {
                            passivity = true;
                        }
                    }
                }
                if (atomicModel.isConflict())
                {
                    atomicModel.deltaConfluent(
                            this.model.getSimulator().getSimulatorTime() - atomicModel.getTimeLastEvent(), value);
                }
                else
                {
                    atomicModel.deltaExternalEventHandler(
                            this.model.getSimulator().getSimulatorTime() - atomicModel.getTimeLastEvent(), value);
                }
                if (!passivity)
                {
                    this.model.getSimulator().cancelEvent(nextEventCopy);
                }
            }
            atomicModel.activePort = null;
        }

        else

        {
            // COUPLED MODEL
            CoupledModel coupledModel = (CoupledModel) this.model;
            for (EIC<?> o : coupledModel.externalInputCouplingSet)
            {
                if (o.getFromPort() == this)
                {
                    try
                    {
                        ((EIC<T>) o).getToPort().receive(value, time);
                    }
                    catch (SimRuntimeException e)
                    {
                        SimLogger.always().error(e);
                    }
                }
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final AbstractDEVSModel getModel()
    {
        return this.model;
    }
}
