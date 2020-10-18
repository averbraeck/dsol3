package nl.tudelft.simulation.dsol.animation.gis.map;

import java.awt.Color;
import java.awt.Font;

import nl.tudelft.simulation.dsol.animation.gis.ScreenPosition;

/**
 * This class defines a general, abstract layer attribute.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class AbstractAttribute implements AttributeInterface
{
    /** */
    private static final long serialVersionUID = 20201015L;

    /** the default font to use. */
    private Font font = new Font("arial", Font.TRUETYPE_FONT, 10);

    /** the font color. */
    private Color fontColor = Color.BLACK;

    /** the position of the attribute. */
    private ScreenPosition position = ScreenPosition.CenterCenter;

    /** the layer of this attribute. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected LayerInterface layer = null;

    /** the minimumScale. */
    private double minScale = Double.MAX_VALUE;

    /** the maximumScale. */
    private double maxScale = 0.0;

    /**
     * constructs a new AbstractAttribute.
     * @param layer LayerInterface; the layer of this attribute
     */
    public AbstractAttribute(final LayerInterface layer)
    {
        this.layer = layer;
    }

    /** {@inheritDoc} */
    @Override
    public void setFont(final Font font)
    {
        this.font = font;
    }

    /** {@inheritDoc} */
    @Override
    public Font getFont()
    {
        return this.font;
    }

    /** {@inheritDoc} */
    @Override
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /** {@inheritDoc} */
    @Override
    public void setFontColor(final Color fontColor)
    {
        this.fontColor = fontColor;
    }

    /** {@inheritDoc} */
    @Override
    public abstract String getValue(int shapeIndex);

    /** {@inheritDoc} */
    @Override
    public abstract double getAngle(int shapeIndex);

    /** {@inheritDoc} */
    @Override
    public ScreenPosition getPosition()
    {
        return this.position;
    }

    /** {@inheritDoc} */
    @Override
    public void setPosition(final ScreenPosition position)
    {
        this.position = position;
    }

    /** {@inheritDoc} */
    @Override
    public LayerInterface getLayer()
    {
        return this.layer;
    }

    /** {@inheritDoc} */
    @Override
    public double getMaxScale()
    {
        return this.maxScale;
    }

    /** {@inheritDoc} */
    @Override
    public double getMinScale()
    {
        return this.minScale;
    }

    /** {@inheritDoc} */
    @Override
    public void setMaxScale(final double maxScale)
    {
        this.maxScale = maxScale;
    }

    /** {@inheritDoc} */
    @Override
    public void setMinScale(final double minScale)
    {
        this.minScale = minScale;
    }
}
