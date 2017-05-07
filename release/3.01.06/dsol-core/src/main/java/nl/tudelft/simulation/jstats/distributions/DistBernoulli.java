package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Bernouilli distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/BernouilliDistribution.html">
 * http://mathworld.wolfram.com/BernouilliDistribution.html </a>
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistBernoulli extends DistDiscrete
{
    /** p is the p-value of the Bernouilli distribution. */
    private double p;

    /**
     * constructs a new Bernoulli distribution. Random occurence with two possible outcomes; used to generate other
     * discrete random variates.
     * @param stream is the stream
     * @param p the p-value of a Bernoulli distribution
     */
    public DistBernoulli(final StreamInterface stream, final double p)
    {
        super(stream);
        if ((p >= 0.0) && (p <= 1.0))
        {
            this.p = p;
        }
        else
        {
            throw new IllegalArgumentException("Error Exponential - p<0 or p>1 (p=" + p + ")");
        }
    }

    /**
     * draws the next value from the Bernoulli distribution. More information on this distribution can be found at <br>
     * <a href="http://mathworld.wolfram.com/BernoulliDistribution.html">http://mathworld.wolfram.com/
     * BernoulliDistribution.html</a>.
     * @return the next value {0,1}.
     */
    @Override
    public long draw()
    {
        if (this.stream.nextDouble() <= this.p)
        {
            return 1L;
        }
        return 0L;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation == 0)
        {
            return 1 - this.p;
        }
        if (observation == 1)
        {
            return this.p;
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Bernoulli(" + this.p + ")";
    }
}
