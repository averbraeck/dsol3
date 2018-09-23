package nl.tudelft.simulation.jstats.distributions;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.tudelft.simulation.jstats.distributions.empirical.Observations;
import nl.tudelft.simulation.jstats.distributions.empirical.ObservationsInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The discrete empirical distribution as defined on page 326 of Law &amp; Kelton.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistDiscreteEmpirical extends DistDiscrete
{
    /** the observations. */
    private ObservationsInterface observations = null;

    /**
     * constructs a new DistDiscreteEmpirical distribution.
     * @param stream the stream to use
     * @param observations the observations feeding the distributions. These observations should be in one of two
     *            possible formats. The first accepted format is the X(i);probability format. This results in a map with
     *            values such as 1=0.33;2=0.167;3=0.167;4=0.33. The second allowed format is the X(i);occurrence
     *            combination resulting in values such as 1=2;2=1;3=1;4=2.
     */
    public DistDiscreteEmpirical(final StreamInterface stream, final ObservationsInterface observations)
    {
        super(stream);
        if (!observations.isGrouped())
        {
            this.observations =
                    new Observations(DistDiscreteEmpirical.constructGroupedMap(observations.getObservations()), false);
        }
        else
        {
            this.observations = observations;
        }
    }

    /**
     * constructs a new DistDiscreteEmpirical distribution.
     * @param stream the stream to use
     * @param observations the observations feeding the distributions. This sortedmap should be filled with observation
     *            probability values. The probability may either reflect the number of times this observation is
     *            observed or may contain a relative probability.
     */
    public DistDiscreteEmpirical(final StreamInterface stream, final Long[] observations)
    {
        super(stream);
        this.observations =
                new Observations(DistDiscreteEmpirical.constructGroupedMap(Arrays.asList(observations)), false);
    }

    /**
     * constructs a new DistDiscreteEmpirical distribution.
     * @param stream the stream to use
     * @param observations the observations feeding the distributions. This sortedmap should be filled with observation
     *            probability values. The probability may either reflect the number of times this observation is
     *            observed or may contain a relative probability.
     */
    public DistDiscreteEmpirical(final StreamInterface stream, final long[] observations)
    {
        super(stream);
        Long[] values = new Long[observations.length];
        for (int i = 0; i < values.length; i++)
        {
            values[i] = new Long(observations[i]);
        }
        this.observations = new Observations(DistDiscreteEmpirical.constructGroupedMap(Arrays.asList(values)), false);
    }

    /**
     * constructs a new DistDiscreteEmpirical distribution.
     * @param stream the stream to use
     * @param observations the observations feeding the distributions. This sortedmap should be filled with observation
     *            probability values. The probability may either reflect the number of times this observation is
     *            observed or may contain a relative probability.
     * @param cummulative reflects whether the probabilities are cummulative
     */
    public DistDiscreteEmpirical(final StreamInterface stream, final SortedMap<Number, Double> observations,
            final boolean cummulative)
    {
        super(stream);
        this.observations = new Observations(observations, cummulative);
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        double u = super.stream.nextDouble();
        return this.observations.getCeilingEntry(new Double(u), ObservationsInterface.CUMPROBABILITY, true)
                .getObservation().longValue();
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (this.observations.contains(new Long(observation), ObservationsInterface.OBSERVATION))
        {
            int index = this.observations
                    .getIndex(this.observations.getEntry(new Long(observation), ObservationsInterface.OBSERVATION));
            if (index > 0)
            {
                return this.observations.get(index).getCumProbability().doubleValue()
                        - this.observations.get(index - 1).getCumProbability().doubleValue();
            }
            return this.observations.get(index).getCumProbability().doubleValue();
        }
        return 0.0;
    }

    /**
     * constructs a grouped map since we do not have the draw and probability specification for the non-grouped discrete
     * empirical distribution.
     * @param observations the non grouped empirical distribution
     * @return a new SortedMap which is not normalized and not cummulative.
     */
    private static SortedMap<Number, Double> constructGroupedMap(final List<? extends Number> observations)
    {
        SortedMap<Number, Double> result = new TreeMap<Number, Double>();
        for (Number entry : observations)
        {
            if (result.containsKey(entry))
            {
                Number value = result.get(entry);
                result.put(entry, value.doubleValue() + 1);
            }
            else
            {
                result.put(entry, 1.0);
            }
        }
        return result;
    }
}
