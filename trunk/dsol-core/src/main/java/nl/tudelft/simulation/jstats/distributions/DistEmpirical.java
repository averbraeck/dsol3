package nl.tudelft.simulation.jstats.distributions;

import java.util.List;
import java.util.SortedMap;

import nl.tudelft.simulation.jstats.distributions.empirical.Observations;
import nl.tudelft.simulation.jstats.distributions.empirical.ObservationsInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:53 $
 * @since 1.5
 */
public class DistEmpirical extends DistContinuous
{
    /** is the distribution grouped? */
    private ObservationsInterface observations = null;

    /**
     * constructs a new DistEmpirical.
     * @param stream the stream to use
     * @param observations the observations underlying this empirical distribution. The observations do not need to be
     *            sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final ObservationsInterface observations)
    {
        super(stream);
        this.observations = observations;
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream the stream to use
     * @param observations the observations underlying this empirical distribution. The observations do not need to be
     *            sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final Double[] observations)
    {
        super(stream);
        this.observations = new Observations(observations);
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream the stream to use
     * @param observations the observations underlying this empirical distribution. The observations do not need to be
     *            sorted. Double observations are allowed and are used.
     */
    public DistEmpirical(final StreamInterface stream, final List<Double> observations)
    {
        this(stream, observations.toArray(new Double[observations.size()]));
    }

    /**
     * constructs a new DistEmpirical.
     * @param stream the stream to use
     * @param observations the observations underlying this empirical distribution. The observations do not need to be
     *            sorted. Double observations are allowed and are used.
     * @param cummulative are the probabilities cummulative?
     */
    public DistEmpirical(final StreamInterface stream, final SortedMap<Number, Double> observations,
            final boolean cummulative)
    {
        super(stream);
        this.observations = new Observations(observations, cummulative);
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
     * draws a new random value based on the empirical distribution and considers the underlying observations as
     * grouped. The formula used reflects Law and Kelton, Simulation Modeling and Analysis, page 470 of grouped data.
     * @return the next random value.
     */
    private double drawGrouped()
    {
        double u = super.stream.nextDouble();
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(new Double(u), ObservationsInterface.CUMPROBABILITY, true);
        int j = this.observations.getIndex(p);
        ObservationsInterface.Entry q = this.observations.get(j + 1);
        double result = p.getObservation().doubleValue();
        result =
                result + (u - p.getCumProbability().doubleValue())
                        * (q.getObservation().doubleValue() - p.getObservation().doubleValue())
                        / (q.getCumProbability().doubleValue() - p.getCumProbability().doubleValue());
        return result;
    }

    /**
     * draws a new random value based on the empirical distribution and considers the underlying observations as non
     * grouped. The formula used reflects Law and Kelton, Simulation Modeling and Analysis, page 470 of grouped data.
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
    public double probDensity(final double observation)
    {
        if (observation < this.observations.get(0).getObservation().doubleValue())
        {
            return 0;
        }
        if (this.observations.get(this.observations.size() - 1).getObservation().doubleValue() <= observation)
        {
            return 1;
        }
        if (this.observations.isGrouped())
        {
            return this.probDensityGrouped(observation);
        }
        return this.probDensityNonGrouped(observation);
    }

    /**
     * returns the probability density of the observation. This method is based on the underlying empirical distribution
     * and considers the underlying observations as grouped. The formula used reflects Law and Kelton, Simulation
     * Modeling and Analysis, page 327 of grouped data.
     * @param observation the observation whose cummulative probability is returned.
     * @return the cummulative probability of observation
     */
    private double probDensityGrouped(final double observation)
    {
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(new Double(observation), ObservationsInterface.OBSERVATION, true);
        int j = this.observations.getIndex(p);
        ObservationsInterface.Entry q = this.observations.get(j + 1);
        return p.getCumProbability().doubleValue() + (observation - p.getObservation().doubleValue())
                / (q.getObservation().doubleValue() - p.getObservation().doubleValue())
                * (q.getCumProbability().doubleValue() - p.getCumProbability().doubleValue());
    }

    /**
     * returns the probability density of the observation. This method is based on the underlying empirical distribution
     * and considers the underlying observations as grouped. The formula used reflects Law and Kelton, Simulation
     * Modeling and Analysis, page 327 of grouped data.
     * @param observation the observation whose cummulative probability is returned.
     * @return the cummulative probability of observation
     */
    private double probDensityNonGrouped(final double observation)
    {
        ObservationsInterface.Entry p =
                this.observations.getPrecedingEntry(new Double(observation), ObservationsInterface.OBSERVATION, true);
        int i = this.observations.getIndex(p) + 1;
        ObservationsInterface.Entry q = this.observations.get(i);
        return ((i - 1) / ((double) (this.observations.size() - 1)))
                + ((observation - p.getObservation().doubleValue()))
                / ((this.observations.size() - 1) * (q.getObservation().doubleValue() - p.getObservation()
                        .doubleValue()));
    }
}
