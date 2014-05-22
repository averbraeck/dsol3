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
    /** the default font to use */
    private Font font = new Font("arial", Font.TRUETYPE_FONT, 10);

    /** the font color */
    private Color fontColor = Color.BLACK;

    /** the position of the attribute */
    private int position = MapInterface.CC;

    /** the layer of this attribute */
    protected LayerInterface layer = null;

    /** the minimumScale */
    private double minScale = Double.MAX_VALUE;

    /** the maximumScale */
    private double maxScale = 0.0;

    /**
     * constructs a new AbstractAttribute
     * @param layer the layer of this attribute
     */
    public AbstractAttribute(LayerInterface layer)
    {
        super();
        this.layer = layer;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#setFont(java.awt.Font)
     */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getFont()
     */
    public Font getFont()
    {
        return this.font;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getFontColor()
     */
    public Color getFontColor()
    {
        return this.fontColor;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#setFontColor(java.awt.Color)
     */
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getValue(int)
     */
    public abstract String getValue(int shapeIndex);

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getAngle(int)
     */
    public abstract double getAngle(int shapeIndex);

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getPosition()
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#setPosition(int)
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getLayer()
     */
    public LayerInterface getLayer()
    {
        return this.layer;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getMaxScale()
     */
    public double getMaxScale()
    {
        return this.maxScale;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#getMinScale()
     */
    public double getMinScale()
    {
        return this.minScale;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#setMaxScale(int)
     */
    public void setMaxScale(double maxScale)
    {
        this.maxScale = maxScale;
    }

    /**
     * @see nl.javel.gisbeans.map.AttributeInterface#setMinScale(int)
     */
    public void setMinScale(double minScale)
    {
        this.minScale = minScale;
    }
}