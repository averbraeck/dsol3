/**
 * Simulation time formats, with and without units.
 * <p>
 * Each of the time classes supports basic operations on time, such as adding, subtracting, comparing, etc.. SimTime is
 * parameterized by three parameters:<br>
 * &lt;A&gt; the absolute storage type, e.g. Calendar for the absolute time to ensure type safety. This is the <i>absolute</i>
 * number, e.g., a Calendar for a simulation time. For simple calendar types such as a double for simulation time, the internal
 * (relative) and external (absolute) storage types are the same. <br>
 * &lt;R&gt; the relative number type, e.g. Double for the internal storage type to ensure type safety. This is the
 * <i>relative</i> number, so in case of a Calendar for a simulation time, the relative storage type is a relative time with a
 * unit.<br>
 * &lt;T&gt; the extended type itself to be able to implement a comparator, and to ease the use of extension return types.
 * </p>
 * The following SimTime types are available:
 * <ul>
 * <li>SimTime: the abstract time from which the other classes are extended</li>
 * <li>SimTimeDouble: a simulation time based on a double</li>
 * <li>SimTimeDoubleUnit: a simulation time based on a double, with a time unit</li>
 * <li>SimTimeFloat: a simulation time based on a float</li>
 * <li>SimTimeFloatUnit: a simulation time based on a float, with a time unit</li>
 * <li>SimTimeLong: a simulation time based on a long</li>
 * </ul>
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
package nl.tudelft.simulation.dsol.simtime;
