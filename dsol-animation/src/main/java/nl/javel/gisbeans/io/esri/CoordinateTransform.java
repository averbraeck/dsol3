package nl.javel.gisbeans.io.esri;

/**
 * Transforms an (x, y) coordinate to a new (x', y') coordinate <br>
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a> <br>
 *         <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface CoordinateTransform
{
    /**
     * transform the (x, y) coordinate to a new (x', y') coordinate represented as a float[2].
     * @param x double; the original x-coordinate, e.g. lon in dd (double degrees)
     * @param y double; the original y-coordinate, e.g. lat in dd (double degrees)
     * @return the new (x', y') coordinate represented as a float[2]
     */
    float[] floatTransform(double x, double y);

    /**
     * transform the (x, y) coordinate to a new (x', y') coordinate represented as a double[2].
     * @param x double; the original x-coordinate, e.g. lon in dd (double degrees)
     * @param y double; the original y-coordinate, e.g. lat in dd (double degrees)
     * @return the new (x', y') coordinate represented as a double[2]
     */
    double[] doubleTransform(double x, double y);

    /**
     * The identical transformation (x,y) =&gt; (x,y). <br>
     * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a> <br>
     *         <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    class NoTransform implements CoordinateTransform
    {

        /** {@inheritDoc} */
        @Override
        public final float[] floatTransform(final double x, final double y)
        {
            return new float[] {(float) x, (float) y};
        }

        /** {@inheritDoc} */
        @Override
        public final double[] doubleTransform(final double x, final double y)
        {
            return new double[] {x, y};
        }

    }
}
