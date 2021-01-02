package nl.tudelft.simulation.jstats.streams;

import java.io.Serializable;

/**
 * The StreamInterface defines the streams to be used within the JSTATS package. Potential implementations include the pseudo
 * random stream, the fully one-time random stream, etc.
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
public interface StreamInterface extends Serializable
{
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
    boolean nextBoolean();

    /**
     * Method return a (pseudo)random number from the stream over the interval (0,1) using this stream, after advancing its
     * state by one step.
     * @return double the (pseudo)random number
     */
    double nextDouble();

    /**
     * Method return a (pseudo)random number from the stream over the interval (0,1) using this stream, after advancing its
     * state by one step.
     * @return float the (pseudo)random number
     */
    float nextFloat();

    /**
     * Method return a (pseudo)random number from the stream over using this stream, after advancing its state by one step.
     * @return int the (pseudo)random number
     */
    int nextInt();

    /**
     * Method returns (pseudo)random number from the stream over the integers i and j .
     * @param i int; the minimal value
     * @param j int; the maximum value
     * @return int
     */
    int nextInt(int i, int j);

    /**
     * Method return a (pseudo)random number from the stream over using this stream, after advancing its state by one step.
     * @return long the (pseudo)random number
     */
    long nextLong();

    /**
     * returns the seed of the generator.
     * @return long the seed
     */
    long getSeed();

    /**
     * sets the seed of the generator.
     * @param seed long; the new seed
     */
    void setSeed(long seed);

    /**
     * resets the stream.
     */
    void reset();

    /**
     * save the state of the RNG into an object, e.g. to roll it back to this state.
     * @return the state as an object specific to the RNG.
     * @throws StreamException when getting the state fails.
     */
    byte[] saveState() throws StreamException;

    /**
     * restore the state to an earlier saved state object.
     * @param state Object; the earlier saved state to which the RNG rolls back.
     * @throws StreamException when resetting the state fails.
     */
    void restoreState(byte[] state) throws StreamException;
}
