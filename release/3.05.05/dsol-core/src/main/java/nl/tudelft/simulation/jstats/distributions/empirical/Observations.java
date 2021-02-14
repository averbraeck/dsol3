package nl.tudelft.simulation.jstats.distributions.empirical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

/**
 * Observations for the empirical distributions.
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
public class Observations implements ObservationsInterface
{
    /** the data. */
    private Number[][] data = null;

    /** is the data grouped ? */
    private boolean grouped = false;

    /**
     * constructs a new Observations.
     * @param observations Number[]; the observations
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
            this.data[CUMPROBABILITY][i] = Double.valueOf((i + 1) * probability);
        }
        this.grouped = false;
    }

    /**
     * constructs a new Observations.
     * @param observations SortedMap&lt;Number,Double&gt;; a sortedMap of observations. The double values in the map either
     *            represent actual times of observation, or represent a probability
     * @param cumulative boolean; are the probabilities in the map cumulative?
     */
    public Observations(final SortedMap<Number, Double> observations, final boolean cumulative)
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
        if (!cumulative)
        {
            this.makecumulative();
        }
        this.grouped = true;
    }

    /**
     * constructs a new Observations.
     * @param observations Number[][]; a sortedMap of observations. The double values in the map either represent actual times
     *            of observation, or represent a probability
     * @param cumulative boolean; are the probabilities in the map cumulative?
     */
    public Observations(final Number[][] observations, final boolean cumulative)
    {
        super();
        this.data = new Number[2][observations.length];
        for (int i = 0; i < observations.length; i++)
        {
            this.data[OBSERVATION][i] = observations[i][0];
            this.data[CUMPROBABILITY][i] = observations[i][1];
        }
        this.normalize();
        if (!cumulative)
        {
            this.makecumulative();
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

    /** {@inheritDoc} */
    @Override
    public int getIndex(final Entry entry)
    {
        int index = this.getIndex(entry.getObservation(), OBSERVATION);
        if (index == this.getIndex(entry.getCumProbability(), CUMPROBABILITY))
        {
            return index;
        }
        return -1;
    }

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
    public List<Number> getObservations()
    {
        return Arrays.asList(this.data[OBSERVATION]);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Number object, final byte type)
    {
        if (this.getIndex(object, type) > -1)
        {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public ObservationsInterface.Entry getEntry(final Number object, final byte type)
    {
        int index = this.getIndex(object, type);
        return this.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public Entry getPrecedingEntry(final Number object, final byte type, final boolean inclusive)
    {
        int index = this.getIndex(object, type);
        if (inclusive && index > -1)
        {
            return this.get(index);
        }
        return this.get(this.getPrecedingIndex(object, type));
    }

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
    public Entry get(final int index)
    {
        if (index < 0 || index >= this.data[OBSERVATION].length)
        {
            return null;
        }
        return new Observations.Observation(this.data[OBSERVATION][index], (Double) this.data[CUMPROBABILITY][index]);
    }

    /**
     * returns the preceding index of the object.
     * @param object Number; the object
     * @param type byte; the type
     * @return the index.
     */
    protected int getIndex(final Number object, final byte type)
    {
        return Arrays.binarySearch(this.data[type], object);
    }

    /**
     * returns the preceding index of the object.
     * @param object Number; the object
     * @param type byte; the type
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
            this.data[CUMPROBABILITY][i] = Double.valueOf(this.data[CUMPROBABILITY][i].doubleValue() / sum);
        }
    }

    /**
     * makes the data structure cumulative.
     */
    private void makecumulative()
    {
        double value = 0.0;
        for (int i = 0; i < this.data[CUMPROBABILITY].length; i++)
        {
            value = value + this.data[CUMPROBABILITY][i].doubleValue();
            this.data[CUMPROBABILITY][i] = Double.valueOf(value);
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
     * The Observation class holds one observation, cumulative probability entry. (c) copyright 2004
     * <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the Netherlands. <br>
     * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
     * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
     * @since 1.5
     */
    public class Observation implements Entry
    {
        /** the observation. */
        private Number observation = null;

        /** the cumulative probability. */
        private Double cumProbability = null;

        /**
         * constructs a new Observation.
         * @param observation Number; the observation
         * @param cumProbability Double; the cumulative probability
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

        /** {@inheritDoc} */
        @Override
        public Number getObservation()
        {
            return this.observation;
        }
    }
}
