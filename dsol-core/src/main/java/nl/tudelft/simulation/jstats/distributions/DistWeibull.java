package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Weibull distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/WeibullDistribution.html"> http://mathworld.wolfram.com/WeibullDistribution.html
 * </a>
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
public class DistWeibull extends DistContinuous
{
    /** alpha is the alpha parameter. */
    private double alpha;

    /** beta is the beta parameter. */
    private double beta;

    /**
     * constructs a new Weibull distribution.
     * @param stream the numberstream
     * @param alpha (shape)
     * @param beta (scale)
     */
    public DistWeibull(final StreamInterface stream, final double alpha, final double beta)
    {
        super(stream);
        if ((alpha > 0.0) && (beta > 0.0))
        {
            this.alpha = alpha;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error Weibull - alpha <= 0.0 or beta <= 0.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return this.beta * Math.pow(-Math.log(this.stream.nextDouble()), 1.0d / this.alpha);
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0)
        {
            return this.alpha * Math.pow(this.beta, -this.alpha) * Math.pow(observation, this.alpha - 1)
                    * Math.exp(Math.pow(-1 * (observation / this.beta), this.alpha));
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Weibull(" + this.alpha + "," + this.beta + ")";
    }
}
