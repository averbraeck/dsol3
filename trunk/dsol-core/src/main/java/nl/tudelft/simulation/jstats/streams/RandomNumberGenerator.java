package nl.tudelft.simulation.jstats.streams;

import nl.tudelft.simulation.language.DSOLException;
import nl.tudelft.simulation.language.reflection.StateSaver;

/**
 * The RandomNumberGenerator class provides an abstract for all pseudo random number generators.
 * <p>
 * copyright (c) 2004-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public abstract class RandomNumberGenerator implements StreamInterface
{
    /** */
    private static final long serialVersionUID = 20150426L;

    /** the seed of the generator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long seed = -1;

    /**
     * constructs a new RandomNumberGenerator. The seed value used in the rng is set to System.currentTimeMillis();
     */
    public RandomNumberGenerator()
    {
        this(System.currentTimeMillis());
    }

    /**
     * constructs a new RandomNumberGenerator.
     * @param seed the seed of the generator.
     */
    public RandomNumberGenerator(final long seed)
    {
        super();
        if (seed <= 0)
        {
            throw new IllegalArgumentException("seed(" + seed + ")<=0");
        }
        this.setSeed(seed);
    }

    /** {@inheritDoc} */
    @Override
    public final void reset()
    {
        this.setSeed(this.seed);
    }

    /**
     * returns the next value in the stream.
     * @param bits the number of bits used
     * @return the next value.
     */
    protected abstract long next(int bits);

    /**
     * Returns the next pseudorandom, uniformly distributed <code>boolean</code> value from this random number
     * generator's sequence. The general contract of <tt>nextBoolean</tt> is that one <tt>boolean</tt> value is
     * pseudorandomly generated and returned. The values <code>true</code> and <code>false</code> are produced with
     * (approximately) equal probability. The method <tt>nextBoolean</tt> is implemented by class <tt>Random</tt> as
     * follows: <blockquote>
     * 
     * <pre>
     * public boolean nextBoolean()
     * {
     *     return next(1) != 0;
     * }
     * </pre>
     * 
     * </blockquote>
     * @return the next pseudorandom, uniformly distributed <code>boolean</code> value from this random number
     *         generator's sequence.
     * @since 1.5
     */
    @Override
    public final boolean nextBoolean()
    {
        return next(1) != 0;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed <code>double</code> value between <code>0.0</code> and
     * <code>1.0</code> from this random number generator's sequence.
     * <p>
     * The general contract of <tt>nextDouble</tt> is that one <tt>double</tt> value, chosen (approximately) uniformly
     * from the range <tt>0.0d</tt> (inclusive) to <tt>1.0d</tt> (exclusive), is pseudorandomly generated and returned.
     * All 2 <sup>64 </sup> possible <tt>float</tt> values of the form <i>m&nbsp;x&nbsp; </i>2 <sup>-64 </sup>, where
     * <i>m </i> is a positive integer less than 2 <sup>64 </sup>, are produced with (approximately) equal probability.
     * @return the next pseudorandom, uniformly distributed <code>double</code> value between <code>0.0</code> and
     *         <code>1.0</code> from this random number generator's sequence.
     */
    @Override
    public final double nextDouble()
    {
        long l = ((next(26)) << 27) + next(27);
        return l / (double) (1L << 53);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed <code>float</code> value between <code>0.0</code> and
     * <code>1.0</code> from this random number generator's sequence.
     * <p>
     * The general contract of <tt>nextFloat</tt> is that one <tt>float</tt> value, chosen (approximately) uniformly
     * from the range <tt>0.0f</tt> (inclusive) to <tt>1.0f</tt> (exclusive), is pseudorandomly generated and returned.
     * All 2<sup>24</sup> possible <tt>float</tt> values of the form <i>m&nbsp;x&nbsp;</i>2<sup>-24</sup>, where <i>m
     * </i> is a positive integer less than 2 <sup>24 </sup>, are produced with (approximately) equal probability. The
     * method <tt>nextFloat</tt> is implemented by class <tt>Random</tt> as follows: <blockquote>
     * 
     * <pre>
     *            public float nextFloat() {
     *            return next(24) / ((float)(1 &lt; &lt; 24));
     *            }
     * </pre>
     * 
     * </blockquote> The hedge "approximately" is used in the foregoing description only because the next method is only
     * approximately an unbiased source of independently chosen bits. If it were a perfect source or randomly chosen
     * bits, then the algorithm shown would choose <tt>float</tt> values from the stated range with perfect uniformity.
     * <p>
     * @return the next pseudorandom, uniformly distributed <code>float</code> value between <code>0.0</code> and
     *         <code>1.0</code> from this random number generator's sequence.
     */
    @Override
    public final float nextFloat()
    {
        int i = (int) this.next(24);
        return i / ((float) (1 << 24));
    }

    /**
     * Returns the next pseudorandom, uniformly distributed <code>int</code> value from this random number generator's
     * sequence. The general contract of <tt>nextInt</tt> is that one <tt>int</tt> value is pseudorandomly generated and
     * returned. All 2 <sup>32 </sup> possible <tt>int</tt> values are produced with (approximately) equal probability.
     * The method <tt>nextInt</tt> is implemented by class <tt>Random</tt> as follows: <blockquote>
     * 
     * <pre>
     * public int nextInt()
     * {
     *     return next(32);
     * }
     * </pre>
     * 
     * </blockquote>
     * @return the next pseudorandom, uniformly distributed <code>int</code> value from this random number generator's
     *         sequence.
     */
    @Override
    public final int nextInt()
    {
        return (int) this.next(32);
    }

    /**
     * Returns a pseudorandom, uniformly distributed <tt>int</tt> value between i (inclusive) and j, drawn from this
     * random number generator's sequence. The general contract of <tt>nextInt</tt> is that one <tt>int</tt> value in
     * the specified range is pseudorandomly generated and returned. All <tt>n</tt> possible <tt>int</tt> values are
     * produced with (approximately) equal probability.
     * @param i the lower value
     * @param j the higher value
     * @return the result
     */
    @Override
    public final synchronized int nextInt(final int i, final int j)
    {
        if (i < 0 || j <= 0 || i >= j)
        {
            throw new IllegalArgumentException("i, j must be positive");
        }
        int n = j - i;
        if ((n & -n) == n) // i.e., n is a power of 2
        {
            return (int) (i + (n * next(31)) >> 31);
        }
        int bits, val;
        do
        {
            bits = (int) this.next(31);
            val = bits % n;
        }
        while (bits - val + (n - 1) < 0);
        return i + val;
    }

    /**
     * Returns the next pseudorandom, uniformly distributed <code>long</code> value from this random number generator's
     * sequence. The general contract of <tt>nextLong</tt> is that one long value is pseudorandomly generated and
     * returned. All 2 <sup>64 </sup> possible <tt>long</tt> values are produced with (approximately) equal probability.
     * The method <tt>nextLong</tt> is implemented by class <tt>Random</tt> as follows: <blockquote>
     * 
     * <pre>
     *            public long nextLong() {
     *            return ((long)next(32) &lt; &lt; 32) + next(32);
     *            }
     * </pre>
     * 
     * </blockquote>
     * @return the next pseudorandom, uniformly distributed <code>long</code> value from this random number generator's
     *         sequence.
     */
    @Override
    public final long nextLong()
    {
        return ((next(32)) << 32) + next(32);
    }

    /** {@inheritDoc} */
    @Override
    public abstract void setSeed(final long seed);

    /** {@inheritDoc} */
    @Override
    public final long getSeed()
    {
        return this.seed;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return this.getClass().toString() + "[" + this.seed + "]";
    }

    /** {@inheritDoc} */
    @Override
    public final Object saveState() throws StreamException
    {
        try
        {
            return StateSaver.saveState(this);
        }
        catch (DSOLException exception)
        {
            throw new StreamException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void restoreState(final Object state) throws StreamException
    {
        try
        {
            StateSaver.restoreState(this, state);
        }
        catch (DSOLException exception)
        {
            throw new StreamException(exception);
        }
    }

}
