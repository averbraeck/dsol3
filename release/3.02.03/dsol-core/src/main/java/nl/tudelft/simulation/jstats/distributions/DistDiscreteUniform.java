package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The discrete Uniform distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/UnifomrDistribution.html">
 * http://mathworld.wolfram.com/UniformDistribution.html </a>
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistDiscreteUniform extends DistDiscrete
{
    /** min is the minimum value of this distribution. */
    private long min;

    /** max is the maximum value of this distribution. */
    private long max;

    /**
     * constructs a new uniform distribution. Random occurence with several possible outcomes, each of which is equally
     * likely.
     * @param stream the numberstream
     * @param min the minimal value
     * @param max the maximum value
     */
    public DistDiscreteUniform(final StreamInterface stream, final long min, final long max)
    {
        super(stream);
        this.min = min;
        if (max >= this.min)
        {
            this.max = max;
        }
        else
        {
            throw new IllegalArgumentException("Error Discrete Uniform - min >= max");
        }
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        return this.stream.nextInt((int) this.min, (int) this.max);
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation > this.min && observation < this.max)
        {
            return 1 / ((double) this.max - this.min + 1);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DiscreteUniform(" + this.min + "," + this.max + ")";
    }
}
