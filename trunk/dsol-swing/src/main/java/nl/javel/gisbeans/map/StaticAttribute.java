package nl.javel.gisbeans.map;

/**
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
public class StaticAttribute extends AbstractAttribute
{
    /** the static angle. */
    private double angle = 0.0;

    /** the static value. */
    private String value = null;

    /**
     * constructs a new StaticAttribute.
     * @param layer the layer
     * @param angle the angle
     * @param value the value
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
