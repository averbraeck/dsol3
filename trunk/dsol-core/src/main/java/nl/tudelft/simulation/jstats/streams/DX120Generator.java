package nl.tudelft.simulation.jstats.streams;

/**
 * The DX-120-4 pseudo random number generator. This generator is described in <a
 * href="http://www.cs.memphis.edu/~dengl/dx-rng/dengxu2002.pdf"> A System of High-dimensional, Efficient, Long-cycle
 * and Portable Uniform Random Number Generators </a>.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class DX120Generator extends RandomNumberGenerator
{
    /** the k value of the DX-120 Generator. */
    private static final int K = 120;

    /** the mask value 2^31-1 (or 1 < < 31)-1 */
    private static final int MASK = Integer.MAX_VALUE;

    /** the LCG multiplier. */
    private static final int MULTIPLIER = 16807;

    /** the buffer for this generator. */
    private int[] buffer = null;

    /** indexing attributes. */
    private int index;

    /** indexing attributes. */
    private int k13;

    /** indexing attributes. */
    private int k23;

    /**
     * constructs a new LC48Generator. the seed value used equals System.currentTimeMillis()
     */
    public DX120Generator()
    {
        this(System.currentTimeMillis());
    }

    /**
     * constructs a new LC48Generator.
     * @param seed the seed
     */
    public DX120Generator(final long seed)
    {
        super(seed);
        this.initialize();
    }

    /**
     * initializes the generator
     */
    private void initialize()
    {
        this.buffer = new int[DX120Generator.K];
        this.buffer[0] = ((int) super.seed) & MASK;
        if (this.buffer[0] == 0)
        {
            // super.seed=Integer.MAXValue --> seed & UMASK==0
            // We set the seed again and enforce a different value.
            this.setSeed(System.currentTimeMillis());
        }
        if (this.buffer[0] < 0)
        {
            this.buffer[0] = Math.abs(this.buffer[0] - 1);
        }
        for (int i = 1; i < K; i++)
        {
            this.buffer[i] = (MULTIPLIER * this.buffer[i - 1]) & MASK;
        }
        this.index = K - 1; /* running index */
        this.k13 = K - K / 3 - 1; // (k13 = 79)
        this.k23 = K - 2 * K / 3 - 1; // (k23 = 39)
    }

    /** {@inheritDoc} */
    @Override
    protected synchronized long next(final int bits)
    {
        if (bits < 0 || bits > 64)
        {
            throw new IllegalArgumentException("bits (" + bits + ") not in range [0,64]");
        }
        int tempIndex = this.index;
        if (++this.index >= K)
        {
            this.index = 0; /* wrap around running index */
        }
        if (++this.k13 >= K)
        {
            this.k13 = 0; /* wrap around k13 */
        }
        if (++this.k23 >= K)
        {
            this.k23 = 0; /* wrap around running k23 */
        }
        this.buffer[this.index] =
                (521673 * (this.buffer[this.index] + this.buffer[this.k13] + this.buffer[this.k23] + this.buffer[tempIndex]))
                        & MASK;
        if (bits <= 32)
        {
            return (this.buffer[this.index]) >>> (32 - bits);
        }
        return (this.buffer[this.index]) << 32 + this.next(bits - 32);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setSeed(final long seed)
    {
        this.seed = seed;
        this.initialize();
    }
}
