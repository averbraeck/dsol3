package nl.tudelft.simulation.jstats.streams;

import java.util.Random;

/**
 * The Java2Random is an extention of the <code>java.util.Random</code> class which implements the StreamInterface.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Java2Random extends Random implements StreamInterface
{

    /**
     * seed is a link to the seed value. The reason to store the seed in this variable is that there is no getSeed() on
     * the Java2Random
     */
    protected long seed;

    /**
     * creates a new Java2Random and in initializes with System.currentTimeMillis constructs a new Java2Random.
     */
    public Java2Random()
    {
        this(System.currentTimeMillis());
    }

    /** {@inheritDoc} */
    public Java2Random(final long seed)
    {
        super(seed);
        this.seed = seed;
    }

    /**
     * resets a stream
     * @see nl.tudelft.simulation.jstats.streams.StreamInterface#reset()
     */
    public void reset()
    {
        this.setSeed(this.seed);
    }

    /** {@inheritDoc} */
    public int nextInt(final int i, final int j)
    {
        return i + (int) Math.floor((j - i + 1) * this.nextDouble());
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setSeed(final long seed)
    {
        this.seed = seed;
        super.setSeed(seed);
    }

    /** {@inheritDoc} */
    public long getSeed()
    {
        return this.seed;
    }

}
