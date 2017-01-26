/**
 * Simulation time formats, with and without units.
 * <p>
 * Each of the time classes supports basic operations on time, such as adding, subtracting, comparing, etc.. SimTime is
 * parameterized by three parameters:<br>
 * &lt;A&gt; the absolute storage type, e.g. Calendar for the absolute time to ensure type safety. This is the
 * <i>absolute</i> number, e.g., a Calendar for a simulation time. For simple calendar types such as a double for
 * simulation time, the internal (relative) and external (absolute) storage types are the same. <br>
 * &lt;R&gt; the relative number type, e.g. Double for the internal storage type to ensure type safety. This is the
 * <i>relative</i> number, so in case of a Calendar for a simulation time, the relative storage type is a relative time
 * with a unit.<br>
 * &lt;T&gt; the extended type itself to be able to implement a comparator, and to ease the use of extension return
 * types.
 * <p>
 * &nbsp;
 * </p>
 * The following SimTime types are available:
 * <ul>
 * <li>SimTime: the abstract time from which the other classes are extended</li>
 * <li>SimTimeCalendarDouble: a simulation time that takes Calendar values with an underlying double to store the time
 * in milliseconds</li>
 * <li>SimTimeCalendarFloat: a simulation time that takes Calendar values with an underlying float to store the time in
 * milliseconds</li>
 * <li>SimTimeCalendarLong: a simulation time that takes Calendar values with an underlying long to store the time in
 * milliseconds</li>
 * <li>SimTimeDouble: a simulation time based on a double</li>
 * <li>SimTimeDoubleUnit: a simulation time based on a double, with a time unit</li>
 * <li>SimTimeFloat: a simulation time based on a float</li>
 * <li>SimTimeFloatUnit: a simulation time based on a float, with a time unit</li>
 * <li>SimTimeLong: a simulation time based on a long</li>
 * <li>SimTimeLongUnit: a simulation time based on a long, with a time unit</li>
 * </ul>
 */
package nl.tudelft.simulation.dsol.simtime;
