package nl.tudelft.simulation.dsol.animation.gis.map;

/**
 * StaticAttribute is a layer attribute with a String value.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class StaticAttribute extends AbstractAttribute
{
    /** */
    private static final long serialVersionUID = 20201015L;

    /** the static angle. */
    private double angle = 0.0;

    /** the static string value. */
    private String value = null;

    /**
     * constructs a new StaticAttribute.
     * @param layer LayerInterface; the layer
     * @param angle double; the angle
     * @param value String; the string value to store
     */
    public StaticAttribute(final LayerInterface layer, final double angle, final String value)
    {
        super(layer);
        this.angle = angle;
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public double getAngle(final int shapeIndex)
    {
        return this.angle;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(final int shapeIndex)
    {
        return this.value;
    }
}
