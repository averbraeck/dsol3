package nl.javel.gisbeans.map;

/**
 * An attribute.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 * @version 1.2 Jun 22, 2004
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
     * @param layer
     * @param mode the mode (degrees or radians)
     * @param angleColumn the angleColumn
     * @param valueColumn the valueColumn
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
            double angle =
                    Double.parseDouble(super.layer.getDataSource().getAttributes()[shapeIndex][this.angleColumn]);
            if (this.mode == DEGREES)
            {
                angle = Math.toRadians(angle);
            }
            return angle;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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
            exception.printStackTrace();
            return "";
        }
    }
}
