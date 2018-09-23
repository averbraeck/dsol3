package nl.tudelft.simulation.jstats.streams;

import org.junit.Assert;

import junit.framework.TestCase;

/**
 * <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Apr 26, 2015
 */
public class StreamStateSaveRestoreTest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new StreamTest.
     */
    public StreamStateSaveRestoreTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new StreamStateSaveRestoreTest.
     * @param method the name of the test method
     */
    public StreamStateSaveRestoreTest(final String method)
    {
        super(method);
    }

    /**
     * @param stream the stream to draw from.
     * @param n the number of draws to make.
     * @return a number between 10 and 99
     */
    private String draw(final StreamInterface stream, final int n)
    {
        String s = "";
        for (int i = 0; i < n; i++)
        {
            s += stream.nextInt(10, 100) + " ";
        }
        return s;
    }

    /**
     * tests the classes in the reference class.
     */
    public final void test()
    {
        StreamInterface[] streams = {new Java2Random(10), new MersenneTwister(10), new DX120Generator(10)};
        for (int j = 0; j < streams.length; j++)
        {
            try
            {
                StreamInterface rng = streams[j];
                String r = draw(rng, 10);
                Assert.assertEquals("r.len != 30 for j=" + j, r.length(), 30);
                Object state = rng.saveState();
                String s = draw(rng, 10);
                Assert.assertEquals("s.len != 30 for j=" + j, s.length(), 30);
                draw(rng, 20);
                rng.restoreState(state);
                String t = draw(rng, 10);
                Assert.assertEquals("t.len != 30 for j=" + j, t.length(), 30);
                Assert.assertEquals("j=" + j, s, t);
            }
            catch (StreamException se)
            {
                TestCase.fail("StreamException for j=" + j);
            }
        }
    }

}
