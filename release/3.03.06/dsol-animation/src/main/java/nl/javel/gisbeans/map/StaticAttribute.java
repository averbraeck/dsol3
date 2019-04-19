package nl.javel.gisbeans.map;

/**
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a> <br>
 *         <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 * @since 1.4
 */
public class StaticAttribute extends AbstractAttribute
{
    /** the static angle. */
    private double angle = 0.0;

    /** the static value. */
    private String value = null;

    /**
     * constructs a new StaticAttribute.
     * @param layer LayerInterface; the layer
     * @param angle double; the angle
     * @param value String; the value
     */
    public StaticAttribute(LayerInterface layer, double angle, String value)
    {
        super(layer);
        this.angle = angle;
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public double getAngle(int shapeIndex)
    {
        return this.angle;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(int shapeIndex)
    {
        return this.value;
    }
}
