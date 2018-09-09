package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Erlang distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/ErlangDistribution.html"> http://mathworld.wolfram.com/ErlangDistribution.html
 * </a>
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
public class DistErlang extends DistContinuous
{
    /** k is the k-value of the erlang distribution. */
    private int k;

    /** beta is the beta value of the erlang distribution. */
    private double beta;

    /** betak is the mean value of the erlang distribution. */
    private double betak;

    /** distGamma is the underlying gamma distribution. */
    private DistGamma distGamma;

    /** GAMMABORDER is the gammaBorder. */
    private static final short GAMMABORDER = 10;

    /**
     * constructs a new Erlang distribution.
     * @param stream the numberstream
     * @param k is the k-parameter
     * @param beta is the beta-parameter
     */
    public DistErlang(final StreamInterface stream, final int k, final double beta)
    {
        super(stream);
        if ((k > 0) && (beta > 0.0))
        {
            this.k = k;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error Erlang - k <= 0 or beta < 0");
        }
        if (this.k <= DistErlang.GAMMABORDER)
        {
            this.betak = -this.beta / this.k;
        }
        else
        {
            this.distGamma = new DistGamma(stream, this.k, this.beta);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        if (this.k <= DistErlang.GAMMABORDER)
        {
            // according to Law and Kelton, Simulation Modeling and Analysis
            // repeated drawing and composition is usually faster for k<=10
            double product = 1.0;
            for (int i = 1; i <= this.k; i++)
            {
                product = product * this.stream.nextDouble();
            }
            return this.betak * Math.log(product);
        }
        // and using the gamma distribution is faster for k>10
        return this.distGamma.draw();
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation < 0)
        {
            return 0;
        }
        return this.beta * Math.exp(-this.beta * observation
                * (Math.pow(this.beta * observation, this.k - 1) / ProbMath.faculty(this.k - 1)));
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Erlang(" + this.k + "," + this.beta + ")";
    }
}
