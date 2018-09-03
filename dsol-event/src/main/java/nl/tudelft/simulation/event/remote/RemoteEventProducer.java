package nl.tudelft.simulation.event.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nl.tudelft.simulation.event.EventProducer;

/**
 * The RemoteEventProducer provides a remote implementation of the eventProducer.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/stijnpietervanhouten">Stijn-Pieter van Houten</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RemoteEventProducer extends EventProducer implements RemoteEventProducerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /**
     * Constructs a new RemoteEventProducer.
     * @throws RemoteException in case of network error
     */
    public RemoteEventProducer() throws RemoteException
    {
        super();
        UnicastRemoteObject.exportObject(this);
    }
}
