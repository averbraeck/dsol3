/**
 * This section reveals the DSOL implementation of the single server queuing system. We consider a system consisting of
 * a single server which receives customers arriving independently and identically distributed (IDD). A customer who
 * arrives and finds the server idle is being serviced immediately. A customer who finds the server busy enters a single
 * queue. Upon completing a service for a customer, the server checks the queue and (if any) services the next customer
 * in a first-in, first-out (FIFO) manner.
 * <p>
 * The simulation begins in an empty-and-idle state. We simulate until a predefined fixed number of customers <i>n</i>
 * have entered the system and completed their service. To measure the performance of this system, we focus on a number
 * of output variables. First of all we focus on the expected delay <i>d(n)</i> of a customer in the queue. From a
 * system perspective we furthermore focus on the number of customers in queue <i>q(n)</i>. The final output variable we
 * consider is the expected utilization of the server <i>u(n)</i>. This is the proportion of the time the server was in
 * its busy state. Since the simulation is dependent on random variable observations for both the inter-arrival time and
 * the service time, the output variables <i>d(n), q(n)</i> and <i>u(n)</i> will be random and, therefore, expected to
 * be variable.
 * </p>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Oct 10, 2016
 */
package nl.tudelft.simulation.dsol.tutorial.section41;