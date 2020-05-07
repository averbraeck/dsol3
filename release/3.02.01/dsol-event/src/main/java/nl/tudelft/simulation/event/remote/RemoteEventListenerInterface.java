package nl.tudelft.simulation.event.remote;

import java.rmi.Remote;

import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The RemoteEventListenerInterface provides a remote implementation of the EventListenerInterface.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/stijnpietervanhouten">Stijn-Pieter van Houten</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface RemoteEventListenerInterface extends EventListenerInterface, Remote
{
    // nothing here
}