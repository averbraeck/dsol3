package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Constant distribution. This distribution maskers a constant discrete value.
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
public class DistDiscreteConstant extends DistDiscrete
{
    /** value is the value of the distribution. */
    private long value;

    /**
     * creates a new discrete constant distribution
     * @param stream the numberstream
     * @param value the value for this distribution
     */
    public DistDiscreteConstant(final StreamInterface stream, final long value)
    {
        super(stream);
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        this.stream.nextDouble();
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation == this.value)
        {
            return 1.0;
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DiscreteConstant(" + this.value + ")";
    }
}
