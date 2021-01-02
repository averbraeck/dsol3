package nl.tudelft.simulation.jstats.distributions;

import java.util.List;
import java.util.SortedMap;

import nl.tudelft.simulation.jstats.distributions.empirical.Observations;
import nl.tudelft.simulation.jstats.distributions.empirical.ObservationsInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The empirical distribution is a distribution based on a sorted list of observations.
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
public class DistEmpirical extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** is the distribution grouped? */
    private ObservationsInterface observations = null;

    /**
     * constructs a new DistEmpirical.
     * @param stream StreamInterface; the stream to use
     * @param observations ObservationsInterface; the observations underlying this empirical distribution. The observations do
     *            not need to be sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final ObservationsInterface observations)
    {
        super(stream);
        this.observations = observations;
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream StreamInterface; the stream to use
     * @param observations Double[]; the observations underlying this empirical distribution. The observations do not need to be
     *            sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final Double[] observations)
    {
        super(stream);
        this.observations = new Observations(observations);
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream StreamInterface; the stream to use
     * @param observations List&lt;Double&gt;; the observations underlying this empirical distribution. The observations do not
     *            need to be sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final List<Double> observations)
    {
        this(stream, observations.toArray(new Double[observations.size()]));
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream StreamInterface; the stream to use
     * @param observations SortedMap&lt;Number,Double&gt;; the observations underlying this empirical distribution. The
     *            observations do not need to be sorted. Double observations are allowed and are used.
     * @param cumulative boolean; are the probabilities cumulative?
     */
    public DistEmpirical(final StreamInterface stream, final SortedMap<Number, Double> observations, final boolean cumulative)
    {
        super(stream);
        this.observations = new Observations(observations, cumulative);
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        if (this.observations.isGrouped())
        {
            return this.drawGrouped();
        }
        return this.drawNonGrouped();
    }

    /**
     * draws a new random value based on the empirical distribution and considers the underlying observations as grouped. The
     * formula used reflects Law and Kelton, Simulation Modeling and Analysis, page 470 of grouped data.
     * @return the next random value.
     */
    private double drawGrouped()
    {
        double u = super.stream.nextDouble();
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(Double.valueOf(u), ObservationsInterface.CUMPROBABILITY, true);
        int j = this.observations.getIndex(p);
        ObservationsInterface.Entry q = this.observations.get(j + 1);
        double result = p.getObservation().doubleValue();
        result = result + (u - p.getCumProbability().doubleValue())
                * (q.getObservation().doubleValue() - p.getObservation().doubleValue())
                / (q.getCumProbability().doubleValue() - p.getCumProbability().doubleValue());
        return result;
    }

    /**
     * draws a new random value based on the empirical distribution and considers the underlying observations as non grouped.
     * The formula used reflects Law and Kelton, Simulation Modeling and Analysis, page 470 of grouped data.
     * @return the next random value.
     */
    private double drawNonGrouped()
    {
        double u = super.stream.nextDouble();
        double p = (this.observations.size() - 1) * u;
        int i = (int) (Math.floor(p) + 1);
        double xi = this.observations.get(i - 1).getObservation().doubleValue();
        return +xi + (p - i + 1) * (this.observations.get(i).getObservation().doubleValue() - xi);
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x < this.observations.get(0).getObservation().doubleValue())
        {
            return 0;
        }
        if (this.observations.get(this.observations.size() - 1).getObservation().doubleValue() <= x)
        {
            return 1;
        }
        if (this.observations.isGrouped())
        {
            return this.probDensityGrouped(x);
        }
        return this.probDensityNonGrouped(x);
    }

    /**
     * returns the probability density of the observation. This method is based on the underlying empirical distribution and
     * considers the underlying observations as grouped. The formula used reflects Law and Kelton, Simulation Modeling and
     * Analysis, page 327 of grouped data.
     * @param observation double; the observation whose cumulative probability is returned.
     * @return the cumulative probability of observation
     */
    private double probDensityGrouped(final double observation)
    {
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(Double.valueOf(observation), ObservationsInterface.OBSERVATION, true);
        int j = this.observations.getIndex(p);
        ObservationsInterface.Entry q = this.observations.get(j + 1);
        return p.getCumProbability().doubleValue() + (observation - p.getObservation().doubleValue())
                / (q.getObservation().doubleValue() - p.getObservation().doubleValue())
                * (q.getCumProbability().doubleValue() - p.getCumProbability().doubleValue());
    }

    /**
     * returns the probability density of the observation. This method is based on the underlying empirical distribution and
     * considers the underlying observations as grouped. The formula used reflects Law and Kelton, Simulation Modeling and
     * Analysis, page 327 of grouped data.
     * @param observation double; the observation whose cumulative probability is returned.
     * @return the cumulative probability of observation
     */
    private double probDensityNonGrouped(final double observation)
    {
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(Double.valueOf(observation), ObservationsInterface.OBSERVATION, true);
        int i = this.observations.getIndex(p) + 1;
        ObservationsInterface.Entry q = this.observations.get(i);
        return ((i - 1) / ((double) (this.observations.size() - 1))) + ((observation - p.getObservation().doubleValue()))
                / ((this.observations.size() - 1) * (q.getObservation().doubleValue() - p.getObservation().doubleValue()));
    }
}
