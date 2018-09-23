package nl.tudelft.simulation.jstats.distributions.empirical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

/**
 * The histogram specifies a histogram chart for the DSOL framework. Copyright (c) 2004-2018
 * <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:55 $
 * @since 1.5
 */
public class Observations implements ObservationsInterface
{
    /** the data. */
    private Number[][] data = null;

    /** is the data grouped ? */
    private boolean grouped = false;

    /**
     * constructs a new Observations.
     * @param observations the observations
     */
    public Observations(final Number[] observations)
    {
        super();
        Arrays.sort(observations);
        double probability = 1.0 / observations.length;
        this.data = new Number[2][observations.length];
        for (int i = 0; i < observations.length; i++)
        {
            this.data[OBSERVATION][i] = observations[i];
            this.data[CUMPROBABILITY][i] = new Double((i + 1) * probability);
        }
        this.grouped = false;
    }

    /**
     * constructs a new Observations.
     * @param observations a sortedMap of observations. The double values in the map either represent actual times of
     *            observation, or represent a probability
     * @param cummulative are the probabilities in the map cummulative?
     */
    public Observations(final SortedMap<Number, Double> observations, final boolean cummulative)
    {
        super();
        this.data = new Number[2][observations.size()];
        int counter = 0;
        for (Number key : observations.keySet())
        {
            this.data[OBSERVATION][counter] = key;
            this.data[CUMPROBABILITY][counter] = observations.get(key);
            counter++;
        }
        this.normalize();
        if (!cummulative)
        {
            this.makeCummulative();
        }
        this.grouped = true;
    }

    /**
     * constructs a new Observations.
     * @param observations a sortedMap of observations. The double values in the map either represent actual times of
     *            observation, or represent a probability
     * @param cummulative are the probabilities in the map cummulative?
     */
    public Observations(final Number[][] observations, final boolean cummulative)
    {
        super();
        this.data = new Number[2][observations.length];
        for (int i = 0; i < observations.length; i++)
        {
            this.data[OBSERVATION][i] = observations[i][0];
            this.data[CUMPROBABILITY][i] = observations[i][1];
        }
        this.normalize();
        if (!cummulative)
        {
            this.makeCummulative();
        }
        this.grouped = true;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.data[ObservationsInterface.OBSERVATION].length;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        if (this.data == null)
        {
            return true;
        }
        for (int i = 0; i < this.data.length; i++)
        {
            for (int j = 0; j < this.data[0].length; j++)
            {
                if (this.data[i][j] != null)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isGrouped()
    {
        return this.grouped;
    }

    /**
     * Returns the index of the entry of this structure. Returns -1 if entry not in structure.
     * @param entry the entry
     * @return the index of this entry.
     */
    public int getIndex(final Entry entry)
    {
        int index = this.getIndex(entry.getObservation(), OBSERVATION);
        if (index == this.getIndex(entry.getCumProbability(), CUMPROBABILITY))
        {
            return index;
        }
        return -1;
    }

    /**
     * Returns the cummulative probabilities
     * @return the list of cummulative probabilities
     */
    public List<Double> getCumProbabilities()
    {
        List<Number> list = Arrays.asList(this.data[CUMPROBABILITY]);
        List<Double> result = new ArrayList<Double>();
        for (Number i : list)
        {
            result.add(i.doubleValue());
        }
        return result;
    }

    /**
     * Returns the observations
     * @return the list of observations
     */
    public List<Number> getObservations()
    {
        return Arrays.asList(this.data[OBSERVATION]);
    }

    /**
     * returns whether the structure contains this specific object (either an observation or a probability).
     * @param object the object to look for
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return true if object in observation.
     */
    public boolean contains(final Number object, final byte type)
    {
        if (this.getIndex(object, type) > -1)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns the entry to which the structure maps the specific object. Returns <tt>null</tt> if the structure
     * contains no entry for the object. The object might either refer to an observation or to a probability. If
     * multiple entries match the search, there is no garantuee which entry is returned.
     * @param object entry whose associated entry is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @return the entries which comply.
     */
    public ObservationsInterface.Entry getEntry(final Number object, final byte type)
    {
        int index = this.getIndex(object, type);
        return this.get(index);
    }

    /**
     * Gets the entry corresponding to the specified key if inclusive; if not inclusive or such entry does not exists,
     * returns the entry for the greatest key less than the specified key; if no such entry exists (i.e., the least key
     * in the Tree is greater than the specified key), returns <tt>null</tt>. If multiple entries match the search,
     * there is no garantuee which entry is returned.
     * @param object object whose next key associated value is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive if inclusive and structure contains key key is returned
     * @return the value to which this structure maps the specified key, or <tt>null</tt> if the structure contains no
     *         mapping for this key.
     */
    public Entry getPrecedingEntry(final Number object, final byte type, final boolean inclusive)
    {
        int index = -1;
        if (inclusive && (index = this.getIndex(object, type)) > -1)
        {
            return this.get(index);
        }
        return this.get(this.getPrecedingIndex(object, type));
    }

    /**
     * Gets the entry corresponding to the specified key if inclusive; if not inclusive or such entry does not exists,
     * returns the entry for the least key greater than the specified key; if no such entry exists (i.e., the greatest
     * key in the Tree is less than the specified key), returns <tt>null</tt>. If multiple entries match the search,
     * there is no garantuee which entry is returned.
     * @param object object whose associated value is to be returned.
     * @param type the type (either the ObservationsInterface.OBSERVATION or the ObservationsInterface.CUMPROBABILITY)
     * @param inclusive if inclusive and structure contains key key is returned
     * @return the value to which this structure maps the specified key, or <tt>null</tt> if the structure contains no
     *         mapping for this key.
     */
    public Entry getCeilingEntry(final Number object, final byte type, final boolean inclusive)
    {
        int index = this.getIndex(object, type);
        if (inclusive && index > -1)
        {
            return this.get(index);
        }
        int precedingIndex = this.getPrecedingIndex(object, type);
        if (precedingIndex < 0)
        {
            if (object.doubleValue() < this.data[type][0].doubleValue())
            {
                return this.get(0);
            }
            return null;
        }
        if (index > -1)
        {
            return this.get(2 + precedingIndex);
        }
        return this.get(1 + precedingIndex);
    }

    /**
     * Returns the element at the specified position in this structure.
     * @param index index of element to return.
     * @return the entry at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public Entry get(final int index)
    {
        if (index < 0 || index >= this.data[OBSERVATION].length)
        {
            return null;
        }
        return new Observations.Observation(this.data[OBSERVATION][index], (Double) this.data[CUMPROBABILITY][index]);
    }

    /**
     * returns the preceding index of the object
     * @param object the object
     * @param type the type
     * @return the index.
     */
    protected int getIndex(final Number object, final byte type)
    {
        return Arrays.binarySearch(this.data[type], object);
    }

    /**
     * returns the preceding index of the object
     * @param object the object
     * @param type the type
     * @return the index.
     */
    protected int getPrecedingIndex(final Number object, final byte type)
    {
        if (object.doubleValue() <= this.data[type][0].doubleValue())
        {
            return -1;
        }
        if (this.data[type][this.size() - 1].doubleValue() <= object.doubleValue())
        {
            return -1;
        }
        int index = (int) Math.ceil(this.size() / 2.0);
        double stepSize = index;
        while (true)
        {
            stepSize = 0.5 * stepSize;
            if (this.data[type][index].doubleValue() < object.doubleValue()
                    && this.data[type][index + 1].doubleValue() >= object.doubleValue())
            {
                return index;
            }
            if (this.data[type][index].doubleValue() >= object.doubleValue())
            {
                index = (int) Math.floor(index - stepSize);
            }
            else
            {
                index = (int) Math.ceil(index + stepSize);
            }
        }
    }

    /**
     * normalizes the data structure. This implies that all observation values are replaced with their 1/sum value.
     */
    private void normalize()
    {
        if (this.data[OBSERVATION].length <= 1)
        {
            return;
        }
        double sum = 0;
        boolean toBeConverted = false;
        for (int i = 0; i < this.data[CUMPROBABILITY].length; i++)
        {
            if (!toBeConverted && this.data[CUMPROBABILITY][i].doubleValue() > 1.0)
            {
                toBeConverted = true;
            }
            sum = sum + this.data[CUMPROBABILITY][i].doubleValue();
        }
        if (!toBeConverted)
        {
            return;
        }
        for (int i = 0; i < this.data[CUMPROBABILITY].length; i++)
        {
            this.data[CUMPROBABILITY][i] = new Double(this.data[CUMPROBABILITY][i].doubleValue() / sum);
        }
    }

    /**
     * makes the data structure cummulative.
     */
    private void makeCummulative()
    {
        double value = 0.0;
        for (int i = 0; i < this.data[CUMPROBABILITY].length; i++)
        {
            value = value + this.data[CUMPROBABILITY][i].doubleValue();
            this.data[CUMPROBABILITY][i] = new Double(value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result = "DistDiscreteEmpirical[\n";
        for (int i = 0; i < this.data[0].length; i++)
        {
            result = result + "(" + this.data[0][i] + ";" + this.data[1][i] + ")\n";
        }
        return result;
    }

    /**
     * The Observation class holds one observation, cummulative probability entry. (c) copyright 2004
     * <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the Netherlands. <br>
     * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
     * <br>
     * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty
     * <br>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
     * @version 1.0 Nov 2, 2004
     * @since 1.5
     */
    public class Observation implements Entry
    {
        /** the observation. */
        private Number observation = null;

        /** the cummulative probability */
        private Double cumProbability = null;

        /**
         * constructs a new Observation.
         * @param observation the observation
         * @param cumProbability the cum probabilty
         */
        public Observation(final Number observation, final Double cumProbability)
        {
            super();
            this.observation = observation;
            this.cumProbability = cumProbability;
        }

        /** {@inheritDoc} */
        @Override
        public Double getCumProbability()
        {
            return this.cumProbability;
        }

        /**
         * Returns the observation corresponding to this entry.
         * @return the observation corresponding to this entry.
         */
        public Number getObservation()
        {
            return this.observation;
        }
    }
}
