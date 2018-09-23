package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Exponential distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">
 * http://mathworld.wolfram.com/ExponentialDistribution.html </a>
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
public class DistExponential extends DistContinuous
{
    /** mean is the mean value of the exponential distribution. */
    private double mean;

    /**
     * constructs a new exponential function. The exponential distribution describes the interarrival times of
     * "cutomers" to a system that occur at a constant rate.
     * @param stream the numberstream
     * @param mean the mean (mean &gt; 0)
     */
    public DistExponential(final StreamInterface stream, final double mean)
    {
        super(stream);
        if (mean > 0.0)
        {
            this.mean = mean;
        }
        else
        {
            throw new IllegalArgumentException("Error Exponential - mean<=0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return -this.mean * Math.log(this.stream.nextDouble());
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= 0)
        {
            return (1 / this.mean) * Math.exp(-observation / this.mean);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Exponential(" + this.mean + ")";
    }
}
