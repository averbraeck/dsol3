package nl.tudelft.simulation.jstats.streams;

import java.io.Serializable;

/**
 * The StreamInterface defines the streams to be used within the JSTATS package. Potential implementations include the
 * pseudo random stream, the fully one-time random stream, etc.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public interface StreamInterface extends Serializable
{
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
    boolean nextBoolean();

    /**
     * Method return a (pseudo)random number from the stream over the interval (0,1) using this stream, after advancing
     * its state by one step.
     * @return double the (pseudo)random number
     */
    double nextDouble();

    /**
     * Method return a (pseudo)random number from the stream over the interval (0,1) using this stream, after advancing
     * its state by one step.
     * @return float the (pseudo)random number
     */
    float nextFloat();

    /**
     * Method return a (pseudo)random number from the stream over using this stream, after advancing its state by one
     * step.
     * @return int the (pseudo)random number
     */
    int nextInt();

    /**
     * Method returns (pseudo)random number from the stream over the integers i and j .
     * @param i the minimal value
     * @param j the maximum value
     * @return int
     */
    int nextInt(int i, int j);

    /**
     * Method return a (pseudo)random number from the stream over using this stream, after advancing its state by one
     * step.
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
     * @param seed the new seed
     */
    void setSeed(final long seed);

    /**
     * resets the stream.
     */
    void reset();
    
    /**
     * save the state of the RNG into an object, e.g. to roll it back to this state.
     * @return the state as an object specific to the RNG.
     * @throws StreamException when getting the state fails.
     */
    Object saveState() throws StreamException;
    
    /**
     * restore the state to an earlier saved state object.
     * @param state the earlier saved state to which the RNG rolls back.
     * @throws StreamException when resetting the state fails.
     */
    void restoreState(Object state) throws StreamException;
}
