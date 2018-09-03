package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The LogNormal distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/LogNormalDistribution.html">
 * http://mathworld.wolfram.com/LogNormalDistribution.html </a>
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistLogNormal extends DistNormal
{
    /**
     * constructs a new logaritmic normal distribution
     * @param stream the numberStream
     * @param mu the medium
     * @param sigma the standard deviation
     */
    public DistLogNormal(final StreamInterface stream, final double mu, final double sigma)
    {
        super(stream);
        this.mu = mu;
        if (sigma > 0.0)
        {
            this.sigma = sigma;
        }
        else
        {
            throw new IllegalArgumentException("Error DistLogNormal - sigma<=0.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double y = this.mu + this.sigma * super.nextGaussian();
        return Math.exp(y);
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0)
        {
            return 1 / (observation * Math.sqrt(2 * Math.PI * Math.pow(this.sigma, 2)))
                    * Math.exp(-1 * Math.pow(Math.log(observation) - this.mu, 2) / (2 * Math.pow(this.sigma, 2)));
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "LogNormal(" + this.mu + "," + this.sigma + ")";
    }
}
