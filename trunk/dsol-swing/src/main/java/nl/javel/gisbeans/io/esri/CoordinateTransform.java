package nl.javel.gisbeans.io.esri;

/**
 * Transforms an (x, y) coordinate to a new (x', y') coordinate
 * <br>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Nov 30, 2015
 */
public interface CoordinateTransform
{
    /**
     * transform the (x, y) coordinate to a new (x', y') coordinate represented as a float[2].
     * @param x the original x-coordinate, e.g. lon in dd (double degrees)
     * @param y the original y-coordinate, e.g. lat in dd (double degrees)
     * @return the new (x', y') coordinate represented as a float[2]
     */
    float[] floatTransform(double x, double y);

    /**
     * transform the (x, y) coordinate to a new (x', y') coordinate represented as a double[2].
     * @param x the original x-coordinate, e.g. lon in dd (double degrees)
     * @param y the original y-coordinate, e.g. lat in dd (double degrees)
     * @return the new (x', y') coordinate represented as a double[2]
     */
    double[] doubleTransform(double x, double y);
    
    /**
     * The identical transformation (x,y) =&gt; (x,y).
     * <br>
     * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
     * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @version Nov 30, 2015
     */
    class NoTransform implements CoordinateTransform
    {

        /** {@inheritDoc} */
        @Override
        public final float[] floatTransform(final double x, final double y)
        {
            return new float[]{(float) x, (float) y};
        }

        /** {@inheritDoc} */
        @Override
        public final double[] doubleTransform(final double x, final double y)
        {
            return new double[]{x, y};
        }
        
    }
}
