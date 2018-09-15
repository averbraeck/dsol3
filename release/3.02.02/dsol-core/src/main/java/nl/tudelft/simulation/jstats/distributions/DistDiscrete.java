package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The discrete distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/DiscreteDistribution.html">
 * http://mathworld.wolfram.com/DiscreteDistribution.html </a>
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
public abstract class DistDiscrete extends Dist
{
    /**
     * constructs a new disctete distribution
     * @param stream the numberstream
     */
    public DistDiscrete(final StreamInterface stream)
    {
        super(stream);
    }

    /**
     * draws the next long from the stream.
     * @return long
     */
    public abstract long draw();

    /**
     * returns the propbability of the observation in this particular distribution.
     * @param observation the discrete observation.
     * @return double the probability.
     */
    public abstract double probability(final int observation);
}
