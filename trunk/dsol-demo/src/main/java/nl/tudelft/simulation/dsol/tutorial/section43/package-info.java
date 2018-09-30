/**
 * Lotka-Volterra System.
 * <p>
 * The following example is taken from to introduce a very commonly discussed continuous problem: the predator-prey
 * population interaction. In the 1920s and 1930s, Vito Volterra and Alfred Lotka independently reduced Darwin's
 * predator-prey interactions to mathematical models.
 * </p>
 * <p>
 * This section presents a model of predator and prey where association includes only natural growth or decay and the
 * preadator-prey interaction itself. All other relationships are considered to be negligible. We will assume that the
 * prey population grows exponentially in the absense of predation, while the predator population declines exponentially
 * if the prey population is extinct. The predator-prey interaction is modeled by mass action terms proportional to the
 * product of the two populations. The model is named the <i>Lotka-Volterra</i> system.
 * </p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank"> DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Oct 10, 2016
 */
package nl.tudelft.simulation.dsol.tutorial.section43;