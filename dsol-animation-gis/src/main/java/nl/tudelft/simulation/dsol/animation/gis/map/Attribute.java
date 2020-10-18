package nl.tudelft.simulation.dsol.animation.gis.map;

import org.djutils.logger.CategoryLogger;

/**
 * Layer attribute implementation.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Attribute extends AbstractAttribute
{
    /** */
    private static final long serialVersionUID = 20201015L;

    /** RADIANS. */
    public static final short RADIANS = 0;

    /** DEGREES. */
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
    public Attribute(final LayerInterface layer, final short mode, final int angleColumn, final int valueColumn)
    {
        super(layer);
        this.angleColumn = angleColumn;
        this.valueColumn = valueColumn;
    }

    /** {@inheritDoc} */
    @Override
    public double getAngle(final int shapeIndex)
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
            CategoryLogger.always().error(exception);
            return 0.0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(final int shapeIndex)
    {
        try
        {
            return super.layer.getDataSource().getAttributes()[shapeIndex][this.valueColumn];
        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
            return "";
        }
    }
}
