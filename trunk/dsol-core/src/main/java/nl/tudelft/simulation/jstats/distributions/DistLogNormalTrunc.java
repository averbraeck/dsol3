package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Truncated Lognormal distribution.
 * <p>
 * (c) copyright 2020-2021 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 */
public class DistLogNormalTrunc extends DistLogNormal
{
    /** */
    private static final long serialVersionUID = 1L;

    /** minimum x-value of the distribution. */
    private final double min;

    /** maximum x-value of the distribution. */
    private final double max;

    /** Non-truncated Lognormal probability density of the min. */
    private final double lognormalProbMin;

    /** Non-truncated Lognormal probability density of the max. */
    private final double lognormalProbMax;

    /**
     * Construct a truncated lognormal distribution with mu=0, sigma=1 with given min and max.
     * @param stream StreamInterface; the numberstream
     * @param min double; minimum x-value of the distribution
     * @param max double; maximum x-value of the distribution
     */
    public DistLogNormalTrunc(final StreamInterface stream, final double min, final double max)
    {
        this(stream, 0.0, 1.0, min, max);
    }

    /**
     * constructs a truncated lognormal distribution with mu and sigma and given min and max.
     * @param stream StreamInterface; the numberstream
     * @param mu double; the mean
     * @param sigma double; the standard deviation
     * @param min double; minimum x-value of the distribution
     * @param max double; maximum x-value of the distribution
     */
    public DistLogNormalTrunc(final StreamInterface stream, final double mu, final double sigma, final double min,
            final double max)
    {
        super(stream, mu, sigma);
        if (max < min)
        {
            throw new IllegalArgumentException("Error Lognormal Truncated - max < min");
        }
        this.min = min;
        this.max = max;
        this.lognormalProbMin = super.getCumulativeProbability(this.min);
        this.lognormalProbMax = super.getCumulativeProbability(this.max);
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return super.getInverseCumulativeProbability(
                this.lognormalProbMin + (this.lognormalProbMax - this.lognormalProbMin) * this.stream.nextDouble());
    }

    /** {@inheritDoc} */
    @Override
    public double getCumulativeProbability(final double x)
    {
        if (x < this.min)
        {
            return 0.0;
        }
        if (x > this.max)
        {
            return 1.0;
        }
        return (super.getCumulativeProbability(x) - this.lognormalProbMin) / (this.lognormalProbMax - this.lognormalProbMin);
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x < this.min || x > this.max)
        {
            return 0.0;
        }
        return super.getProbabilityDensity(x) / (this.lognormalProbMax - this.lognormalProbMin);
    }

    /** {@inheritDoc} */
    @Override
    public double getInverseCumulativeProbability(final double cumulativeProbability)
    {
        return super.getInverseCumulativeProbability(
                cumulativeProbability * (this.lognormalProbMax - this.lognormalProbMin) + this.lognormalProbMin);
    }

    /**
     * @return min
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * @return max
     */
    public final double getMax()
    {
        return this.max;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "LogNormalTrunc(" + this.mu + "," + this.sigma + "," + this.min + "," + this.max + ")";
    }

    /**
     * Test of the truncated lognormal distribution.
     * @param args empty
     */
    public static void main(final String[] args)
    {
        StreamInterface stream = new MersenneTwister(2);
        DistLogNormal tln10 = new DistLogNormalTrunc(stream, Math.log(5.0), Math.log(2.0), 2.0, 14.0);
        for (double x = 0.0; x <= 15.0; x += 0.01)
        {
            // System.out.println(x + "\t" + tln10.getProbabilityDensity(x) + "\t" + tln10.getCumulativeProbability(x));
        }

        for (int i = 0; i < 100000; i++)
        {
            // System.out.println(tln10.draw());
        }
        
        DistLogNormalTrunc dl = new DistLogNormalTrunc(stream, 1.0,  0.75, 0, 10);
        System.out.println(dl.getProbabilityDensity(2.0) + "   " + dl.getProbabilityDensity(4.0));
        System.out.println(dl.getCumulativeProbability(2.0) + "   " + dl.getCumulativeProbability(4.0));
        System.out.println(dl.getInverseCumulativeProbability(0.5));
    }

}
