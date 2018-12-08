/**
 * Provides classes and interfaces for asynchronous communication. The event package is designed around three
 * interfaces:
 * <ul>
 * <li>The EventListenerInterface defining callback mechanism for a listener.</li>
 * <li>The EventProducerInterface defining registration capabilities.</li>
 * <li>The EventInterface defining the events which are sent by a producer and received by a listener.</li>
 * </ul>
 * In this package, two aspects are of crucial importance. Operations defined in the interfaces are prepared for
 * distribution. The event.remote package extends all listeners and producers to throw the <code>RemoteException</code>
 * on network problems. The second aspect is concurrency. The classes providing a reference implementation of these
 * interfaces are designed for multi-threaded deployment.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
package nl.tudelft.simulation.event;
