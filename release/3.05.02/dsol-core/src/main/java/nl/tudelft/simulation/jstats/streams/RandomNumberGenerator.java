package nl.tudelft.simulation.jstats.streams;

import nl.tudelft.simulation.language.DSOLException;
import nl.tudelft.simulation.language.reflection.StateSaver;

/**
 * The RandomNumberGenerator class provides an abstract basis for all pseudo random number generators.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
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
     * @param seed long; the seed of the generator.
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
     * @param bits int; the number of bits used
     * @return the next value.
     */
    protected abstract long next(int bits);

    /**
     * Returns the next pseudorandom, uniformly distributed <code>boolean</code> value from this random number generator's
     * sequence. The general contract of <code>nextBoolean</code> is that one <code>boolean</code> value is pseudorandomly
     * generated and returned. The values <code>true</code> and <code>false</code> are produced with (approximately) equal
     * probability. The method <code>nextBoolean</code> is implemented by class <code>Random</code> as follows: <blockquote>
     * 
     * <pre>
     * public boolean nextBoolean()
     * {
     *     return next(1) != 0;
     * }
     * </pre>
     * 
     * </blockquote>
     * @return the next pseudorandom, uniformly distributed <code>boolean</code> value from this random number generator's
     *         sequence.
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
     * The general contract of <code>nextDouble</code> is that one <code>double</code> value, chosen (approximately) uniformly
     * from the range <code>0.0d</code> (inclusive) to <code>1.0d</code> (exclusive), is pseudorandomly generated and returned.
     * All 2 <sup>64 </sup> possible <code>float</code> values of the form <i>m&nbsp;x&nbsp; </i>2 <sup>-64 </sup>, where <i>m
     * </i> is a positive integer less than 2 <sup>64 </sup>, are produced with (approximately) equal probability.
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
     * The general contract of <code>nextFloat</code> is that one <code>float</code> value, chosen (approximately) uniformly
     * from the range <code>0.0f</code> (inclusive) to <code>1.0f</code> (exclusive), is pseudorandomly generated and returned.
     * All 2<sup>24</sup> possible <code>float</code> values of the form <i>m&nbsp;x&nbsp;</i>2<sup>-24</sup>, where <i>m </i>
     * is a positive integer less than 2 <sup>24 </sup>, are produced with (approximately) equal probability. The method
     * <code>nextFloat</code> is implemented by class <code>Random</code> as follows: <blockquote>
     * 
     * <pre>
     *            public float nextFloat() {
     *            return next(24) / ((float)(1 &lt; &lt; 24));
     *            }
     * </pre>
     * 
     * </blockquote> The hedge "approximately" is used in the foregoing description only because the next method is only
     * approximately an unbiased source of independently chosen bits. If it were a perfect source or randomly chosen bits, then
     * the algorithm shown would choose <code>float</code> values from the stated range with perfect uniformity.
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
     * Returns the next pseudorandom, uniformly distributed <code>int</code> value from this random number generator's sequence.
     * The general contract of <code>nextInt</code> is that one <code>int</code> value is pseudorandomly generated and returned.
     * All 2 <sup>32 </sup> possible <code>int</code> values are produced with (approximately) equal probability. The method
     * <code>nextInt</code> is implemented by class <code>Random</code> as follows: <blockquote>
     * 
     * <pre>
     * public int nextInt()
     * {
     *     return next(32);
     * }
     * </pre>
     * 
     * </blockquote>
     * @return the next pseudorandom, uniformly distributed <code>int</code> value from this random number generator's sequence.
     */
    @Override
    public final int nextInt()
    {
        return (int) this.next(32);
    }

    /**
     * Returns a pseudorandom, uniformly distributed <code>int</code> value between i (inclusive) and j, drawn from this random
     * number generator's sequence. The general contract of <code>nextInt</code> is that one <code>int</code> value in the
     * specified range is pseudorandomly generated and returned. All <code>n</code> possible <code>int</code> values are
     * produced with (approximately) equal probability.
     * @param i int; the lower value
     * @param j int; the higher value
     * @return the result
     */
    @Override
    public final synchronized int nextInt(final int i, final int j)
    {
        if (i < 0 || j <= 0 || i >= j)
        {
            throw new IllegalArgumentException("i, j must be positive");
        }
        return i + (int) Math.floor((j - i + 1) * this.nextDouble());
    }

    /**
     * Returns the next pseudorandom, uniformly distributed <code>long</code> value from this random number generator's
     * sequence. The general contract of <code>nextLong</code> is that one long value is pseudorandomly generated and returned.
     * All 2 <sup>64 </sup> possible <code>long</code> values are produced with (approximately) equal probability. The method
     * <code>nextLong</code> is implemented by class <code>Random</code> as follows: <blockquote>
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
    public abstract void setSeed(long seed);

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
    public final byte[] saveState() throws StreamException
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
    public final void restoreState(final byte[] state) throws StreamException
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
