package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Triangular distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/TriangularDistribution.html">
 * http://mathworld.wolfram.com/TriangularDistribution.html </a>
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistTriangular extends DistContinuous
{
    /** a is the minimum. */
    private double a;

    /** b is the mode. */
    private double b;

    /** c is the maximum. */
    private double c;

    /**
     * constructs a new triangular distribution
     * @param stream the numberstream
     * @param a the minimum
     * @param b the mode
     * @param c the maximum
     */
    public DistTriangular(final StreamInterface stream, final double a, final double b, final double c)
    {
        super(stream);
        if ((a < b) && (b < c))
        {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        else
        {
            throw new IllegalArgumentException("Error condition for tria: a<b<c");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double u = this.stream.nextDouble();
        if (u <= ((this.b - this.a) / (this.c - this.a)))
        {
            return this.a + Math.sqrt((this.b - this.a) * (this.c - this.a) * u);
        }
        return this.c - Math.sqrt((this.c - this.a) * (this.c - this.b) * (1.0d - u));
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= this.a && observation <= this.b)
        {
            return 2 * (observation - this.a) / ((this.c - this.a) * (this.b - this.a));
        }
        if (observation >= this.b && observation <= this.c)
        {
            return 2 * (this.c - observation) / ((this.c - this.a) * (this.c - this.b));
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Triangular(" + this.a + "," + this.b + "," + this.c + ")";
    }
}