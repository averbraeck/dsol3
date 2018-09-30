/**
 * Contains a set of immutable collection interfaces and wrapper implementations. Two versions of immutable collections
 * are implemented:
 * <ol>
 * <li>A version, identified by Immutable.COPY, where the immutable collection can neither be changed by any object
 * "using" the ImmutableCollection nor anymore by objects that have a pointer to the collection, as an internal
 * (shallow) copy is made of the collection. This is the <b>default</b> implementation.</li>
 * <li>A version, identified by Immutable.WRAP, where the immutable collection can not be changed by any object "using"
 * the ImmutableCollection, but it can still be changed by any object that has a pointer to the original collection that
 * is "wrapped". Instead of a (shallow) copy of the collection, a pointer to the collection is stored.</li>
 * </ol>
 * <p>
 * Copyright (c) 2016-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
package nl.tudelft.simulation.immutablecollections;
