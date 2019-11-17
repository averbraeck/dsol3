package nl.javel.gisbeans.map;

import nl.tudelft.simulation.dsol.logger.SimLogger;

/**
 * An attribute.
 * <p>
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a> <br>
 *         <a href="mailto:peter.jacobs@javel.nl">Peter Jacobs </a>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 * @since 1.4
 */
public class Attribute extends AbstractAttribute
{
    /** RADIANS */
    public static final short RADIANS = 0;

    /** DEGREES */
    public static final short DEGREES = 1;

    /** the angleColumn. */
    private int angleColumn = 0;

    /** the valueColumn. */
    private int valueColumn = 0;

    /** the mode of the angle. */
    private short mode = RADIANS;

    /**
     * constructs a new Attribute.
     * @param layer LayerInterface; the layer to add the attribute to
     * @param mode short; the mode (degrees or radians)
     * @param angleColumn int; the angleColumn
     * @param valueColumn int; the valueColumn
     */
    public Attribute(LayerInterface layer, short mode, int angleColumn, int valueColumn)
    {
        super(layer);
        this.angleColumn = angleColumn;
        this.valueColumn = valueColumn;
    }

    /** {@inheritDoc} */
    @Override
    public double getAngle(int shapeIndex)
    {
        if (this.angleColumn == -1)
        {
            return 0.0;
        }
        try
        {
            double angle = Double.parseDouble(super.layer.getDataSource().getAttributes()[shapeIndex][this.angleColumn]);
            if (this.mode == DEGREES)
            {
                angle = Math.toRadians(angle);
            }
            return angle;
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
            return 0.0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(int shapeIndex)
    {
        try
        {
            return super.layer.getDataSource().getAttributes()[shapeIndex][this.valueColumn];
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
            return "";
        }
    }
}
