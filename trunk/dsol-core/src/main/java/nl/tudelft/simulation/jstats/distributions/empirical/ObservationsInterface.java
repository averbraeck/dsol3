package nl.tudelft.simulation.jstats.distributions.empirical;

import java.util.List;

/**
 * The observations interface is an interface for empirical observations to be used in JStats.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
     * <code>Integer.MAX_VALUE</code> elements, returns <code>Integer.MAX_VALUE</code>.
     * @return the number of object-value mappings in this structure.
     */
    int size();

    /**
     * Returns <code>true</code> if this structure contains no object-value mappings.
     * @return <code>true</code> if this structure contains no object-value mappings.
     */
    boolean isEmpty();

    /**
     * Returns <code>true</code> if this structure contains grouped empirical data.
     * @return <code>true</code> if this structure contains grouped empirical data.
     */
    boolean isGrouped();

    /**
     * Returns the index of the entry of this structure. Returns -1 if entry not in structure.
     * @param entry ObservationsInterface.Entry; the entry
     * @return the index of this entry.
     */
    int getIndex(ObservationsInterface.Entry entry);

    /**
     * returns whether the structure contains this specific object (either an observation or a probability).
     * @param object Number; the object to look for
     * @param type byte; the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return true if object in observation.
     */
    boolean contains(Number object, byte type);

    /**
     * Returns the observations.
     * @return the list of observations
     */
    List<Number> getObservations();

    /**
     * Returns the observations.
     * @return the list of observations
     */
    List<Double> getCumProbabilities();

    /**
     * Returns the entry to which the structure maps the specific object. Returns <code>null</code> if the structure contains no
     * entry for the object. The object might either refer to an observation or to a probability. If multiple entries match the
     * search, there is no guarantee which entry is returned.
     * @param object Number; entry whose associated entry is to be returned.
     * @param type byte; the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return the entries which comply.
     */
    ObservationsInterface.Entry getEntry(Number object, byte type);

    /**
     * Gets the entry corresponding to the specified object if inclusive; if not inclusive or such entry does not exists,
     * returns the entry for the greatest object less than the specified object; if no such entry exists (i.e., the least object
     * in the Tree is greater than the specified object), returns <code>null</code>. If multiple entries match the search, there
     * is no guarantee which entry is returned.
     * @param object Number; object whose next object associated value is to be returned.
     * @param type byte; the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive boolean; if inclusive and structure contains object object is returned
     * @return the value to which this structure maps the specified object, or <code>null</code> if the structure contains no
     *         mapping for this object.
     */
    ObservationsInterface.Entry getPrecedingEntry(Number object, byte type, boolean inclusive);

    /**
     * Gets the entry corresponding to the specified object if inclusive; if not inclusive or such entry does not exists,
     * returns the entry for the least object greater than the specified object; if no such entry exists (i.e., the greatest
     * object in the Tree is less than the specified object), returns <code>null</code>. If multiple entries match the search,
     * there is no guarantee which entry is returned.
     * @param object Number; object whose associated value is to be returned.
     * @param type byte; the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive boolean; if inclusive and structure contains object object is returned
     * @return the value to which this structure maps the specified object, or <code>null</code> if the structure contains no
     *         mapping for this object.
     */
    ObservationsInterface.Entry getCeilingEntry(Number object, byte type, boolean inclusive);

    /**
     * Returns the element at the specified position in this structure.
     * @param index int; index of element to return.
     * @return the entry at the specified position in this list.
     */
    ObservationsInterface.Entry get(int index);

    /**
     * A structure entry (observation-cumulative probability pair).
     */
    interface Entry
    {
        /**
         * Returns the observation corresponding to this entry.
         * @return the observation corresponding to this entry.
         */
        Number getObservation();

        /**
         * Returns the cumulative probability corresponding to this entry.
         * @return the cumulative probability corresponding to this entry.
         */
        Double getCumProbability();
    }
}
