package nl.tudelft.simulation.jstats.distributions.empirical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.djutils.exceptions.Throw;

/**
 * The EmpiricalDistribution implements a cumulative distribution function for an empirical distribution. In other words, it
 * describes an ordered list of pairs (cumulative probability, value) from which values can be drawn using the inverse function
 * method with a Uniform(0, 1) random distribution.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EmpiricalDistribution implements EmpiricalDistributionInterface
{
    /** */
    private static final long serialVersionUID = 20210402L;

    /** the map from cumulative probabilities to values. */
    private final TreeMap<Double, Number> cumulativeProbabilityMap = new TreeMap<>();

    /** whether the values will be interpolated or not. */
    private final boolean interpolated;

    /**
     * Construct the empirical distribution based on two arrays of the same length, one with sorted values, and one with
     * corresponding sorted cumulative probabilities. This constructor assumes that the arrays have been properly cloned to
     * avoid changes to their content after the construction of the distribution. Tests for arrays not being equal to null
     * should have been carried out when calling this constructor.
     * @param values Number[] Number[]; the values belonging to each cumulative probability
     * @param cumulativeProbabilities double[]; the cumulative probabilities
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @throws NullPointerException when one of the values is null
     * @throws IllegalArgumentException when cumulativeProbabilities array or values array are empty, or have unequal length, or
     *             when cumulativeProbabilities are not between 0 and 1, or when cumulativeProbabilities are not in ascending
     *             order, or when values are not in ascending order, or when the last cumulative probability is not 1.0, or
     *             there is no interpolation but the first cumulative probability is zero, or there is interpolation but the
     *             first cumulative probability is not zero, or there is only one value when interpolation is used
     */
    public EmpiricalDistribution(final Number[] values, final double[] cumulativeProbabilities, final boolean interpolated)
    {
        Throw.when(values.length == 0, IllegalArgumentException.class, "values array cannot be empty");
        Throw.when(cumulativeProbabilities.length == 0, IllegalArgumentException.class,
                "cumulativeProbabilities array cannot be empty");
        Throw.when(cumulativeProbabilities.length != values.length, IllegalArgumentException.class,
                "values array and cumulativeProbabilities array should have the same length");
        Throw.when(cumulativeProbabilities[0] == 0.0 && !interpolated, IllegalArgumentException.class,
                "no interpolation, but first cumulative probability is zero");
        Throw.when(cumulativeProbabilities[0] != 0.0 && interpolated, IllegalArgumentException.class,
                "interpolation, but first cumulative probability is not zero");
        Throw.when(cumulativeProbabilities.length < 2 && interpolated, IllegalArgumentException.class,
                "interpolation needs at least two cumulative probability values");
        double prevCP = -1.0;
        double prevV = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < cumulativeProbabilities.length; i++)
        {
            Throw.whenNull(values[i], "one of the values is null");
            Throw.when(cumulativeProbabilities[i] < 0.0 || cumulativeProbabilities[i] > 1.0, IllegalArgumentException.class,
                    "cumulative probability not between 0 and 1");
            Throw.when(cumulativeProbabilities[i] <= prevCP, IllegalArgumentException.class,
                    "cumulative probabilities not in ascending order");
            Throw.when(values[i].doubleValue() <= prevV, IllegalArgumentException.class, "values not in ascending order");
            prevCP = cumulativeProbabilities[i];
            prevV = values[i].doubleValue();
        }
        Throw.when(cumulativeProbabilities[cumulativeProbabilities.length - 1] != 1.0, IllegalArgumentException.class,
                "last cumulative probability should be 1.0");

        for (int i = 0; i < values.length; i++)
        {
            this.cumulativeProbabilityMap.put(cumulativeProbabilities[i], values[i]);
        }
        this.interpolated = interpolated;
    }

    /* ******************************************************************************************************************** */
    /* ******************************** CONSTRUCT BASED ON CUMULATIVE DISTRIBUTION **************************************** */
    /* ******************************************************************************************************************** */

    /**
     * Create an empirical distribution based on two arrays of the same length, one with sorted values, and one with
     * corresponding sorted cumulative probabilities. A copy of the original values is made to deal with mutability of the
     * array.
     * @param values Number[] the values
     * @param cumulativeProbabilities double[]; the cumulative probabilities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when cumulativeProbabilities array is null or values array is null, or when one of the
     *             values is null
     * @throws IllegalArgumentException when cumulativeProbabilities array or values array are empty, or have unequal length, or
     *             when cumulativeProbabilities are not between 0 and 1, or when cumulativeProbabilities are not in ascending
     *             order, or when values are not in ascending order, or when the last cumulative probability is not 1.0, or when
     *             there is interpolation but the first cumulative probability is not zero, or there is no interpolation but the
     *             first cumulative probability is zero, or there is only one value when interpolation is used
     */
    public static EmpiricalDistribution createFromCumulativeProbabilities(final Number[] values,
            final double[] cumulativeProbabilities, final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(cumulativeProbabilities, "cumulativeProbabilities array cannot be null");
        return new EmpiricalDistribution(values.clone(), cumulativeProbabilities.clone(), interpolated);
    }

    /**
     * Create an empirical distribution based on two arrays of the same length, one with sorted values, and one with
     * corresponding sorted cumulative probabilities. A copy of the original values is made to deal with mutability of the
     * array.
     * @param values double[] the values
     * @param cumulativeProbabilities double[]; the cumulative probabilities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when cumulativeProbabilities array is null or values array is null, or when one of the
     *             values is null
     * @throws IllegalArgumentException when cumulativeProbabilities array or values array are empty, or have unequal length, or
     *             when cumulativeProbabilities are not between 0 and 1, or when cumulativeProbabilities are not in ascending
     *             order, or when values are not in ascending order, or when the last cumulative probability is not 1.0, or when
     *             there is interpolation but the first cumulative probability is not zero, or there is no interpolation but the
     *             first cumulative probability is zero, or there is only one value when interpolation is used
     */
    public static EmpiricalDistribution createFromCumulativeProbabilities(final double[] values,
            final double[] cumulativeProbabilities, final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(cumulativeProbabilities, "cumulativeProbabilities array cannot be null");
        Double[] doubleValues = new Double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleValues[i] = values[i];
        }
        return new EmpiricalDistribution(doubleValues, cumulativeProbabilities.clone(), interpolated);
    }

    /**
     * Create an empirical distribution based on two arrays of the same length, one with sorted values, and one with
     * corresponding sorted cumulative probabilities. A copy of the original values is made to deal with mutability of the
     * array.
     * @param values long[] the values
     * @param cumulativeProbabilities double[]; the cumulative probabilities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when cumulativeProbabilities array is null or values array is null, or when one of the
     *             values is null
     * @throws IllegalArgumentException when cumulativeProbabilities array or values array are empty, or have unequal length, or
     *             when cumulativeProbabilities are not between 0 and 1, or when cumulativeProbabilities are not in ascending
     *             order, or when values are not in ascending order, or when the last cumulative probability is not 1.0, or when
     *             there is interpolation but the first cumulative probability is not zero, or there is no interpolation but the
     *             first cumulative probability is zero, or there is only one value when interpolation is used
     */
    public static EmpiricalDistribution createFromCumulativeProbabilities(final long[] values,
            final double[] cumulativeProbabilities, final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(cumulativeProbabilities, "cumulativeProbabilities array cannot be null");
        Long[] longValues = new Long[values.length];
        for (int i = 0; i < values.length; i++)
        {
            longValues[i] = values[i];
        }
        return new EmpiricalDistribution(longValues, cumulativeProbabilities.clone(), interpolated);
    }

    /**
     * Create an empirical distribution based on two Lists of the same length, one with sorted values, and one with
     * corresponding sorted cumulative probabilities.
     * @param values List&lt;? extends Number&gt;; the values
     * @param cumulativeProbabilities List&lt;Double&gt;; the cumulative probabilities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution lists
     * @throws NullPointerException when cumulativeProbabilities list is null, or when values list is null, or when one of the
     *             values is null
     * @throws IllegalArgumentException when cumulativeProbabilities array or values array are empty, or have unequal length, or
     *             when cumulativeProbabilities are not between 0 and 1, or when cumulativeProbabilities are not in ascending
     *             order, or when values are not in ascending order, or when the last cumulative probability is not 1.0, or when
     *             there is interpolation but the first cumulative probability is not zero, or there is no interpolation but the
     *             first cumulative probability is zero, or there is only one value when interpolation is used
     */
    public static EmpiricalDistribution createFromCumulativeProbabilities(final List<? extends Number> values,
            final List<Double> cumulativeProbabilities, final boolean interpolated)
    {
        Throw.whenNull(values, "values list cannot be null");
        Throw.whenNull(cumulativeProbabilities, "cumulativeProbabilities list cannot be null");
        return new EmpiricalDistribution(values.toArray(new Number[0]),
                cumulativeProbabilities.stream().mapToDouble(d -> d).toArray(), interpolated);
    }

    /**
     * Create an empirical distribution based on a sorted map with sorted values mapping to cumulative probabilities.
     * @param cumulativeProbabilitiesMap SortedMap&lt;Number, Double&gt;; the map with the entries
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution map
     * @throws NullPointerException when cumulativeProbabilitiesMap is null, or when one of the values in the map is null
     * @throws IllegalArgumentException when cumulativeProbabilitiesMap is empty, or when cumulativeProbabilities are not
     *             between 0 and 1, or when cumulativeProbabilities are not in ascending order, or when values are not in
     *             ascending order, or when the last cumulative probability is not 1.0, or when there is interpolation but the
     *             first cumulative probability is not zero, or there is no interpolation but the first cumulative probability
     *             is zero, or there is only one value when interpolation is used
     */
    public static EmpiricalDistribution createFromCumulativeProbabilities(
            final SortedMap<? extends Number, Double> cumulativeProbabilitiesMap, final boolean interpolated)
    {
        Throw.whenNull(cumulativeProbabilitiesMap, "cumulativeProbabilitiesMap cannot be null");
        return new EmpiricalDistribution(cumulativeProbabilitiesMap.keySet().toArray(new Number[0]),
                cumulativeProbabilitiesMap.values().stream().mapToDouble(d -> d).toArray(), interpolated);
    }

    /* ******************************************************************************************************************** */
    /* ********************************* CONSTRUCT BASED ON PROBABILITY DENSITIES ***************************************** */
    /* ******************************************************************************************************************** */

    /**
     * Create an empirical distribution from two arrays, one with values, and one with corresponding densities (summing to 1.0).
     * @param values Number[] the values
     * @param densities double[]; the densities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry will get a cumulative probability of zero.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when densities array is null or values array is null, or when one of the values is null
     * @throws IllegalArgumentException when densities array or values array are empty, or have unequal length, or when
     *             densities are not between 0 and 1, or when values are not in ascending order, or when the sum of the
     *             probability densities is not 1.0
     */
    public static EmpiricalDistribution createFromDensities(final Number[] values, final double[] densities,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(densities, "densities array cannot be null");
        Throw.when(values.length == 0, IllegalArgumentException.class, "values array cannot be empty");
        Throw.when(densities.length == 0, IllegalArgumentException.class, "densities array cannot be empty");
        Throw.when(densities.length != values.length, IllegalArgumentException.class,
                "values array and densities array should have the same length");

        double cumulativeProbability = 0;
        double[] cumulativeProbabilities;
        Number[] newValues;
        if (interpolated)
        {
            cumulativeProbabilities = new double[densities.length + 1];
            newValues = new Number[densities.length + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values[0];
            for (int i = 0; i < densities.length; i++)
            {
                Throw.when(densities[i] <= 0.0 || densities[i] > 1.0, IllegalArgumentException.class,
                        "densities should be between 0 and 1");
                cumulativeProbability += densities[i];
                cumulativeProbabilities[i + 1] = cumulativeProbability;
                newValues[i + 1] = values[i];
            }
        }
        else
        {
            cumulativeProbabilities = new double[densities.length];
            newValues = values.clone();
            for (int i = 0; i < densities.length; i++)
            {
                Throw.when(densities[i] <= 0.0 || densities[i] > 1.0, IllegalArgumentException.class,
                        "densities should be between 0 and 1");
                cumulativeProbability += densities[i];
                cumulativeProbabilities[i] = cumulativeProbability;
            }
        }
        Throw.when(Math.abs(cumulativeProbability - 1.0) > 10.0 * Math.ulp(1.0), IllegalArgumentException.class,
                "probabilities do not add up to 1.0");
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with values, and one with corresponding densities (summing to 1.0).
     * @param values double[] the values
     * @param densities double[]; the densities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when densities array is null or values array is null, or when one of the values is null
     * @throws IllegalArgumentException when densities array or values array are empty, or have unequal length, or when
     *             densities are not between 0 and 1, or when values are not in ascending order, or when the sum of the
     *             probability densities is not 1.0
     */
    public static EmpiricalDistribution createFromDensities(final double[] values, final double[] densities,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Double[] doubleValues = new Double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleValues[i] = values[i];
        }
        return createFromDensities(doubleValues, densities, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with values, and one with corresponding densities (summing to 1.0).
     * @param values long[] the values
     * @param densities double[]; the densities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution arrays
     * @throws NullPointerException when densities array is null or values array is null, or when one of the values is null
     * @throws IllegalArgumentException when densities array or values array are empty, or have unequal length, or when
     *             densities are not between 0 and 1, or when values are not in ascending order, or when the sum of the
     *             probability densities is not 1.0
     */
    public static EmpiricalDistribution createFromDensities(final long[] values, final double[] densities,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Long[] longValues = new Long[values.length];
        for (int i = 0; i < values.length; i++)
        {
            longValues[i] = values[i];
        }
        return createFromDensities(longValues, densities, interpolated);
    }

    /**
     * Create an empirical distribution based on two Lists of the same length, one with probability densities, and one with
     * sorted values.
     * @param values List&lt;? extends Number&gt;; the values
     * @param densities List&lt;Double&gt;; the probability densities for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution lists
     * @throws NullPointerException when densities list is null or values list is null, or when one of the values is null
     * @throws IllegalArgumentException when densities list or values list are empty, or have unequal length, or when densities
     *             are not between 0 and 1, or when values are not in ascending order, or when the sum of the probability
     *             densities is not 1.0
     */
    public static EmpiricalDistribution createFromDensities(final List<? extends Number> values, final List<Double> densities,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values list cannot be null");
        Throw.whenNull(densities, "densities list cannot be null");
        Throw.when(values.isEmpty(), IllegalArgumentException.class, "values list cannot be empty");
        Throw.when(densities.isEmpty(), IllegalArgumentException.class, "densities list cannot be empty");
        Throw.when(densities.size() != values.size(), IllegalArgumentException.class,
                "values list and densities list should have the same size");
        double cumulativeProbability = 0;
        double[] cumulativeProbabilities;
        Number[] newValues;
        if (interpolated)
        {
            cumulativeProbabilities = new double[densities.size() + 1];
            newValues = new Number[densities.size() + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values.get(0);
            for (int i = 0; i < densities.size(); i++)
            {
                Throw.when(densities.get(i) <= 0.0 || densities.get(i) > 1.0, IllegalArgumentException.class,
                        "densities should be between 0 and 1");
                cumulativeProbability += densities.get(i);
                cumulativeProbabilities[i + 1] = cumulativeProbability;
                newValues[i + 1] = values.get(i);
            }
        }
        else
        {
            cumulativeProbabilities = new double[densities.size()];
            newValues = values.toArray(new Number[0]);
            for (int i = 0; i < densities.size(); i++)
            {
                Throw.when(densities.get(i) <= 0.0 || densities.get(i) > 1.0, IllegalArgumentException.class,
                        "densities should be between 0 and 1");
                cumulativeProbability += densities.get(i);
                cumulativeProbabilities[i] = cumulativeProbability;
            }
        }
        Throw.when(Math.abs(cumulativeProbability - 1.0) > 10.0 * Math.ulp(1.0), IllegalArgumentException.class,
                "probabilities do not add up to 1.0");
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution based on a sorted map with sorted values mapping to probability densities.
     * @param densitiesMap SortedMap&lt;? extends Number, Double&gt;; the map with the entries
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution map
     * @throws NullPointerException when densities map is null, or when one of the values or densities is null
     * @throws IllegalArgumentException when densities map is empty, or when densities are not between 0 and 1, or when the sum
     *             of the probability densities is not 1.0
     */
    public static EmpiricalDistribution createFromDensities(final SortedMap<? extends Number, Double> densitiesMap,
            final boolean interpolated)
    {
        Throw.whenNull(densitiesMap, "densitiesMap cannot be null");
        return createFromDensities(densitiesMap.values().toArray(new Number[0]),
                densitiesMap.keySet().stream().mapToDouble(d -> d.doubleValue()).toArray(), interpolated);
    }

    /* ******************************************************************************************************************** */
    /* ********************************** CONSTRUCT BASED ON FREQUENCIES OR WEIGHTS *************************************** */
    /* ******************************************************************************************************************** */

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values Number[] the values
     * @param frequencies long[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final Number[] values, final long[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(frequencies, "frequencies array cannot be null");
        Throw.when(values.length == 0, IllegalArgumentException.class, "values array cannot be empty");
        Throw.when(frequencies.length == 0, IllegalArgumentException.class, "frequencies array cannot be empty");
        Throw.when(frequencies.length != values.length, IllegalArgumentException.class,
                "values array and frequencies array should have the same length");

        double sum = 0.0;
        for (int i = 0; i < frequencies.length; i++)
        {
            Throw.when(frequencies[i] <= 0, IllegalArgumentException.class, "frequency cannot be zero or negative");
            sum += 1.0 * frequencies[i];
        }

        double[] cumulativeProbabilities;
        Number[] newValues;
        double partialSum = 0;
        if (interpolated)
        {
            cumulativeProbabilities = new double[frequencies.length + 1];
            newValues = new Number[frequencies.length + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values[0];
            for (int i = 0; i < frequencies.length; i++)
            {
                partialSum += 1.0 * frequencies[i];
                cumulativeProbabilities[i + 1] = partialSum / sum;
                newValues[i + 1] = values[i];
            }
        }
        else
        {
            newValues = values.clone();
            cumulativeProbabilities = new double[frequencies.length];
            for (int i = 0; i < frequencies.length; i++)
            {
                partialSum += 1.0 * frequencies[i];
                cumulativeProbabilities[i] = partialSum / sum;
            }

        }
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values double[] the values
     * @param frequencies long[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final double[] values, final long[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Double[] doubleValues = new Double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleValues[i] = values[i];
        }
        return createFromFrequencies(doubleValues, frequencies, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values long[] the values
     * @param frequencies long[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final long[] values, final long[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Long[] longValues = new Long[values.length];
        for (int i = 0; i < values.length; i++)
        {
            longValues[i] = values[i];
        }
        return createFromFrequencies(longValues, frequencies, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values Number[] the values
     * @param frequencies int[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final Number[] values, final int[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(frequencies, "frequencies array cannot be null");
        Throw.when(values.length == 0, IllegalArgumentException.class, "values array cannot be empty");
        Throw.when(frequencies.length == 0, IllegalArgumentException.class, "frequencies array cannot be empty");
        Throw.when(frequencies.length != values.length, IllegalArgumentException.class,
                "values array and frequencies array should have the same length");

        double sum = 0.0;
        for (int i = 0; i < frequencies.length; i++)
        {
            Throw.when(frequencies[i] <= 0, IllegalArgumentException.class, "frequency cannot be zero or negative");
            sum += 1.0 * frequencies[i];
        }

        double[] cumulativeProbabilities;
        Number[] newValues;
        double partialSum = 0;
        if (interpolated)
        {
            cumulativeProbabilities = new double[frequencies.length + 1];
            newValues = new Number[frequencies.length + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values[0];
            for (int i = 0; i < frequencies.length; i++)
            {
                partialSum += 1.0 * frequencies[i];
                cumulativeProbabilities[i + 1] = partialSum / sum;
                newValues[i + 1] = values[i];
            }
        }
        else
        {
            newValues = values.clone();
            cumulativeProbabilities = new double[frequencies.length];
            for (int i = 0; i < frequencies.length; i++)
            {
                partialSum += 1.0 * frequencies[i];
                cumulativeProbabilities[i] = partialSum / sum;
            }

        }
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values double[] the values
     * @param frequencies int[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final double[] values, final int[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Double[] doubleValues = new Double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleValues[i] = values[i];
        }
        return createFromFrequencies(doubleValues, frequencies, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with frequencies or weights, and one with corresponding values.
     * @param values long[] the values
     * @param frequencies int[]; the frequencies for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or frequencies array is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies array or values array are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequencies(final long[] values, final int[] frequencies,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Long[] longValues = new Long[values.length];
        for (int i = 0; i < values.length; i++)
        {
            longValues[i] = values[i];
        }
        return createFromFrequencies(longValues, frequencies, interpolated);
    }

    /**
     * Create an empirical distribution based on two Lists of the same length, one with sorted values, and one with
     * corresponding frequencies or weights.
     * @param values List&lt;? extends Number&gt;; the values
     * @param frequencies List&lt;? extends Number&gt;; the frequencies or weights for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution lists
     * @throws NullPointerException when frequencies list is null or values list is null, or when one of the values is null
     * @throws IllegalArgumentException when frequencies list or values list are empty, or have unequal length, or when
     *             frequencies are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromFrequenciesOrWeights(final List<? extends Number> values,
            final List<? extends Number> frequencies, final boolean interpolated)
    {
        Throw.whenNull(values, "values list cannot be null");
        Throw.whenNull(frequencies, "frequencies list cannot be null");
        Throw.when(values.isEmpty(), IllegalArgumentException.class, "values list cannot be empty");
        Throw.when(frequencies.isEmpty(), IllegalArgumentException.class, "frequencies list cannot be empty");
        Throw.when(frequencies.size() != values.size(), IllegalArgumentException.class,
                "values list and frequencies list should have the same size");

        double sum = 0.0;
        for (int i = 0; i < frequencies.size(); i++)
        {
            Throw.when(frequencies.get(i).doubleValue() <= 0, IllegalArgumentException.class,
                    "frequency cannot be zero or negative");
            sum += 1.0 * frequencies.get(i).doubleValue();
        }

        double[] cumulativeProbabilities;
        Number[] newValues;
        double partialSum = 0;
        if (interpolated)
        {
            cumulativeProbabilities = new double[frequencies.size() + 1];
            newValues = new Number[frequencies.size() + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values.get(0);
            for (int i = 0; i < frequencies.size(); i++)
            {
                partialSum += frequencies.get(i).doubleValue();
                cumulativeProbabilities[i + 1] = partialSum / sum;
                newValues[i + 1] = values.get(i);
            }
        }
        else
        {
            newValues = values.toArray(new Number[0]);
            cumulativeProbabilities = new double[frequencies.size()];
            for (int i = 0; i < frequencies.size(); i++)
            {
                partialSum += frequencies.get(i).doubleValue();
                cumulativeProbabilities[i] = partialSum / sum;
            }

        }
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution based on a sorted map with sorted values mapping to frequencies.
     * @param frequenciesMap SortedMap&lt;? extends Number, ? extends Number&gt;; the map with the entries
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given distribution map
     * @throws NullPointerException when frequencies map is null, or when one of the values or frequencies is null
     * @throws IllegalArgumentException when frequencies map is empty, or when frequencies are not between 0 and 1, or when the
     *             sum of the probability frequencies is not 1.0
     */
    public static EmpiricalDistribution createFromFrequenciesOrWeights(
            final SortedMap<? extends Number, ? extends Number> frequenciesMap, final boolean interpolated)
    {
        Throw.whenNull(frequenciesMap, "frequenciesMap cannot be null");
        return createFromWeights(frequenciesMap.values().toArray(new Number[0]), frequenciesMap.keySet().toArray(new Number[0]),
                interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with weights, and one with corresponding values.
     * @param values Number[] the values
     * @param weights Number[]; the weights for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or weights array is null, or when one of the values is null
     * @throws IllegalArgumentException when weights array or values array are empty, or have unequal length, or when weights
     *             are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromWeights(final Number[] values, final Number[] weights,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(weights, "weights array cannot be null");
        double[] doubleWeights = new double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleWeights[i] = values[i].doubleValue();
        }
        return createFromWeights(values, doubleWeights, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with weights, and one with corresponding values.
     * @param values Number[] the values
     * @param weights double[]; the weights for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or weights array is null, or when one of the values is null
     * @throws IllegalArgumentException when weights array or values array are empty, or have unequal length, or when weights
     *             are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromWeights(final Number[] values, final double[] weights,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Throw.whenNull(weights, "weights array cannot be null");
        Throw.when(values.length == 0, IllegalArgumentException.class, "values array cannot be empty");
        Throw.when(weights.length == 0, IllegalArgumentException.class, "weights array cannot be empty");
        Throw.when(weights.length != values.length, IllegalArgumentException.class,
                "values array and weights array should have the same length");

        double sum = 0.0;
        for (int i = 0; i < weights.length; i++)
        {
            Throw.when(weights[i] <= 0, IllegalArgumentException.class, "weight cannot be zero or negative");
            sum += 1.0 * weights[i];
        }

        double[] cumulativeProbabilities;
        Number[] newValues;
        double partialSum = 0;
        if (interpolated)
        {
            cumulativeProbabilities = new double[weights.length + 1];
            newValues = new Number[weights.length + 1];
            cumulativeProbabilities[0] = 0.0;
            newValues[0] = values[0];
            for (int i = 0; i < weights.length; i++)
            {
                partialSum += 1.0 * weights[i];
                cumulativeProbabilities[i + 1] = partialSum / sum;
                newValues[i + 1] = values[i];
            }
        }
        else
        {
            newValues = values.clone();
            cumulativeProbabilities = new double[weights.length];
            for (int i = 0; i < weights.length; i++)
            {
                partialSum += 1.0 * weights[i];
                cumulativeProbabilities[i] = partialSum / sum;
            }

        }
        cumulativeProbabilities[cumulativeProbabilities.length - 1] = 1.0;
        return new EmpiricalDistribution(newValues, cumulativeProbabilities, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with weights, and one with corresponding values.
     * @param values double[] the values
     * @param weights int[]; the weights for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or weights array is null, or when one of the values is null
     * @throws IllegalArgumentException when weights array or values array are empty, or have unequal length, or when weights
     *             are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromWeights(final double[] values, final double[] weights,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Double[] doubleValues = new Double[values.length];
        for (int i = 0; i < values.length; i++)
        {
            doubleValues[i] = values[i];
        }
        return createFromWeights(doubleValues, weights, interpolated);
    }

    /**
     * Create an empirical distribution from two arrays, one with weights, and one with corresponding values.
     * @param values long[] the values
     * @param weights int[]; the weights for the corresponding values
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given arrays
     * @throws NullPointerException when values array is null or weights array is null, or when one of the values is null
     * @throws IllegalArgumentException when weights array or values array are empty, or have unequal length, or when weights
     *             are zero or negative, or when values are not in ascending order
     */
    public static EmpiricalDistribution createFromWeights(final long[] values, final double[] weights,
            final boolean interpolated)
    {
        Throw.whenNull(values, "values array cannot be null");
        Long[] longValues = new Long[values.length];
        for (int i = 0; i < values.length; i++)
        {
            longValues[i] = values[i];
        }
        return createFromWeights(longValues, weights, interpolated);
    }

    /* ******************************************************************************************************************** */
    /* *************************************** CONSTRUCT BASED ON OBSERVATIONS ******************************************** */
    /* ******************************************************************************************************************** */

    /**
     * Create an empirical distribution from an array with observations.
     * @param observations Number[] the observations
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given observations
     * @throws NullPointerException when observations array is null, or when one of the observations is null
     * @throws IllegalArgumentException when observations array is empty
     */
    public static EmpiricalDistribution createFromObservations(final Number[] observations, final boolean interpolated)
    {
        Throw.whenNull(observations, "observations array cannot be null");
        SortedMap<Number, Integer> map = new TreeMap<>();
        for (Number n : observations)
        {
            Throw.whenNull(n, "observation cannot be null");
            if (map.containsKey(n))
            {
                map.put(n, map.get(n) + 1);
            }
            else
            {
                map.put(n, 1);
            }
        }
        return createFromWeights(map.keySet().toArray(new Number[0]), map.values().toArray(new Integer[0]), interpolated);
    }

    /**
     * Create an empirical distribution from an array with observations.
     * @param observations double[] the observations
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given observations
     * @throws NullPointerException when observations array is null, or when one of the observations is null
     * @throws IllegalArgumentException when observations array is empty
     */
    public static EmpiricalDistribution createFromObservations(final double[] observations, final boolean interpolated)
    {
        Throw.whenNull(observations, "observations array cannot be null");
        SortedMap<Double, Integer> map = new TreeMap<>();
        for (Double n : observations)
        {
            Throw.whenNull(n, "observation cannot be null");
            if (map.containsKey(n))
            {
                map.put(n, map.get(n) + 1);
            }
            else
            {
                map.put(n, 1);
            }
        }
        return createFromWeights(map.keySet().toArray(new Number[0]), map.values().toArray(new Integer[0]), interpolated);
    }

    /**
     * Create an empirical distribution from an array with observations.
     * @param observations long[] the observations
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given observations
     * @throws NullPointerException when observations array is null, or when one of the observations is null
     * @throws IllegalArgumentException when observations array is empty
     */
    public static EmpiricalDistribution createFromObservations(final long[] observations, final boolean interpolated)
    {
        Throw.whenNull(observations, "observations array cannot be null");
        SortedMap<Long, Integer> map = new TreeMap<>();
        for (Long n : observations)
        {
            Throw.whenNull(n, "observation cannot be null");
            if (map.containsKey(n))
            {
                map.put(n, map.get(n) + 1);
            }
            else
            {
                map.put(n, 1);
            }
        }
        return createFromWeights(map.keySet().toArray(new Number[0]), map.values().toArray(new Integer[0]), interpolated);
    }

    /**
     * Create an empirical distribution from a list with observations.
     * @param observations List&lt;Number&gt;; the observations
     * @param interpolated boolean; indicated whether the values will be interpolated or not. When the values are interpolated,
     *            the first entry has to have a cumulative probability of zero. When the values are not interpolated, the first
     *            cumulative probability has to be larger than 0.0.
     * @return the cumulative distribution object belonging to the given observations
     * @throws NullPointerException when observations list is null, or when one of the observations is null
     * @throws IllegalArgumentException when observations list is empty
     */
    public static EmpiricalDistribution createFromObservations(final List<? extends Number> observations,
            final boolean interpolated)
    {
        Throw.whenNull(observations, "observations list cannot be null");
        SortedMap<Number, Integer> map = new TreeMap<>();
        for (Number n : observations)
        {
            if (map.containsKey(n))
            {
                map.put(n, map.get(n) + 1);
            }
            else
            {
                map.put(n, 1);
            }
        }
        return createFromWeights(map.keySet().toArray(new Number[0]), map.values().toArray(new Integer[0]), interpolated);
    }

    /* ******************************************************************************************************************** */
    /* ********************************************* METHODS FOR DRAWING ************************************************** */
    /* ******************************************************************************************************************** */

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.cumulativeProbabilityMap.size();
    }

    /** {@inheritDoc} */
    @Override
    public List<Double> getCumulativeProbabilities()
    {
        return new ArrayList<Double>(this.cumulativeProbabilityMap.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public List<Number> getValues()
    {
        return new ArrayList<Number>(this.cumulativeProbabilityMap.values());
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isInterpolated()
    {
        return this.interpolated;
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getFloorEntry(final double cumulativeProbability)
    {
        Map.Entry<Double, Number> floorEntry = this.cumulativeProbabilityMap.floorEntry(cumulativeProbability);
        if (floorEntry == null)
        {
            return null;
        }
        return new DistributionEntry(floorEntry.getValue(), floorEntry.getKey());
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getCeilingEntry(final double cumulativeProbability)
    {
        Map.Entry<Double, Number> ceilingEntry = this.cumulativeProbabilityMap.ceilingEntry(cumulativeProbability);
        if (ceilingEntry == null)
        {
            return null;
        }
        return new DistributionEntry(ceilingEntry.getValue(), ceilingEntry.getKey());
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getPrevEntry(final double cumulativeProbability)
    {
        Map.Entry<Double, Number> floorEntry = this.cumulativeProbabilityMap.floorEntry(cumulativeProbability);
        if (floorEntry != null)
        {
            if (floorEntry.getKey() == cumulativeProbability)
            {
                floorEntry = this.cumulativeProbabilityMap.lowerEntry(floorEntry.getKey());
            }
            if (floorEntry != null)
            {
                return new DistributionEntry(floorEntry.getValue(), floorEntry.getKey());
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getNextEntry(final double cumulativeProbability)
    {
        Map.Entry<Double, Number> ceilingEntry = this.cumulativeProbabilityMap.ceilingEntry(cumulativeProbability);
        if (ceilingEntry != null)
        {
            if (ceilingEntry.getKey() == cumulativeProbability)
            {
                ceilingEntry = this.cumulativeProbabilityMap.higherEntry(ceilingEntry.getKey());
            }
            if (ceilingEntry != null)
            {
                return new DistributionEntry(ceilingEntry.getValue(), ceilingEntry.getKey());
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getFloorEntryForValue(final Number value)
    {
        Map.Entry<Double, Number> floorEntry = null;
        for (double key : this.cumulativeProbabilityMap.keySet())
        {
            Map.Entry<Double, Number> entry = this.cumulativeProbabilityMap.floorEntry(key);
            if (entry.getValue().equals(value))
            {
                return new DistributionEntry(entry.getValue(), entry.getKey());
            }
            if (entry.getValue().doubleValue() > value.doubleValue())
            {
                break;
            }
            floorEntry = entry;
        }
        return floorEntry == null ? null : new DistributionEntry(floorEntry.getValue(), floorEntry.getKey());
    }

    /** {@inheritDoc} */
    @Override
    public DistributionEntry getCeilingEntryForValue(final Number value)
    {
        for (double key : this.cumulativeProbabilityMap.keySet())
        {
            Map.Entry<Double, Number> entry = this.cumulativeProbabilityMap.floorEntry(key);
            if (entry.getValue().equals(value) || entry.getValue().doubleValue() > value.doubleValue())
            {
                return new DistributionEntry(entry.getValue(), entry.getKey());
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Number getLowestValue()
    {
        return this.cumulativeProbabilityMap.ceilingEntry(-1.0).getValue();
    }

    /** {@inheritDoc} */
    @Override
    public Number getHighestValue()
    {
        return this.cumulativeProbabilityMap.floorEntry(2.0).getValue();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.cumulativeProbabilityMap.hashCode();
        result = prime * result + (this.interpolated ? 1231 : 1237);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmpiricalDistribution other = (EmpiricalDistribution) obj;
        if (!this.cumulativeProbabilityMap.equals(other.cumulativeProbabilityMap))
            return false;
        if (this.interpolated != other.interpolated)
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "EmpiricalDistribution [cumulativeProbabilityMap=" + this.cumulativeProbabilityMap + ", interpolated="
                + this.interpolated + "]";
    }

}
