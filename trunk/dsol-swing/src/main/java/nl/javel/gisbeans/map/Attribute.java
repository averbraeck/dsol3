package nl.javel.gisbeans.map;

import nl.tudelft.simulation.dsol.logger.SimLogger;

/**
 * An attribute.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
     * @param layer the layer to add the attribute to
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
