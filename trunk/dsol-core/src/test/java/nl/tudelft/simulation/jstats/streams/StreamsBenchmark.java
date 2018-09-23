package nl.tudelft.simulation.jstats.streams;

/**
 * The StreamsBenchmark provides computational execution speed insight in the different streams.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.0, 2004-03-18
 * @since 1.5
 */
public final class StreamsBenchmark
{
    /**
     * constructs a new StreamBenchmark.
     */
    private StreamsBenchmark()
    {
        super();
        // unreachable code
    }

    /**
     * benchmarks a stream by drawing 1000000 double values.
     * @param stream the stream to test
     * @return the execution time in milliseconds
     */
    public static long benchmark(final StreamInterface stream)
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++)
        {
            stream.nextDouble();
        }
        return System.currentTimeMillis() - startTime;
    }

    /**
     * executes the benchmark
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        System.out.println("Java2Random : " + StreamsBenchmark.benchmark(new Java2Random()));
        System.out.println("MersenneTwister : " + StreamsBenchmark.benchmark(new MersenneTwister()));
        System.out.println("DX120Generator : " + StreamsBenchmark.benchmark(new DX120Generator()));
    }
}
