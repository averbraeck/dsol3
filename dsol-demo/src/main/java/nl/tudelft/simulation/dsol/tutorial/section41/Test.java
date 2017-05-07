package nl.tudelft.simulation.dsol.tutorial.section41;

import java.util.Random;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.DX120Generator;
import nl.tudelft.simulation.jstats.streams.Java2Random;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * <p>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Mar 4, 2017
 */
public final class Test
{
    /**
     * 
     */
    private Test()
    {
        //
    }

    /**
     * @param args args
     */
    public static void main(final String[] args)
    {
        Random random = new Random(10);
        long timeMs = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            random.nextGaussian();
        }
        System.out.println("1000000x random.nextGaussian() takes " + (System.currentTimeMillis() - timeMs) + " msec.");

        StreamInterface si = new MersenneTwister(10);
        DistContinuous dist = new DistNormal(si);
        timeMs = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            dist.draw();
        }
        System.out.println(
                "1000000x DistNormal() with MersenneTwister takes " + (System.currentTimeMillis() - timeMs) + " msec.");

        si = new Java2Random(10);
        dist = new DistNormal(si);
        timeMs = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            dist.draw();
        }
        System.out.println(
                "1000000x DistNormal() with Java2Random takes " + (System.currentTimeMillis() - timeMs) + " msec.");

        si = new DX120Generator(10);
        dist = new DistNormal(si);
        timeMs = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            dist.draw();
        }
        System.out
                .println("1000000x DistNormal() with DX120 takes " + (System.currentTimeMillis() - timeMs) + " msec.");

        timeMs = System.currentTimeMillis();
        double z = 0.0;
        for (double a = 0; a < 1000000; a += 1)
        {
            z += Math.exp(a);
        }
        System.out.println("1000000x Math.exp(a) takes " + (System.currentTimeMillis() - timeMs) + " msec.");

        timeMs = System.currentTimeMillis();
        z = 0.0;
        for (double a = 0; a < 1000000; a += 1)
        {
            z += Math.sqrt(a);
        }
        System.out.println("1000000x Math.sqrt(a) takes " + (System.currentTimeMillis() - timeMs) + " msec.");

    }

}
