package nl.tudelft.simulation.jstats.distributions.empirical;

import java.util.List;

/**
 * The observations interface is an interface for empirical observations to be used in JStats.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @since 1.5
 */
public interface ObservationsInterface
{
    /**
     * the OBSERVATION attribute used to define whether operations should be applied on the OBSERVATION or on the
     * CUMPROBABILITY.
     */
    byte OBSERVATION = 0;

    /**
     * the CUMPROBABILITY attribute used to define whether operations should be applied on the OBSERVATION or on the
     * CUMPROBABILITY.
     */
    byte CUMPROBABILITY = 1;

    /**
     * Returns the number of observation-probability mappings in this structure. If the structure contains more than
     * <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     * @return the number of object-value mappings in this structure.
     */
    int size();

    /**
     * Returns <tt>true</tt> if this structure contains no object-value mappings.
     * @return <tt>true</tt> if this structure contains no object-value mappings.
     */
    boolean isEmpty();

    /**
     * Returns <tt>true</tt> if this structure contains grouped empirical data.
     * @return <tt>true</tt> if this structure contains grouped empirical data.
     */
    boolean isGrouped();

    /**
     * Returns the index of the entry of this structure. Returns -1 if entry not in structure.
     * @param entry the entry
     * @return the index of this entry.
     */
    int getIndex(ObservationsInterface.Entry entry);

    /**
     * returns whether the structure contains this specific object (either an observation or a probability).
     * @param object the object to look for
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return true if object in observation.
     */
    boolean contains(Number object, byte type);

    /**
     * Returns the observations
     * @return the list of observations
     */
    List<Number> getObservations();

    /**
     * Returns the observations
     * @return the list of observations
     */
    List<Double> getCumProbabilities();

    /**
     * Returns the entry to which the structure maps the specific object. Returns <tt>null</tt> if the structure
     * contains no entry for the object. The object might either refer to an observation or to a probability. If
     * multiple entries match the search, there is no garantuee which entry is returned.
     * @param object entry whose associated entry is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return the entries which comply.
     */
    ObservationsInterface.Entry getEntry(Number object, byte type);

    /**
     * Gets the entry corresponding to the specified object if inclusive; if not inclusive or such entry does not
     * exists, returns the entry for the greatest object less than the specified object; if no such entry exists (i.e.,
     * the least object in the Tree is greater than the specified object), returns <tt>null</tt>. If multiple entries
     * match the search, there is no garantuee which entry is returned.
     * @param object object whose next object associated value is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive if inclusive and structure contains object object is returned
     * @return the value to which this structure maps the specified object, or <tt>null</tt> if the structure contains
     *         no mapping for this object.
     */
    ObservationsInterface.Entry getPrecedingEntry(Number object, byte type, boolean inclusive);

    /**
     * Gets the entry corresponding to the specified object if inclusive; if not inclusive or such entry does not
     * exists, returns the entry for the least object greater than the specified object; if no such entry exists (i.e.,
     * the greatest object in the Tree is less than the specified object), returns <tt>null</tt>. If multiple entries
     * match the search, there is no garantuee which entry is returned.
     * @param object object whose associated value is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive if inclusive and structure contains object object is returned
     * @return the value to which this structure maps the specified object, or <tt>null</tt> if the structure contains
     *         no mapping for this object.
     */
    ObservationsInterface.Entry getCeilingEntry(Number object, byte type, boolean inclusive);

    /**
     * Returns the element at the specified position in this structure.
     * @param index index of element to return.
     * @return the entry at the specified position in this list.
     */
    ObservationsInterface.Entry get(int index);

    /**
     * A structure entry (observation-cummulative probability pair).
     * @since 1.5
     */
    interface Entry
    {
        /**
         * Returns the observation corresponding to this entry.
         * @return the observation corresponding to this entry.
         */
        Number getObservation();

        /**
         * Returns the cummulative probability corresponding to this entry.
         * @return the cummulative probability corresponding to this entry.
         */
        Double getCumProbability();
    }
}
