package nl.tudelft.simulation.jstats.streams;

/**
 * A java implementation of the Mersenne Twister pseudo random number generator.
 * <P>
 * This is a Java version of the C-program for MT19937: Integer version.
 * genrand() generates one pseudorandom unsigned integer (32bit) which is
 * uniformly distributed among 0 to 2^32-1 for each call. sgenrand(seed) set
 * initial values to the working area of 624 words. (seed is any 32-bit integer
 * except for 0).
 * <p>
 * Orignally Coded by Takuji Nishimura, considering the suggestions by Topher
 * Cooper and Marc Rieffel in July-Aug. 1997. More information can be found at
 * <A HREF="http://www.math.keio.ac.jp/matumoto/emt.html">
 * http://www.math.keio.ac.jp/matumoto/emt.html </A>.
 * <P>
 * Makoto Matsumoto and Takuji Nishimura, the original authors ask "When you use
 * this, send an email to: matumoto@math.keio.ac.jp with an appropriate
 * reference to your work" You might also point out this was a translation.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:54 $
 * @since 1.5
 */
public class MersenneTwister extends RandomNumberGenerator
{
    /** Period parameter N */
    private static final int N = 624;

    /** Period parameter M */
    private static final int M = 397;

    /** constant vector a */
    private static final int MATRIX_A = 0x9908b0df;

    /** most significant w-r bits */
    private static final int UPPER_MASK = 0x80000000;

    /** least significant w-r bits */
    private static final int LOWER_MASK = 0x7fffffff;

    /** tempering mask B */
    private static final int TEMPERING_MASK_B = 0x9d2c5680;

    /** tempering mask B */
    private static final int TEMPERING_MASK_C = 0xefc60000;

    /** unsigned mask for promoting int -> long */
    private static final int UMASK = (1 << 31) - 1;

    /** the array for the state vector */
    private int[] mt;

    /** The counter mti==N+1 means mt[N] is not initialized */
    private int mti;

    /** magic01 */
    private int[] mag01;

    /**
     * constructs a new Mersenne Twister.
     * <code>System.currentTimeMillis()</code> is used as seed value.
     */
    public MersenneTwister()
    {
        this(System.currentTimeMillis());
    }

    /**
     * Constructor using a given seed.
     * 
     * @param seed The initial seed.
     */
    public MersenneTwister(final long seed)
    {
        super(seed);
    }

    /**
     * initalizes the MersenneTwister
     */
    private void initialize()
    {
        this.mt = new int[N];

        this.mt[0] = ((int) super.seed) & UMASK;
        if (this.mt[0] == 0)
        {
            // super.seed=Integer.MAXValue --> seed & UMASK==0
            // We set the seed again and enforce a different value.
            this.setSeed(System.currentTimeMillis());
        }
        for (this.mti = 1; this.mti < N; this.mti++)
        {
            this.mt[this.mti] = (69069 * this.mt[this.mti - 1]);
        }

        // mag01[x] = x * MATRIX_A for x=0,1
        this.mag01 = new int[2];
        this.mag01[0] = 0x0;
        this.mag01[1] = MATRIX_A;
    }

    /**
     * @see nl.tudelft.simulation.jstats.streams.RandomNumberGenerator#next(int)
     */
    @Override
    protected synchronized long next(final int bits)
    {
        if (bits < 0 || bits > 64)
        {
            throw new IllegalArgumentException("bits (" + bits
                    + ") not in range [0,64]");
        }
        int y;
        if (this.mti >= N) // generate N words at one time
        {
            int kk;
            for (kk = 0; kk < N - M; kk++)
            {
                y = (this.mt[kk] & UPPER_MASK) | (this.mt[kk + 1] & LOWER_MASK);
                this.mt[kk] = this.mt[kk + M] ^ (y >>> 1) ^ this.mag01[y & 0x1];
            }
            for (; kk < N - 1; kk++)
            {
                y = (this.mt[kk] & UPPER_MASK) | (this.mt[kk + 1] & LOWER_MASK);
                this.mt[kk] = this.mt[kk + (M - N)] ^ (y >>> 1)
                        ^ this.mag01[y & 0x1];
            }
            y = (this.mt[N - 1] & UPPER_MASK) | (this.mt[0] & LOWER_MASK);
            this.mt[N - 1] = this.mt[M - 1] ^ (y >>> 1) ^ this.mag01[y & 0x1];
            this.mti = 0;
        }
        y = this.mt[this.mti++];
        y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
        y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
        y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
        y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)
        if (bits <= 32)
        {
            return y >>> (32 - bits);
        }
        return y << 32 + this.next(bits - 32);
    }

    /**
     * @see nl.tudelft.simulation.jstats.streams.StreamInterface#setSeed(long)
     */
    @Override
    public synchronized void setSeed(final long seed)
    {
        super.seed = seed;
        this.initialize();
    }
}