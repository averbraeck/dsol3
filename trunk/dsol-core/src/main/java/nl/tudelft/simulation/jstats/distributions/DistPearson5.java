package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;
import cern.jet.stat.Gamma;

/**
 * The Pearson5 distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/Pearson5Distribution.html">
 * http://mathworld.wolfram.com/Pearson5Distribution.html </a>
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
public class DistPearson5 extends DistContinuous
{

    /** dist is the gamma distribution. */
    private DistGamma dist;

    /** alpha is the alpha parameter of the distribution. */
    private double alpha;

    /** beta is the beta parameter of the distribtution. */
    private double beta;

    /**
     * constructs a new Pearson5 distribution
     * @param stream the numberstream
     * @param alpha the scale parameter
     * @param beta the shape parameter
     */
    public DistPearson5(final StreamInterface stream, final double alpha, final double beta)
    {
        super(stream);
        if ((alpha > 0.0) && (beta > 0.0))
        {
            this.alpha = alpha;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error alpha <= 0.0 or beta <= 0.0");
        }
        this.dist = new DistGamma(stream, this.alpha, 1.0d / this.beta);
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991
        // pages 492-493
        return 1.0d / this.dist.draw();
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0)
        {
            return (Math.pow(observation, -1 * (this.alpha + 1)) * Math.exp(-this.beta / observation))
                    / (Math.pow(this.beta, -this.alpha) * Gamma.gamma(this.alpha));
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Pearson5(" + this.alpha + "," + this.beta + ")";
    }
}
