/*
 * AbstractAttribute.java
 * 
 * Created on April 17, 2002, 7:23 PM Last edited on October 11, 2002
 */
package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Font;

/**
 * This class defines the attribute.
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public abstract class AbstractAttribute implements AttributeInterface
{
    /** the default font to use. */
    private Font font = new Font("arial", Font.TRUETYPE_FONT, 10);

    /** the font color. */
    private Color fontColor = Color.BLACK;

    /** the position of the attribute. */
    private int position = MapInterface.CC;

    /** the layer of this attribute. */
    protected LayerInterface layer = null;

    /** the minimumScale. */
    private double minScale = Double.MAX_VALUE;

    /** the maximumScale. */
    private double maxScale = 0.0;

    /**
     * constructs a new AbstractAttribute.
     * @param layer the layer of this attribute
     */
    public AbstractAttribute(LayerInterface layer)
    {
        super();
        this.layer = layer;
    }

    /** {@inheritDoc} */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /** {@inheritDoc} */
    public Font getFont()
    {
        return this.font;
    }

    /** {@inheritDoc} */
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /** {@inheritDoc} */
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

    /** {@inheritDoc} */
    public abstract String getValue(int shapeIndex);

    /** {@inheritDoc} */
    public abstract double getAngle(int shapeIndex);

    /** {@inheritDoc} */
    public int getPosition()
    {
        return this.position;
    }

    /** {@inheritDoc} */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /** {@inheritDoc} */
    public LayerInterface getLayer()
    {
        return this.layer;
    }

    /** {@inheritDoc} */
    public double getMaxScale()
    {
        return this.maxScale;
    }

    /** {@inheritDoc} */
    public double getMinScale()
    {
        return this.minScale;
    }

    /** {@inheritDoc} */
    public void setMaxScale(double maxScale)
    {
        this.maxScale = maxScale;
    }

    /** {@inheritDoc} */
    public void setMinScale(double minScale)
    {
        this.minScale = minScale;
    }
}
